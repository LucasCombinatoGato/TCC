package com.camora.service;

import com.camora.config.UserPrincipal;
import com.camora.dto.EventoRequest;
import com.camora.dto.EventoResponse;
import com.camora.dto.InformacoesDiaDTO;
import com.camora.dto.TipoIngressoDTO;
import com.camora.exception.BusinessException;
import com.camora.exception.ForbiddenException;
import com.camora.exception.ResourceNotFoundException;
import com.camora.model.*;
import com.camora.repository.EventoRepository;
import com.camora.repository.EventoSalvoRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final EventoSalvoRepository eventoSalvoRepository;
    private final TematicaService tematicaService;
    private final ProdutorService produtorService;
    private final OngService ongService;
    private final UsuarioService usuarioService;
    private final NotificacaoService notificacaoService;
    private final MapperService mapper;

    public EventoService(
            EventoRepository eventoRepository,
            EventoSalvoRepository eventoSalvoRepository,
            TematicaService tematicaService,
            ProdutorService produtorService,
            OngService ongService,
            UsuarioService usuarioService,
            NotificacaoService notificacaoService,
            MapperService mapper) {
        this.eventoRepository = eventoRepository;
        this.eventoSalvoRepository = eventoSalvoRepository;
        this.tematicaService = tematicaService;
        this.produtorService = produtorService;
        this.ongService = ongService;
        this.usuarioService = usuarioService;
        this.notificacaoService = notificacaoService;
        this.mapper = mapper;
    }

    @Transactional
    public EventoResponse criar(EventoRequest req, UserPrincipal principal) {
        validarDatas(req.dataInicio(), req.dataFim());

        Evento evento = new Evento();
        if (principal.getRole() == Role.ROLE_PRODUCER) {
            evento.setProdutor(produtorService.buscarEntidade(principal.getId()));
            evento.setTipoAmbito(req.tipoAmbito() != null ? req.tipoAmbito() : TipoAmbito.EVENTO);
        } else if (principal.getRole() == Role.ROLE_ONG) {
            evento.setOng(ongService.buscarEntidade(principal.getId()));
            evento.setTipoAmbito(req.tipoAmbito() != null ? req.tipoAmbito() : TipoAmbito.ACAO_SOCIAL);
        } else {
            throw new ForbiddenException("Apenas produtores e ONGs podem criar eventos ou ações.");
        }

        preencherDadosEvento(evento, req);
        evento.setStatus(req.status() != null ? req.status() : StatusEvento.RASCUNHO);

        Evento salvo = eventoRepository.save(evento);
        return mapper.toEventoResponse(salvo, Set.of(), Set.of());
    }

    @Transactional
    public EventoResponse atualizar(Long eventoId, EventoRequest req, UserPrincipal principal) {
        validarDatas(req.dataInicio(), req.dataFim());
        Evento evento = buscarEntidade(eventoId);
        validarPropriedade(evento, principal);

        // Capturar estado anterior para detectar alterações relevantes (RF-15, RN-06, RN-17)
        String snapshotDataLocal = capturarAssinaturaRelevante(evento);

        preencherDadosEvento(evento, req);
        if (req.tipoAmbito() != null) {
            evento.setTipoAmbito(req.tipoAmbito());
        }
        if (req.status() != null) {
            evento.setStatus(req.status());
        }

        Evento atualizado = eventoRepository.save(evento);

        String novoSnapshot = capturarAssinaturaRelevante(atualizado);
        if (!snapshotDataLocal.equals(novoSnapshot)) {
            notificacaoService.notificarUsuariosEventoSalvo(
                    atualizado,
                    String.format("O evento \"%s\" teve alterações importantes de data, horário ou local. Confira os detalhes atualizados.", atualizado.getNome())
            );
        }

        return mapper.toEventoResponse(atualizado, Set.of(), Set.of());
    }

    @Transactional
    public EventoResponse publicar(Long eventoId, UserPrincipal principal) {
        Evento evento = buscarEntidade(eventoId);
        validarPropriedade(evento, principal);
        evento.setStatus(StatusEvento.PUBLICADO);
        return mapper.toEventoResponse(eventoRepository.save(evento), Set.of(), Set.of());
    }

    @Transactional
    public EventoResponse despublicar(Long eventoId, UserPrincipal principal) {
        Evento evento = buscarEntidade(eventoId);
        validarPropriedade(evento, principal);
        boolean estavaPublicado = evento.getStatus() == StatusEvento.PUBLICADO;
        evento.setStatus(StatusEvento.RASCUNHO);
        Evento salvo = eventoRepository.save(evento);

        if (estavaPublicado) {
            notificacaoService.notificarUsuariosEventoSalvo(
                    salvo,
                    String.format("O evento \"%s\" foi temporariamente despublicado pelo organizador.", salvo.getNome())
            );
        }
        return mapper.toEventoResponse(salvo, Set.of(), Set.of());
    }

    @Transactional
    public EventoResponse cancelar(Long eventoId, UserPrincipal principal) {
        Evento evento = buscarEntidade(eventoId);
        validarPropriedade(evento, principal);
        boolean naoEstavaCancelado = evento.getStatus() != StatusEvento.CANCELADO;
        evento.setStatus(StatusEvento.CANCELADO);
        Evento salvo = eventoRepository.save(evento);

        if (naoEstavaCancelado) {
            notificacaoService.notificarUsuariosEventoSalvo(
                    salvo,
                    String.format("Atenção: o evento \"%s\" foi cancelado pelo organizador.", salvo.getNome())
            );
        }
        return mapper.toEventoResponse(salvo, Set.of(), Set.of());
    }

    public List<EventoResponse> listarMeusEventos(UserPrincipal principal) {
        List<Evento> eventos;
        if (principal.getRole() == Role.ROLE_PRODUCER) {
            eventos = eventoRepository.findByProdutorIdOrderByDataCriacaoDesc(principal.getId());
        } else if (principal.getRole() == Role.ROLE_ONG) {
            eventos = eventoRepository.findByOngIdOrderByDataCriacaoDesc(principal.getId());
        } else {
            throw new ForbiddenException("Perfil não possui eventos criados.");
        }
        return eventos.stream()
                .map(e -> mapper.toEventoResponse(e, Set.of(), Set.of()))
                .toList();
    }

    public EventoResponse buscarPorId(Long eventoId, UserPrincipal principal) {
        Evento evento = buscarEntidade(eventoId);
        if (evento.getStatus() != StatusEvento.PUBLICADO) {
            if (principal == null || !ehProprietario(evento, principal)) {
                throw new ResourceNotFoundException("Evento não encontrado ou indisponível.");
            }
        }
        Set<Long> prefs = resolverPreferencias(principal);
        Set<Long> salvos = resolverSalvos(principal);
        return mapper.toEventoResponse(evento, prefs, salvos);
    }

    public List<EventoResponse> listarPublicos(
            String q,
            Long tematicaId,
            String cidade,
            String uf,
            Boolean gratuito,
            LocalDate dataInicio,
            LocalDate dataFim,
            TipoAmbito tipoAmbito,
            Long ongId,
            Long produtorId,
            UserPrincipal principal) {

        Specification<Evento> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), StatusEvento.PUBLICADO));

            if (q != null && !q.isBlank()) {
                String pattern = "%" + q.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("nome")), pattern),
                        cb.like(cb.lower(root.get("descricao")), pattern)
                ));
            }
            if (tematicaId != null) {
                predicates.add(cb.equal(root.get("tematica").get("id"), tematicaId));
            }
            if (cidade != null && !cidade.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("local").get("cidade")), "%" + cidade.trim().toLowerCase() + "%"));
            }
            if (uf != null && !uf.isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("local").get("uf")), uf.trim().toUpperCase()));
            }
            if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataFim"), dataInicio.atStartOfDay()));
            }
            if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataInicio"), dataFim.atTime(23, 59, 59)));
            }
            if (tipoAmbito != null) {
                predicates.add(cb.equal(root.get("tipoAmbito"), tipoAmbito));
            }
            if (ongId != null) {
                predicates.add(cb.equal(root.get("ong").get("id"), ongId));
            }
            if (produtorId != null) {
                predicates.add(cb.equal(root.get("produtor").get("id"), produtorId));
            }
            if (Boolean.TRUE.equals(gratuito)) {
                query.distinct(true);
                Join<Evento, TipoIngresso> ingressoJoin = root.join("tiposIngresso", JoinType.LEFT);
                predicates.add(cb.or(
                        cb.isNull(ingressoJoin.get("id")),
                        cb.equal(ingressoJoin.get("valor"), BigDecimal.ZERO)
                ));
            } else if (Boolean.FALSE.equals(gratuito)) {
                query.distinct(true);
                Join<Evento, TipoIngresso> ingressoJoin = root.join("tiposIngresso", JoinType.INNER);
                predicates.add(cb.greaterThan(ingressoJoin.get("valor"), BigDecimal.ZERO));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<Evento> encontrados = eventoRepository.findAll(spec);
        Set<Long> prefs = resolverPreferencias(principal);
        Set<Long> salvos = resolverSalvos(principal);

        // Priorização personalizada por temáticas favoritas do usuário logado (RN-12)
        return encontrados.stream()
                .map(e -> mapper.toEventoResponse(e, prefs, salvos))
                .sorted(Comparator
                        .comparing(EventoResponse::priorizado).reversed()
                        .thenComparing(EventoResponse::dataInicio))
                .toList();
    }

    private void preencherDadosEvento(Evento evento, EventoRequest req) {
        evento.setNome(req.nome().trim());
        evento.setDescricao(req.descricao());
        evento.setDataInicio(req.dataInicio());
        evento.setDataFim(req.dataFim());
        evento.setClassificacaoIndicativa(req.classificacaoIndicativa());
        evento.setEstacionamento(req.estacionamento());
        evento.setValorEstacionamento(req.estacionamento() && req.valorEstacionamento() != null
                ? req.valorEstacionamento() : BigDecimal.ZERO);
        evento.setImagemCapaUrl(req.imagemCapaUrl());
        evento.setTematica(tematicaService.buscarEntidadePorId(req.tematicaId()));

        if (req.local() != null) {
            evento.setLocal(mapper.toLocalEntity(req.local(), evento.getLocal()));
        }

        evento.getInformacoesDias().clear();
        if (req.informacoesDias() != null) {
            for (InformacoesDiaDTO d : req.informacoesDias()) {
                InformacoesDia dia = new InformacoesDia();
                dia.setEvento(evento);
                dia.setData(d.data());
                dia.setHoraInicio(d.horaInicio());
                dia.setHoraIntervalo(d.horaIntervalo());
                dia.setHoraFinal(d.horaFinal());
                evento.getInformacoesDias().add(dia);
            }
        }

        evento.getTiposIngresso().clear();
        if (req.tiposIngresso() != null) {
            for (TipoIngressoDTO t : req.tiposIngresso()) {
                TipoIngresso ingresso = new TipoIngresso();
                ingresso.setEvento(evento);
                ingresso.setNome(t.nome().trim());
                ingresso.setDescricao(t.descricao());
                ingresso.setValor(t.valor() != null ? t.valor() : BigDecimal.ZERO);
                ingresso.setQuantidadeDisponivel(t.quantidadeDisponivel());
                ingresso.setBeneficios(t.beneficios());
                ingresso.setLinkCompra(t.linkCompra());
                evento.getTiposIngresso().add(ingresso);
            }
        }
    }

    private String capturarAssinaturaRelevante(Evento e) {
        Local l = e.getLocal();
        String localStr = l == null ? "" : String.join("|",
                safe(l.getCep()), safe(l.getLogradouro()), safe(l.getNumero()),
                safe(l.getBairro()), safe(l.getCidade()), safe(l.getUf()));
        String diasStr = e.getInformacoesDias() == null ? "" :
                e.getInformacoesDias().stream()
                        .map(d -> d.getData() + ":" + d.getHoraInicio() + "-" + d.getHoraFinal())
                        .collect(Collectors.joining(";"));
        return safe(e.getDataInicio()) + "#" + safe(e.getDataFim()) + "#" + localStr + "#" + diasStr;
    }

    private String safe(Object o) {
        return o == null ? "" : o.toString().trim();
    }

    private void validarDatas(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio != null && fim != null && fim.isBefore(inicio)) {
            throw new BusinessException("A data de término não pode ser anterior à data de início.");
        }
    }

    private void validarPropriedade(Evento evento, UserPrincipal principal) {
        if (!ehProprietario(evento, principal)) {
            throw new ForbiddenException("Você não tem permissão para alterar um evento de outro organizador.");
        }
    }

    private boolean ehProprietario(Evento evento, UserPrincipal principal) {
        if (principal == null) return false;
        if (principal.getRole() == Role.ROLE_PRODUCER && evento.getProdutor() != null) {
            return evento.getProdutor().getId().equals(principal.getId());
        }
        if (principal.getRole() == Role.ROLE_ONG && evento.getOng() != null) {
            return evento.getOng().getId().equals(principal.getId());
        }
        return false;
    }

    private Set<Long> resolverPreferencias(UserPrincipal principal) {
        if (principal == null || principal.getRole() != Role.ROLE_USER) return Set.of();
        return usuarioService.extrairPreferenciasIds(principal.getId());
    }

    private Set<Long> resolverSalvos(UserPrincipal principal) {
        if (principal == null || principal.getRole() != Role.ROLE_USER) return Set.of();
        return eventoSalvoRepository.findByUsuarioIdOrderByDataSalvamentoDesc(principal.getId())
                .stream()
                .map(s -> s.getEvento().getId())
                .collect(Collectors.toSet());
    }

    private Evento buscarEntidade(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));
    }
}
