package com.camora.config;

import com.camora.model.Role;
import com.camora.repository.OngRepository;
import com.camora.repository.ProdutorRepository;
import com.camora.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final ProdutorRepository produtorRepository;
    private final OngRepository ongRepository;

    public CustomUserDetailsService(
            UsuarioRepository usuarioRepository,
            ProdutorRepository produtorRepository,
            OngRepository ongRepository) {
        this.usuarioRepository = usuarioRepository;
        this.produtorRepository = produtorRepository;
        this.ongRepository = ongRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .map(u -> new UserPrincipal(u.getId(), u.getNome(), u.getEmail(), u.getSenha(), Role.ROLE_USER))
                .or(() -> produtorRepository.findByEmail(email)
                        .map(p -> new UserPrincipal(p.getId(), p.getNomeEmpresa(), p.getEmail(), p.getSenha(), Role.ROLE_PRODUCER)))
                .or(() -> ongRepository.findByEmail(email)
                        .map(o -> new UserPrincipal(o.getId(), o.getNome(), o.getEmail(), o.getSenha(), Role.ROLE_ONG)))
                .orElseThrow(() -> new UsernameNotFoundException("Conta não encontrada para o e-mail: " + email));
    }

    public UserPrincipal loadByEmailAndRole(String email, Role role) {
        return switch (role) {
            case ROLE_USER -> usuarioRepository.findByEmail(email)
                    .map(u -> new UserPrincipal(u.getId(), u.getNome(), u.getEmail(), u.getSenha(), Role.ROLE_USER))
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
            case ROLE_PRODUCER -> produtorRepository.findByEmail(email)
                    .map(p -> new UserPrincipal(p.getId(), p.getNomeEmpresa(), p.getEmail(), p.getSenha(), Role.ROLE_PRODUCER))
                    .orElseThrow(() -> new UsernameNotFoundException("Produtor não encontrado."));
            case ROLE_ONG -> ongRepository.findByEmail(email)
                    .map(o -> new UserPrincipal(o.getId(), o.getNome(), o.getEmail(), o.getSenha(), Role.ROLE_ONG))
                    .orElseThrow(() -> new UsernameNotFoundException("ONG não encontrada."));
        };
    }
}
