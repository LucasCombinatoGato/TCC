package com.camora.service;

import com.camora.dto.IdentidadeVisualDTO;
import com.camora.dto.OngResponse;
import com.camora.dto.OngUpdateRequest;
import com.camora.exception.ResourceNotFoundException;
import com.camora.model.IdentidadeVisual;
import com.camora.model.Ong;
import com.camora.repository.OngRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OngService {

    private final OngRepository ongRepository;
    private final MapperService mapper;

    public OngService(OngRepository ongRepository, MapperService mapper) {
        this.ongRepository = ongRepository;
        this.mapper = mapper;
    }

    public List<OngResponse> listarTodas() {
        return ongRepository.findAll().stream()
                .map(mapper::toOngResponse)
                .toList();
    }

    public OngResponse buscarPorId(Long id) {
        return mapper.toOngResponse(buscarEntidade(id));
    }

    @Transactional
    public OngResponse atualizarPerfil(Long ongId, OngUpdateRequest req) {
        Ong o = buscarEntidade(ongId);
        if (req.nome() != null && !req.nome().isBlank()) {
            o.setNome(req.nome().trim());
        }
        if (req.descricao() != null) {
            o.setDescricao(req.descricao());
        }
        if (req.local() != null) {
            o.setLocal(mapper.toLocalEntity(req.local(), o.getLocal()));
        }
        if (req.dadosContato() != null) {
            o.setDadosContato(mapper.toDadosContatoEntity(req.dadosContato(), o.getDadosContato()));
        }
        if (req.identidadeVisual() != null) {
            o.setIdentidadeVisual(mapper.toIdentidadeVisualEntity(req.identidadeVisual(), o.getIdentidadeVisual()));
        }
        return mapper.toOngResponse(ongRepository.save(o));
    }

    @Transactional
    public IdentidadeVisualDTO atualizarIdentidadeVisual(Long ongId, IdentidadeVisualDTO dto) {
        Ong o = buscarEntidade(ongId);
        o.setIdentidadeVisual(mapper.toIdentidadeVisualEntity(dto, o.getIdentidadeVisual()));
        return mapper.toIdentidadeVisualDTO(ongRepository.save(o).getIdentidadeVisual());
    }

    @Transactional
    public IdentidadeVisualDTO restaurarIdentidadePadrao(Long ongId) {
        Ong o = buscarEntidade(ongId);
        IdentidadeVisual iv = o.getIdentidadeVisual() != null ? o.getIdentidadeVisual() : IdentidadeVisual.padrao();
        iv.setUsarIdentidadePadrao(true);
        iv.setCorPrincipal(IdentidadeVisual.COR_PRINCIPAL_PADRAO);
        iv.setCorSecundaria(IdentidadeVisual.COR_SECUNDARIA_PADRAO);
        iv.setCorDestaque(IdentidadeVisual.COR_DESTAQUE_PADRAO);
        o.setIdentidadeVisual(iv);
        return mapper.toIdentidadeVisualDTO(ongRepository.save(o).getIdentidadeVisual());
    }

    public Ong buscarEntidade(Long id) {
        return ongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ONG não encontrada."));
    }
}
