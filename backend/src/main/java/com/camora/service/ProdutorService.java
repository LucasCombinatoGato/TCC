package com.camora.service;

import com.camora.dto.IdentidadeVisualDTO;
import com.camora.dto.ProdutorResponse;
import com.camora.dto.ProdutorUpdateRequest;
import com.camora.exception.ResourceNotFoundException;
import com.camora.model.IdentidadeVisual;
import com.camora.model.Produtor;
import com.camora.repository.ProdutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutorService {

    private final ProdutorRepository produtorRepository;
    private final MapperService mapper;

    public ProdutorService(ProdutorRepository produtorRepository, MapperService mapper) {
        this.produtorRepository = produtorRepository;
        this.mapper = mapper;
    }

    public List<ProdutorResponse> listarTodos() {
        return produtorRepository.findAll().stream()
                .map(mapper::toProdutorResponse)
                .toList();
    }

    public ProdutorResponse buscarPorId(Long id) {
        return mapper.toProdutorResponse(buscarEntidade(id));
    }

    @Transactional
    public ProdutorResponse atualizarPerfil(Long produtorId, ProdutorUpdateRequest req) {
        Produtor p = buscarEntidade(produtorId);
        if (req.nomeEmpresa() != null && !req.nomeEmpresa().isBlank()) {
            p.setNomeEmpresa(req.nomeEmpresa().trim());
        }
        if (req.local() != null) {
            p.setLocal(mapper.toLocalEntity(req.local(), p.getLocal()));
        }
        if (req.dadosContato() != null) {
            p.setDadosContato(mapper.toDadosContatoEntity(req.dadosContato(), p.getDadosContato()));
        }
        if (req.identidadeVisual() != null) {
            p.setIdentidadeVisual(mapper.toIdentidadeVisualEntity(req.identidadeVisual(), p.getIdentidadeVisual()));
        }
        return mapper.toProdutorResponse(produtorRepository.save(p));
    }

    @Transactional
    public IdentidadeVisualDTO atualizarIdentidadeVisual(Long produtorId, IdentidadeVisualDTO dto) {
        Produtor p = buscarEntidade(produtorId);
        p.setIdentidadeVisual(mapper.toIdentidadeVisualEntity(dto, p.getIdentidadeVisual()));
        Produtor salvo = produtorRepository.save(p);
        return mapper.toIdentidadeVisualDTO(salvo.getIdentidadeVisual());
    }

    @Transactional
    public IdentidadeVisualDTO restaurarIdentidadePadrao(Long produtorId) {
        Produtor p = buscarEntidade(produtorId);
        IdentidadeVisual iv = p.getIdentidadeVisual() != null ? p.getIdentidadeVisual() : IdentidadeVisual.padrao();
        iv.setUsarIdentidadePadrao(true);
        iv.setCorPrincipal(IdentidadeVisual.COR_PRINCIPAL_PADRAO);
        iv.setCorSecundaria(IdentidadeVisual.COR_SECUNDARIA_PADRAO);
        iv.setCorDestaque(IdentidadeVisual.COR_DESTAQUE_PADRAO);
        p.setIdentidadeVisual(iv);
        return mapper.toIdentidadeVisualDTO(produtorRepository.save(p).getIdentidadeVisual());
    }

    public Produtor buscarEntidade(Long id) {
        return produtorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produtor não encontrado."));
    }
}
