package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

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

import com.grupo2.diabetter.dto.usuario.UsuarioChangePasswordDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import com.grupo2.diabetter.enuns.Genero;
import com.grupo2.diabetter.exception.InvalidPasswordException;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.AlterarSenhaService;
import com.grupo2.diabetter.service.usuario.AtualizarUsuarioService;
import com.grupo2.diabetter.service.usuario.CriarUsuarioService;
import com.grupo2.diabetter.service.usuario.DesativarUsuarioService;
import com.grupo2.diabetter.service.usuario.ListarUsuarioService;

public class UsuarioServiceTests {
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @InjectMocks
    private CriarUsuarioService criarUsuarioService;

    @InjectMocks
    private AlterarSenhaService alterarSenhaService;

    @InjectMocks
    private AtualizarUsuarioService atualizarUsuarioService;

    @InjectMocks
    private DesativarUsuarioService desativarUsuarioService;

    @InjectMocks
    private ListarUsuarioService listarUsuarioService;

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

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        Usuario response = criarUsuarioService.criarUsuario(dto);

        assertEquals(usuarioSalvo.getNome(), response.getNome());
        assertEquals(usuarioSalvo.getEmail(), response.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }


    @Test
    void testeCriarUsuarioComErroInterno() {
        UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
        dto.setNome("João");
        dto.setEmail("joao@example.com");
        dto.setPassword("senha123");
        dto.setDataNasc("1990-01-01");
        dto.setGenero(Genero.MASCULINO);
        dto.setTelefone("1234567890");

        when(usuarioRepository.save(any(Usuario.class))).thenThrow(new RuntimeException("Erro interno no servidor"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            criarUsuarioService.criarUsuario(dto);
        });

        assertEquals("Erro interno no servidor", exception.getMessage());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
    @Test
    void testCriarUsuarioComEmailDuplicado() {
        UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
        dto.setNome("João");
        dto.setEmail("joao@example.com");
        dto.setPassword("senha123");
        dto.setDataNasc("1990-01-01");
        dto.setGenero(Genero.MASCULINO);
        dto.setTelefone("1234567890");
    
        when(usuarioRepository.save(any(Usuario.class)))
                .thenThrow(new DataIntegrityViolationException("Email já cadastrado"));
    
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            criarUsuarioService.criarUsuario(dto);
        });

        assertEquals("Email já cadastrado", exception.getMessage());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

}

@Nested
@DisplayName("Conjunto de casos de lê usuário")
class  leituraUsuario {
    @Test
    void testListarUsuarioComIdValido() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("João");
        usuario.setEmail("joao@example.com");
        usuario.setDataNasc("1990-01-01");
        usuario.setGenero(Genero.MASCULINO); // Campo obrigatório
        usuario.setTelefone("1234567890");
        usuario.setPassword("senha123");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = listarUsuarioService.listarUsuario(id);

        assertNotNull(response);
        assertEquals(usuario.getNome(), response.getNome());
        assertEquals(usuario.getEmail(), response.getEmail());
        verify(usuarioRepository, times(1)).findById(id);
    }
    @Test
    void testListarUsuarioComIdInvalido() {
        UUID idInvalido = UUID.randomUUID();

        when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

        UsuarioResponseDTO response = listarUsuarioService.listarUsuario(idInvalido);

        assertNull(response);
        verify(usuarioRepository, times(1)).findById(idInvalido);
    }
    @Test
    void testListarUsuarioComErroInterno() {
        UUID id = UUID.randomUUID();

        when(usuarioRepository.findById(id)).thenThrow(new IllegalStateException("Erro interno no servidor"));

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
        dto.setTelefone("1234567890");
        dto.setEmail("joao@example.com");
        dto.setPassword("senha123");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("João");
        usuarioExistente.setDataNasc("1990-01-01");
        usuarioExistente.setGenero(Genero.MASCULINO);
        usuarioExistente.setTelefone("1234567890");
        usuarioExistente.setEmail("joao@example.com");
        usuarioExistente.setPassword("senha123");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario response = atualizarUsuarioService.atualizarUsuario(id, dto);

        assertEquals(dto.getNome(), response.getNome());
        assertEquals(dto.getEmail(), response.getEmail());
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

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("João");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            atualizarUsuarioService.atualizarUsuario(id, dto);
        });

        assertEquals("Dados inválidos", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
    @Test
    void testAtualizarUsuarioSemAlteracoes() {
        UUID id = UUID.randomUUID();
        UsuarioPostPutRequestDTO dto = new UsuarioPostPutRequestDTO();
        dto.setNome("João"); // Nome igual ao existente

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome(dto.getNome());

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario response = atualizarUsuarioService.atualizarUsuario(id, dto);

        assertEquals(usuarioExistente.getNome(), response.getNome());
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

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setPassword("senhaAntiga"); // Senha atual correta

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
        dto.setSenhaAtual("senhaErrada"); // Senha atual incorreta
        dto.setNovaSenha("novaSenha");

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setPassword("senhaAntiga"); // Senha atual correta no banco de dados

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> {
            alterarSenhaService.alterarSenha(id, dto);
        });

        assertEquals("Password Inválido:", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

@Nested
@DisplayName("Conjunto de casos de deletar o usuario")
class DeletarUsuario {
    @Test
    void testeDesativarUsuarioComSucesso() {
        UUID id = UUID.randomUUID();
        UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(id);

        ResponseEntity<?> response = desativarUsuarioService.desativarUsuario(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário desativado com sucesso!", response.getBody());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).deleteById(id);
    }

    @Test
    void testeDesativarUsuarioComIdInexistente() {
        UUID idInvalido = UUID.randomUUID();
        UsuarioDeleteRequestDTO dto = new UsuarioDeleteRequestDTO();

        when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

        ResponseEntity<?> response = desativarUsuarioService.desativarUsuario(idInvalido, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
        verify(usuarioRepository, times(1)).findById(idInvalido);
        verify(usuarioRepository, times(0)).deleteById(idInvalido);
    }
   


}
}


}


