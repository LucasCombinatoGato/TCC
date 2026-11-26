package com.camora.service;

import com.camora.dto.NotificacaoResponse;
import com.camora.exception.ForbiddenException;
import com.camora.exception.ResourceNotFoundException;
import com.camora.model.Evento;
import com.camora.model.EventoSalvo;
import com.camora.model.Notificacao;
import com.camora.repository.EventoSalvoRepository;
import com.camora.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final EventoSalvoRepository eventoSalvoRepository;
    private final MapperService mapper;

    public NotificacaoService(
            NotificacaoRepository notificacaoRepository,
            EventoSalvoRepository eventoSalvoRepository,
            MapperService mapper) {
        this.notificacaoRepository = notificacaoRepository;
        this.eventoSalvoRepository = eventoSalvoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public void notificarUsuariosEventoSalvo(Evento evento, String mensagem) {
        List<EventoSalvo> salvos = eventoSalvoRepository.findByEventoId(evento.getId());
        if (salvos.isEmpty()) return;

        List<Notificacao> notificacoes = salvos.stream()
                .map(s -> new Notificacao(s.getUsuario(), evento, mensagem))
                .toList();
        notificacaoRepository.saveAll(notificacoes);
    }

    public List<NotificacaoResponse> listarPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId).stream()
                .map(mapper::toNotificacaoResponse)
                .toList();
    }

    public long contarNaoLidas(Long usuarioId) {
        return notificacaoRepository.countByUsuarioIdAndLidaFalse(usuarioId);
    }

    @Transactional
    public NotificacaoResponse marcarComoLida(Long notificacaoId, Long usuarioId) {
        Notificacao n = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada."));
        if (!n.getUsuario().getId().equals(usuarioId)) {
            throw new ForbiddenException("Você não tem permissão para alterar esta notificação.");
        }
        n.setLida(true);
        return mapper.toNotificacaoResponse(notificacaoRepository.save(n));
    }

    @Transactional
    public void marcarTodasComoLidas(Long usuarioId) {
        List<Notificacao> lista = notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);
        lista.forEach(n -> n.setLida(true));
        notificacaoRepository.saveAll(lista);
    }
}
