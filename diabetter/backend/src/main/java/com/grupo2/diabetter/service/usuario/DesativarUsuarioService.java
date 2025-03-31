package com.grupo2.diabetter.service.usuario;

import java.util.Optional;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.interfaces.IDesativarUsuarioService;

@Service
public class DesativarUsuarioService implements IDesativarUsuarioService{
    
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public ResponseEntity<?> desativarUsuario(UUID id, UsuarioDeleteRequestDTO dto) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().body("Usuário desativado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Erro ao desativar usuário: " + e.getMessage()); // Captura a exceção
        }
    }
    
}
