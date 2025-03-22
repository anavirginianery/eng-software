package com.grupo2.diabetter;

import com.grupo2.diabetter.controller.UsuarioController;
import com.grupo2.diabetter.dto.usuario.UsuarioChangePasswordDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import com.grupo2.diabetter.enuns.Genero;
import com.grupo2.diabetter.enuns.TipoDiabetes;
import com.grupo2.diabetter.enuns.TipoInsulina;
import com.grupo2.diabetter.exception.InvalidPasswordException;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Usuario;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class UsuarioControllerTests {

    @Mock
    private AlterarSenhaService alterarSenhaService;

    @Mock
    private AtualizarUsuarioService atualizarUsuarioService;

    @Mock
    private CriarUsuarioService criarUsuarioService;

    @Mock
    private DesativarUsuarioService desativarUsuarioService;

    @Mock
    private ListarUsuarioService listarUsuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

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

            Usuario usuarioSalvo = Usuario.builder()
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

            when(criarUsuarioService.criarUsuario(any(UsuarioPostPutRequestDTO.class)))
                    .thenReturn(usuarioSalvo);

            Usuario response = usuarioController.criarUsuario(dto);

            assertEquals(usuarioSalvo.getNome(), response.getNome());
            assertEquals(usuarioSalvo.getEmail(), response.getEmail());
            assertEquals(usuarioSalvo.getAltura(), response.getAltura());
            assertEquals(usuarioSalvo.getPeso(), response.getPeso());
            assertEquals(usuarioSalvo.getTipoDiabetes(), response.getTipoDiabetes());
            assertEquals(usuarioSalvo.getTipoInsulina(), response.getTipoInsulina());
            assertEquals(usuarioSalvo.getComorbidades(), response.getComorbidades());
            verify(criarUsuarioService, times(1)).criarUsuario(any(UsuarioPostPutRequestDTO.class));
        }

        @Test
        void testCriarUsuarioComEmailDuplicado() {
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setNome("João");
            dto.setEmail("joao@example.com");
            dto.setPassword("senha123");

            when(criarUsuarioService.criarUsuario(any(UsuarioPostPutRequestDTO.class)))
                    .thenThrow(new RuntimeException("Email já cadastrado"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                usuarioController.criarUsuario(dto);
            });

            assertEquals("Email já cadastrado", exception.getMessage());
            verify(criarUsuarioService, times(1)).criarUsuario(any(UsuarioPostPutRequestDTO.class));
        }

        @Test
        void testCriarUsuarioComErroInterno() {
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setNome("João");
            dto.setEmail("joao@example.com");
            dto.setPassword("senha123");

            when(criarUsuarioService.criarUsuario(any(UsuarioPostPutRequestDTO.class)))
                    .thenThrow(new RuntimeException("Erro interno no servidor"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                usuarioController.criarUsuario(dto);
            });

            assertEquals("Erro interno no servidor", exception.getMessage());
            verify(criarUsuarioService, times(1)).criarUsuario(any(UsuarioPostPutRequestDTO.class));
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de ler usuário")
    class LeituraUsuario {

        @Test
        void testLerUsuarioComIdValido() {
            UUID id = UUID.randomUUID();
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
            responseDTO.setId(id);
            responseDTO.setNome("João");
            responseDTO.setAltura(1.75f);
            responseDTO.setPeso(70.0f);
            responseDTO.setTipoDiabetes(TipoDiabetes.TIPO_1);
            responseDTO.setTipoInsulina(TipoInsulina.RAPIDA);
            responseDTO.setComorbidades(List.of("Hipertensão"));

            when(listarUsuarioService.listarUsuario(id)).thenReturn(responseDTO);

            ResponseEntity<UsuarioResponseDTO> response = usuarioController.listarUsuario(id);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("João", response.getBody().getNome());
            assertEquals(1.75f, response.getBody().getAltura());
            assertEquals(70.0f, response.getBody().getPeso());
            assertEquals(TipoDiabetes.TIPO_1, response.getBody().getTipoDiabetes());
            assertEquals(TipoInsulina.RAPIDA, response.getBody().getTipoInsulina());
            assertEquals(List.of("Hipertensão"), response.getBody().getComorbidades());
            verify(listarUsuarioService, times(1)).listarUsuario(id);
        }

        @Test
        void testLerUsuarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();

            when(listarUsuarioService.listarUsuario(idInvalido)).thenReturn(null);

            ResponseEntity<UsuarioResponseDTO> response = usuarioController.listarUsuario(idInvalido);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNull(response.getBody());
            verify(listarUsuarioService, times(1)).listarUsuario(idInvalido);
        }

        @Test
        void testLerUsuarioComErroInterno() {
            UUID id = UUID.randomUUID();

            when(listarUsuarioService.listarUsuario(id))
                    .thenThrow(new IllegalStateException("Erro interno no servidor"));

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                usuarioController.listarUsuario(id);
            });

            assertEquals("Erro interno no servidor", exception.getMessage());
            verify(listarUsuarioService, times(1)).listarUsuario(id);
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

            Usuario usuarioAtualizado = Usuario.builder()
                    .id(id)
                    .nome(dto.getNome())
                    .dataNasc(dto.getDataNasc())
                    .genero(dto.getGenero())
                    .altura(dto.getAltura())
                    .peso(dto.getPeso())
                    .tipoDiabetes(dto.getTipoDiabetes())
                    .tipoInsulina(dto.getTipoInsulina())
                    .comorbidades(dto.getComorbidades())
                    .build();

            when(atualizarUsuarioService.atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class)))
                    .thenReturn(usuarioAtualizado);

            Usuario response = usuarioController.atualizarUsuario(id, dto);

            assertEquals(usuarioAtualizado.getNome(), response.getNome());
            assertEquals(usuarioAtualizado.getAltura(), response.getAltura());
            assertEquals(usuarioAtualizado.getPeso(), response.getPeso());
            assertEquals(usuarioAtualizado.getTipoDiabetes(), response.getTipoDiabetes());
            assertEquals(usuarioAtualizado.getTipoInsulina(), response.getTipoInsulina());
            assertEquals(usuarioAtualizado.getComorbidades(), response.getComorbidades());
            verify(atualizarUsuarioService, times(1)).atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class));
        }

        @Test
        void testAtualizarUsuarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setNome("João Atualizado");

            when(atualizarUsuarioService.atualizarUsuario(eq(idInvalido), any(UsuarioPostPutRequestDTO.class)))
                    .thenThrow(new NotFoundException("Usuário não encontrado"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                usuarioController.atualizarUsuario(idInvalido, dto);
            });

            assertEquals("Usuário não encontrado", exception.getMessage());
            verify(atualizarUsuarioService, times(1)).atualizarUsuario(eq(idInvalido), any(UsuarioPostPutRequestDTO.class));
        }

        @Test
        void testAtualizarUsuarioComDadosInvalidos() {
            UUID id = UUID.randomUUID();
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO(); // DTO sem dados obrigatórios

            when(atualizarUsuarioService.atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class)))
                    .thenThrow(new IllegalArgumentException("Dados inválidos"));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                usuarioController.atualizarUsuario(id, dto);
            });

            assertEquals("Dados inválidos", exception.getMessage());
            verify(atualizarUsuarioService, times(1)).atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class));
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

            doNothing().when(alterarSenhaService).alterarSenha(eq(id), any(UsuarioChangePasswordDTO.class));

            ResponseEntity<?> response = usuarioController.alterarSenha(id, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Senha alterada com sucesso", response.getBody());
            verify(alterarSenhaService, times(1)).alterarSenha(eq(id), any(UsuarioChangePasswordDTO.class));
        }

        @Test
        void testAlterarSenhaComIdInvalido() throws NotFoundException, InvalidPasswordException {
            UUID idInvalido = UUID.randomUUID();
            UsuarioChangePasswordDTO dto = new UsuarioChangePasswordDTO();
            dto.setSenhaAtual("senhaAntiga");
            dto.setNovaSenha("novaSenha");

            doThrow(new NotFoundException("Usuário não encontrado"))
                    .when(alterarSenhaService).alterarSenha(eq(idInvalido), any(UsuarioChangePasswordDTO.class));

            ResponseEntity<?> response = usuarioController.alterarSenha(idInvalido, dto);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Usuário não encontrado", response.getBody());
            verify(alterarSenhaService, times(1)).alterarSenha(eq(idInvalido), any(UsuarioChangePasswordDTO.class));
        }

        @Test
        void testAlterarSenhaComSenhaAtualInvalida() throws NotFoundException, InvalidPasswordException {
            UUID id = UUID.randomUUID();
            UsuarioChangePasswordDTO dto = new UsuarioChangePasswordDTO();
            dto.setSenhaAtual("senhaErrada");
            dto.setNovaSenha("novaSenha");

            doThrow(new InvalidPasswordException("Senha atual inválida"))
                    .when(alterarSenhaService).alterarSenha(eq(id), any(UsuarioChangePasswordDTO.class));

            ResponseEntity<?> response = usuarioController.alterarSenha(id, dto);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Password Inválido:", response.getBody());
            verify(alterarSenhaService, times(1)).alterarSenha(eq(id), any(UsuarioChangePasswordDTO.class));
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de deletar usuário")
    class DeletarUsuario {

        @Test
        void testDesativarUsuarioComSucesso() {
            UUID id = UUID.randomUUID();
            UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

            when(desativarUsuarioService.desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class)))
                    .thenReturn((ResponseEntity) ResponseEntity.ok("Usuário desativado com sucesso!"));

            ResponseEntity<?> response = usuarioController.desativarUsuario(id, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Usuário desativado com sucesso!", response.getBody());
            verify(desativarUsuarioService, times(1)).desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class));
        }

        @Test
        void testDesativarUsuarioComIdInvalido() {
            UUID idInvalido = UUID.randomUUID();
            UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

            when(desativarUsuarioService.desativarUsuario(eq(idInvalido), any(UsuarioDeleteRequestDTO.class)))
                    .thenThrow(new NotFoundException("Usuário não encontrado"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                usuarioController.desativarUsuario(idInvalido, dto);
            });

            assertEquals("Usuário não encontrado", exception.getMessage());
            verify(desativarUsuarioService, times(1)).desativarUsuario(eq(idInvalido), any(UsuarioDeleteRequestDTO.class));
        }
    }

        @Test
        void completarPerfilUsuario_UsuarioEncontrado_DeveRetornarUsuarioAtualizado() {
            UUID id = UUID.randomUUID();
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setAltura(1.80f);
            dto.setPeso(75.0f);

            Usuario usuarioAtualizado = Usuario.builder()
                    .id(id)
                    .altura(dto.getAltura())
                    .peso(dto.getPeso())
                    .build();

            when(criarUsuarioService.completarPerfil(id, dto)).thenReturn(usuarioAtualizado);

            ResponseEntity<Usuario> response = usuarioController.completarPerfilUsuario(id, dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1.80f, response.getBody().getAltura());
            assertEquals(75.0f, response.getBody().getPeso());
            verify(criarUsuarioService, times(1)).completarPerfil(id, dto);
    }


    @Test
void listarUsuarios_ListaVazia_DeveRetornarListaVazia() {
    when(listarUsuarioService.listarUsuarios()).thenReturn(Collections.emptyList());

    ResponseEntity<List<UsuarioResponseDTO>> response = usuarioController.listarUsuarios();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().isEmpty());
    verify(listarUsuarioService, times(1)).listarUsuarios();
}

@Test
void listarUsuarios_ListaComUsuarios_DeveRetornarListaComUsuarios() {
    UsuarioResponseDTO usuario1 = new UsuarioResponseDTO();
    usuario1.setNome("João");
    UsuarioResponseDTO usuario2 = new UsuarioResponseDTO();
    usuario2.setNome("Maria");

    when(listarUsuarioService.listarUsuarios()).thenReturn(List.of(usuario1, usuario2));

    ResponseEntity<List<UsuarioResponseDTO>> response = usuarioController.listarUsuarios();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
    assertEquals("João", response.getBody().get(0).getNome());
    assertEquals("Maria", response.getBody().get(1).getNome());
    verify(listarUsuarioService, times(1)).listarUsuarios();
}
}