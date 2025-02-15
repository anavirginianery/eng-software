package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import com.grupo2.diabetter.enuns.Genero;
import com.grupo2.diabetter.exception.EmailDuplicadoException;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.UsuarioServiceImpl;

public class UsuarioServiceTests {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

@Nested
@DisplayName("Conjunto de casos de criar usuario")
class UsuarioCriacao {
    @Test
    void testCriarUsuarioComDadosValidos() {
    	UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
    	dto.setNome("João");
   		dto.setEmail("joao@example.com");
    	dto.setPassword("senha123");

        Usuario usuarioSalvo = Usuario.builder()
        .nome(dto.getNome())
        .email(dto.getEmail())
        .password(dto.getPassword())
        .build();

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        ResponseEntity<?> response = usuarioService.criarUsuario(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário criado com sucesso!", response.getBody());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
    //test com e-mail duplicado


    /*@Test
    void testCreateUserWithInvalidData() {
        UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO(); 
    
        ResponseEntity<?> response = usuarioService.criarUsuario(dto);
    
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody());
    } /* */
  

   
    }

@Nested
@DisplayName("Conjunto de casos de lê usuário")
class  leituraUsuario {
    @Test
    void testeReadUserWithValidId() {
    Long id = 1L;
    Usuario usuario = Usuario.builder()
            .id(id)
            .nome("João")
            .email("joao@example.com")
            .genero(Genero.MASCULINO) 
            .build();

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

    UsuarioResponseDTO responseDTO = usuarioService.listarUsuario(id);

    assertEquals("João", responseDTO.getNome());
    assertEquals("joao@example.com", responseDTO.getEmail());
    assertEquals("MASCULINO", responseDTO.getGenero()); 
    verify(usuarioRepository, times(1)).findById(id);
}

    @Test
    void testeReadUserWithInvalidId() {
        Long idInvalido = 999L;
    
        when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.listarUsuario(idInvalido);
        });
    
        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(idInvalido);
    }

    @Test
    void testReadUserWithInternalError() {
        Long id = 1L;
    
        when(usuarioRepository.findById(id)).thenThrow(new RuntimeException("Erro interno no servidor"));
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.listarUsuario(id);
        });
    
        assertEquals("Erro interno no servidor", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
    }
}

@Nested
@DisplayName("Conjunto de casos de atualizar  usuario")
class UsuarioUpdate {
    @Test
    void testeAtualizarUsuarioComDadosValidos() {
        Long id = 1L;
        UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
        dto.setNome("João Atualizado");
        dto.setEmail("joao@example.com");
        dto.setPassword("senha123");
    
        Usuario usuarioExistente = Usuario.builder()
                .id(id)
                .nome("João")
                .email("joao@example.com")
                .password("senha123")
                .build();
    
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);
    
        ResponseEntity<?> response = usuarioService.atualizarUsuario(id, dto);
    
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário atualizado com sucesso!", response.getBody());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testUpdateUserWithNonexistentId() {
    Long idInexistente = 999L;
    UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
    dto.setNome("João Atualizado");

    
    when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

    
    ResponseEntity<?> response = usuarioService.atualizarUsuario(idInexistente, dto);

    
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Usuário não encontrado", response.getBody());
    verify(usuarioRepository, times(1)).findById(idInexistente);
    verify(usuarioRepository, never()).save(any(Usuario.class));
}
   /*  @Test
    void testUpdateUserWithInvalidData() {
        Long id = 1L;
        UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO(); 
    
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(new Usuario()));
    
        ResponseEntity<?> response = usuarioService.atualizarUsuario(id, dto);
    
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }/* 

    /*@Test
    void testUpdateUserWithoutChanges() {
        Long id = 1L;
        UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
        dto.setNome("João"); 
        dto.setEmail("joao@example.com"); 
    
        Usuario usuarioExistente = Usuario.builder()
                .id(id)
                .nome("João")
                .email("joao@example.com")
                .build();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);
  
        ResponseEntity<?> response = usuarioService.atualizarUsuario(id, dto);
    
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Nenhuma alteração realizada", response.getBody()); //aqui ta dando usuario salvo com sucesso?
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, never()).save(any(Usuario.class)); //pra o metodo nunca ser chamado ao salvar
    }/* */

    
}

@Nested
@DisplayName("Conjunto de casos de deletar o usuario")
class DeletarUsuario {
    @Test
    void testeDisableUserWithSuccess() {
    Long id = 1L;
    UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

    Usuario usuarioExistente = Usuario.builder()
            .id(id)
            .nome("João")
            .email("joao@example.com")
            .build();

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));

    ResponseEntity<?> response = usuarioService.desativarUsuario(id, dto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Usuário desativado com sucesso!", response.getBody());
    verify(usuarioRepository, times(1)).findById(id);
    verify(usuarioRepository, times(1)).deleteById(id);
}

    /* @Test
    void testeDisableUserWithInvalidData() {
        Long id = 1L;
        UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO(); 
    
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(new Usuario()));
    
        ResponseEntity<?> response = usuarioService.desativarUsuario(id, dto);
    
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, never()).deleteById(id);
    } /* */

    @Test
    void testeDisableUserWithNonexistentId() {
        Long idInexistente = 999L;
        UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();
    
        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());
    
        ResponseEntity<?> response = usuarioService.desativarUsuario(idInexistente, dto);
    
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
        verify(usuarioRepository, times(1)).findById(idInexistente);
        verify(usuarioRepository, never()).deleteById(idInexistente);
    }

    @Test
    void testDisableUserAlreadyDeleted() {
        Long id = 1L;
        UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();
    
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
    
        ResponseEntity<?> response = usuarioService.desativarUsuario(id, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, never()).deleteById(id);
    }

    //teste de nenhum usuario desativar outro 
   
    
}
        

    
    
    
    

}
