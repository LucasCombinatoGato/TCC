package com.camora.config;

import com.camora.model.*;
import com.camora.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private final ProdutorRepository produtorRepository;
    private final OngRepository ongRepository;
    private final UsuarioRepository usuarioRepository;
    private final TematicaRepository tematicaRepository;
    private final EventoRepository eventoRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDataSeeder(
            ProdutorRepository produtorRepository,
            OngRepository ongRepository,
            UsuarioRepository usuarioRepository,
            TematicaRepository tematicaRepository,
            EventoRepository eventoRepository,
            PasswordEncoder passwordEncoder) {
        this.produtorRepository = produtorRepository;
        this.ongRepository = ongRepository;
        this.usuarioRepository = usuarioRepository;
        this.tematicaRepository = tematicaRepository;
        this.eventoRepository = eventoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (produtorRepository.count() > 0 || ongRepository.count() > 0 || eventoRepository.count() > 0) {
            return;
        }

        Tematica cultural = tematicaRepository.findByNomeIgnoreCase("Cultural").orElse(null);
        Tematica musical = tematicaRepository.findByNomeIgnoreCase("Musical").orElse(null);
        Tematica social = tematicaRepository.findByNomeIgnoreCase("Social").orElse(null);
        Tematica educacional = tematicaRepository.findByNomeIgnoreCase("Educacional").orElse(null);
        Tematica ambiental = tematicaRepository.findByNomeIgnoreCase("Ambiental").orElse(null);

        Produtor produtor = new Produtor();
        produtor.setCnpj("12.345.678/0001-90");
        produtor.setNomeEmpresa("Camaleão Produções Culturais");
        produtor.setEmail("produtor@camora.com");
        produtor.setSenha(passwordEncoder.encode("Camora123!"));
        produtor.setLocal(criarLocal("01310-100", "Avenida", "Paulista", "Bela Vista", "São Paulo", "SP", "1578", "Conj. 402"));
        produtor.setDadosContato(criarContato("contato@camaleaoproducoes.com.br", "(11) 99999-1000", "@camaleaoproducoes", "https://camora.dev"));
        produtor.setIdentidadeVisual(IdentidadeVisual.padrao());
        produtor = produtorRepository.save(produtor);

        Ong ong = new Ong();
        ong.setCnpj("98.765.432/0001-10");
        ong.setNome("Instituto Raízes Vivas");
        ong.setDescricao("Organização sem fins lucrativos dedicada à educação comunitária, mutirões ambientais e inclusão social na Grande São Paulo.");
        ong.setEmail("ong@camora.com");
        ong.setSenha(passwordEncoder.encode("Camora123!"));
        ong.setLocal(criarLocal("04011-060", "Rua", "Domingos de Morais", "Vila Mariana", "São Paulo", "SP", "845", "Sala 3"));
        ong.setDadosContato(criarContato("apoio@raizesvivas.org.br", "(11) 98888-2000", "@raizesvivas.ong", "https://raizesvivas.org.br"));

        IdentidadeVisual ivOng = new IdentidadeVisual();
        ivOng.setUsarIdentidadePadrao(false);
        ivOng.setCorPrincipal("#1E3A8A");
        ivOng.setCorSecundaria("#3A86FF");
        ivOng.setCorDestaque("#FFBE0B");
        ong.setIdentidadeVisual(ivOng);
        ong = ongRepository.save(ong);

        if (usuarioRepository.count() == 0) {
            Usuario usuario = new Usuario();
            usuario.setCpf("123.456.789-00");
            usuario.setNome("Lucas Visitante");
            usuario.setEmail("usuario@camora.com");
            usuario.setSenha(passwordEncoder.encode("Camora123!"));
            usuario.setLocal(criarLocal("01001-000", "Praça", "da Sé", "Sé", "São Paulo", "SP", "100", null));
            if (musical != null) usuario.getPreferencias().add(new PreferenciaUsuario(usuario, musical));
            if (social != null) usuario.getPreferencias().add(new PreferenciaUsuario(usuario, social));
            usuarioRepository.save(usuario);
        }

        // Evento 1: Festival Musical
        Evento e1 = new Evento();
        e1.setNome("Festival Camaleão de Música & Arte Urbana");
        e1.setDescricao("Dois dias de música independente, grafite ao vivo, feira criativa e gastronomia local celebrando a diversidade cultural.");
        e1.setDataInicio(LocalDateTime.now().plusDays(12).withHour(14).withMinute(0));
        e1.setDataFim(LocalDateTime.now().plusDays(13).withHour(23).withMinute(0));
        e1.setClassificacaoIndicativa("14 anos");
        e1.setEstacionamento(true);
        e1.setValorEstacionamento(new BigDecimal("35.00"));
        e1.setTipoAmbito(TipoAmbito.EVENTO);
        e1.setStatus(StatusEvento.PUBLICADO);
        e1.setTematica(musical != null ? musical : cultural);
        e1.setProdutor(produtor);
        e1.setLocal(criarLocal("05001-200", "Avenida", "Francisco Matarazzo", "Água Branca", "São Paulo", "SP", "455", "Parque da Água Branca"));

        InformacoesDia d1 = new InformacoesDia();
        d1.setEvento(e1);
        d1.setData(LocalDate.now().plusDays(12));
        d1.setHoraInicio(LocalTime.of(14, 0));
        d1.setHoraIntervalo(LocalTime.of(18, 30));
        d1.setHoraFinal(LocalTime.of(23, 0));
        e1.getInformacoesDias().add(d1);

        TipoIngresso t1 = new TipoIngresso();
        t1.setEvento(e1);
        t1.setNome("Pista Solidária");
        t1.setDescricao("Meia-entrada social mediante doação de 1kg de alimento não perecível.");
        t1.setValor(new BigDecimal("45.00"));
        t1.setQuantidadeDisponivel(500);
        t1.setBeneficios("Acesso a todos os palcos e área gastronômica");
        t1.setLinkCompra("https://camora.dev/ingressos/festival-camaleao");
        e1.getTiposIngresso().add(t1);
        eventoRepository.save(e1);

        // Evento 2: Oficina Educacional Gratuita
        Evento e2 = new Evento();
        e2.setNome("Hackathon & Oficina de Tecnologia Criativa");
        e2.setDescricao("Maratona gratuita de inovação cívica, design inclusivo e programação voltada a jovens estudantes.");
        e2.setDataInicio(LocalDateTime.now().plusDays(20).withHour(9).withMinute(0));
        e2.setDataFim(LocalDateTime.now().plusDays(20).withHour(19).withMinute(0));
        e2.setClassificacaoIndicativa("Livre");
        e2.setEstacionamento(false);
        e2.setValorEstacionamento(BigDecimal.ZERO);
        e2.setTipoAmbito(TipoAmbito.EVENTO);
        e2.setStatus(StatusEvento.PUBLICADO);
        e2.setTematica(educacional != null ? educacional : cultural);
        e2.setProdutor(produtor);
        e2.setLocal(criarLocal("01311-000", "Avenida", "Paulista", "Bela Vista", "São Paulo", "SP", "1374", "Auditório Principal"));

        TipoIngresso t2 = new TipoIngresso();
        t2.setEvento(e2);
        t2.setNome("Inscrição Gratuita");
        t2.setDescricao("Vagas limitadas com certificado de participação.");
        t2.setValor(BigDecimal.ZERO);
        t2.setQuantidadeDisponivel(120);
        t2.setBeneficios("Kit boas-vindas, mentoria e coffee break");
        t2.setLinkCompra("https://camora.dev/inscricoes/hackathon-criativo");
        e2.getTiposIngresso().add(t2);
        eventoRepository.save(e2);

        // Ação Social 3: Mutirão ONG
        Evento e3 = new Evento();
        e3.setNome("Mutirão Verde & Feira de Adoção Solidária");
        e3.setDescricao("Ação comunitária de plantio urbano, oficinas de compostagem e arrecadação de cestas básicas e ração.");
        e3.setDataInicio(LocalDateTime.now().plusDays(7).withHour(8).withMinute(30));
        e3.setDataFim(LocalDateTime.now().plusDays(7).withHour(16).withMinute(0));
        e3.setClassificacaoIndicativa("Livre");
        e3.setEstacionamento(true);
        e3.setValorEstacionamento(BigDecimal.ZERO);
        e3.setTipoAmbito(TipoAmbito.ACAO_SOCIAL);
        e3.setStatus(StatusEvento.PUBLICADO);
        e3.setTematica(social != null ? social : ambiental);
        e3.setOng(ong);
        e3.setLocal(criarLocal("04094-050", "Avenida", "Pedro Álvares Cabral", "Vila Mariana", "São Paulo", "SP", "s/n", "Portão 10 - Parque Ibirapuera"));

        TipoIngresso t3 = new TipoIngresso();
        t3.setEvento(e3);
        t3.setNome("Voluntário / Cota Apoio");
        t3.setDescricao("Participação aberta ao público e opção de contribuição solidária.");
        t3.setValor(BigDecimal.ZERO);
        t3.setQuantidadeDisponivel(300);
        t3.setBeneficios("Certificado de horas complementares");
        t3.setLinkCompra("https://raizesvivas.org.br/mutirao");
        e3.getTiposIngresso().add(t3);
        eventoRepository.save(e3);
    }

    private Local criarLocal(String cep, String tipo, String logradouro, String bairro, String cidade, String uf, String numero, String comp) {
        Local l = new Local();
        l.setCep(cep);
        l.setTipoLogradouro(tipo);
        l.setLogradouro(logradouro);
        l.setBairro(bairro);
        l.setCidade(cidade);
        l.setUf(uf);
        l.setNumero(numero);
        l.setComplemento(comp);
        return l;
    }

    private DadosContato criarContato(String email, String telefone, String redes, String site) {
        DadosContato dc = new DadosContato();
        dc.setEmail(email);
        dc.setTelefone(telefone);
        dc.setRedesSociais(redes);
        dc.setSite(site);
        return dc;
    }
}
