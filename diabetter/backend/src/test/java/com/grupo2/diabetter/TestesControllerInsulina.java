package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grupo2.diabetter.exception.ErrorHandlingControllerAdvice;
import com.grupo2.diabetter.model.Horario;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo2.diabetter.enuns.TipoInsulina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.grupo2.diabetter.controller.InsulinController;
import com.grupo2.diabetter.dto.insulina.InsulinDeleteResponseDTO;
import com.grupo2.diabetter.dto.insulina.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import com.grupo2.diabetter.exception.CommerceException;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulina.AtualizarInsulinService;
import com.grupo2.diabetter.service.insulina.CriarInsulinService;
import com.grupo2.diabetter.service.insulina.DeletarInsulinService;
import com.grupo2.diabetter.service.insulina.ListarInsulinService;
import com.grupo2.diabetter.service.insulina.RecuperarInsulinService;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class TestesControllerInsulina {

    private MockMvc mockMvc;

    @Mock
    private AtualizarInsulinService atualizarInsulinService;

    @Mock
    private CriarInsulinService criarInsulinService;

    @Mock
    private DeletarInsulinService deletarInsulinService;

    @Mock
    private ListarInsulinService listarInsulinService;

    @Mock
    private RecuperarInsulinService recuperarInsulinService;

    @InjectMocks
    private InsulinController insulinController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(insulinController)
                .setControllerAdvice(new ErrorHandlingControllerAdvice())
                .build();
    }

    @Nested
    @DisplayName("Casos de testes de criação de insulina")
    class CriarInsulina {

        @Test
        @DisplayName("Cria insulina com dados válidos")
        void testCreateInsulinWithValidData(){
            UUID horarioId = UUID.randomUUID();

            // Criação do DTO usando os nomes corretos
            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)  // supondo que NOVORAPIDO seja um valor válido do enum
                    .unidades(10)
                    .horarioId(horarioId)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            // Criação da resposta esperada com os nomes corretos
            InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
                    .insulidaId(UUID.randomUUID())
                    .tipoInsulina(dto.getTipoInsulina())
                    .unidades(dto.getUnidades())
                    .horarioId(dto.getHorarioId())
                    .dataAplicacao(dto.getDataAplicacao())
                    .build();

            when(criarInsulinService.criarInsulina(any(InsulinPostPutRequestDTO.class))).thenReturn(responseDTO);

            ResponseEntity<InsulinResponseDTO> response = insulinController.criarInsulina(dto);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(horarioId, response.getBody().getHorarioId());
        }

        @Test
        @DisplayName("Cria insulina com tipo inválido")
        void testCreateInsulinWithInvalidType() {
            UUID horarioId = UUID.randomUUID();

            // Para simular tipo inválido, passamos null ou outro valor inadequado
            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(null)
                    .unidades(10)
                    .horarioId(horarioId)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(criarInsulinService.criarInsulina(any(InsulinPostPutRequestDTO.class)))
                    .thenThrow(new CommerceException("Tipo de insulina inválido"));

            CommerceException exception = assertThrows(CommerceException.class, () -> {
                insulinController.criarInsulina(dto);
            });

            assertEquals("Tipo de insulina inválido", exception.getMessage());
        }

        @Test
        @DisplayName("Cria insulina com unidades inválidas")
        void testCreateInsulinWithInvalidUnits() {
            UUID horarioId = UUID.randomUUID();

            // Usando os nomes corretos no builder
            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(-5)  // Unidades inválidas (negativas)
                    .horarioId(horarioId)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(criarInsulinService.criarInsulina(any(InsulinPostPutRequestDTO.class)))
                    .thenThrow(new CommerceException("Unidades de insulina inválidas"));

            CommerceException exception = assertThrows(CommerceException.class, () -> {
                insulinController.criarInsulina(dto);
            });

            assertEquals("Unidades de insulina inválidas", exception.getMessage());
        }

        @Test
        @DisplayName("Cria insulina sem horário")
        void testCreateInsulinWithoutSchedule() {
            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(10)
                    .horarioId(null) // Horário inválido (nulo)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(criarInsulinService.criarInsulina(any(InsulinPostPutRequestDTO.class)))
                    .thenThrow(new CommerceException("Horário de insulina inválido"));

            CommerceException exception = assertThrows(CommerceException.class, () -> {
                insulinController.criarInsulina(dto);
            });

            assertEquals("Horário de insulina inválido", exception.getMessage());
        }

        @Test
        @DisplayName("Cria insulina com erro interno")
        void testCreateInsulinWithInternalError() {
            UUID horarioId = UUID.randomUUID();

            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(10)
                    .horarioId(horarioId)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(criarInsulinService.criarInsulina(any(InsulinPostPutRequestDTO.class)))
                    .thenThrow(new RuntimeException("Erro interno no servidor"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                insulinController.criarInsulina(dto);
            });

            assertEquals("Erro interno no servidor", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de leitura de insulina")
    class LeituraInsulina {
        @Test
        @DisplayName("Lê insulina com dados válidos")
        void testReadInsulinWithValidId(){
            UUID horarioId = UUID.randomUUID();
            UUID insulinId = UUID.randomUUID();

            InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
                    .insulidaId(insulinId)
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(10)
                    .horarioId(horarioId)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(recuperarInsulinService.recuperarInsulina(insulinId)).thenReturn(responseDTO);

            ResponseEntity<InsulinResponseDTO> response = insulinController.recuperarInsulina(insulinId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(insulinId, response.getBody().getInsulidaId());
            assertEquals(horarioId, response.getBody().getHorarioId());
        }

        @Test
        @DisplayName("Leitura de insulina com dados inválidos")
        void testReadInsulinWithInvalidData(){
            UUID insulinId = UUID.randomUUID();

            when(recuperarInsulinService.recuperarInsulina(insulinId))
                    .thenThrow(new RuntimeException("Insulina não encontrada"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                insulinController.recuperarInsulina(insulinId);
            });
            assertEquals("Insulina não encontrada", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Casos de listar insulina")
    class ListarInsulina {
        @Test
        @DisplayName("Listar insulinas registradas")
        void testListAllInsulinWithRecords(){
            UUID horarioId1 = UUID.randomUUID();
            UUID horarioId2 = UUID.randomUUID();

            List<InsulinResponseDTO> insulinList = List.of(
                    InsulinResponseDTO.builder()
                            .insulidaId(UUID.randomUUID())
                            .tipoInsulina(TipoInsulina.RAPIDA)
                            .unidades(10)
                            .horarioId(horarioId1)
                            .dataAplicacao(LocalDateTime.now())
                            .build(),
                    InsulinResponseDTO.builder()
                            .insulidaId(UUID.randomUUID())
                            .tipoInsulina(TipoInsulina.RAPIDA)
                            .unidades(20)
                            .horarioId(horarioId2)
                            .dataAplicacao(LocalDateTime.now())
                            .build()
            );

            when(listarInsulinService.listarTodasInsulinas()).thenReturn(insulinList);

            ResponseEntity<List<InsulinResponseDTO>> response = insulinController.listarTodasInsulinas();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertEquals(horarioId1, response.getBody().get(0).getHorarioId());
            assertEquals(horarioId2, response.getBody().get(1).getHorarioId());
        }

        @Test
        @DisplayName("Listar todas as insulinas quando não há registros")
        void testListAllInsulinWithoutRecords(){
            when(listarInsulinService.listarTodasInsulinas())
                    .thenThrow(new NotFoundException("Não existem registros"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                insulinController.listarTodasInsulinas();
            });

            assertEquals("Não existem registros", exception.getMessage());
        }

        @Test
        @DisplayName("Listar insulinas com erro interno")
        void testListAllInsulinWithInternalError(){
            when(listarInsulinService.listarTodasInsulinas())
                    .thenThrow(new RuntimeException("Erro interno no servidor"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                insulinController.listarTodasInsulinas();
            });

            assertEquals("Erro interno no servidor", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Casos de atualização de insulina")
    class UpdateInsulin {
        @Test
        @DisplayName("Atualizar insulina com dados válidos")
        void testUpdateInsulinWithValidData(){
            UUID insulinId = UUID.randomUUID();
            UUID horarioId = UUID.randomUUID();

            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(15)
                    .horarioId(horarioId)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
                    .insulidaId(insulinId)
                    .tipoInsulina(TipoInsulina.BASAL)  // Supondo que a atualização mude o tipo
                    .unidades(15)
                    .horarioId(horarioId)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(atualizarInsulinService.atualizarInsulina(insulinId, dto)).thenReturn(responseDTO);

            ResponseEntity<InsulinResponseDTO> response = insulinController.atualizarInsulina(insulinId, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(insulinId, response.getBody().getInsulidaId());
            assertEquals(TipoInsulina.BASAL, response.getBody().getTipoInsulina());
            assertEquals(15, response.getBody().getUnidades(), 0.001);
            assertEquals(horarioId, response.getBody().getHorarioId());

            verify(atualizarInsulinService, times(1)).atualizarInsulina(insulinId, dto);
        }

        @Test
        @DisplayName("Atualização de insulina com ID inválido")
        void testUpdateInsulinWithInvalidID(){
            UUID invalidId = UUID.randomUUID();
            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(15)
                    .horarioId(UUID.randomUUID())
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(atualizarInsulinService.atualizarInsulina(invalidId, dto))
                    .thenThrow(new RuntimeException("Insulina não encontrada"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                insulinController.atualizarInsulina(invalidId, dto);
            });

            assertEquals("Insulina não encontrada", exception.getMessage());
            verify(atualizarInsulinService, times(1)).atualizarInsulina(invalidId, dto);
        }

        @Test
        @DisplayName("Teste de atualização de insulina com tipo inválido")
        void testUpdateInsulinWithInvalidType(){
            UUID insulinId = UUID.randomUUID();
            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(null) // Tipo inválido
                    .unidades(15)
                    .horarioId(UUID.randomUUID())
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(atualizarInsulinService.atualizarInsulina(insulinId, dto))
                    .thenThrow(new CommerceException("Tipo de insulina inválido"));

            CommerceException exception = assertThrows(CommerceException.class, () -> {
                insulinController.atualizarInsulina(insulinId, dto);
            });

            assertEquals("Tipo de insulina inválido", exception.getMessage());
            verify(atualizarInsulinService, times(1)).atualizarInsulina(insulinId, dto);
        }

        @Test
        @DisplayName("Atualização de insulina com unidades inválidas")
        void testUpdateInsulinWithInvalidUnits() {
            UUID insulinId = UUID.randomUUID();
            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(-5) // Unidades inválidas
                    .horarioId(UUID.randomUUID())
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(atualizarInsulinService.atualizarInsulina(insulinId, dto))
                    .thenThrow(new CommerceException("Unidades de insulina inválidas"));

            CommerceException exception = assertThrows(CommerceException.class, () -> {
                insulinController.atualizarInsulina(insulinId, dto);
            });

            assertEquals("Unidades de insulina inválidas", exception.getMessage());
            verify(atualizarInsulinService, times(1)).atualizarInsulina(insulinId, dto);
        }

        @Test
        @DisplayName("Atualização de insulina com horário inválido")
        void testUpdateInsulinWithInvalidSchedule() {
            UUID insulinId = UUID.randomUUID();
            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(15)
                    .horarioId(null) // Horário inválido (nulo)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(atualizarInsulinService.atualizarInsulina(insulinId, dto))
                    .thenThrow(new CommerceException("Horário de insulina inválido"));

            CommerceException exception = assertThrows(CommerceException.class, () -> {
                insulinController.atualizarInsulina(insulinId, dto);
            });

            assertEquals("Horário de insulina inválido", exception.getMessage());
            verify(atualizarInsulinService, times(1)).atualizarInsulina(insulinId, dto);
        }

        @Test
        @DisplayName("Atualização de insulina sem alterações")
        void testUpdateInsulinWithoutChanges() {
            UUID insulinId = UUID.randomUUID();
            UUID horarioId = UUID.randomUUID();

            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(15)
                    .horarioId(horarioId)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
                    .insulidaId(insulinId)
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(15)
                    .horarioId(horarioId)
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(atualizarInsulinService.atualizarInsulina(insulinId, dto)).thenReturn(responseDTO);

            ResponseEntity<InsulinResponseDTO> response = insulinController.atualizarInsulina(insulinId, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(insulinId, response.getBody().getInsulidaId());
            assertEquals(TipoInsulina.RAPIDA, response.getBody().getTipoInsulina());
            assertEquals(15, response.getBody().getUnidades(), 0.001);
            assertEquals(horarioId, response.getBody().getHorarioId());

            verify(atualizarInsulinService, times(1)).atualizarInsulina(insulinId, dto);
        }

        @Test
        @DisplayName("Atualização de insulina com erro interno")
        void testUpdateInsulinWithInternalError() {
            UUID insulinId = UUID.randomUUID();
            InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(15)
                    .horarioId(UUID.randomUUID())
                    .dataAplicacao(LocalDateTime.now())
                    .build();

            when(atualizarInsulinService.atualizarInsulina(insulinId, dto))
                    .thenThrow(new RuntimeException("Erro interno no servidor"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                insulinController.atualizarInsulina(insulinId, dto);
            });

            assertEquals("Erro interno no servidor", exception.getMessage());
            verify(atualizarInsulinService, times(1)).atualizarInsulina(insulinId, dto);
        }
    }

    @Test
    @DisplayName("POST /api/insulin - cria insulina com sucesso")
    void testCriarInsulinaComSucesso() throws Exception {
        InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(UUID.randomUUID())
                .glicemia(UUID.randomUUID())
                .dataAplicacao(LocalDateTime.now())
                .build();

        InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
                .insulidaId(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .build();

        Mockito.when(criarInsulinService.criarInsulina(any(InsulinPostPutRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/insulin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.insulidaId").value(responseDTO.getInsulidaId().toString()));
    }

    @Test
    @DisplayName("POST /api/insulin - falha ao criar insulina com tipo nulo")
    void testCriarInsulinaFalha() throws Exception {
        InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(null)
                .unidades(10)
                .horarioId(UUID.randomUUID())
                .glicemia(UUID.randomUUID())
                .dataAplicacao(LocalDateTime.now())
                .build();

        Mockito.when(criarInsulinService.criarInsulina(any(InsulinPostPutRequestDTO.class)))
                .thenThrow(new CommerceException("Tipo de insulina inválido"));

        mockMvc.perform(post("/api/insulin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Tipo de insulina inválido"));

        verify(criarInsulinService, times(1)).criarInsulina(any(InsulinPostPutRequestDTO.class));
    }

    @Test
    @DisplayName("GET /api/insulin/{id} - recupera insulina com sucesso")
    void testRecuperarInsulinaComSucesso() throws Exception {
        UUID insulinId = UUID.randomUUID();
        InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
                .insulidaId(insulinId)
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .build();

        Mockito.when(recuperarInsulinService.recuperarInsulina(insulinId))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/api/insulin/{id}", insulinId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insulidaId").value(insulinId.toString()))
                .andExpect(jsonPath("$.tipoInsulina").value("RAPIDA"))
                .andExpect(jsonPath("$.unidades").value(10));

        verify(recuperarInsulinService, times(1)).recuperarInsulina(insulinId);
    }

    @Test
    @DisplayName("GET /api/insulin/{id} - insulina não encontrada")
    void testRecuperarInsulinaNaoEncontrada() throws Exception {
        UUID insulinId = UUID.randomUUID();

        Mockito.when(recuperarInsulinService.recuperarInsulina(insulinId))
                .thenThrow(new NotFoundException("Insulina não encontrada"));

        mockMvc.perform(get("/api/insulin/{id}", insulinId))
                .andExpect(status().isNotFound());

        verify(recuperarInsulinService, times(1)).recuperarInsulina(insulinId);
    }

    @Test
    @DisplayName("GET /api/insulin - lista todas as insulinas")
    void testListarTodasInsulinas() throws Exception {
        InsulinResponseDTO r1 = InsulinResponseDTO.builder()
                .insulidaId(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(UUID.randomUUID())
                .horario(null) // ou crie um objeto Horario se necessário
                .glicemia(new Glicemia()) // considerando que Glicemia possui um construtor padrão para teste
                .dataAplicacao(LocalDateTime.now())
                .build();

        InsulinResponseDTO r2 = InsulinResponseDTO.builder()
                .insulidaId(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.BASAL)
                .unidades(20)
                .horarioId(UUID.randomUUID())
                .horario(null) // ou crie um objeto Horario se necessário
                .glicemia(null) // para testar o caminho onde a glicemia é nula
                .dataAplicacao(LocalDateTime.now())
                .build();

        Mockito.when(listarInsulinService.listarTodasInsulinas())
                .thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/insulin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].insulidaId").value(r1.getInsulidaId().toString()))
                .andExpect(jsonPath("$[1].insulidaId").value(r2.getInsulidaId().toString()));

        verify(listarInsulinService, times(1)).listarTodasInsulinas();
    }

    @Test
    @DisplayName("GET /api/insulin/horario/{horarioId} - lista insulinas por horário")
    void testListarInsulinaPorHorario() throws Exception {
        UUID horarioId = UUID.randomUUID();

        InsulinResponseDTO r1 = InsulinResponseDTO.builder()
                .insulidaId(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(UUID.randomUUID())
                .horario(Horario.builder()
                        .id(UUID.randomUUID())
                        .horario("08:00")
                        .data_criacao("2025-03-22")
                        .build())
                .glicemia(Glicemia.builder()
                        .id(UUID.randomUUID())
                        .valorGlicemia(120.0f)
                        .build())
                .dataAplicacao(LocalDateTime.now())
                .build();

        InsulinResponseDTO r2 = InsulinResponseDTO.builder()
                .insulidaId(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.BASAL)
                .unidades(20)
                .horarioId(UUID.randomUUID())
                .horario(Horario.builder()
                        .id(UUID.randomUUID())
                        .horario("09:00")
                        .data_criacao("2025-03-22")
                        .build())
                .glicemia(null) // para testar o caso sem glicemia associada
                .dataAplicacao(LocalDateTime.now())
                .build();

        Mockito.when(listarInsulinService.listarInsulinaPorHorario(horarioId))
                .thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/insulin/horario/{horarioId}", horarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].insulidaId").value(r1.getInsulidaId().toString()))
                .andExpect(jsonPath("$[1].insulidaId").value(r2.getInsulidaId().toString()));

        verify(listarInsulinService, times(1)).listarInsulinaPorHorario(horarioId);
    }

    @Test
    @DisplayName("PUT /api/insulin/{id} - atualiza insulina com sucesso")
    void testAtualizarInsulinaComSucesso() throws Exception {
        UUID insulinId = UUID.randomUUID();
        InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .glicemia(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .dataAplicacao(LocalDateTime.now())
                .build();

        InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
                .insulidaId(UUID.fromString("33333333-3333-3333-3333-333333333333"))
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .horario(Horario.builder()
                        .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                        .horario("08:00")
                        .data_criacao("2025-03-22")
                        .build())
                .glicemia(Glicemia.builder()
                        .id(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                        .valorGlicemia(120.0f)
                        .build())
                .dataAplicacao(LocalDateTime.now())
                .build();

        Mockito.when(atualizarInsulinService.atualizarInsulina(eq(insulinId), any(InsulinPostPutRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/insulin/{id}", insulinId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insulidaId").value(responseDTO.getInsulidaId().toString()));

        verify(atualizarInsulinService, times(1)).atualizarInsulina(eq(insulinId), any(InsulinPostPutRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/insulin/{id} - deleta insulina com sucesso")
    void testDeletarInsulinaComSucesso() throws Exception {
        UUID insulinId = UUID.randomUUID();

        InsulinDeleteResponseDTO responseDTO = InsulinDeleteResponseDTO.builder()
                .mensagem("Insulin deletada com sucesso")
                .build();

        Mockito.when(deletarInsulinService.deletarInsulina(insulinId))
                .thenReturn(responseDTO);

        mockMvc.perform(delete("/api/insulin/{id}", insulinId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Insulin deletada com sucesso"));

        verify(deletarInsulinService, times(1)).deletarInsulina(insulinId);
    }


    @Test
    @DisplayName("PUT /api/insulin/{id} - falha ao atualizar insulina")
    void testAtualizarInsulinaFalha() throws Exception {
        UUID insulinId = UUID.randomUUID();
        InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(null) // causa erro
                .unidades(15)
                .build();

        Mockito.when(atualizarInsulinService.atualizarInsulina(eq(insulinId), any(InsulinPostPutRequestDTO.class)))
                .thenThrow(new CommerceException("Tipo de insulina inválido"));

        mockMvc.perform(put("/api/insulin/{id}", insulinId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(atualizarInsulinService, times(1)).atualizarInsulina(eq(insulinId), any(InsulinPostPutRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/insulin/{id} - insulina não encontrada")
    void testDeletarInsulinaNaoEncontrada() throws Exception {
        UUID insulinId = UUID.randomUUID();

        Mockito.when(deletarInsulinService.deletarInsulina(insulinId))
                .thenThrow(new NotFoundException("Insulina não encontrada"));

        mockMvc.perform(delete("/api/insulin/{id}", insulinId))
                .andExpect(status().isNotFound());

        verify(deletarInsulinService, times(1)).deletarInsulina(insulinId);
    }
}