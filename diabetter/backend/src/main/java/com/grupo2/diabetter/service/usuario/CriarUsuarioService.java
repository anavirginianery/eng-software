package com.grupo2.diabetter.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.interfaces.ICriarUsuarioService;

@Service
public class CriarUsuarioService implements ICriarUsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public Usuario criarUsuario(UsuarioPostPutRequestDTO dto) {
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .dataNasc(dto.getDataNasc())
                .genero(dto.getGenero())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .password(dto.getPassword()) 
                .build();

        return this.usuarioRepository.save(usuario);
        
    }

}
