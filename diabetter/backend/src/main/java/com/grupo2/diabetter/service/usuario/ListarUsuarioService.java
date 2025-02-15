package com.grupo2.diabetter.service.usuario;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.interfaces.IListarUsuarioService;

@Service
public class ListarUsuarioService implements IListarUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UsuarioResponseDTO listarUsuario(UUID id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponseDTO::new)
                .orElse(null);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }
}