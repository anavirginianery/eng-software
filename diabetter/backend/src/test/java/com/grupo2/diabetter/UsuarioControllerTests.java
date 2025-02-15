package com.grupo2.diabetter;


import com.grupo2.diabetter.controller.UsuarioController;
import com.grupo2.diabetter.dto.usuario.UsuarioChangePasswordDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import com.grupo2.diabetter.enuns.Genero;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.UUID;


@SpringBootTest
class UsuarioControllerTests{

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
@DisplayName("Conjunto de casos de criar usuario")
    class UsuarioCriacao {
    @Test
	void testeCriarUsuarioComDadosValidos() {
            UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
            dto.setNome("João");
            dto.setEmail("joao@example.com");
            dto.setPassword("senha123");
            dto.setDataNasc("1990-01-01");
            dto.setGenero(Genero.MASCULINO);
            dto.setTelefone("1234567890");

            Usuario usuarioSalvo = Usuario.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .password(dto.getPassword())
                    .dataNasc(dto.getDataNasc())
                    .genero(dto.getGenero())
                    .telefone(dto.getTelefone())
                    .build();

            when(criarUsuarioService.criarUsuario(any(UsuarioPostPutRequestDTO.class)))
                    .thenReturn(usuarioSalvo);

            Usuario response = usuarioController.criarUsuario(dto);

            assertEquals(usuarioSalvo.getNome(), response.getNome());
            assertEquals(usuarioSalvo.getEmail(), response.getEmail());
            verify(criarUsuarioService, times(1)).criarUsuario(any(UsuarioPostPutRequestDTO.class));
        }



	@Test
	void testCreateUserWithDuplicateEmail() {
		UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
		dto.setNome("João");
		dto.setEmail("joao@example.com");
		dto.setPassword("senha123");
		dto.setDataNasc("1990-01-01");
		dto.setGenero(Genero.MASCULINO);
		dto.setTelefone("1234567890");

		when(criarUsuarioService.criarUsuario(any(UsuarioPostPutRequestDTO.class)))
				.thenThrow(new RuntimeException("Email já cadastrado"));

		try {
			usuarioController.criarUsuario(dto);
		} catch (RuntimeException e) {
			assertEquals("Email já cadastrado", e.getMessage());
		}

		verify(criarUsuarioService, times(1)).criarUsuario(any(UsuarioPostPutRequestDTO.class));
	}

	@Test
	void testeCriarUsuarioComErroInterno() {
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
        void testReadUserWithValidId() {
            UUID id = UUID.randomUUID();
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
            responseDTO.setId(id);
            responseDTO.setNome("João");

            when(listarUsuarioService.listarUsuario(id)).thenReturn(responseDTO);

            ResponseEntity<UsuarioResponseDTO> response = usuarioController.listarUsuario(id);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("João", response.getBody().getNome());
            verify(listarUsuarioService, times(1)).listarUsuario(id);
        }

        @Test
        void testReadUserWithInvalidId() {
            UUID idInvalido = UUID.randomUUID();

            when(listarUsuarioService.listarUsuario(idInvalido)).thenReturn(null);

            ResponseEntity<UsuarioResponseDTO> response = usuarioController.listarUsuario(idInvalido);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNull(response.getBody());
            verify(listarUsuarioService, times(1)).listarUsuario(idInvalido);
        }

		@Test
		void testReadUserWithInternalError() {
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
	void testAtualizarUsuariosComDadosValidos() {
		UUID id = UUID.randomUUID();
		UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
		dto.setNome("João Atualizado");
		dto.setDataNasc("1990-01-01");
		dto.setGenero(Genero.MASCULINO);
		dto.setTelefone("1234567890");
		dto.setEmail("joao@example.com");
		dto.setPassword("senha123");

		Usuario usuarioAtualizado = new Usuario();
		usuarioAtualizado.setId(id);
		usuarioAtualizado.setNome(dto.getNome());
		usuarioAtualizado.setDataNasc(dto.getDataNasc());
		usuarioAtualizado.setGenero(dto.getGenero());
		usuarioAtualizado.setTelefone(dto.getTelefone());
		usuarioAtualizado.setEmail(dto.getEmail());
		usuarioAtualizado.setPassword(dto.getPassword());

		when(atualizarUsuarioService.atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class)))
				.thenReturn(usuarioAtualizado);

		Usuario response = usuarioController.atualizarUsuario(id, dto);

		assertEquals(usuarioAtualizado.getNome(), response.getNome());
		verify(atualizarUsuarioService, times(1)).atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class));
	}

	@Test
	void testUpdateUserWithInvalidId() {
		UUID idInexistente = UUID.randomUUID();
		UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
		dto.setNome("João Atualizado");

		when(atualizarUsuarioService.atualizarUsuario(eq(idInexistente), any(UsuarioPostPutRequestDTO.class)))
				.thenThrow(new NotFoundException("Usuário não encontrado"));

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			usuarioController.atualizarUsuario(idInexistente, dto);
		});

		assertEquals("Usuário não encontrado", exception.getMessage());
		verify(atualizarUsuarioService, times(1)).atualizarUsuario(eq(idInexistente), any(UsuarioPostPutRequestDTO.class));
	}

	@Test
	void testUpdateUserWithInvalidData() {
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

	@Test
	void testUpdateUserWithoutChanges() {
		UUID id = UUID.randomUUID();
		UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
		dto.setNome("João"); // Nome igual ao existente

		Usuario usuarioExistente = new Usuario();
		usuarioExistente.setId(id);
		usuarioExistente.setNome(dto.getNome());

		when(atualizarUsuarioService.atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class)))
				.thenReturn(usuarioExistente);

		Usuario response = usuarioController.atualizarUsuario(id, dto);

		assertEquals(usuarioExistente.getNome(), response.getNome());
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
@DisplayName("Conjunto de casos de deletar o usuario")
class DeletarUsuario {

	@Test
    void testeDisableUserWithSuccess() {
        UUID id = UUID.randomUUID();
        UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

        when(desativarUsuarioService.desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class)))
		.thenAnswer(invocation -> ResponseEntity.ok("Usuário desativado com sucesso!"));

        ResponseEntity<?> response = usuarioController.desativarUsuario(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário desativado com sucesso!", response.getBody());
        verify(desativarUsuarioService, times(1)).desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class));
    }

    @Test
    void testeDisableUserWithInvalidData() {
        UUID id = UUID.randomUUID();
        UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO(); // DTO sem dados obrigatórios

        when(desativarUsuarioService.desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class)))
		.thenAnswer(invocation -> ResponseEntity.badRequest().body("Dados inválidos"));

        ResponseEntity<?> response = usuarioController.desativarUsuario(id, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody());
        verify(desativarUsuarioService, times(1)).desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class));
    }

    
    @Test
    void testDisableUserAlreadyDeleted() {
        UUID id = UUID.randomUUID();
        UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

        when(desativarUsuarioService.desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class)))
		.thenAnswer(invocation -> ResponseEntity.badRequest().body("Usuário já desativado"));

        ResponseEntity<?> response = usuarioController.desativarUsuario(id, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Usuário já desativado", response.getBody());
        verify(desativarUsuarioService, times(1)).desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class));
    }

    @Test
	void testeDisableUserWithNonexistentId() {
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

}






