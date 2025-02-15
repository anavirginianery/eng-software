package com.grupo2.diabetter.service.usuario;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.interfaces.IAtualizarUsuarioService;

@Service
public class AtualizarUsuarioService implements IAtualizarUsuarioService{
    
    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Override
    public Usuario atualizarUsuario(UUID id, UsuarioPostPutRequestDTO dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        
        if (usuarioOptional.isEmpty()) {
            throw new NotFoundException()(message: "Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setNome(dto.getNome());
        usuario.setDataNasc(dto.getDataNasc());
        usuario.setGenero(dto.getGenero());
        usuario.setTelefone(dto.getTelefone());
        usuario.setEmail(dto.getEmail());

        return this.usuarioRepository.save(usuario);
        
    }

}
