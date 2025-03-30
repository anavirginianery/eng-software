package com.grupo2.diabetter.service.usuario;

import com.grupo2.diabetter.dto.usuario.CompletarPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.interfaces.ICriarUsuarioService;

import java.util.UUID;

@Service
public class CriarUsuarioService implements ICriarUsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public Usuario criarUsuario(UsuarioPostPutRequestDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .dataNasc(dto.getDataNasc())
                .genero(dto.getGenero())
                .email(dto.getEmail())
                .senha(dto.getPassword())
                .build();

        return this.usuarioRepository.save(usuario);
    }

    @Override
    public Usuario completarPerfil(UUID usuarioId, CompletarPerfil dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setAltura(dto.getAltura());
        usuario.setPeso(dto.getPeso());
        usuario.setTipoDiabetes(dto.getTipoDiabetes());
        usuario.setTipoInsulina(dto.getTipoInsulina());
        usuario.setComorbidades(dto.getComorbidades());
        usuario.setHorarios_afericao(dto.getHorarios_afericao());

        return usuarioRepository.save(usuario);
    }
}
