package com.grupo2.diabetter;

import com.grupo2.diabetter.controller.HorarioController;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.enuns.Genero;
import com.grupo2.diabetter.enuns.TipoDiabetes;
import com.grupo2.diabetter.enuns.TipoInsulina;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.horario.*;

import com.grupo2.diabetter.service.usuario.CriarUsuarioService;
import jakarta.validation.ConstraintViolationException;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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

    @Mock
    private CriarUsuarioService criarUsuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HorarioRepository horarioRepository;

    @InjectMocks
    private HorarioController horarioController;

    private UsuarioPostPutRequestDTO userDto;

    private Usuario usuarioSalvo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UsuarioPostPutRequestDTO();
              userDto.setNome("João");
              userDto.setEmail("joao@example.com");
              userDto.setPassword("senha123");
              userDto.setDataNasc("1990-01-01");
              userDto.setGenero(Genero.MASCULINO);
              userDto.setAltura(1.75f);
              userDto.setPeso(70.0f);
              userDto.setTipoDiabetes(TipoDiabetes.TIPO_1);
              userDto.setTipoInsulina(TipoInsulina.RAPIDA);
              userDto.setComorbidades(List.of("Hipertensão"));

         usuarioSalvo = Usuario.builder()
                .nome(userDto.getNome())
                .email(userDto.getEmail())
                .senha(userDto.getPassword())
                .dataNasc(userDto.getDataNasc())
                .genero(userDto.getGenero())
                .altura(userDto.getAltura())
                .peso(userDto.getPeso())
                .tipoDiabetes(userDto.getTipoDiabetes())
                .tipoInsulina(userDto.getTipoInsulina())
                .comorbidades(userDto.getComorbidades())
                .build();

    }

    @Nested
    @DisplayName("Conjunto de casos de criar horário")
    class CriacaoHorario {
        @Test
        void testCriarHorarioComDadosValidos() {

            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(UUID.randomUUID(), usuarioSalvo, "08:00", "2025-02-20");

            HorarioResponseDTO horarioSalvo = new HorarioResponseDTO(dto.getId(), dto.getUsuario(), dto.getHorario(), dto.getData_criacao());

            when(criarHorarioService.createHorario(any(HorarioPostPutRequestDTO.class))).thenReturn(horarioSalvo);

            ResponseEntity<?> response = horarioController.createHorario(dto);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(horarioSalvo, response.getBody());
        }
        
        @Test
        void testCriarHorarioComDataInvalida() {

            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(UUID.randomUUID(), usuarioSalvo, "08:00", "data-invalida");

            when(criarHorarioService.createHorario(any(HorarioPostPutRequestDTO.class)))
                    .thenThrow(new IllegalArgumentException("Data inválida"));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                horarioController.createHorario(dto);
            });

            assertEquals("Data inválida", exception.getMessage());
        }

    @Test
    void testCriarHorarioComErroInterno() {

        HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(UUID.randomUUID(), usuarioSalvo, "08:00", "2025-02-20");

        when(criarHorarioService.createHorario(any(HorarioPostPutRequestDTO.class)))
                .thenThrow(new RuntimeException("Erro interno"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            horarioController.createHorario(dto);
        });

        assertEquals("Erro interno", exception.getMessage());
    }
    @Test
    void testCriarHorarioComDadosInvalidos() {

        HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(null, usuarioSalvo, "08:00", "2025-02-20");

        when(criarHorarioService.createHorario(any(HorarioPostPutRequestDTO.class)))
                .thenThrow(new ConstraintViolationException("Dados inválidos", Set.of()));

  
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            horarioController.createHorario(dto);
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

            HorarioResponseDTO responseDTO = new HorarioResponseDTO(id, usuarioSalvo, "08:00", "2025-02-20");

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

        @Test
        void testReadHorarioComErroInterno() {
            UUID id = UUID.randomUUID();

            when(recuperarHorarioService.recuperarHorario(id))
                    .thenThrow(new RuntimeException("Erro interno"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                horarioController.readHorario(id);
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

            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "10:00", "2025-02-21");
            HorarioResponseDTO horarioAtualizado = new HorarioResponseDTO(dto.getId(), dto.getUsuario(), dto.getHorario(), dto.getData_criacao());

            when(atualizarHorarioService.updateHorario(eq(id), any(HorarioPostPutRequestDTO.class))).thenReturn(horarioAtualizado);

            ResponseEntity<?> response = horarioController.updateHorario(id, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(horarioAtualizado, response.getBody());
        }

        @Test
        void testAtualizarHorarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(idInvalido, usuarioSalvo, "10:00", "2025-02-21");

            when(atualizarHorarioService.updateHorario(eq(idInvalido), any(HorarioPostPutRequestDTO.class)))
                    .thenThrow(new NotFoundException("Horario not found"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                horarioController.updateHorario(idInvalido, dto);
            });

            assertEquals("Horario not found", exception.getMessage());
        }

        @Test
        void testAtualizarHorarioComValorInvalido() {
            UUID id = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "valor-invalido", "2025-02-21");

            when(atualizarHorarioService.updateHorario(eq(id), any(HorarioPostPutRequestDTO.class)))
                    .thenThrow(new IllegalArgumentException("Valor inválido"));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                horarioController.updateHorario(id, dto);
            });

            assertEquals("Valor inválido", exception.getMessage());
        }

        @Test
        void testAtualizarHorarioSemMudanca() {
            UUID id = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "08:00", "2025-02-21");
            HorarioResponseDTO horarioAtualizado = new HorarioResponseDTO(id, dto.getUsuario(), dto.getHorario(), dto.getData_criacao());

            when(atualizarHorarioService.updateHorario(eq(id), any(HorarioPostPutRequestDTO.class))).thenReturn(horarioAtualizado);

            ResponseEntity<?> response = horarioController.updateHorario(id, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(horarioAtualizado, response.getBody());
        }

        @Test
        void testAtualizarHorarioComErroInterno() {
            UUID id = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "08:00", "2025-02-21");

            when(atualizarHorarioService.updateHorario(eq(id), any(HorarioPostPutRequestDTO.class)))
                    .thenThrow(new RuntimeException("Erro interno"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                horarioController.updateHorario(id, dto);
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

        @Test
        void testDeletarHorarioJaDesativado() {
            UUID id = UUID.randomUUID();

            doThrow(new IllegalStateException("Horário já desativado")).when(deletarHorarioService).deletarHorario(id);

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                horarioController.disableHorario(id);
            });

            assertEquals("Horário já desativado", exception.getMessage());
        }

        @Test
        void testDeletarHorarioComErroInterno() {
            UUID id = UUID.randomUUID();

            doThrow(new RuntimeException("Erro interno")).when(deletarHorarioService).deletarHorario(id);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                horarioController.disableHorario(id);
            });

            assertEquals("Erro interno", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de listar horários por usuário")
    class ListagemHorario {
        @Test
        void testListarHorariosComUsuarioValido() {
            UUID id = UUID.randomUUID();

            List<HorarioResponseDTO> horariosMockados = Arrays.asList(
                new HorarioResponseDTO(id, usuarioSalvo,"10:00", "2025-02-20"),
                new HorarioResponseDTO(id, usuarioSalvo,"10:00", "2025-02-21")
            );

            when(listarHorarioService.listarHorario(usuarioSalvo.getId())).thenReturn(horariosMockados);

            ResponseEntity<?> response = horarioController.listarHorarios(usuarioSalvo.getId());

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(horariosMockados, response.getBody());
        }

        @Test
        void testListarHorariosUsuarioSemHorarios() {

            when(listarHorarioService.listarHorario(usuarioSalvo.getId())).thenReturn(Collections.emptyList());

            ResponseEntity<?> response = horarioController.listarHorarios(usuarioSalvo.getId());

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(((List<?>) response.getBody()).isEmpty());
        }
    }
}
