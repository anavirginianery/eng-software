package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.repository.HorarioRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.grupo2.diabetter.dto.insulina.InsulinDeleteResponseDTO;
import com.grupo2.diabetter.dto.insulina.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import com.grupo2.diabetter.exception.CommerceException;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulina.AtualizarInsulinService;
import com.grupo2.diabetter.service.insulina.CriarInsulinService;
import com.grupo2.diabetter.service.insulina.DeletarInsulinService;
import com.grupo2.diabetter.service.insulina.ListarInsulinService;
import com.grupo2.diabetter.service.insulina.RecuperarInsulinService;
import com.grupo2.diabetter.enuns.TipoInsulina;

@SpringBootTest
public class TestesServiceInsulina {


    @InjectMocks
    private CriarInsulinService criarInsulinService;

    @InjectMocks
    private DeletarInsulinService deletarInsulinService;

    @InjectMocks
    private ListarInsulinService listarInsulinService;

    @InjectMocks
    private RecuperarInsulinService recuperarInsulinService;

    @Mock
    private InsulinRepository insulinRepository;

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private GlicemiaRepository glicemiaRepository;

    @InjectMocks
    private AtualizarInsulinService atualizarInsulinService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Conjunto de casos de criação de insulina")
    class createInsulin {
        @Test
        @DisplayName("Cria insulina com dados válidos")
        void testCreateInsulinWithValidData() {
            UUID horarioId = UUID.randomUUID();
            UUID glicemiaId = UUID.randomUUID();

            InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(10)
                    .horarioId(horarioId)
                    .glicemia(glicemiaId)
                    .build();

            Horario horario = new Horario();
            horario.setId(horarioId);

            when(horarioRepository.findById(horarioId))
                    .thenReturn(Optional.of(horario));

            Glicemia glicemia = new Glicemia();
            glicemia.setId(glicemiaId);

            when(glicemiaRepository.findById(glicemiaId))
                    .thenReturn(Optional.of(glicemia));

            Insulina insulinSalva = Insulina.builder()
                    .id(UUID.randomUUID())
                    .tipoInsulina(requestDTO.getTipoInsulina())
                    .unidades(requestDTO.getUnidades())
                    .horario(horario)
                    .glicemia(glicemia)
                    .build();

            when(insulinRepository.save(any(Insulina.class))).thenReturn(insulinSalva);

            InsulinResponseDTO responseDTO = criarInsulinService.criarInsulina(requestDTO);

            assertNotNull(responseDTO);
            assertEquals(insulinSalva.getId(), responseDTO.getInsulidaId());
            assertEquals(insulinSalva.getTipoInsulina(), responseDTO.getTipoInsulina());
            assertEquals(insulinSalva.getUnidades(), responseDTO.getUnidades());
            assertEquals(insulinSalva.getHorario().getId(), responseDTO.getHorarioId());
        }

        @Test
        @DisplayName("Cria insulina com tipo inválido")
        void testCreateInsulinWithInvalidType() {
            UUID horarioId = UUID.randomUUID();

            // Para simular tipo inválido, passamos null
            InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(null)
                    .unidades(10)
                    .horarioId(horarioId)
                    .build();

            CommerceException exception = assertThrows(CommerceException.class, () -> {
                criarInsulinService.criarInsulina(requestDTO);
            });

            assertEquals("Tipo de insulina inválido", exception.getMessage());
        }

        @Test
        @DisplayName("Cria insulina com unidades inválidas")
        void testCreateInsulinWithInvalidUnits() {
            UUID horarioId = UUID.randomUUID();

            InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(-5)
                    .horarioId(horarioId)
                    .build();

            CommerceException exception = assertThrows(CommerceException.class, () -> {
                criarInsulinService.criarInsulina(requestDTO);
            });

            assertEquals("Unidades de insulina inválidas", exception.getMessage());
        }

        @Test
        @DisplayName("Cria insulina com erro interno")
        void testCreateInsulinWithInternalError() {
            UUID horarioId = UUID.randomUUID();
            UUID glicemiaId = UUID.randomUUID();

            InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(10)
                    .horarioId(horarioId)
                    .glicemia(glicemiaId)
                    .build();

            when(horarioRepository.findById(horarioId))
                    .thenReturn(Optional.of(new Horario()));
            when(glicemiaRepository.findById(glicemiaId))
                    .thenReturn(Optional.of(new Glicemia()));
            when(insulinRepository.save(any(Insulina.class)))
                    .thenThrow(new RuntimeException("Erro interno no servidor"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                criarInsulinService.criarInsulina(requestDTO);
            });

            assertEquals("Erro interno no servidor", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Testes de leitura de insulina")
    class readInsulin {
        @Test
        @DisplayName("Leitura de insulina com ID válido")
        void testReadInsulinWithValidId() {
            UUID horarioId = UUID.randomUUID();
            UUID validId = UUID.randomUUID();

            // Criar um Horario simulado
            Horario horario = new Horario();
            horario.setId(horarioId);

            Insulina insulin = Insulina.builder()
                    .id(validId)
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(10)
                    .horario(horario)
                    .build();

            when(insulinRepository.findById(validId)).thenReturn(Optional.of(insulin));

            InsulinResponseDTO responseDTO = recuperarInsulinService.recuperarInsulina(validId);

            assertNotNull(responseDTO);
            assertEquals(validId, responseDTO.getInsulidaId());
            assertEquals(TipoInsulina.RAPIDA, responseDTO.getTipoInsulina());
            assertEquals(10, responseDTO.getUnidades());
            assertEquals(horarioId, responseDTO.getHorarioId());

            verify(insulinRepository, times(1)).findById(validId);
        }

        @Test
        @DisplayName("Leitura de insulina com ID inválido")
        void testReadInsulinWithInvalidId() {
            UUID invalidId = UUID.randomUUID();

            when(insulinRepository.findById(invalidId)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                recuperarInsulinService.recuperarInsulina(invalidId);
            });

            assertEquals("Insulina não encontrada", exception.getMessage());

            verify(insulinRepository, times(1)).findById(invalidId);
        }
    }

    @Nested
    @DisplayName("Casos de listar insulina")
    class ListarInsulina {

        @Test
        @DisplayName("Listar insulinas com registros")
        void testListAllInsulinWithRecords() {
            UUID horarioId1 = UUID.randomUUID();
            UUID horarioId2 = UUID.randomUUID();

            // Criando Horarios simulados
            Horario horario1 = new Horario();
            horario1.setId(horarioId1);
            Horario horario2 = new Horario();
            horario2.setId(horarioId2);

            Insulina insulin1 = Insulina.builder()
                    .id(UUID.randomUUID())
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(10)
                    .horario(horario1)
                    .build();

            Insulina insulin2 = Insulina.builder()
                    .id(UUID.randomUUID())
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .unidades(20)
                    .horario(horario2)
                    .build();

            List<Insulina> insulinas = List.of(insulin1, insulin2);

            when(insulinRepository.findAll()).thenReturn(insulinas);

            List<InsulinResponseDTO> responseDTOs = listarInsulinService.listarTodasInsulinas();

            assertNotNull(responseDTOs);
            assertEquals(2, responseDTOs.size());
            assertEquals(horarioId1, responseDTOs.get(0).getHorarioId());
            assertEquals(horarioId2, responseDTOs.get(1).getHorarioId());

            verify(insulinRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Listar insulinas sem registros")
        void testListAllInsulinWithoutRecords() {
            when(insulinRepository.findAll()).thenReturn(Collections.emptyList());

            List<InsulinResponseDTO> responseDTOs = listarInsulinService.listarTodasInsulinas();

            assertNotNull(responseDTOs);
            assertTrue(responseDTOs.isEmpty());

            verify(insulinRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Listar insulinas com erro interno")
        void testListAllInsulinWithInternalError() {
            when(insulinRepository.findAll()).thenThrow(new RuntimeException("Erro interno no servidor"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                listarInsulinService.listarTodasInsulinas();
            });

            assertEquals("Erro interno no servidor", exception.getMessage());

            verify(insulinRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Casos de teste de deletar a insulina")
    class DeletarInsulina {

        @Test
        @DisplayName("Desativa insulina com sucesso")
        void testDisableInsulinWithSuccess() {
            UUID validId = UUID.randomUUID();

            Insulina insulina = Insulina.builder().id(validId).build();
            when(insulinRepository.findById(validId)).thenReturn(Optional.of(insulina));

            InsulinDeleteResponseDTO responseDTO = deletarInsulinService.deletarInsulina(validId);

            assertNotNull(responseDTO);
            assertEquals("Insulin deletada com sucesso", responseDTO.getMensagem());

            verify(insulinRepository, times(1)).findById(validId);
            verify(insulinRepository, times(1)).deleteById(validId);
        }

        @Test
        @DisplayName("Desativa insulina com ID inválido")
        void testDisableInsulinWithInvalidId() {
            UUID invalidId = UUID.randomUUID();

            when(insulinRepository.findById(invalidId)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                deletarInsulinService.deletarInsulina(invalidId);
            });

            assertEquals("Insulina não encontrada", exception.getMessage());

            verify(insulinRepository, times(1)).findById(invalidId);
            verify(insulinRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Desativa insulina com dados inválidos")
        void testDisableInsulinWithInvalidData() {
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                deletarInsulinService.deletarInsulina(null);
            });

            assertEquals("ID não pode ser nulo", exception.getMessage());

            verify(insulinRepository, never()).existsById(any());
            verify(insulinRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Desativa insulina já desativada")
        void testDisableInsulinAlreadyDisabled() {
            UUID validId = UUID.randomUUID();

            when(insulinRepository.findById(validId)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                deletarInsulinService.deletarInsulina(validId);
            });

            assertEquals("Insulina não encontrada", exception.getMessage());

            verify(insulinRepository, times(1)).findById(validId);
            verify(insulinRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Desativa insulina com erro interno")
        void testDisableInsulinWithInternalError() {
            UUID validId = UUID.randomUUID();

            Insulina insulina = Insulina.builder().id(validId).build();
            when(insulinRepository.findById(validId)).thenReturn(Optional.of(insulina));

            doThrow(new RuntimeException("Erro interno no servidor")).when(insulinRepository).deleteById(validId);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                deletarInsulinService.deletarInsulina(validId);
            });

            assertEquals("Erro interno no servidor", exception.getMessage());

            verify(insulinRepository, times(1)).findById(validId);
            verify(insulinRepository, times(1)).deleteById(validId);
        }
    }

    @Test
    @DisplayName("Atualiza insulina com dados válidos")
    void testAtualizarInsulinaComDadosValidos() {
        UUID insulinId = UUID.randomUUID();
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(horarioId)
                .build();

        Insulina insulinaExistente = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(TipoInsulina.BASAL)
                .unidades(5)
                .build();

        Horario horario = new Horario();
        horario.setId(horarioId);

        Mockito.when(insulinRepository.findById(insulinId))
                .thenReturn(Optional.of(insulinaExistente));
        Mockito.when(horarioRepository.findById(horarioId))
                .thenReturn(Optional.of(horario));

        Insulina insulinaAtualizada = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(dto.getTipoInsulina())
                .unidades(dto.getUnidades())
                .horario(horario)
                .build();

        Mockito.when(insulinRepository.save(Mockito.any(Insulina.class)))
                .thenReturn(insulinaAtualizada);

        InsulinResponseDTO response = atualizarInsulinService.atualizarInsulina(insulinId, dto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(insulinId, response.getInsulidaId());
        Assertions.assertEquals(TipoInsulina.RAPIDA, response.getTipoInsulina());
        Assertions.assertEquals(10, response.getUnidades());
        Assertions.assertEquals(horarioId, response.getHorarioId());
    }

    @Test
    @DisplayName("Atualiza insulina com tipo de insulina nulo")
    void testAtualizarInsulinaComTipoInsulinaNulo() {
        UUID insulinId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(null)
                .unidades(10)
                .horarioId(UUID.randomUUID())
                .build();

        CommerceException exception = Assertions.assertThrows(
                CommerceException.class,
                () -> atualizarInsulinService.atualizarInsulina(insulinId, dto)
        );

        Assertions.assertEquals("Tipo de insulina inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Atualiza insulina com unidades inválidas")
    void testAtualizarInsulinaComUnidadesInvalidas() {
        UUID insulinId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(-1)
                .horarioId(UUID.randomUUID())
                .build();

        CommerceException exception = Assertions.assertThrows(
                CommerceException.class,
                () -> atualizarInsulinService.atualizarInsulina(insulinId, dto)
        );

        Assertions.assertEquals("Unidades de insulina inválidas", exception.getMessage());
    }


    @Test
    @DisplayName("Atualiza insulina com horário nulo")
    void testAtualizarInsulinaComHorarioNulo() {
        UUID insulinId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(null)
                .build();

        CommerceException exception = Assertions.assertThrows(
                CommerceException.class,
                () -> atualizarInsulinService.atualizarInsulina(insulinId, dto)
        );

        Assertions.assertEquals("Horário de insulina inválido", exception.getMessage());
    }


    @Test
    @DisplayName("Atualiza insulina com ID inexistente")
    void testAtualizarInsulinaComIdInexistente() {
        UUID insulinId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(UUID.randomUUID())
                .build();

        Mockito.when(insulinRepository.findById(insulinId))
                .thenReturn(Optional.empty());

        CommerceException exception = Assertions.assertThrows(
                CommerceException.class,
                () -> atualizarInsulinService.atualizarInsulina(insulinId, dto)
        );

        Assertions.assertEquals("Insulina não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Atualiza insulina com horário inexistente")
    void testAtualizarInsulinaComHorarioInexistente() {
        UUID insulinId = UUID.randomUUID();
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(horarioId)
                .build();

        Insulina insulinaExistente = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(TipoInsulina.BASAL)
                .unidades(5)
                .build();
        Mockito.when(insulinRepository.findById(insulinId))
                .thenReturn(Optional.of(insulinaExistente));

        Mockito.when(horarioRepository.findById(horarioId))
                .thenReturn(Optional.empty());

        CommerceException exception = Assertions.assertThrows(
                CommerceException.class,
                () -> atualizarInsulinService.atualizarInsulina(insulinId, dto)
        );

        Assertions.assertEquals("Horário não encontrado", exception.getMessage());
    }


    @Test
    @DisplayName("Retorna lista vazia quando não há insulinas")
    void testListarTodasInsulinasVazia() {
        when(insulinRepository.findAll()).thenReturn(Collections.emptyList());

        List<InsulinResponseDTO> result = listarInsulinService.listarTodasInsulinas();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(insulinRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retorna lista com insulina quando há insulinas com glicemia != null")
    void testListarTodasInsulinasComGlicemia() {
        Insulina insulina = Insulina.builder()
                .id(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horario(Horario.builder().id(UUID.randomUUID()).build())
                .glicemia(new Glicemia())
                .dataAplicacao(LocalDateTime.now())
                .build();

        when(insulinRepository.findAll()).thenReturn(List.of(insulina));

        List<InsulinResponseDTO> result = listarInsulinService.listarTodasInsulinas();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(insulinRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retorna lista com insulina quando há insulinas com glicemia == null")
    void testListarTodasInsulinasSemGlicemia() {
        Insulina insulina = Insulina.builder()
                .id(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horario(Horario.builder().id(UUID.randomUUID()).build())
                .glicemia(null)
                .dataAplicacao(LocalDateTime.now())
                .build();

        when(insulinRepository.findAll()).thenReturn(List.of(insulina));

        List<InsulinResponseDTO> result = listarInsulinService.listarTodasInsulinas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getGlicemia());
        verify(insulinRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retorna lista vazia quando não há insulinas para determinado horário")
    void testListarInsulinaPorHorarioVazio() {
        UUID horarioId = UUID.randomUUID();

        when(insulinRepository.findByHorarioId(horarioId)).thenReturn(Collections.emptyList());

        List<InsulinResponseDTO> result = listarInsulinService.listarInsulinaPorHorario(horarioId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(insulinRepository, times(1)).findByHorarioId(horarioId);
    }

    @Test
    @DisplayName("Retorna insulinas com glicemia != null para determinado horário")
    void testListarInsulinaPorHorarioComGlicemia() {
        UUID horarioId = UUID.randomUUID();

        Insulina insulina = Insulina.builder()
                .id(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horario(Horario.builder().id(horarioId).build())
                .glicemia(new Glicemia()) // != null
                .dataAplicacao(LocalDateTime.now())
                .build();

        when(insulinRepository.findByHorarioId(horarioId)).thenReturn(List.of(insulina));

        List<InsulinResponseDTO> result = listarInsulinService.listarInsulinaPorHorario(horarioId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(insulinRepository, times(1)).findByHorarioId(horarioId);
    }

    @Test
    @DisplayName("Retorna insulinas com glicemia == null para determinado horário")
    void testListarInsulinaPorHorarioSemGlicemia() {
        UUID horarioId = UUID.randomUUID();

        Insulina insulina = Insulina.builder()
                .id(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horario(Horario.builder().id(horarioId).build())
                .glicemia(null)
                .dataAplicacao(LocalDateTime.now())
                .build();

        when(insulinRepository.findByHorarioId(horarioId)).thenReturn(List.of(insulina));

        List<InsulinResponseDTO> result = listarInsulinService.listarInsulinaPorHorario(horarioId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getGlicemia());
        verify(insulinRepository, times(1)).findByHorarioId(horarioId);
    }

    @Test
    @DisplayName("Recupera insulina com glicemia não nula")
    void testRecuperarInsulinaComGlicemiaNaoNula() {
        UUID insulinId = UUID.randomUUID();
        Insulina insulina = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horario(Horario.builder().id(UUID.randomUUID()).build())
                .glicemia(new Glicemia())
                .dataAplicacao(LocalDateTime.now())
                .build();

        when(insulinRepository.findById(insulinId)).thenReturn(Optional.of(insulina));

        InsulinResponseDTO response = recuperarInsulinService.recuperarInsulina(insulinId);

        assertNotNull(response);
        assertEquals(insulinId, response.getInsulidaId());
        assertEquals(TipoInsulina.RAPIDA, response.getTipoInsulina());
        assertEquals(10, response.getUnidades());
        verify(insulinRepository, times(1)).findById(insulinId);
    }

    @Test
    @DisplayName("Recupera insulina com glicemia nula")
    void testRecuperarInsulinaComGlicemiaNula() {
        // Arrange
        UUID insulinId = UUID.randomUUID();
        Insulina insulina = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horario(Horario.builder().id(UUID.randomUUID()).build())
                .glicemia(null)
                .dataAplicacao(LocalDateTime.now())
                .build();

        when(insulinRepository.findById(insulinId)).thenReturn(Optional.of(insulina));

        InsulinResponseDTO response = recuperarInsulinService.recuperarInsulina(insulinId);

        assertNotNull(response);
        assertEquals(insulinId, response.getInsulidaId());
        assertEquals(TipoInsulina.RAPIDA, response.getTipoInsulina());
        assertEquals(10, response.getUnidades());
        assertNull(response.getGlicemia());
        verify(insulinRepository, times(1)).findById(insulinId);
    }

    @Test
    @DisplayName("Tenta recuperar insulina inexistente")
    void testRecuperarInsulinaInexistente() {
        UUID insulinId = UUID.randomUUID();

        when(insulinRepository.findById(insulinId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recuperarInsulinService.recuperarInsulina(insulinId);
        });
        assertEquals("Insulina não encontrada", exception.getMessage());
        verify(insulinRepository, times(1)).findById(insulinId);
    }

    @Test
    @DisplayName("Falha ao atualizar insulina não encontrada")
    void testAtualizarInsulinaNaoEncontrada() {
        UUID insulinId = UUID.randomUUID();
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(horarioId)
                .build();

        Mockito.when(insulinRepository.findById(insulinId))
                .thenReturn(Optional.empty());

        CommerceException exception = Assertions.assertThrows(
                CommerceException.class,
                () -> atualizarInsulinService.atualizarInsulina(insulinId, dto)
        );

        Assertions.assertEquals("Insulina não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Falha ao atualizar insulina com horário inexistente")
    void testAtualizarInsulinaHorarioNaoEncontrado() {
        UUID insulinId = UUID.randomUUID();
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(horarioId)
                .build();

        Insulina insulinaExistente = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(TipoInsulina.BASAL)
                .unidades(5)
                .build();

        Mockito.when(insulinRepository.findById(insulinId))
                .thenReturn(Optional.of(insulinaExistente));

        Mockito.when(horarioRepository.findById(horarioId))
                .thenReturn(Optional.empty());

        CommerceException exception = Assertions.assertThrows(
                CommerceException.class,
                () -> atualizarInsulinService.atualizarInsulina(insulinId, dto)
        );

        Assertions.assertEquals("Horário não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Atualiza insulina com sucesso - glicemia nula")
    void testAtualizarInsulinaComSucessoGlicemiaNula() {
        UUID insulinId = UUID.randomUUID();
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(horarioId)
                .build();

        Insulina insulinaExistente = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(TipoInsulina.BASAL)
                .unidades(5)
                .glicemia(null)
                .build();

        Horario horario = new Horario();
        horario.setId(horarioId);

        Mockito.when(insulinRepository.findById(insulinId))
                .thenReturn(Optional.of(insulinaExistente));

        Mockito.when(horarioRepository.findById(horarioId))
                .thenReturn(Optional.of(horario));

        Insulina insulinaAtualizada = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(dto.getTipoInsulina())
                .unidades(dto.getUnidades())
                .horario(horario)
                .glicemia(null)
                .build();

        Mockito.when(insulinRepository.save(Mockito.any(Insulina.class)))
                .thenReturn(insulinaAtualizada);

        InsulinResponseDTO response = atualizarInsulinService.atualizarInsulina(insulinId, dto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(insulinId, response.getInsulidaId());
        Assertions.assertEquals(TipoInsulina.RAPIDA, response.getTipoInsulina());
        Assertions.assertEquals(10, response.getUnidades());
        Assertions.assertEquals(horarioId, response.getHorarioId());
        Assertions.assertNull(response.getGlicemia()); // Ternário (false)
    }

    @Test
    @DisplayName("Atualiza insulina com sucesso - glicemia não nula")
    void testAtualizarInsulinaComSucessoGlicemiaNaoNula() {
        UUID insulinId = UUID.randomUUID();
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(horarioId)
                .build();

        Insulina insulinaExistente = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(TipoInsulina.BASAL)
                .unidades(5)
                .glicemia(new Glicemia())
                .build();

        Horario horario = new Horario();
        horario.setId(horarioId);

        Mockito.when(insulinRepository.findById(insulinId))
                .thenReturn(Optional.of(insulinaExistente));

        Mockito.when(horarioRepository.findById(horarioId))
                .thenReturn(Optional.of(horario));
        Insulina insulinaAtualizada = Insulina.builder()
                .id(insulinId)
                .tipoInsulina(dto.getTipoInsulina())
                .unidades(dto.getUnidades())
                .horario(horario)
                .glicemia(new Glicemia())
                .build();

        Mockito.when(insulinRepository.save(Mockito.any(Insulina.class)))
                .thenReturn(insulinaAtualizada);

        InsulinResponseDTO response = atualizarInsulinService.atualizarInsulina(insulinId, dto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(insulinId, response.getInsulidaId());
        Assertions.assertEquals(TipoInsulina.RAPIDA, response.getTipoInsulina());
        Assertions.assertEquals(10, response.getUnidades());
        Assertions.assertEquals(horarioId, response.getHorarioId());
    }

    @Test
    @DisplayName("Falha ao criar insulina com tipoInsulina nulo")
    void testCriarInsulinaTipoNulo() {
        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(null)
                .unidades(10)
                .horarioId(UUID.randomUUID())
                .glicemia(UUID.randomUUID())
                .build();

        CommerceException exception = assertThrows(
                CommerceException.class,
                () -> criarInsulinService.criarInsulina(dto)
        );
        assertEquals("Tipo de insulina inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Falha ao criar insulina com unidades <= 0")
    void testCriarInsulinaComUnidadesInvalidas() {
        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(-5)
                .horarioId(UUID.randomUUID())
                .glicemia(UUID.randomUUID())
                .build();

        CommerceException exception = assertThrows(
                CommerceException.class,
                () -> criarInsulinService.criarInsulina(dto)
        );
        assertEquals("Unidades de insulina inválidas", exception.getMessage());
    }

    @Test
    @DisplayName("Falha ao criar insulina com horárioId nulo")
    void testCriarInsulinaHorarioNulo() {
        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(null)
                .glicemia(UUID.randomUUID())
                .build();

        CommerceException exception = assertThrows(
                CommerceException.class,
                () -> criarInsulinService.criarInsulina(dto)
        );
        assertEquals("Horário de insulina inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Falha ao criar insulina com glicemia nula")
    void testCriarInsulinaGlicemiaNula() {
        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(UUID.randomUUID())
                .glicemia(null)
                .build();

        CommerceException exception = assertThrows(
                CommerceException.class,
                () -> criarInsulinService.criarInsulina(dto)
        );
        assertEquals("Glicemia inválida", exception.getMessage());
    }

    @Test
    @DisplayName("Falha ao criar insulina quando horário não é encontrado")
    void testCriarInsulinaHorarioNaoEncontrado() {
        UUID horarioId = UUID.randomUUID();
        UUID glicemiaId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(horarioId)
                .glicemia(glicemiaId)
                .build();

        when(horarioRepository.findById(horarioId)).thenReturn(Optional.empty());

        CommerceException exception = assertThrows(
                CommerceException.class,
                () -> criarInsulinService.criarInsulina(dto)
        );
        assertEquals("Horário não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Falha ao criar insulina quando glicemia não é encontrada")
    void testCriarInsulinaGlicemiaNaoEncontrada() {
        UUID horarioId = UUID.randomUUID();
        UUID glicemiaId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(horarioId)
                .glicemia(glicemiaId)
                .build();

        // Simula que o horário foi encontrado
        Horario horario = new Horario();
        horario.setId(horarioId);
        when(horarioRepository.findById(horarioId))
                .thenReturn(Optional.of(horario));

        when(glicemiaRepository.findById(glicemiaId))
                .thenReturn(Optional.empty());

        CommerceException exception = assertThrows(
                CommerceException.class,
                () -> criarInsulinService.criarInsulina(dto)
        );
        assertEquals("Glicemia não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Cria insulina com sucesso")
    void testCriarInsulinaComSucesso() {
        UUID horarioId = UUID.randomUUID();
        UUID glicemiaId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horarioId(horarioId)
                .glicemia(glicemiaId)
                .build();

        Horario horario = new Horario();
        horario.setId(horarioId);
        when(horarioRepository.findById(horarioId))
                .thenReturn(Optional.of(horario));

        Glicemia glicemia = new Glicemia();
        glicemia.setId(glicemiaId);
        when(glicemiaRepository.findById(glicemiaId))
                .thenReturn(Optional.of(glicemia));

        Insulina insulinaSalva = Insulina.builder()
                .id(UUID.randomUUID())
                .tipoInsulina(TipoInsulina.RAPIDA)
                .unidades(10)
                .horario(horario)
                .glicemia(glicemia)
                .build();

        when(insulinRepository.save(any(Insulina.class))).thenReturn(insulinaSalva);

        InsulinResponseDTO response = criarInsulinService.criarInsulina(dto);

        assertNotNull(response);
        assertEquals(insulinaSalva.getId(), response.getInsulidaId());
        assertEquals(TipoInsulina.RAPIDA, response.getTipoInsulina());
        assertEquals(10, response.getUnidades());
        assertEquals(horarioId, response.getHorarioId());
        assertEquals(glicemiaId, response.getGlicemia());
    }
}