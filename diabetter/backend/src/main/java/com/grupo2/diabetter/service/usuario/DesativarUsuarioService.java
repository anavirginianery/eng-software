package com.grupo2.diabetter.service.usuario;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.interfaces.IDesativarUsuarioService;

@Service
public class DesativarUsuarioService implements IDesativarUsuarioService{
    
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public ResponseEntity<?> desativarUsuario(UUID id, UsuarioDeleteRequestDTO dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuário desativado com sucesso!");
    }
    
}
