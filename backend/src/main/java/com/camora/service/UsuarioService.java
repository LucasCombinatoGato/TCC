package com.camora.service;

import com.camora.dto.AtualizarPreferenciasRequest;
import com.camora.dto.EventoSalvoResponse;
import com.camora.dto.UsuarioResponse;
import com.camora.exception.ResourceNotFoundException;
import com.camora.model.*;
import com.camora.repository.EventoRepository;
import com.camora.repository.EventoSalvoRepository;
import com.camora.repository.TematicaRepository;
import com.camora.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TematicaRepository tematicaRepository;
    private final EventoRepository eventoRepository;
    private final EventoSalvoRepository eventoSalvoRepository;
    private final MapperService mapper;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            TematicaRepository tematicaRepository,
            EventoRepository eventoRepository,
            EventoSalvoRepository eventoSalvoRepository,
            MapperService mapper) {
        this.usuarioRepository = usuarioRepository;
        this.tematicaRepository = tematicaRepository;
        this.eventoRepository = eventoRepository;
        this.eventoSalvoRepository = eventoSalvoRepository;
        this.mapper = mapper;
    }

    public UsuarioResponse buscarPerfil(Long usuarioId) {
        Usuario u = buscarEntidade(usuarioId);
        return mapper.toUsuarioResponse(u);
    }

    @Transactional
    public UsuarioResponse atualizarPreferencias(Long usuarioId, AtualizarPreferenciasRequest req) {
        Usuario u = buscarEntidade(usuarioId);
        u.getPreferencias().clear();
        if (req.tematicasIds() != null) {
            for (Long tId : req.tematicasIds()) {
                Tematica t = tematicaRepository.findById(tId)
                        .orElseThrow(() -> new ResourceNotFoundException("Temática não encontrada: " + tId));
                u.getPreferencias().add(new PreferenciaUsuario(u, t));
            }
        }
        return mapper.toUsuarioResponse(usuarioRepository.save(u));
    }

    @Transactional
    public EventoSalvoResponse salvarEvento(Long usuarioId, Long eventoId) {
        Usuario u = buscarEntidade(usuarioId);
        Evento e = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));

        EventoSalvo salvo = eventoSalvoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId)
                .orElseGet(() -> eventoSalvoRepository.save(new EventoSalvo(u, e)));

        Set<Long> prefs = extrairPreferenciasIds(u);
        Set<Long> salvosIds = Set.of(eventoId);
        return new EventoSalvoResponse(salvo.getId(), salvo.getDataSalvamento(), mapper.toEventoResponse(e, prefs, salvosIds));
    }

    @Transactional
    public void removerEventoSalvo(Long usuarioId, Long eventoId) {
        eventoSalvoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId)
                .ifPresent(eventoSalvoRepository::delete);
    }

    public List<EventoSalvoResponse> listarEventosSalvos(Long usuarioId) {
        Usuario u = buscarEntidade(usuarioId);
        Set<Long> prefs = extrairPreferenciasIds(u);
        List<EventoSalvo> salvos = eventoSalvoRepository.findByUsuarioIdOrderByDataSalvamentoDesc(usuarioId);
        Set<Long> salvosIds = salvos.stream().map(s -> s.getEvento().getId()).collect(Collectors.toSet());

        return salvos.stream()
                .map(s -> new EventoSalvoResponse(
                        s.getId(),
                        s.getDataSalvamento(),
                        mapper.toEventoResponse(s.getEvento(), prefs, salvosIds)))
                .toList();
    }

    public Set<Long> extrairPreferenciasIds(Long usuarioId) {
        if (usuarioId == null) return Set.of();
        return usuarioRepository.findById(usuarioId)
                .map(this::extrairPreferenciasIds)
                .orElse(Set.of());
    }

    private Set<Long> extrairPreferenciasIds(Usuario u) {
        return u.getPreferencias().stream()
                .map(p -> p.getTematica().getId())
                .collect(Collectors.toSet());
    }

    private Usuario buscarEntidade(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }
}
