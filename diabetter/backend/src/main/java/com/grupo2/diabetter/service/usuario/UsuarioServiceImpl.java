package com.grupo2.diabetter.service.usuario;

import com.grupo2.diabetter.service.usuario.interfaces.UsuarioServiceInterface;
import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioServiceInterface {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ResponseEntity<?> criarUsuario(UsuarioPostPutRequestDTO dto) {
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .dataNasc(dto.getDataNasc())
                .genero(dto.getGenero())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .password(dto.getPassword()) 
                .build();

        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário criado com sucesso!");
    }

    @Override
    public UsuarioResponseDTO listarUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(u -> new UsuarioResponseDTO(
                u.getId(), u.getNome(), u.getDataNasc(), u.getTelefone(), u.getEmail(), u.getGenero().name()))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Override
    public ResponseEntity<?> atualizarUsuario(Long id, UsuarioPostPutRequestDTO dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setNome(dto.getNome());
        usuario.setDataNasc(dto.getDataNasc());
        usuario.setGenero(dto.getGenero());
        usuario.setTelefone(dto.getTelefone());
        usuario.setEmail(dto.getEmail());

        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário atualizado com sucesso!");
    }

    @Override
    public ResponseEntity<?> desativarUsuario(Long id, UsuarioDeleteRequestDTO dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuário desativado com sucesso!");
    }
}
