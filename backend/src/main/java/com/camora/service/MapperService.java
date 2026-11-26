package com.camora.service;

import com.camora.dto.*;
import com.camora.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class MapperService {

    public LocalDTO toLocalDTO(Local local) {
        if (local == null) return null;
        return new LocalDTO(
                local.getId(),
                local.getCep(),
                local.getTipoLogradouro(),
                local.getLogradouro(),
                local.getBairro(),
                local.getCidade(),
                local.getUf(),
                local.getNumero(),
                local.getComplemento()
        );
    }

    public Local toLocalEntity(LocalDTO dto, Local target) {
        if (dto == null) return target;
        Local local = target != null ? target : new Local();
        local.setCep(dto.cep());
        local.setTipoLogradouro(dto.tipoLogradouro());
        local.setLogradouro(dto.logradouro());
        local.setBairro(dto.bairro());
        local.setCidade(dto.cidade());
        local.setUf(dto.uf() != null ? dto.uf().toUpperCase() : null);
        local.setNumero(dto.numero());
        local.setComplemento(dto.complemento());
        return local;
    }

    public DadosContatoDTO toDadosContatoDTO(DadosContato dc) {
        if (dc == null) return null;
        return new DadosContatoDTO(dc.getId(), dc.getEmail(), dc.getTelefone(), dc.getRedesSociais(), dc.getSite());
    }

    public DadosContato toDadosContatoEntity(DadosContatoDTO dto, DadosContato target) {
        if (dto == null) return target;
        DadosContato dc = target != null ? target : new DadosContato();
        dc.setEmail(dto.email());
        dc.setTelefone(dto.telefone());
        dc.setRedesSociais(dto.redesSociais());
        dc.setSite(dto.site());
        return dc;
    }

    public IdentidadeVisualDTO toIdentidadeVisualDTO(IdentidadeVisual iv) {
        if (iv == null) {
            IdentidadeVisual padrao = IdentidadeVisual.padrao();
            return new IdentidadeVisualDTO(null, null, padrao.getCorPrincipal(), padrao.getCorSecundaria(), padrao.getCorDestaque(), true);
        }
        if (iv.isUsarIdentidadePadrao()) {
            return new IdentidadeVisualDTO(
                    iv.getId(),
                    iv.getLogo(),
                    IdentidadeVisual.COR_PRINCIPAL_PADRAO,
                    IdentidadeVisual.COR_SECUNDARIA_PADRAO,
                    IdentidadeVisual.COR_DESTAQUE_PADRAO,
                    true
            );
        }
        return new IdentidadeVisualDTO(
                iv.getId(),
                iv.getLogo(),
                iv.getCorPrincipal() != null ? iv.getCorPrincipal() : IdentidadeVisual.COR_PRINCIPAL_PADRAO,
                iv.getCorSecundaria() != null ? iv.getCorSecundaria() : IdentidadeVisual.COR_SECUNDARIA_PADRAO,
                iv.getCorDestaque() != null ? iv.getCorDestaque() : IdentidadeVisual.COR_DESTAQUE_PADRAO,
                false
        );
    }

    public IdentidadeVisual toIdentidadeVisualEntity(IdentidadeVisualDTO dto, IdentidadeVisual target) {
        IdentidadeVisual iv = target != null ? target : IdentidadeVisual.padrao();
        if (dto == null) return iv;
        iv.setLogo(dto.logo());
        iv.setUsarIdentidadePadrao(dto.usarIdentidadePadrao());
        if (dto.usarIdentidadePadrao()) {
            iv.setCorPrincipal(IdentidadeVisual.COR_PRINCIPAL_PADRAO);
            iv.setCorSecundaria(IdentidadeVisual.COR_SECUNDARIA_PADRAO);
            iv.setCorDestaque(IdentidadeVisual.COR_DESTAQUE_PADRAO);
        } else {
            iv.setCorPrincipal(nonBlankOrDefault(dto.corPrincipal(), IdentidadeVisual.COR_PRINCIPAL_PADRAO));
            iv.setCorSecundaria(nonBlankOrDefault(dto.corSecundaria(), IdentidadeVisual.COR_SECUNDARIA_PADRAO));
            iv.setCorDestaque(nonBlankOrDefault(dto.corDestaque(), IdentidadeVisual.COR_DESTAQUE_PADRAO));
        }
        return iv;
    }

    public TematicaDTO toTematicaDTO(Tematica t) {
        if (t == null) return null;
        return new TematicaDTO(t.getId(), t.getNome(), t.getDescricao(), t.getCorHex());
    }

    public UsuarioResponse toUsuarioResponse(Usuario u) {
        List<TematicaDTO> prefs = u.getPreferencias() == null ? Collections.emptyList() :
                u.getPreferencias().stream()
                        .map(p -> toTematicaDTO(p.getTematica()))
                        .toList();
        return new UsuarioResponse(u.getId(), u.getCpf(), u.getNome(), u.getEmail(), toLocalDTO(u.getLocal()), prefs);
    }

    public ProdutorResponse toProdutorResponse(Produtor p) {
        return new ProdutorResponse(
                p.getId(),
                p.getCnpj(),
                p.getNomeEmpresa(),
                p.getEmail(),
                toLocalDTO(p.getLocal()),
                toDadosContatoDTO(p.getDadosContato()),
                toIdentidadeVisualDTO(p.getIdentidadeVisual())
        );
    }

    public OngResponse toOngResponse(Ong o) {
        return new OngResponse(
                o.getId(),
                o.getCnpj(),
                o.getNome(),
                o.getDescricao(),
                o.getEmail(),
                toLocalDTO(o.getLocal()),
                toDadosContatoDTO(o.getDadosContato()),
                toIdentidadeVisualDTO(o.getIdentidadeVisual())
        );
    }

    public EventoResponse toEventoResponse(Evento e, Set<Long> preferenciasUsuario, Set<Long> eventosSalvosIds) {
        Long organizadorId = null;
        String organizadorNome = null;
        String organizadorTipo = null;
        DadosContatoDTO contato = null;

        if (e.getProdutor() != null) {
            organizadorId = e.getProdutor().getId();
            organizadorNome = e.getProdutor().getNomeEmpresa();
            organizadorTipo = "PRODUTOR";
            contato = toDadosContatoDTO(e.getProdutor().getDadosContato());
        } else if (e.getOng() != null) {
            organizadorId = e.getOng().getId();
            organizadorNome = e.getOng().getNome();
            organizadorTipo = "ONG";
            contato = toDadosContatoDTO(e.getOng().getDadosContato());
        }

        List<InformacoesDiaDTO> dias = e.getInformacoesDias() == null ? Collections.emptyList() :
                e.getInformacoesDias().stream()
                        .map(d -> new InformacoesDiaDTO(d.getId(), d.getData(), d.getHoraInicio(), d.getHoraIntervalo(), d.getHoraFinal()))
                        .toList();

        List<TipoIngressoDTO> ingressos = e.getTiposIngresso() == null ? Collections.emptyList() :
                e.getTiposIngresso().stream()
                        .map(t -> new TipoIngressoDTO(t.getId(), t.getNome(), t.getDescricao(), t.getValor(), t.getQuantidadeDisponivel(), t.getBeneficios(), t.getLinkCompra()))
                        .toList();

        boolean gratuito = ingressos.isEmpty() || ingressos.stream()
                .anyMatch(t -> t.valor() == null || t.valor().compareTo(BigDecimal.ZERO) == 0);

        boolean priorizado = e.getTematica() != null
                && preferenciasUsuario != null
                && preferenciasUsuario.contains(e.getTematica().getId());

        boolean salvo = eventosSalvosIds != null && eventosSalvosIds.contains(e.getId());

        return new EventoResponse(
                e.getId(),
                e.getNome(),
                e.getDescricao(),
                e.getDataInicio(),
                e.getDataFim(),
                e.getClassificacaoIndicativa(),
                e.isEstacionamento(),
                e.getValorEstacionamento(),
                e.getImagemCapaUrl(),
                e.getTipoAmbito(),
                e.getStatus(),
                toTematicaDTO(e.getTematica()),
                toLocalDTO(e.getLocal()),
                organizadorId,
                organizadorNome,
                organizadorTipo,
                contato,
                toIdentidadeVisualDTO(e.resolverIdentidadeVisual()),
                dias,
                ingressos,
                gratuito,
                priorizado,
                salvo,
                e.getDataCriacao(),
                e.getDataAtualizacao()
        );
    }

    public NotificacaoResponse toNotificacaoResponse(Notificacao n) {
        return new NotificacaoResponse(
                n.getId(),
                n.getEvento().getId(),
                n.getEvento().getNome(),
                n.getMensagem(),
                n.getDataCriacao(),
                n.isLida()
        );
    }

    private String nonBlankOrDefault(String value, String fallback) {
        return (value != null && !value.isBlank()) ? value.trim() : fallback;
    }
}
