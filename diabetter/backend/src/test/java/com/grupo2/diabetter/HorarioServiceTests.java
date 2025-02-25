package com.grupo2.diabetter;

import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HorarioServiceTests {

    @Mock
    private HorarioRepository horarioRepository;

    @InjectMocks
    private CriarHorarioService criarHorarioService;

    @InjectMocks
    private AtualizarHorarioService atualizarHorarioService;

    @InjectMocks
    private DeletarHorarioService deletarHorarioService;

    @InjectMocks
    private ListarHorarioService listarHorarioService;

    @InjectMocks
    private RecuperarHorarioService recuperarHorarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Conjunto de casos de criar horário")
    class CriacaoHorario {
        @Test
        void testCreareScheduleWithvalidDate() {
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("08:00", "2025-02-20", 1L);
            Horario horarioSalvo = new Horario();
            horarioSalvo.setUuid(UUID.randomUUID());
            horarioSalvo.setValue(dto.getValue());
            horarioSalvo.setDate(dto.getDate());
            horarioSalvo.setUserId(dto.getUserId());

            when(horarioRepository.save(any(Horario.class))).thenReturn(horarioSalvo);

            HorarioResponseDTO response = criarHorarioService.createHorario(dto);

            assertNotNull(response);
            assertEquals(dto.getValue(), response.getValue());
            assertEquals(dto.getDate(), response.getDate());
            assertEquals(dto.getUserId(), response.getUserId());
        }

        
    @Test
    void testCreateScheduleWithInvalidDate() {
        HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("08:00", "data-invalida", 1L);

        when(horarioRepository.save(any(Horario.class)))
                .thenThrow(new IllegalArgumentException("Data inválida"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            criarHorarioService.createHorario(dto);
        });

        assertEquals("Data inválida", exception.getMessage());
    }
    @Test
    void testCreateScheduleWithInternalError() {
        HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("08:00", "2025-02-20", 1L);
    
        when(horarioRepository.save(any(Horario.class)))
                .thenThrow(new RuntimeException("Erro interno"));
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            criarHorarioService.createHorario(dto);
        });
    
        assertEquals("Erro interno", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar horário com dados inválidos (value nulo)")
    void testCriarHorarioComDadosInvalidos() {
      
        HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(null, "2025-02-20", 1L);

   
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            criarHorarioService.createHorario(dto);
        });

        assertEquals("O valor do horário não pode ser nulo ou vazio", exception.getMessage());

   
        verify(horarioRepository, never()).save(any(Horario.class));
    }

    }


    @Nested
    @DisplayName("Conjunto de casos de ler horário")
    class LeituraHorario {
        @Test
        void testRecuperarHorarioComIdValido() {
            UUID id = UUID.randomUUID();
            Horario horario = new Horario();
            horario.setUuid(id);
            horario.setValue("08:00");
            horario.setDate("2025-02-21");
            horario.setUserId(1L);

            when(horarioRepository.findById(id)).thenReturn(Optional.of(horario));

            HorarioResponseDTO response = recuperarHorarioService.recuperarHorario(id);

            assertNotNull(response);
            assertEquals(horario.getValue(), response.getValue());
            assertEquals(horario.getDate(), response.getDate());
            assertEquals(horario.getUserId(), response.getUserId());
        }

        @Test
        void testRecuperarHorarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();

            when(horarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                recuperarHorarioService.recuperarHorario(idInvalido);
            });

            assertEquals("Horario not found", exception.getMessage());
        }

        @Test
        void testReadScheduleWithInternalError() {
            UUID id = UUID.randomUUID();

            when(horarioRepository.findById(id)).thenThrow(new RuntimeException("Erro interno"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                recuperarHorarioService.recuperarHorario(id);
            });

            assertEquals("Erro interno", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de atualizar horário")
    class AtualizacaoHorario {
        @Test
        void testAtualizarHorarioComIdValido() {
            UUID id = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("10:00", "2025-02-21", 1L);
            Horario horarioExistente = new Horario();
            horarioExistente.setUuid(id);
            horarioExistente.setValue("08:00");
            horarioExistente.setDate("2025-02-20");
            horarioExistente.setUserId(1L);

            when(horarioRepository.findById(id)).thenReturn(Optional.of(horarioExistente));
            when(horarioRepository.save(any(Horario.class))).thenReturn(horarioExistente);

            HorarioResponseDTO response = atualizarHorarioService.updateHorario(id, dto);

            assertNotNull(response);
            assertEquals(dto.getValue(), response.getValue());
            assertEquals(dto.getDate(), response.getDate());
            assertEquals(dto.getUserId(), response.getUserId());
        }

        @Test
        void testAtualizarHorarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("10:00", "2025-02-21", 1L);

            when(horarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                atualizarHorarioService.updateHorario(idInvalido, dto);
            });

            assertEquals("Horario not found", exception.getMessage());
        }

        @Test
        void testUpdateScheduleWithInvalidValue() {
            UUID id = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("valor-invalido", "2025-02-21", 1L);

            when(horarioRepository.findById(id)).thenReturn(Optional.of(new Horario()));
            when(horarioRepository.save(any(Horario.class)))
                    .thenThrow(new IllegalArgumentException("Valor inválido"));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                atualizarHorarioService.updateHorario(id, dto);
            });

            assertEquals("Valor inválido", exception.getMessage());
        }

        @Test
        void testUpdateScheduleWithoutChange() {
            UUID id = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("08:00", "2025-02-21", 1L);
            Horario horarioExistente = new Horario();
            horarioExistente.setUuid(id);
            horarioExistente.setValue(dto.getValue());
            horarioExistente.setDate(dto.getDate());
            horarioExistente.setUserId(dto.getUserId());

            when(horarioRepository.findById(id)).thenReturn(Optional.of(horarioExistente));
            when(horarioRepository.save(any(Horario.class))).thenReturn(horarioExistente);

            HorarioResponseDTO response = atualizarHorarioService.updateHorario(id, dto);

            assertNotNull(response);
            assertEquals(dto.getValue(), response.getValue());
            assertEquals(dto.getDate(), response.getDate());
            assertEquals(dto.getUserId(), response.getUserId());
        }

        @Test
        void testUpdateScheduleWithInternalError() {
            UUID id = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO("10:00", "2025-02-21", 1L);

            when(horarioRepository.findById(id))
                    .thenThrow(new RuntimeException("Erro interno"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                atualizarHorarioService.updateHorario(id, dto);
            });

            assertEquals("Erro interno", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de deletar horário")
    class DelecaoHorario {
        @Test
        void testDeletarHorarioComSucesso() {
            UUID id = UUID.randomUUID();
            when(horarioRepository.existsById(id)).thenReturn(true);
            doNothing().when(horarioRepository).deleteById(id);

            deletarHorarioService.deletarHorario(id);

            verify(horarioRepository, times(1)).deleteById(id);
        }

        @Test
        void testDeletarHorarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            when(horarioRepository.existsById(idInvalido)).thenReturn(false);

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                deletarHorarioService.deletarHorario(idInvalido);
            });

            assertEquals("Horario not found", exception.getMessage());
        }

        @Test
        void testDisableScheduleAlreadyDisabled() {
            UUID id = UUID.randomUUID();
            when(horarioRepository.existsById(id)).thenReturn(false);

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                deletarHorarioService.deletarHorario(id);
            });

            assertEquals("Horario not found", exception.getMessage());
        }

        @Test
        void testDisableScheduleWithInternalError() {
            UUID id = UUID.randomUUID();
            when(horarioRepository.existsById(id)).thenReturn(true);
            doThrow(new RuntimeException("Erro interno")).when(horarioRepository).deleteById(id);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                deletarHorarioService.deletarHorario(id);
            });

            assertEquals("Erro interno", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de listar horários por usuário")
    class ListagemHorario {
        @Test
        void testListarHorariosComUsuarioValido() {
            Long userId = 1L;
            Horario horario1 = new Horario();
            horario1.setUuid(UUID.randomUUID());
            horario1.setValue("08:00");
            horario1.setDate("2025-02-21");
            horario1.setUserId(userId);

            Horario horario2 = new Horario();
            horario2.setUuid(UUID.randomUUID());
            horario2.setValue("14:00");
            horario2.setDate("2025-02-22");
            horario2.setUserId(userId);

            List<Horario> horarios = Arrays.asList(horario1, horario2);

            when(horarioRepository.findAllByUserId(userId)).thenReturn(horarios);

            List<HorarioResponseDTO> response = listarHorarioService.listarHorario(userId);

            assertNotNull(response);
            assertEquals(2, response.size());
            assertEquals(horario1.getValue(), response.get(0).getValue());
            assertEquals(horario2.getValue(), response.get(1).getValue());
        }

        @Test
        void testListarHorariosUsuarioSemHorarios() {
            Long userId = 1L;

            when(horarioRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

            List<HorarioResponseDTO> response = listarHorarioService.listarHorario(userId);

            assertNotNull(response);
            assertTrue(response.isEmpty());
        }
    }
}