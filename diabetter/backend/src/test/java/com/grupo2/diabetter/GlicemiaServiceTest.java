package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.grupo2.diabetter.controller.GlicemiaController;
import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.repository.HorarioRepository; // Mock HorarioRepository
import com.grupo2.diabetter.service.glicemia.AtualizarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.CriarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.DeletarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.ListarGlicemiasService;
import com.grupo2.diabetter.service.glicemia.RecuperarGlicemiaService;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlicemiaServiceTest {
    @Mock
    private GlicemiaRepository glicemiaRepository;

    @Mock
    private InsulinRepository insulinRepository;

    @Mock
    private HorarioRepository horarioRepository; // Mock HorarioRepository

    @InjectMocks
    private CriarGlicemiaService criarGlicemiaService;

    @InjectMocks
    private ListarGlicemiasService listarGlicemiasService;

    @InjectMocks
    private AtualizarGlicemiaService atualizarGlicemiaService;

    @InjectMocks
    private DeletarGlicemiaService deletarGlicemiaService;

    @InjectMocks
    private RecuperarGlicemiaService recuperarGlicemiaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Conjunto de casos de criar Glicemia")
    class UsuarioCriacao {

        @Test
        @DisplayName("Cria uma glicemia com dados válidos")
        void testCreateBloodSugarWithValidData1() {
            // Prepare valid data
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(120.0f);
            UUID horarioId = UUID.randomUUID();
            dto.setHorario(horarioId);

            // Mock Horario repository
            Horario horario = new Horario();
            when(horarioRepository.findById(horarioId)).thenReturn(Optional.of(horario));

            // Mock Insulina repository (null in this case)
            when(insulinRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            // Prepare Glicemia object
            Glicemia glicemiaSalva = Glicemia.builder()
                    .id(UUID.randomUUID())
                    .valorGlicemia(dto.getValorGlicemia())
                    .horario(horario)
                    .insulina(null)
                    .comentario(dto.getComentario())
                    .build();

            when(glicemiaRepository.save(any(Glicemia.class))).thenReturn(glicemiaSalva);

            // Execute service method
            GlicemiaResponseDTO resultado = criarGlicemiaService.executar(dto);

            // Assertions
            assertNotNull(resultado);
            assertNotNull(resultado.getValorGlicemia());
            assertEquals(dto.getValorGlicemia(), resultado.getValorGlicemia(), 0.001);
            assertNull(resultado.getInsulina());  // No Insulina associated
        }

        @Test
        @DisplayName("Tenta criar uma glicemia com horário não encontrado")
        void testCreateBloodSugarWithHorarioNotFound() {
            UUID horarioId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(120.0f);
            dto.setHorario(horarioId);

            // Mock Horario repository to return empty
            when(horarioRepository.findById(horarioId)).thenReturn(Optional.empty());

            // Expecting IllegalArgumentException
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                criarGlicemiaService.executar(dto);
            });

            assertEquals("Horario not found", exception.getMessage());
        }

        @Test
        @DisplayName("Tenta criar uma glicemia com insulina não encontrada")
        void testCreateBloodSugarWithInsulinaNotFound() {
            UUID horarioId = UUID.randomUUID();
            UUID insulinaId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(120.0f);
            dto.setHorario(horarioId);
            dto.setInsulina(insulinaId);

            // Mock Horario repository
            Horario horario = new Horario();
            when(horarioRepository.findById(horarioId)).thenReturn(Optional.of(horario));

            // Mock Insulina repository to return empty
            when(insulinRepository.findById(insulinaId)).thenReturn(Optional.empty());

            // Expecting IllegalArgumentException
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                criarGlicemiaService.executar(dto);
            });

            assertEquals("Insulina not found", exception.getMessage());
        }

        @Test
        @DisplayName("Cria glicemia com comentário válido")
        void testCreateBloodSugarWithValidComentario() {
            UUID horarioId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(120.0f);
            dto.setHorario(horarioId);
            dto.setComentario("Test Comment");

            // Mock Horario repository
            Horario horario = new Horario();
            when(horarioRepository.findById(horarioId)).thenReturn(Optional.of(horario));

            // Prepare Glicemia object
            Glicemia glicemiaSalva = Glicemia.builder()
                    .id(UUID.randomUUID())
                    .valorGlicemia(dto.getValorGlicemia())
                    .horario(horario)
                    .comentario(dto.getComentario())
                    .build();

            when(glicemiaRepository.save(any(Glicemia.class))).thenReturn(glicemiaSalva);

            // Execute service method
            GlicemiaResponseDTO resultado = criarGlicemiaService.executar(dto);

            // Assertions
            assertNotNull(resultado);
            assertEquals(dto.getComentario(), resultado.getComentario());
        }

        @Test
        @DisplayName("Cria uma glicemia com dados válidos")
        void testCreateBloodSugarWithValidData() {
            // Prepare data
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(120.0f); // Valid glicemia value
            dto.setHorario(UUID.randomUUID()); // Adding horario

            // Mock the HorarioRepository to return a valid Horario object
            Horario horario = new Horario();
            when(horarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(horario)); // Ensure this returns a valid Horario object

            Glicemia glicemiaSalva = Glicemia.builder()
                    .id(UUID.randomUUID())
                    .valorGlicemia(dto.getValorGlicemia())
                    .horario(horario)
                    .build();

            when(glicemiaRepository.save(any(Glicemia.class))).thenReturn(glicemiaSalva);

            GlicemiaResponseDTO resultado = criarGlicemiaService.executar(dto);

            // Asserting that the result is not null and matches the expected values
            assertNotNull(resultado);
            assertNotNull(resultado.getValorGlicemia());
            assertEquals(dto.getValorGlicemia(), resultado.getValorGlicemia(), 0.001);
        }

        @Test
        @DisplayName("Tenta criar uma glicemia com dados inválidos")
        void testCreateBloodSugarWithInvalidData() {
            // Prepare invalid data
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(-10.0f); // Invalid glicemia value
            dto.setHorario(UUID.randomUUID()); // Adding horario

            // Mock the HorarioRepository to return a valid Horario object
            Horario horario = new Horario();
            when(horarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(horario)); // Ensure this returns a valid Horario object

            // Assert that the IllegalArgumentException is thrown
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                criarGlicemiaService.executar(dto);
            });

            // Assert error message
            assertEquals("Medição de glicemia inválida", exception.getMessage());
        }


        @Test
        @DisplayName("Tenta criar uma glicemia e ocorre um erro interno")
        void testCreateBloodSugarWithInternalError() {
            // Prepare valid data
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(100.0f); // Invalid glicemia value
            dto.setHorario(UUID.randomUUID()); // Adding horario

            // Mock the HorarioRepository to return a valid Horario object
            Horario horario = new Horario();
            when(horarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(horario)); // Ensure this returns a valid Horario object

            // Mock repository to throw error
            when(glicemiaRepository.save(any(Glicemia.class)))
                    .thenThrow(new RuntimeException("Erro interno ao criar glicemia"));

            // Assert that the runtime exception is thrown
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                criarGlicemiaService.executar(dto);
            });

            // Assert error message
            assertEquals("Erro interno ao criar glicemia", exception.getMessage());
            verify(glicemiaRepository, times(1)).save(any(Glicemia.class));
        }

        @Test
        @DisplayName("Cria uma glicemia com Insulina quando dto.getInsulina não for nulo")
        void testCreateBloodSugarWithInsulina() {
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(100.0f);
            dto.setHorario(UUID.randomUUID());

            // Prepare Insulina object
            UUID insulinaId = UUID.randomUUID();
            Insulina insulina = new Insulina();
            insulina.setId(insulinaId);

            // Set Insulina in dto
            dto.setInsulina(insulinaId);

            // Mock repository calls
            Horario horario = new Horario();
            when(horarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(horario));
            when(insulinRepository.findById(insulinaId)).thenReturn(Optional.of(insulina));

            Glicemia glicemiaSalva = Glicemia.builder()
                    .id(UUID.randomUUID())
                    .valorGlicemia(dto.getValorGlicemia())
                    .horario(horario)
                    .insulina(insulina)
                    .build();

            when(glicemiaRepository.save(any(Glicemia.class))).thenReturn(glicemiaSalva);

            // Execute the service method
            GlicemiaResponseDTO resultado = criarGlicemiaService.executar(dto);

            // Assertions
            assertNotNull(resultado);
            assertEquals(dto.getValorGlicemia(), resultado.getValorGlicemia(), 0.001);
            assertNotNull(resultado.getInsulina());
            assertEquals(insulinaId, resultado.getInsulina().getId());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de ler glicemia")
    class LeituraGlicemia {

        @Test
        @DisplayName("Recupera glicemia com ID válido")
        void testRecuperarGlicemiaWithValidId() {
            // Prepare data
            UUID validId = UUID.randomUUID();
            Horario horario = new Horario(); // Assume this is populated properly
            Insulina insulina = new Insulina(); // Assume this is populated properly

            Glicemia glicemia = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(120.0f)
                    .horario(horario)
                    .insulina(insulina)
                    .comentario("Test Comentário")
                    .build();

            // Mock the repository to return the glicemia
            when(glicemiaRepository.findById(validId)).thenReturn(Optional.of(glicemia));

            // Execute the service method
            GlicemiaResponseDTO resultado = recuperarGlicemiaService.executar(validId);

            // Assertions
            assertNotNull(resultado);
            assertEquals(validId, resultado.getId());
            assertEquals(120.0f, resultado.getValorGlicemia(), 0.001);
            assertEquals(horario, resultado.getHorario());
            assertEquals(insulina, resultado.getInsulina());
            assertEquals("Test Comentário", resultado.getComentario());
        }

        @Test
        @DisplayName("Tenta recuperar glicemia com ID inválido")
        void testRecuperarGlicemiaWithInvalidId() {
            // Prepare data
            UUID invalidId = UUID.randomUUID();

            // Mock the repository to return empty for the given invalid ID
            when(glicemiaRepository.findById(invalidId)).thenReturn(Optional.empty());

            // Execute the service method and assert that NotFoundException is thrown
            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                recuperarGlicemiaService.executar(invalidId);
            });

            // Assert the error message
            assertEquals("Glicemia não encontrada", exception.getMessage());
        }

        @Test
        @DisplayName("Tenta recuperar glicemia com ID válido, mas sem insulina associada")
        void testRecuperarGlicemiaWithNullInsulina() {
            // Prepare data
            UUID validId = UUID.randomUUID();
            Horario horario = new Horario(); // Assume this is populated properly

            Glicemia glicemia = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(110.0f)
                    .horario(horario)
                    .insulina(null) // No Insulina in this case
                    .comentario("Test Comentário")
                    .build();

            // Mock the repository to return the glicemia with no insulina
            when(glicemiaRepository.findById(validId)).thenReturn(Optional.of(glicemia));

            // Execute the service method
            GlicemiaResponseDTO resultado = recuperarGlicemiaService.executar(validId);

            // Assertions
            assertNotNull(resultado);
            assertNull(resultado.getInsulina());
            assertEquals(validId, resultado.getId());
            assertEquals(110.0f, resultado.getValorGlicemia(), 0.001);
        }

        @Test
        @DisplayName("Lista glicemias com dados válidos (banco não vazio)")
        void testReadBloodSugarWithValidData() {
            // Prepare valid Glicemia objects
            Glicemia glicemia1 = Glicemia.builder()
                    .id(UUID.randomUUID())
                    .valorGlicemia(120.0f)
                    .horario(new Horario()) // Assuming Horario is set properly
                    .build();

            Glicemia glicemia2 = Glicemia.builder()
                    .id(UUID.randomUUID())
                    .valorGlicemia(130.0f)
                    .horario(new Horario()) // Assuming Horario is set properly
                    .build();

            List<Glicemia> glicemias = List.of(glicemia1, glicemia2);

            // Mock repository
            when(glicemiaRepository.findAll()).thenReturn(glicemias);

            // Execute service
            List<GlicemiaResponseDTO> resultado = listarGlicemiasService.executar();

            // Assertions
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals(glicemia1.getValorGlicemia(), resultado.get(0).getValorGlicemia(), 0.001);
            assertEquals(glicemia2.getValorGlicemia(), resultado.get(1).getValorGlicemia(), 0.001);
        }

        @Test
        @DisplayName("Lista glicemias com banco vazio")
        void testReadBloodSugarWithEmptyDatabase() {
            // Mock repository to return empty list
            when(glicemiaRepository.findAll()).thenReturn(List.of());

            // Execute service
            List<GlicemiaResponseDTO> resultado = listarGlicemiasService.executar();

            // Assertions
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("Lista glicemias filtradas por ID de horário com sucesso")
        void testListBloodSugarsByHorarioId() {
            UUID horarioId = UUID.randomUUID();

            // Create mock Horario object
            Horario horario = new Horario();
            horario.setId(horarioId);

            // Create Glicemia objects and associate them with the Horario
            Glicemia glicemia1 = Glicemia.builder()
                    .id(UUID.randomUUID())
                    .valorGlicemia(120.0f)
                    .horario(horario)
                    .build();
            Glicemia glicemia2 = Glicemia.builder()
                    .id(UUID.randomUUID())
                    .valorGlicemia(130.0f)
                    .horario(horario)
                    .build();

            List<Glicemia> glicemias = List.of(glicemia1, glicemia2);

            // Mock the repository call to return the list of Glicemias
            when(glicemiaRepository.findByHorarioId(horarioId)).thenReturn(glicemias);

            // Call the service method
            List<GlicemiaResponseDTO> result = listarGlicemiasService.listarGlicemiaByHorario(horarioId);

            // Validate the result
            assertNotNull(result);
            assertEquals(2, result.size());
            // Check that the horarioId matches for both glicemias
            assertEquals(horarioId, result.get(0).getHorario().getId());
            assertEquals(horarioId, result.get(1).getHorario().getId());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de atualizar glicemia")
    class GlicemiaUpdate {

        @Test
        @DisplayName("Atualiza glicemia com ID válido e horário")
        void testUpdateBloodSugarWithValidDataAndHorario() {
            UUID validId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(150.0f);
            UUID horarioId = UUID.randomUUID();
            dto.setHorario(horarioId);

            Glicemia glicemiaExistente = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(120.0f)
                    .horario(new Horario()) // Mocked horario
                    .comentario("Old comment")
                    .build();

            Horario horario = new Horario(); // Mocked horario
            Glicemia glicemiaAtualizada = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(dto.getValorGlicemia())
                    .horario(horario)
                    .comentario(dto.getComentario())
                    .build();

            when(glicemiaRepository.findById(validId)).thenReturn(Optional.of(glicemiaExistente));
            when(horarioRepository.findById(horarioId)).thenReturn(Optional.of(horario));
            when(glicemiaRepository.save(glicemiaAtualizada)).thenReturn(glicemiaAtualizada);

            GlicemiaResponseDTO response = atualizarGlicemiaService.executar(validId, dto);

            assertNotNull(response);
            assertEquals(validId, response.getId());
            assertEquals(dto.getValorGlicemia(), response.getValorGlicemia(), 0.001);
            assertEquals(horario, response.getHorario());
        }

        @Test
        @DisplayName("Atualiza glicemia sem horário, com valor de glicemia válido")
        void testUpdateBloodSugarWithoutHorario() {
            UUID validId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(150.0f);
            dto.setHorario(null); // No horario provided

            Glicemia glicemiaExistente = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(120.0f)
                    .horario(new Horario()) // Mocked horario
                    .comentario("Old comment")
                    .build();

            Glicemia glicemiaAtualizada = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(dto.getValorGlicemia())
                    .horario(glicemiaExistente.getHorario()) // Use existing horario
                    .comentario(dto.getComentario())
                    .build();

            when(glicemiaRepository.findById(validId)).thenReturn(Optional.of(glicemiaExistente));
            when(glicemiaRepository.save(glicemiaAtualizada)).thenReturn(glicemiaAtualizada);

            GlicemiaResponseDTO response = atualizarGlicemiaService.executar(validId, dto);

            assertNotNull(response);
            assertEquals(validId, response.getId());
            assertEquals(dto.getValorGlicemia(), response.getValorGlicemia(), 0.001);
            assertEquals(glicemiaExistente.getHorario(), response.getHorario());
        }

        @Test
        @DisplayName("Tenta atualizar glicemia com ID inválido")
        void testUpdateBloodSugarWithInvalidId() {
            UUID invalidId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(150.0f);

            when(glicemiaRepository.findById(invalidId)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                atualizarGlicemiaService.executar(invalidId, dto);
            });

            assertEquals("Glicemia não encontrada", exception.getMessage());
        }

        @Test
        @DisplayName("Tenta atualizar glicemia com horário inválido")
        void testUpdateBloodSugarWithInvalidHorario() {
            UUID validId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(150.0f);
            UUID invalidHorarioId = UUID.randomUUID();
            dto.setHorario(invalidHorarioId); // Invalid horario ID

            Glicemia glicemiaExistente = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(120.0f)
                    .horario(new Horario()) // Mocked horario
                    .comentario("Old comment")
                    .build();

            when(glicemiaRepository.findById(validId)).thenReturn(Optional.of(glicemiaExistente));
            when(horarioRepository.findById(invalidHorarioId)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                atualizarGlicemiaService.executar(validId, dto);
            });

            assertEquals("Horario não encontrado", exception.getMessage());
        }

        @Test
        @DisplayName("Atualiza glicemia com comentario")
        void testUpdateBloodSugarWithComentario1() {
            UUID validId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(150.0f);
            dto.setComentario("New comment");

            Glicemia glicemiaExistente = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(120.0f)
                    .horario(new Horario()) // Mocked horario
                    .comentario("Old comment")
                    .build();

            Glicemia glicemiaAtualizada = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(dto.getValorGlicemia())
                    .horario(glicemiaExistente.getHorario()) // Use existing horario
                    .comentario(dto.getComentario())
                    .build();

            when(glicemiaRepository.findById(validId)).thenReturn(Optional.of(glicemiaExistente));
            when(glicemiaRepository.save(glicemiaAtualizada)).thenReturn(glicemiaAtualizada);

            GlicemiaResponseDTO response = atualizarGlicemiaService.executar(validId, dto);

            assertNotNull(response);
            assertEquals(validId, response.getId());
            assertEquals(dto.getValorGlicemia(), response.getValorGlicemia(), 0.001);
            assertEquals(dto.getComentario(), response.getComentario());
        }

        @Test
        @DisplayName("Atualiza glicemia com dados válidos")
        void testUpdateWithValidData() {
            UUID id = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(150.0f); // Updated glicemia value
            dto.setHorario(UUID.randomUUID()); // New horario

            // Existing glicemia object
            Glicemia glicemiaExistente = Glicemia.builder()
                    .id(id)
                    .valorGlicemia(120.0f)
                    .horario(new Horario()) // Assuming Horario is set properly
                    .build();

            // Updated glicemia object
            Glicemia glicemiaAtualizada = Glicemia.builder()
                    .id(id)
                    .valorGlicemia(dto.getValorGlicemia())
                    .horario(new Horario()) // Assuming Horario is set properly
                    .build();

            // Mock repository to return valid Glicemia
            when(glicemiaRepository.findById(id)).thenReturn(Optional.of(glicemiaExistente));

            // Mock the HorarioRepository to return a valid Horario object
            Horario horario = new Horario();
            when(horarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(horario)); // Ensure this returns a valid Horario object

            // Mock save behavior
            when(glicemiaRepository.save(any(Glicemia.class))).thenReturn(glicemiaAtualizada);

            // Execute service method
            GlicemiaResponseDTO resultado = atualizarGlicemiaService.executar(id, dto);

            // Assertions
            assertNotNull(resultado);
            assertEquals(id, resultado.getId());
            assertEquals(dto.getValorGlicemia(), resultado.getValorGlicemia(), 0.001);
        }

        @Test
        @DisplayName("Atualiza glicemia com comentário não nulo")
        void testUpdateBloodSugarWithComentario() {
            UUID validId = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(130.0f);
            dto.setComentario("Novo comentário");

            // Existing glicemia object
            Glicemia glicemiaExistente = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(120.0f)
                    .horario(new Horario()) // Assuming Horario is set properly
                    .comentario(null) // Initially null
                    .build();

            // Mock repository to return valid Glicemia
            when(glicemiaRepository.findById(validId)).thenReturn(Optional.of(glicemiaExistente));

            // Mock save behavior
            Glicemia glicemiaAtualizada = Glicemia.builder()
                    .id(validId)
                    .valorGlicemia(dto.getValorGlicemia())
                    .horario(new Horario()) // Assuming Horario is set properly
                    .comentario(dto.getComentario())
                    .build();
            when(glicemiaRepository.save(any(Glicemia.class))).thenReturn(glicemiaAtualizada);

            // Execute service method
            GlicemiaResponseDTO resultado = atualizarGlicemiaService.executar(validId, dto);

            // Assertions
            assertNotNull(resultado);
            assertEquals(validId, resultado.getId());
            assertEquals(dto.getValorGlicemia(), resultado.getValorGlicemia(), 0.001);
            assertEquals(dto.getComentario(), resultado.getComentario());
        }

        @Test
        @DisplayName("Tenta atualizar glicemia com ID inválido")
        void testUpdateWithInvalidId() {
            UUID id = UUID.randomUUID();
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setValorGlicemia(150.0f);

            // Mock repository to return empty
            when(glicemiaRepository.findById(id)).thenReturn(Optional.empty());

            // Assert that NotFoundException is thrown
            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                atualizarGlicemiaService.executar(id, dto);
            });

            // Assert error message
            assertEquals("Glicemia não encontrada", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de deletar glicemia")
    class DeletarGlicemia {

        @Test
        @DisplayName("Desativa glicemia com sucesso")
        void testDisableBloodSugarSuccessfuly() {

            UUID id = UUID.randomUUID();
            Glicemia glicemia = Glicemia.builder()
                    .id(id)
                    .valorGlicemia(142)
                    .build();

            when(glicemiaRepository.findById(id)).thenReturn(Optional.of(glicemia));

            doNothing().when(glicemiaRepository).deleteById(id);

            deletarGlicemiaService.executar(id);

            verify(glicemiaRepository, times(1)).findById(id);
            verify(glicemiaRepository, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("Tenta desativar glicemia com ID inválido")
        void testDisableBloodSugarWithInvalidId() {

            UUID id = UUID.randomUUID();

            when(glicemiaRepository.findById(id)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                deletarGlicemiaService.executar(id);
            });

            assertEquals("Glicemia não encontrada", exception.getMessage());
        }
    }
}
