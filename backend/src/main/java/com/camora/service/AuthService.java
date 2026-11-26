package com.camora.service;

import com.camora.config.CustomUserDetailsService;
import com.camora.config.JwtService;
import com.camora.config.UserPrincipal;
import com.camora.dto.*;
import com.camora.exception.BusinessException;
import com.camora.model.*;
import com.camora.repository.OngRepository;
import com.camora.repository.ProdutorRepository;
import com.camora.repository.TematicaRepository;
import com.camora.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ProdutorRepository produtorRepository;
    private final OngRepository ongRepository;
    private final TematicaRepository tematicaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final MapperService mapper;

    public AuthService(
            UsuarioRepository usuarioRepository,
            ProdutorRepository produtorRepository,
            OngRepository ongRepository,
            TematicaRepository tematicaRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService,
            MapperService mapper) {
        this.usuarioRepository = usuarioRepository;
        this.produtorRepository = produtorRepository;
        this.ongRepository = ongRepository;
        this.tematicaRepository = tematicaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
    }

    public AuthResponse login(LoginRequest req) {
        UserPrincipal principal;
        if (req.tipoPerfil() != null) {
            try {
                principal = userDetailsService.loadByEmailAndRole(req.email().trim().toLowerCase(), req.tipoPerfil());
            } catch (Exception e) {
                throw new BadCredentialsException("Credenciais inválidas");
            }
        } else {
            try {
                principal = (UserPrincipal) userDetailsService.loadUserByUsername(req.email().trim().toLowerCase());
            } catch (Exception e) {
                throw new BadCredentialsException("Credenciais inválidas");
            }
        }

        if (!passwordEncoder.matches(req.senha(), principal.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.gerarToken(principal);
        IdentidadeVisualDTO iv = resolverIdentidadeConta(principal);
        return new AuthResponse(token, principal.getId(), principal.getNome(), principal.getEmail(), principal.getRole(), iv);
    }

    @Transactional
    public AuthResponse registrarUsuario(RegistroUsuarioRequest req) {
        String email = req.email().trim().toLowerCase();
        validarEmailUnicoGlobal(email);
        if (usuarioRepository.existsByCpf(req.cpf().trim())) {
            throw new BusinessException("CPF já cadastrado.");
        }

        Usuario u = new Usuario();
        u.setCpf(req.cpf().trim());
        u.setNome(req.nome().trim());
        u.setEmail(email);
        u.setSenha(passwordEncoder.encode(req.senha()));
        u.setLocal(mapper.toLocalEntity(req.local(), null));

        if (req.tematicasPreferidasIds() != null) {
            for (Long tematicaId : req.tematicasPreferidasIds()) {
                tematicaRepository.findById(tematicaId)
                        .ifPresent(t -> u.getPreferencias().add(new PreferenciaUsuario(u, t)));
            }
        }

        Usuario salvo = usuarioRepository.save(u);
        UserPrincipal principal = new UserPrincipal(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getSenha(), Role.ROLE_USER);
        return new AuthResponse(jwtService.gerarToken(principal), salvo.getId(), salvo.getNome(), salvo.getEmail(), Role.ROLE_USER, null);
    }

    @Transactional
    public AuthResponse registrarProdutor(RegistroProdutorRequest req) {
        String email = req.email().trim().toLowerCase();
        validarEmailUnicoGlobal(email);
        if (produtorRepository.existsByCnpj(req.cnpj().trim())) {
            throw new BusinessException("CNPJ já cadastrado para outro produtor.");
        }

        Produtor p = new Produtor();
        p.setCnpj(req.cnpj().trim());
        p.setNomeEmpresa(req.nomeEmpresa().trim());
        p.setEmail(email);
        p.setSenha(passwordEncoder.encode(req.senha()));
        p.setLocal(mapper.toLocalEntity(req.local(), null));
        p.setDadosContato(mapper.toDadosContatoEntity(req.dadosContato(), new DadosContato()));
        p.setIdentidadeVisual(mapper.toIdentidadeVisualEntity(req.identidadeVisual(), IdentidadeVisual.padrao()));

        Produtor salvo = produtorRepository.save(p);
        UserPrincipal principal = new UserPrincipal(salvo.getId(), salvo.getNomeEmpresa(), salvo.getEmail(), salvo.getSenha(), Role.ROLE_PRODUCER);
        return new AuthResponse(jwtService.gerarToken(principal), salvo.getId(), salvo.getNomeEmpresa(), salvo.getEmail(), Role.ROLE_PRODUCER, mapper.toIdentidadeVisualDTO(salvo.getIdentidadeVisual()));
    }

    @Transactional
    public AuthResponse registrarOng(RegistroOngRequest req) {
        String email = req.email().trim().toLowerCase();
        validarEmailUnicoGlobal(email);
        if (ongRepository.existsByCnpj(req.cnpj().trim())) {
            throw new BusinessException("CNPJ já cadastrado para outra ONG.");
        }

        Ong o = new Ong();
        o.setCnpj(req.cnpj().trim());
        o.setNome(req.nome().trim());
        o.setDescricao(req.descricao());
        o.setEmail(email);
        o.setSenha(passwordEncoder.encode(req.senha()));
        o.setLocal(mapper.toLocalEntity(req.local(), null));
        o.setDadosContato(mapper.toDadosContatoEntity(req.dadosContato(), new DadosContato()));
        o.setIdentidadeVisual(mapper.toIdentidadeVisualEntity(req.identidadeVisual(), IdentidadeVisual.padrao()));

        Ong salva = ongRepository.save(o);
        UserPrincipal principal = new UserPrincipal(salva.getId(), salva.getNome(), salva.getEmail(), salva.getSenha(), Role.ROLE_ONG);
        return new AuthResponse(jwtService.gerarToken(principal), salva.getId(), salva.getNome(), salva.getEmail(), Role.ROLE_ONG, mapper.toIdentidadeVisualDTO(salva.getIdentidadeVisual()));
    }

    private void validarEmailUnicoGlobal(String email) {
        if (usuarioRepository.existsByEmail(email)
                || produtorRepository.existsByEmail(email)
                || ongRepository.existsByEmail(email)) {
            throw new BusinessException("E-mail já cadastrado na plataforma.");
        }
    }

    private IdentidadeVisualDTO resolverIdentidadeConta(UserPrincipal principal) {
        return switch (principal.getRole()) {
            case ROLE_PRODUCER -> produtorRepository.findById(principal.getId())
                    .map(p -> mapper.toIdentidadeVisualDTO(p.getIdentidadeVisual()))
                    .orElse(null);
            case ROLE_ONG -> ongRepository.findById(principal.getId())
                    .map(o -> mapper.toIdentidadeVisualDTO(o.getIdentidadeVisual()))
                    .orElse(null);
            default -> null;
        };
    }
}
