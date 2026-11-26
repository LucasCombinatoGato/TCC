package com.camora;

import com.camora.dto.*;
import com.camora.model.Role;
import com.camora.model.StatusEvento;
import com.camora.model.TipoAmbito;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class CamoraIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarTematicasPublicas() throws Exception {
        mockMvc.perform(get("/api/tematicas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").exists());
    }

    @Test
    void deveRegistrarProdutorECriarPublicarEventoComWhiteLabel() throws Exception {
        RegistroProdutorRequest reg = new RegistroProdutorRequest(
                "11.222.333/0001-81",
                "Produtora Teste Camora",
                "teste.produtor@camora.com",
                "Senha123!",
                new LocalDTO(null, "01310-100", "Avenida", "Paulista", "Bela Vista", "São Paulo", "SP", "100", null),
                new DadosContatoDTO(null, "contato@teste.com", "(11) 91234-5678", "@teste", "https://teste.com"),
                new IdentidadeVisualDTO(null, null, "#471CA8", "#884AB2", "#FF930A", true)
        );

        MvcResult regRes = mockMvc.perform(post("/api/auth/register/produtor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value(Role.ROLE_PRODUCER.name()))
                .andReturn();

        AuthResponse auth = objectMapper.readValue(regRes.getResponse().getContentAsString(), AuthResponse.class);

        EventoRequest eventoReq = new EventoRequest(
                "Encontro Teste Camora",
                "Descrição do evento de integração.",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(4),
                "Livre",
                false,
                BigDecimal.ZERO,
                null,
                TipoAmbito.EVENTO,
                StatusEvento.RASCUNHO,
                1L,
                new LocalDTO(null, "01310-100", "Avenida", "Paulista", "Bela Vista", "São Paulo", "SP", "200", null),
                List.of(),
                List.of(new TipoIngressoDTO(null, "Entrada Franca", "Acesso livre", BigDecimal.ZERO, 100, "Gratuito", "https://camora.dev"))
        );

        MvcResult evtRes = mockMvc.perform(post("/api/eventos")
                        .header("Authorization", "Bearer " + auth.token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventoReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("RASCUNHO"))
                .andReturn();

        EventoResponse criado = objectMapper.readValue(evtRes.getResponse().getContentAsString(), EventoResponse.class);

        mockMvc.perform(patch("/api/eventos/" + criado.id() + "/publicar")
                        .header("Authorization", "Bearer " + auth.token()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PUBLICADO"));

        mockMvc.perform(get("/api/eventos/" + criado.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Encontro Teste Camora"));
    }
}
