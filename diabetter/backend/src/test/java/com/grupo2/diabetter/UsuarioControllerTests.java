package com.grupo2.diabetter;


import com.grupo2.diabetter.controller.UsuarioController;
import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import com.grupo2.diabetter.service.usuario.UsuarioServiceImpl;
import com.grupo2.diabetter.service.usuario.interfaces.UsuarioServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest
class UsuarioControllerTests{

	@Mock
    private UsuarioServiceInterface usuarioService;

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

    
    	when(usuarioService.criarUsuario(any(UsuarioPostPutRequestDTO.class)))
            	.thenAnswer(invocation -> ResponseEntity.ok("Usuário criado com sucesso!"));

    	ResponseEntity<?> response = usuarioController.criarUsuario(dto);

    	assertEquals(HttpStatus.OK, response.getStatusCode());
    	assertEquals("Usuário criado com sucesso!", response.getBody());
    	verify(usuarioService, times(1)).criarUsuario(any(UsuarioPostPutRequestDTO.class));
	}

	@Test
	void testCreateUserWithDuplicateEmail(){
		UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
		dto.setNome("João");
		dto.setEmail("joao@example.com");
    	dto.setPassword("senha123");

		when(usuarioService.criarUsuario(any(UsuarioPostPutRequestDTO.class)))
		.thenAnswer(invocation -> ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado"));

		ResponseEntity<?> response = usuarioController.criarUsuario(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email já cadastrado", response.getBody());
        verify(usuarioService, times(1)).criarUsuario(any(UsuarioPostPutRequestDTO.class));
    }

	@Test
	void testCreateUserWithInvalidDate(){
		UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
		
		when(usuarioService.criarUsuario(any(UsuarioPostPutRequestDTO.class)))
                .thenAnswer(invocation -> ResponseEntity.badRequest().body("Dados inválidos"));

        ResponseEntity<?> response = usuarioController.criarUsuario(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody());
        verify(usuarioService, times(1)).criarUsuario(any(UsuarioPostPutRequestDTO.class));


	}
}

	
@Nested
@DisplayName("Conjunto de casos de lê usuário")
class  leituraUsuario {	

	@Test
	void testReadUserWithValidId(){
		UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNome("João");

        when(usuarioService.listarUsuario(1L)).thenReturn(responseDTO);

        ResponseEntity<UsuarioResponseDTO> response = usuarioController.listarUsuario(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("João", response.getBody().getNome());
	}

	@Test
	void testReadUserWithInvalidId() {
    Long idInvalido = 999L; 

   
    when(usuarioService.listarUsuario(idInvalido))
            .thenThrow(new RuntimeException("Usuário não encontrado"));

   
    Exception exception = assertThrows(RuntimeException.class, () -> {
        usuarioController.listarUsuario(idInvalido);
    });

    
    assertEquals("Usuário não encontrado", exception.getMessage());
    verify(usuarioService, times(1)).listarUsuario(idInvalido);
}

}

@Nested
@DisplayName("Conjunto de casos de atualizar  usuario")
class UsuarioUpdate {

	@Test
	void testAtualizarUsuariosComDadosValidos(){
		UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
        dto.setNome("João Atualizado");

        when(usuarioService.atualizarUsuario(1L, dto))
                .thenAnswer(invocation -> ResponseEntity.ok("Usuário atualizado com sucesso!"));

        ResponseEntity<?> response = usuarioController.atualizarUsuario(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário atualizado com sucesso!", response.getBody());
        verify(usuarioService, times(1)).atualizarUsuario(1L, dto);
	}

	@Test
	void testUpdateUserWithInvalidtId() {
    Long idInexistente = 999L;
    UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
    dto.setNome("João Atualizado");

    
    when(usuarioService.atualizarUsuario(eq(idInexistente), any(UsuarioPostPutRequestDTO.class)))
            .thenAnswer(invocation -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado"));

    
    ResponseEntity<?> response = usuarioController.atualizarUsuario(idInexistente, dto);

    
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Usuário não encontrado", response.getBody());
    verify(usuarioService, times(1)).atualizarUsuario(eq(idInexistente), any(UsuarioPostPutRequestDTO.class));
}

	@Test
	void testUpdateUserWithInvalidData() {
		Long id = 1L;
		UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO(); 
	
		
		when(usuarioService.atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class)))
				.thenAnswer(invocation -> ResponseEntity.badRequest().body("Dados inválidos"));
	
		
		ResponseEntity<?> response = usuarioController.atualizarUsuario(id, dto);
	
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Dados inválidos", response.getBody());
		verify(usuarioService, times(1)).atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class));
	}

	@Test
	void testUpdateUserWithoutChanges() {
		Long id = 1L;
		UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
		dto.setNome("João"); 
	
		
		when(usuarioService.atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class)))
				.thenAnswer(invocation -> ResponseEntity.ok("Nenhuma alteração realizada"));
	
		
		ResponseEntity<?> response = usuarioController.atualizarUsuario(id, dto);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Nenhuma alteração realizada", response.getBody());
		verify(usuarioService, times(1)).atualizarUsuario(eq(id), any(UsuarioPostPutRequestDTO.class));
	}
}

	



@Nested
@DisplayName("Conjunto de casos de deletar o usuario")
class DeletarUsuario {
	@Test
    void testeDisableUserWithSuccess() {
        UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

        when(usuarioService.desativarUsuario(1L, dto))
                .thenAnswer(invocation -> ResponseEntity.ok("Usuário desativado com sucesso!"));

        ResponseEntity<?> response = usuarioController.desativarUsuario(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário desativado com sucesso!", response.getBody());
        verify(usuarioService, times(1)).desativarUsuario(1L, dto);
    }

	@Test
	void testeDisableUserWithInvalidData() {
		Long id = 1L;
		UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO(); 
	
		
		when(usuarioService.desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class)))
				.thenAnswer(invocation -> ResponseEntity.badRequest().body("Dados inválidos"));
	
		
		ResponseEntity<?> response = usuarioController.desativarUsuario(id, dto);
	
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Dados inválidos", response.getBody());
		verify(usuarioService, times(1)).desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class));
	}
	@Test
	void testeDisableUserWithNonexistentId() {
		Long idInexistente = 999L;
		UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();
	
		when(usuarioService.desativarUsuario(eq(idInexistente), any(UsuarioDeleteRequestDTO.class)))
				.thenAnswer(invocation -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado"));
	
		ResponseEntity<?> response = usuarioController.desativarUsuario(idInexistente, dto);
	
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Usuário não encontrado", response.getBody());
		verify(usuarioService, times(1)).desativarUsuario(eq(idInexistente), any(UsuarioDeleteRequestDTO.class));
	}

	@Test
	void testDisableUserAlreadyDeleted() {
		Long id = 1L;
		UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();
	
		when(usuarioService.desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class)))
				.thenAnswer(invocation ->ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já desativado"));
	
		ResponseEntity<?> response = usuarioController.desativarUsuario(id, dto);
	
		
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Usuário já desativado", response.getBody());
		verify(usuarioService, times(1)).desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class));
	}

	@Test
	void testeDisableUserWithoutPermission() {
		Long id = 1L;
		UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();
	
		
		when(usuarioService.desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class)))
				.thenAnswer(invocation ->ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sem permissão para desativar o usuário"));
	
		
		ResponseEntity<?> response = usuarioController.desativarUsuario(id, dto);
	
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("Sem permissão para desativar o usuário", response.getBody());
		verify(usuarioService, times(1)).desativarUsuario(eq(id), any(UsuarioDeleteRequestDTO.class));
	}
}
}






