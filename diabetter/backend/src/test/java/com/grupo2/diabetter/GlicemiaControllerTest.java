package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.model.Horario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.grupo2.diabetter.controller.GlicemiaController;
import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.exception.ErrorHandlingControllerAdvice;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.AtualizarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.CriarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.DeletarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.ListarGlicemiasService;
import com.grupo2.diabetter.service.glicemia.RecuperarGlicemiaService;

@SpringBootTest
class GlicemiaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GlicemiaRepository glicemiaRepository;

    @Mock
    private CriarGlicemiaService criarGlicemiaService;

    @Mock
    private DeletarGlicemiaService deletarGlicemiaService;

    @Mock
    private ListarGlicemiasService listarGlicemiasService;

    @Mock
    private AtualizarGlicemiaService atualizarGlicemiaService;

    @Mock
    private RecuperarGlicemiaService recuperarGlicemiaService;

    @InjectMocks
    private GlicemiaController glicemiaController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(glicemiaController)
                .setControllerAdvice(new ErrorHandlingControllerAdvice())
                .build();
    }

    @Nested
    @DisplayName("Conjunto de casos de criar glicemia")
    class UsuarioCriacao {

        @Test
        void testCreateBloodSugarWithValidData() throws Exception {
            // Prepare valid data
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(120.0f);  // Use the correct field name

            GlicemiaResponseDTO glicemia = new GlicemiaResponseDTO();
            glicemia.setId(UUID.randomUUID());
            glicemia.setValorGlicemia(dto.getValorGlicemia());

            when(criarGlicemiaService.executar(any(GlicemiaPostPutRequestDto.class))).thenReturn(glicemia);

            // Execute and validate response
            ResponseEntity<GlicemiaResponseDTO> response = glicemiaController.criarGlicemia(dto);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(glicemia.getId(), response.getBody().getId());
            assertEquals(glicemia.getValorGlicemia(), response.getBody().getValorGlicemia(), 0.001);
        }

        @Test
        @DisplayName("Tenta criar uma glicemia e ocorre um erro interno")
        void testCreateBloodSugarWithInternalError() {
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(120.0f);

            when(criarGlicemiaService.executar(any(GlicemiaPostPutRequestDto.class)))
                    .thenThrow(new RuntimeException("Erro interno ao criar glicemia"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                glicemiaController.criarGlicemia(dto);
            });

            assertEquals("Erro interno ao criar glicemia", exception.getMessage());
            verify(criarGlicemiaService, times(1)).executar(any(GlicemiaPostPutRequestDto.class));
        }

        @Test
        @DisplayName("Tenta criar uma glicemia com dados inválidos")
        void testCreateBloodSugarWithInvalidData() throws Exception {
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(-10.0f);  // Invalid glicemia value

            glicemiaController.criarGlicemia(dto);
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de lê glicemia")
    class LeituraGlicemia {

        @Test
        @DisplayName("Recupera uma glicemia com ID válido")
        void testReadBloodSugarWithValidId() {
            UUID validId = UUID.randomUUID();
            GlicemiaResponseDTO glicemia = new GlicemiaResponseDTO();
            glicemia.setId(validId);
            glicemia.setValorGlicemia(120.0f);
            glicemia.setHorario(UUID.randomUUID()); // Assuming Horario is properly set

            when(recuperarGlicemiaService.executar(validId)).thenReturn(glicemia);

            ResponseEntity<GlicemiaResponseDTO> response = glicemiaController.recuperarGlicemia(validId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(validId, response.getBody().getId());
            assertEquals(120.0f, response.getBody().getValorGlicemia(), 0.001);
        }

        @Test
        @DisplayName("Tenta recuperar uma glicemia e ocorre um erro interno")
        void testReadBloodSugarWithInternalError() {
            UUID validId = UUID.randomUUID();

            when(recuperarGlicemiaService.executar(validId))
                    .thenThrow(new RuntimeException("Erro interno ao recuperar glicemia"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                glicemiaController.recuperarGlicemia(validId);
            });

            assertEquals("Erro interno ao recuperar glicemia", exception.getMessage());
            verify(recuperarGlicemiaService, times(1)).executar(validId);
        }

        @Test
        @DisplayName("Tenta recuperar uma glicemia com ID inválido")
        void testReadBloodSugarWithInvalidId() {
            UUID invalidId = UUID.randomUUID();

            when(recuperarGlicemiaService.executar(invalidId)).thenThrow(new RuntimeException("Glicemia não encontrada"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                glicemiaController.recuperarGlicemia(invalidId);
            });

            assertEquals("Glicemia não encontrada", exception.getMessage());
        }

        @Test
        @DisplayName("Lista todas as glicemias com sucesso")
        void testListAllBloodSugars() throws Exception {
            // Prepare mock data
            GlicemiaResponseDTO glicemia1 = new GlicemiaResponseDTO();
            glicemia1.setId(UUID.randomUUID());
            glicemia1.setValorGlicemia(120.0f);

            GlicemiaResponseDTO glicemia2 = new GlicemiaResponseDTO();
            glicemia2.setId(UUID.randomUUID());
            glicemia2.setValorGlicemia(130.0f);

            List<GlicemiaResponseDTO> glicemias = List.of(glicemia1, glicemia2);

            when(listarGlicemiasService.executar()).thenReturn(glicemias);

            // Execute the test
            ResponseEntity<List<GlicemiaResponseDTO>> response = glicemiaController.listarGlicemias();

            // Validate the result
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertEquals(glicemia1.getValorGlicemia(), response.getBody().get(0).getValorGlicemia(), 0.001);
            assertEquals(glicemia2.getValorGlicemia(), response.getBody().get(1).getValorGlicemia(), 0.001);
        }

        @Test
        @DisplayName("Retorna uma lista vazia quando não há glicemias")
        void testListAllBloodSugarsWhenEmpty() throws Exception {
            // Prepare mock data
            List<GlicemiaResponseDTO> glicemias = List.of();

            when(listarGlicemiasService.executar()).thenReturn(glicemias);

            // Execute the test
            ResponseEntity<List<GlicemiaResponseDTO>> response = glicemiaController.listarGlicemias();

            // Validate the result
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
        }

        @Test
        @DisplayName("Lista glicemias filtradas por ID de horário com sucesso")
        void testListBloodSugarsByHorarioId() throws Exception {
            UUID horarioId = UUID.randomUUID();

            // Create mock Horario object
            Horario horario = new Horario();
            horario.setId(horarioId);

            // Create GlicemiaResponseDTO objects with the correct Horario
            GlicemiaResponseDTO glicemia1 = new GlicemiaResponseDTO();
            glicemia1.setId(UUID.randomUUID());
            glicemia1.setValorGlicemia(120.0f);
            glicemia1.setHorario(horario.getId());  // Set Horario object directly

            GlicemiaResponseDTO glicemia2 = new GlicemiaResponseDTO();
            glicemia2.setId(UUID.randomUUID());
            glicemia2.setValorGlicemia(130.0f);
            glicemia2.setHorario(horario.getId());  // Set Horario object directly

            // Mock service to return the list of GlicemiaResponseDTOs
            List<GlicemiaResponseDTO> glicemias = List.of(glicemia1, glicemia2);
            when(listarGlicemiasService.listarGlicemiaByHorario(horarioId)).thenReturn(glicemias);

            // Execute the test by calling the controller method
            ResponseEntity<List<GlicemiaResponseDTO>> response = glicemiaController.listarGlicemiaByHorario(horarioId);

            // Validate the result
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            // Check that the horarioId matches for both glicemias
            assertEquals(horarioId, response.getBody().get(0).getHorario());
            assertEquals(horarioId, response.getBody().get(1).getHorario());
        }

        @Test
        @DisplayName("Retorna uma lista vazia quando não há glicemias para o horário informado")
        void testListBloodSugarsByHorarioIdWhenEmpty() throws Exception {
            UUID horarioId = UUID.randomUUID();

            // Prepare mock data
            List<GlicemiaResponseDTO> glicemias = List.of();

            when(listarGlicemiasService.listarGlicemiaByHorario(horarioId)).thenReturn(glicemias);

            // Execute the test
            ResponseEntity<List<GlicemiaResponseDTO>> response = glicemiaController.listarGlicemiaByHorario(horarioId);

            // Validate the result
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de atualizar glicemia")
    class GlicemiaUpdate {

        @Test
        @DisplayName("Atualiza uma glicemia com dados válidos")
        void testUpdateWithValidData() {
            UUID validId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(130.0f);

            GlicemiaResponseDTO glicemiaAtualizada = new GlicemiaResponseDTO();
            glicemiaAtualizada.setId(validId);
            glicemiaAtualizada.setValorGlicemia(dto.getValorGlicemia());
            glicemiaAtualizada.setHorario(UUID.randomUUID());

            when(atualizarGlicemiaService.executar(validId, dto)).thenReturn(glicemiaAtualizada);

            ResponseEntity<GlicemiaResponseDTO> response = glicemiaController.atualizarGlicemia(validId, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(validId, response.getBody().getId());
            assertEquals(130.0f, response.getBody().getValorGlicemia(), 0.001);
        }

        @Test
        @DisplayName("Tenta atualizar uma glicemia com ID inválido")
        void testUpdateWithInvalidId() {
            UUID invalidId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(130.0f);

            when(atualizarGlicemiaService.executar(invalidId, dto))
                    .thenThrow(new NotFoundException("Glicemia não encontrada"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                glicemiaController.atualizarGlicemia(invalidId, dto);
            });

            assertEquals("Glicemia não encontrada", exception.getMessage());
        }

        @Test
        @DisplayName("Tenta atualizar uma glicemia e ocorre um erro interno")
        void testUpdateBloodSugarWithInternalError() {
            UUID validId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(130.0f);

            when(atualizarGlicemiaService.executar(validId, dto))
                    .thenThrow(new RuntimeException("Erro interno ao atualizar glicemia"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                glicemiaController.atualizarGlicemia(validId, dto);
            });

            assertEquals("Erro interno ao atualizar glicemia", exception.getMessage());
            verify(atualizarGlicemiaService, times(1)).executar(validId, dto);
        }

        @Test
        @DisplayName("Atualiza uma glicemia sem alterar o valor da medição")
        void testUpdateBloodSugarWithoutChange() {
            UUID validId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(120.0f);

            GlicemiaResponseDTO glicemiaAtualizada = new GlicemiaResponseDTO();
            glicemiaAtualizada.setId(validId);
            glicemiaAtualizada.setValorGlicemia(dto.getValorGlicemia());
            glicemiaAtualizada.setHorario(UUID.randomUUID());

            when(atualizarGlicemiaService.executar(validId, dto)).thenReturn(glicemiaAtualizada);

            ResponseEntity<GlicemiaResponseDTO> response = glicemiaController.atualizarGlicemia(validId, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(validId, response.getBody().getId());
            assertEquals(120.0f, response.getBody().getValorGlicemia(), 0.001);
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de deletar glicemia")
    class DeletarGlicemia {

        @Test
        @DisplayName("Desativa uma glicemia com sucesso")
        void testDisableBloodSugarSuccessfully() {
            UUID validId = UUID.randomUUID();

            doNothing().when(deletarGlicemiaService).executar(validId);

            ResponseEntity<?> response = glicemiaController.removerGlicemia(validId);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(deletarGlicemiaService, times(1)).executar(validId);
        }

        @Test
        @DisplayName("Tenta desativar uma glicemia com ID inválido")
        void testDisableBloodSugarWithInvalidId() {
            UUID invalidId = UUID.randomUUID();

            doThrow(new NotFoundException("Glicemia não encontrada")).when(deletarGlicemiaService).executar(invalidId);

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                glicemiaController.removerGlicemia(invalidId);
            });

            assertEquals("Glicemia não encontrada", exception.getMessage());
            verify(deletarGlicemiaService, times(1)).executar(invalidId);
        }

        @Test
        @DisplayName("Tenta desativar uma glicemia e ocorre um erro interno")
        void testDisableBloodSugarWithInternalError() {
            UUID validId = UUID.randomUUID();

            doThrow(new RuntimeException("Erro interno ao desativar glicemia"))
                    .when(deletarGlicemiaService).executar(validId);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                glicemiaController.removerGlicemia(validId);
            });

            assertEquals("Erro interno ao desativar glicemia", exception.getMessage());
            verify(deletarGlicemiaService, times(1)).executar(validId);
        }

        @Test
        @DisplayName("Tenta desativar uma glicemia que já foi desativada")
        void testDisableBloodSugarJaDesativado() {
            UUID validId = UUID.randomUUID();

            doThrow(new NotFoundException("Glicemia já desativada")).when(deletarGlicemiaService).executar(validId);

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                glicemiaController.removerGlicemia(validId);
            });

            assertEquals("Glicemia já desativada", exception.getMessage());
            verify(deletarGlicemiaService, times(1)).executar(validId);
        }
    }
}
