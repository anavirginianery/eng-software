package com.grupo2.diabetter;

import com.grupo2.diabetter.dto.usuario.*;
import com.grupo2.diabetter.enuns.Genero;
import com.grupo2.diabetter.enuns.TipoDiabetes;
import com.grupo2.diabetter.enuns.TipoInsulina;
import com.grupo2.diabetter.exception.InvalidPasswordException;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.AlterarSenhaService;
import com.grupo2.diabetter.service.usuario.AtualizarUsuarioService;
import com.grupo2.diabetter.service.usuario.CriarUsuarioService;
import com.grupo2.diabetter.service.usuario.DesativarUsuarioService;
import com.grupo2.diabetter.service.usuario.ListarUsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTests {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CriarUsuarioService criarUsuarioService;

    @InjectMocks
    private AtualizarUsuarioService atualizarUsuarioService;

    @InjectMocks
    private AlterarSenhaService alterarSenhaService;

    @InjectMocks
    private DesativarUsuarioService desativarUsuarioService;

    @InjectMocks
    private ListarUsuarioService listarUsuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Conjunto de casos de criar usuário")
    class UsuarioCriacao {

        @Test
        void testCriarUsuarioComDadosValidos() {
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setNome("João");
            dto.setEmail("joao@example.com");
            dto.setPassword("senha123");
            dto.setDataNasc("1990-01-01");
            dto.setGenero(Genero.MASCULINO);
            dto.setAltura(1.75f);
            dto.setPeso(70.0f);
            dto.setTipoDiabetes(TipoDiabetes.TIPO_1);
            dto.setTipoInsulina(TipoInsulina.RAPIDA);
            dto.setComorbidades(List.of("Hipertensão"));

            Usuario usuario = Usuario.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .senha(dto.getPassword())
                    .dataNasc(dto.getDataNasc())
                    .genero(dto.getGenero())
                    .altura(dto.getAltura())
                    .peso(dto.getPeso())
                    .tipoDiabetes(dto.getTipoDiabetes())
                    .tipoInsulina(dto.getTipoInsulina())
                    .comorbidades(dto.getComorbidades())
                    .build();

            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

            Usuario response = criarUsuarioService.criarUsuario(dto);

            assertEquals(usuario.getNome(), response.getNome());
            assertEquals(usuario.getEmail(), response.getEmail());
            assertEquals(usuario.getAltura(), response.getAltura());
            assertEquals(usuario.getPeso(), response.getPeso());
            assertEquals(usuario.getTipoDiabetes(), response.getTipoDiabetes());
            assertEquals(usuario.getTipoInsulina(), response.getTipoInsulina());
            assertEquals(usuario.getComorbidades(), response.getComorbidades());
            verify(usuarioRepository, times(1)).save(any(Usuario.class));
        }

        @Test
        void testCriarUsuarioComEmailDuplicado() {
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setNome("João");
            dto.setEmail("joao@example.com");
            dto.setPassword("senha123");

            when(usuarioRepository.save(any(Usuario.class)))
                    .thenThrow(new DataIntegrityViolationException("Email já cadastrado"));

            DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
                criarUsuarioService.criarUsuario(dto);
            });

            assertEquals("Email já cadastrado", exception.getMessage());
            verify(usuarioRepository, times(1)).save(any(Usuario.class));
        }

        @Test
        void testCriarUsuarioComErroInterno() {
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setNome("João");
            dto.setEmail("joao@example.com");
            dto.setPassword("senha123");

            when(usuarioRepository.save(any(Usuario.class)))
                    .thenThrow(new RuntimeException("Erro interno no servidor"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                criarUsuarioService.criarUsuario(dto);
            });

            assertEquals("Erro interno no servidor", exception.getMessage());
            verify(usuarioRepository, times(1)).save(any(Usuario.class));
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de ler usuário")
    class LeituraUsuario {

        @Test
        void testLerUsuarioComIdValido() {
            UUID id = UUID.randomUUID();
            Usuario usuario = Usuario.builder()
                    .id(id)
                    .nome("João")
                    .email("joao@example.com")
                    .senha("senha123")
                    .dataNasc("1990-01-01")
                    .genero(Genero.MASCULINO)
                    .altura(1.75f)
                    .peso(70.0f)
                    .tipoDiabetes(TipoDiabetes.TIPO_1)
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .comorbidades(List.of("Hipertensão"))
                    .build();

            when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

            UsuarioResponseDTO response = listarUsuarioService.listarUsuario(id);

            assertNotNull(response);
            assertEquals(usuario.getNome(), response.getNome());
            assertEquals(usuario.getEmail(), response.getEmail());
            assertEquals(usuario.getAltura(), response.getAltura());
            assertEquals(usuario.getPeso(), response.getPeso());
            assertEquals(usuario.getTipoDiabetes(), response.getTipoDiabetes());
            assertEquals(usuario.getTipoInsulina(), response.getTipoInsulina());
            assertEquals(usuario.getComorbidades(), response.getComorbidades());
            verify(usuarioRepository, times(1)).findById(id);
        }

        @Test
        void testLerUsuarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();

            when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

            UsuarioResponseDTO response = listarUsuarioService.listarUsuario(idInvalido);

            assertNull(response);
            verify(usuarioRepository, times(1)).findById(idInvalido);
        }

        @Test
        void testLerUsuarioComErroInterno() {
            UUID id = UUID.randomUUID();

            when(usuarioRepository.findById(id))
                    .thenThrow(new IllegalStateException("Erro interno no servidor"));

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                listarUsuarioService.listarUsuario(id);
            });

            assertEquals("Erro interno no servidor", exception.getMessage());
            verify(usuarioRepository, times(1)).findById(id);
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de atualizar usuário")
    class UsuarioUpdate {

        @Test
        void testAtualizarUsuarioComDadosValidos() {
            UUID id = UUID.randomUUID();
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setNome("João Atualizado");
            dto.setDataNasc("1990-01-01");
            dto.setGenero(Genero.MASCULINO);
            dto.setAltura(1.80f);
            dto.setPeso(75.0f);
            dto.setTipoDiabetes(TipoDiabetes.TIPO_2);
            dto.setTipoInsulina(TipoInsulina.LENTA);
            dto.setComorbidades(List.of("Hipertensão", "Obesidade"));

            Usuario usuarioExistente = Usuario.builder()
                    .id(id)
                    .nome("João")
                    .dataNasc("1990-01-01")
                    .genero(Genero.MASCULINO)
                    .altura(1.75f)
                    .peso(70.0f)
                    .tipoDiabetes(TipoDiabetes.TIPO_1)
                    .tipoInsulina(TipoInsulina.RAPIDA)
                    .comorbidades(List.of("Hipertensão"))
                    .build();

            when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Usuario response = atualizarUsuarioService.atualizarUsuario(id, dto);

            assertEquals(dto.getNome(), response.getNome());
            assertEquals(dto.getAltura(), response.getAltura());
            assertEquals(dto.getPeso(), response.getPeso());
            assertEquals(dto.getTipoDiabetes(), response.getTipoDiabetes());
            assertEquals(dto.getTipoInsulina(), response.getTipoInsulina());
            assertEquals(dto.getComorbidades(), response.getComorbidades());
            verify(usuarioRepository, times(1)).findById(id);
            verify(usuarioRepository, times(1)).save(any(Usuario.class));
        }

        @Test
        void testAtualizarUsuarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setNome("João Atualizado");

            when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                atualizarUsuarioService.atualizarUsuario(idInvalido, dto);
            });

            assertEquals("Usuário não encontrado", exception.getMessage());
            verify(usuarioRepository, times(1)).findById(idInvalido);
            verify(usuarioRepository, times(0)).save(any(Usuario.class));
        }

        @Test
        void testAtualizarUsuarioComDadosInvalidos() {
            UUID id = UUID.randomUUID();
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO(); // DTO sem dados obrigatórios

            Usuario usuarioExistente = Usuario.builder()
                    .id(id)
                    .nome("João")
                    .build();

            when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
            when(usuarioRepository.save(any(Usuario.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                atualizarUsuarioService.atualizarUsuario(id, dto);
            });

            assertEquals("Dados inválidos", exception.getMessage());
            verify(usuarioRepository, times(1)).findById(id);
            verify(usuarioRepository, times(1)).save(any(Usuario.class));
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de alterar senha")
    class SenhaUsuario {

        @Test
        void testAlterarSenhaComDadosValidos() throws NotFoundException, InvalidPasswordException {
            UUID id = UUID.randomUUID();
            UsuarioChangePasswordDTO dto = new UsuarioChangePasswordDTO();
            dto.setSenhaAtual("senhaAntiga");
            dto.setNovaSenha("novaSenha");

            Usuario usuario = Usuario.builder()
                    .id(id)
                    .senha("senhaAntiga")
                    .build();

            when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            alterarSenhaService.alterarSenha(id, dto);

            verify(usuarioRepository, times(1)).findById(id);
            verify(usuarioRepository, times(1)).save(any(Usuario.class));
        }

        @Test
        void testAlterarSenhaComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            UsuarioChangePasswordDTO dto = new UsuarioChangePasswordDTO();
            dto.setSenhaAtual("senhaAntiga");
            dto.setNovaSenha("novaSenha");

            when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                alterarSenhaService.alterarSenha(idInvalido, dto);
            });

            assertEquals("Usuário não encontrado", exception.getMessage());
            verify(usuarioRepository, times(1)).findById(idInvalido);
            verify(usuarioRepository, times(0)).save(any(Usuario.class));
        }

        @Test
        void testAlterarSenhaComSenhaAtualInvalida() {
            UUID id = UUID.randomUUID();
            UsuarioChangePasswordDTO dto = new UsuarioChangePasswordDTO();
            dto.setSenhaAtual("senhaErrada");
            dto.setNovaSenha("novaSenha");

            Usuario usuario = Usuario.builder()
                    .id(id)
                    .senha("senhaAntiga")
                    .build();

            when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

            InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> {
                alterarSenhaService.alterarSenha(id, dto);
            });

            assertEquals("Password Inválido:", exception.getMessage());
            verify(usuarioRepository, times(1)).findById(id);
            verify(usuarioRepository, times(0)).save(any(Usuario.class));
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de deletar usuário")
    class DeletarUsuario {

        @Test
        void testDesativarUsuarioComSucesso() {
            UUID id = UUID.randomUUID();
            UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

            Usuario usuario = Usuario.builder()
                    .id(id)
                    .build();

            when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
            doNothing().when(usuarioRepository).deleteById(id);

            ResponseEntity<?> response = desativarUsuarioService.desativarUsuario(id, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Usuário desativado com sucesso!", response.getBody());
            verify(usuarioRepository, times(1)).findById(id);
            verify(usuarioRepository, times(1)).deleteById(id);
        }

        @Test
        void testDesativarUsuarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

            when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

            ResponseEntity<?> response = desativarUsuarioService.desativarUsuario(idInvalido, dto);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Erro ao desativar usuário: Usuário não encontrado", response.getBody());
            verify(usuarioRepository, times(1)).findById(idInvalido);
            verify(usuarioRepository, times(0)).deleteById(idInvalido);
        }
    }

    @Test
void completarPerfil_UsuarioEncontrado_DeveRetornarUsuarioAtualizado() {
    UUID id = UUID.randomUUID();
    CompletarPerfil dto = new CompletarPerfil();
    dto.setAltura(1.80f);
    dto.setPeso(75.0f);
    dto.setTipoDiabetes(TipoDiabetes.TIPO_2);
    dto.setTipoInsulina(TipoInsulina.LENTA);
    dto.setComorbidades(List.of("Hipertensão"));

    Usuario usuarioExistente = Usuario.builder()
            .id(id)
            .nome("João")
            .build();

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
    when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Usuario response = criarUsuarioService.completarPerfil(id, dto);

    assertEquals(dto.getAltura(), response.getAltura());
    assertEquals(dto.getPeso(), response.getPeso());
    assertEquals(dto.getTipoDiabetes(), response.getTipoDiabetes());
    assertEquals(dto.getTipoInsulina(), response.getTipoInsulina());
    assertEquals(dto.getComorbidades(), response.getComorbidades());
    verify(usuarioRepository, times(1)).findById(id);
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
}

@Test
void completarPerfil_UsuarioNaoEncontrado_DeveLancarExcecao() {
    UUID id = UUID.randomUUID();
    CompletarPerfil dto = new CompletarPerfil();

    when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        criarUsuarioService.completarPerfil(id, dto);
    });

    assertEquals("Usuário não encontrado", exception.getMessage());
    verify(usuarioRepository, times(1)).findById(id);
    verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    @Test
void listarUsuarios_ListaVazia_DeveRetornarListaVazia() {
    when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

    List<UsuarioResponseDTO> response = listarUsuarioService.listarUsuarios();

    assertTrue(response.isEmpty());
    verify(usuarioRepository, times(1)).findAll();
}

@Test
void desativarUsuario_ErroAoDesativar_DeveRetornarErro() {
    UUID id = UUID.randomUUID();
    UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(new Usuario()));
    doThrow(new RuntimeException("Erro ao desativar usuário")).when(usuarioRepository).deleteById(id);

    ResponseEntity<?> response = desativarUsuarioService.desativarUsuario(id, dto);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Erro ao desativar usuário: Erro ao desativar usuário", response.getBody());
    verify(usuarioRepository, times(1)).findById(id);
    verify(usuarioRepository, times(1)).deleteById(id);
}

@Test
void atualizarUsuario_ErroAoSave_DeveLancarExcecao() {
    UUID id = UUID.randomUUID();
    UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
    dto.setNome("João Atualizado");

    Usuario usuarioExistente = Usuario.builder().id(id).nome("João").build();

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
    when(usuarioRepository.save(any(Usuario.class))).thenThrow(new RuntimeException("Erro ao salvar usuário"));

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        atualizarUsuarioService.atualizarUsuario(id, dto);
    });

    assertEquals("Erro ao salvar usuário", exception.getMessage());
    verify(usuarioRepository, times(1)).findById(id);
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
}

@Test
void alterarSenha_ErroAoSave_DeveLancarExcecao() {
    UUID id = UUID.randomUUID();
    UsuarioChangePasswordDTO dto = new UsuarioChangePasswordDTO();
    dto.setSenhaAtual("senhaAntiga");
    dto.setNovaSenha("novaSenha");

    Usuario usuario = Usuario.builder().id(id).senha("senhaAntiga").build();

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
    when(usuarioRepository.save(any(Usuario.class))).thenThrow(new RuntimeException("Erro ao salvar senha"));

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        alterarSenhaService.alterarSenha(id, dto);
    });

    assertEquals("Erro ao salvar senha", exception.getMessage());
    verify(usuarioRepository, times(1)).findById(id);
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
}

@Test
void criarUsuario_PasswordVazio_DeveLancarExcecao() {
    UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
    dto.setNome("João");
    dto.setEmail("joao@example.com");
    dto.setPassword(""); // Password vazio

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        criarUsuarioService.criarUsuario(dto);
    });

    assertEquals("Password cannot be empty", exception.getMessage());
    verify(usuarioRepository, times(0)).save(any(Usuario.class)); // Nenhum save deve ser chamado
}

@Test
void criarUsuario_PasswordNulo_DeveLancarExcecao() {
    UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
    dto.setNome("João");
    dto.setEmail("joao@example.com");
    dto.setPassword(null); // Password nulo

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        criarUsuarioService.criarUsuario(dto);
    });

    assertEquals("Password cannot be empty", exception.getMessage());
    verify(usuarioRepository, times(0)).save(any(Usuario.class)); // Nenhum save deve ser chamado
}
}