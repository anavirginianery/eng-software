package com.grupo2.diabetter;

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
import org.junit.jupiter.api.*;
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

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioPostPutRequestDTO userDto;

    private Usuario usuarioSalvo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID usuarioId = UUID.fromString("3fb126ed-dcf0-43e4-9d49-3d00ba55c00a");

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
                .id(usuarioId)
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
        void testCreareScheduleWithvalidDate() {
            UUID id = UUID.randomUUID();
            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "08:00", "2025-02-20");
            Horario horarioSalvo = new Horario();
            horarioSalvo.setId(dto.getId());
            horarioSalvo.setUsuario(dto.getUsuario());
            horarioSalvo.setHorario(dto.getHorario());
            horarioSalvo.setData_criacao(dto.getData_criacao());

            when(usuarioRepository.findById(usuarioSalvo.getId())).thenReturn(Optional.of(usuarioSalvo));
            when(horarioRepository.save(any(Horario.class))).thenReturn(horarioSalvo);

            HorarioResponseDTO response = criarHorarioService.createHorario(dto);

            assertNotNull(response);
            assertEquals(dto.getHorario(), response.getHorario());
            assertEquals(dto.getData_criacao(), response.getData_criacao() );
            assertEquals(dto.getUsuario(), response.getUsuario());
        }

        
    @Test
    void testCreateScheduleWithInvalidDate() {
        UUID id = UUID.randomUUID();
        HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo,"08:00", "data-invalida");

        when(usuarioRepository.findById(usuarioSalvo.getId())).thenReturn(Optional.of(usuarioSalvo));
        when(horarioRepository.save(any(Horario.class)))
                .thenThrow(new IllegalArgumentException("Data inválida"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            criarHorarioService.createHorario(dto);
        });

        assertEquals("Data inválida", exception.getMessage());
    }


    @Test
    void testCreateScheduleWithInternalError() {
        UUID id = UUID.randomUUID();
        HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "08:00", "2025-02-20");

        when(usuarioRepository.findById(usuarioSalvo.getId())).thenReturn(Optional.of(usuarioSalvo));
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

        UUID id = UUID.randomUUID();
        HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, null, "2025-02-20");

        when(usuarioRepository.findById(usuarioSalvo.getId())).thenReturn(Optional.of(usuarioSalvo));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            criarHorarioService.createHorario(dto);
        });

        assertEquals("O valor do horário não pode ser nulo ou vazio", exception.getMessage());

   
        verify(horarioRepository, never()).save(any(Horario.class));
    }

    @Test
    void testCriarHorarioComUsuarioInvalido(){

        UUID id = UUID.randomUUID();
        HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "08:00", "2025-02-20");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            criarHorarioService.createHorario(dto);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());


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
            horario.setId(id);
            horario.setUsuario(usuarioSalvo);
            horario.setData_criacao("2025-02-20");
            horario.setHorario("08:00");

            when(horarioRepository.findById(id)).thenReturn(Optional.of(horario));

            HorarioResponseDTO response = recuperarHorarioService.recuperarHorario(id);

            assertNotNull(response);
            assertEquals(horario.getHorario(), response.getHorario() );
            assertEquals(horario.getData_criacao(), response.getData_criacao() );
            assertEquals(horario.getUsuario(), response.getUsuario());
        }

        @Test
        void testRecuperarHorarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();

            when(usuarioRepository.findById(usuarioSalvo.getId())).thenReturn(Optional.of(usuarioSalvo));
            when(horarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                recuperarHorarioService.recuperarHorario(idInvalido);
            });

            assertEquals("Horário não encontrado", exception.getMessage());
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

            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "10:00", "2025-02-21");

            Horario horarioExistente = new Horario();
            horarioExistente.setId(id);
            horarioExistente.setHorario("08:00");
            horarioExistente.setData_criacao("2025-02-20");
            horarioExistente.setUsuario(usuarioSalvo);

            when(usuarioRepository.findById(usuarioSalvo.getId())).thenReturn(Optional.of(usuarioSalvo));
            when(horarioRepository.findById(id)).thenReturn(Optional.of(horarioExistente));

            Horario horarioAtualizado = new Horario();
            horarioAtualizado.setId(dto.getId());
            horarioAtualizado.setHorario(dto.getHorario());
            horarioAtualizado.setData_criacao(dto.getData_criacao());
            horarioAtualizado.setUsuario(dto.getUsuario());

            when(usuarioRepository.findById(usuarioSalvo.getId())).thenReturn(Optional.of(usuarioSalvo));
            when(horarioRepository.save(any(Horario.class))).thenReturn(horarioAtualizado);

            HorarioResponseDTO response = atualizarHorarioService.updateHorario(id, dto);

            assertNotNull(response);
            assertEquals(dto.getHorario(), response.getHorario());
            assertEquals(dto.getData_criacao(), response.getData_criacao());
            assertEquals(dto.getUsuario(), response.getUsuario());
        }

        @Test
        void testAtualizarHorarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();

            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(idInvalido, usuarioSalvo, "10:00", "2025-02-21");

            when(horarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                atualizarHorarioService.updateHorario(idInvalido, dto);
            });

            assertEquals("Horario não encontrado", exception.getMessage());
        }

        @Test
        void testAtualizarHorarioComUsuarioInvalido() {
            UUID id = UUID.randomUUID();
            UUID idUserInexistente = UUID.randomUUID();

            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "10:00", "2025-02-21");

            Horario horario = new Horario();
            horario.setId(dto.getId());
            horario.setHorario(dto.getHorario());
            horario.setData_criacao(dto.getData_criacao());
            horario.setUsuario(dto.getUsuario());

            when(horarioRepository.findById(id)).thenReturn(Optional.of(horario));
            when(usuarioRepository.findById(idUserInexistente)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                atualizarHorarioService.updateHorario(id, dto);
            });

            assertEquals("Usuário não encontrado", exception.getMessage());
        }

        @Test
        void testUpdateScheduleWithInvalidValue() {
            UUID id = UUID.randomUUID();

            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "valor-invalido", "2025-02-20");

            when(usuarioRepository.findById(usuarioSalvo.getId())).thenReturn(Optional.of(usuarioSalvo));
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

            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo, "08:00", "2025-02-20");

            Horario horarioExistente = new Horario();
            horarioExistente.setId(id);
            horarioExistente.setHorario(dto.getHorario());
            horarioExistente.setData_criacao(dto.getData_criacao());
            horarioExistente.setUsuario(dto.getUsuario());

            when(usuarioRepository.findById(usuarioSalvo.getId())).thenReturn(Optional.of(usuarioSalvo));
            when(horarioRepository.findById(id)).thenReturn(Optional.of(horarioExistente));
            when(horarioRepository.save(any(Horario.class))).thenReturn(horarioExistente);

            HorarioResponseDTO response = atualizarHorarioService.updateHorario(id, dto);

            assertNotNull(response);
            assertEquals(dto.getHorario(), response.getHorario());
            assertEquals(dto.getData_criacao(), response.getData_criacao());
            assertEquals(dto.getUsuario(), response.getUsuario());
        }

        @Test
        void testUpdateScheduleWithInternalError() {
            UUID id = UUID.randomUUID();

            HorarioPostPutRequestDTO dto = new HorarioPostPutRequestDTO(id, usuarioSalvo,"08:00", "2025-02-20");

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
            UUID id = UUID.randomUUID();

            Horario horarioExistente = new Horario();
            horarioExistente.setId(id);
            horarioExistente.setHorario("08:00");
            horarioExistente.setData_criacao("2025-02-20");
            horarioExistente.setUsuario(usuarioSalvo);

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

            Horario horarioExistente = new Horario();
            horarioExistente.setId(id);
            horarioExistente.setHorario("08:00");
            horarioExistente.setData_criacao("2025-02-20");
            horarioExistente.setUsuario(usuarioSalvo);

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
            UUID id = UUID.randomUUID();

            Horario horarioExistente = new Horario();
            horarioExistente.setId(id);
            horarioExistente.setHorario("08:00");
            horarioExistente.setData_criacao("2025-02-20");
            horarioExistente.setUsuario(usuarioSalvo);

            UUID id_2 = UUID.randomUUID();

            Horario horarioExistente_2 = new Horario();
            horarioExistente_2.setId(id_2);
            horarioExistente_2.setHorario("10:00");
            horarioExistente_2.setData_criacao("2025-02-21");
            horarioExistente_2.setUsuario(usuarioSalvo);

            List<Horario> horarios = Arrays.asList(horarioExistente, horarioExistente_2);

            when(horarioRepository.findAllByUserId(usuarioSalvo.getId())).thenReturn(horarios);

            List<HorarioResponseDTO> response = listarHorarioService.listarHorario(usuarioSalvo.getId());

            assertNotNull(response);
            assertEquals(2, response.size());
            assertEquals(horarioExistente.getUsuario(), response.get(0).getUsuario());
            assertEquals(horarioExistente.getUsuario(), response.get(1).getUsuario());
        }

        @Test
        void testListarHorariosUsuarioSemHorarios() {

            when(horarioRepository.findAllByUserId(usuarioSalvo.getId())).thenReturn(Collections.emptyList());

            List<HorarioResponseDTO> response = listarHorarioService.listarHorario(usuarioSalvo.getId());

            assertNotNull(response);
            assertTrue(response.isEmpty());
        }
    }
}