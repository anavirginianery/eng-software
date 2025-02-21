package com.grupo2.diabetter;

import com.grupo2.diabetter.controller.HorarioController;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.service.horario.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class HorarioControllerTests {

    @Mock
    private CriarHorarioService criarHorarioService;
    
    @Mock
    private AtualizarHorarioService atualizarHorarioService;
    
    @Mock
    private DeletarHorarioService deletarHorarioService;
    
    @Mock
    private ListarHorarioService listarHorarioService;
    
    @Mock
    private RecuperarHorarioService recuperarHorarioService;

    @InjectMocks
    private HorarioController horarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Conjunto de casos de criar horário")
    class CriacaoHorario {
        @Test
        void testCriarHorarioComDadosValidos() {
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("08:00", "2025-02-20", 1L);

            HorarioResponseDTO horarioSalvo = new HorarioResponseDTO(UUID.randomUUID(), dto.getValue(), dto.getDate(), dto.getUserId());

            when(criarHorarioService.createHorario(any(HorarioPostPutRequestDTO.class))).thenReturn(horarioSalvo);

            ResponseEntity<?> response = horarioController.createHorario(dto);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(horarioSalvo, response.getBody());
        }

        @Test
        void testCriarHorarioComDadosInvalidos() {
            when(criarHorarioService.createHorario(any(HorarioPostPutRequestDTO.class)))
                    .thenThrow(new IllegalArgumentException("Dados inválidos"));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                horarioController.createHorario(null);
            });

            assertEquals("Dados inválidos", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de ler horário")
    class LeituraHorario {
        @Test
        void testReadHorarioComIdValido() {
            UUID id = UUID.randomUUID();
            HorarioResponseDTO responseDTO = new HorarioResponseDTO(id, "08:00", "2025-02-21", 1L);

            when(recuperarHorarioService.recuperarHorario(id)).thenReturn(responseDTO);

            ResponseEntity<?> response = horarioController.readHorario(id);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }

        @Test
        void testReadHorarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();

            when(recuperarHorarioService.recuperarHorario(idInvalido)).thenThrow(new NotFoundException("Horario not found"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                horarioController.readHorario(idInvalido);
            });

            assertEquals("Horario not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de atualizar horário")
    class AtualizacaoHorario {
        @Test
        void testAtualizarHorarioComIdValido() {
            UUID id = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("10:00", "2025-02-21", 1L);
            HorarioResponseDTO horarioAtualizado = new HorarioResponseDTO(id, dto.getValue(), dto.getDate(), dto.getUserId());

            when(atualizarHorarioService.updateHorario(eq(id), any(HorarioPostPutRequestDTO.class))).thenReturn(horarioAtualizado);

            ResponseEntity<?> response = horarioController.updateHorario(id, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(horarioAtualizado, response.getBody());
        }

        @Test
        void testAtualizarHorarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("10:00", "2025-02-21", 1L);

            when(atualizarHorarioService.updateHorario(eq(idInvalido), any(HorarioPostPutRequestDTO.class)))
                    .thenThrow(new NotFoundException("Horario not found"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                horarioController.updateHorario(idInvalido, dto);
            });

            assertEquals("Horario not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de deletar horário")
    class DelecaoHorario {
        @Test
        void testDeletarHorarioComSucesso() {
            UUID id = UUID.randomUUID();
            doNothing().when(deletarHorarioService).deletarHorario(id);

            ResponseEntity<?> response = horarioController.disableHorario(id);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        void testDeletarHorarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            doThrow(new NotFoundException("Horario not found")).when(deletarHorarioService).deletarHorario(idInvalido);

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                horarioController.disableHorario(idInvalido);
            });

            assertEquals("Horario not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de listar horários por usuário")
    class ListagemHorario {
        @Test
        void testListarHorariosComUsuarioValido() {
            Long userId = 1L;

            List<HorarioResponseDTO> horariosMockados = Arrays.asList(
                new HorarioResponseDTO(UUID.randomUUID(), "08:00", "2025-02-21", userId),
                new HorarioResponseDTO(UUID.randomUUID(), "14:00", "2025-02-22", userId)
            );

            when(listarHorarioService.listarHorario(userId)).thenReturn(horariosMockados);

            ResponseEntity<?> response = horarioController.listarHorarios(userId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(horariosMockados, response.getBody());
        }

        @Test
        void testListarHorariosUsuarioSemHorarios() {
            Long userId = 1L;

            when(listarHorarioService.listarHorario(userId)).thenReturn(Collections.emptyList());

            ResponseEntity<?> response = horarioController.listarHorarios(userId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(((List<?>) response.getBody()).isEmpty());
        }
    }
}
