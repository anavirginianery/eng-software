package com.grupo2.diabetter.service.usuario;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.interfaces.IAtualizarUsuarioService;

@Service
public class AtualizarUsuarioService implements IAtualizarUsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public Usuario atualizarUsuario(UUID id, UsuarioPostPutRequestDTO dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        usuario.setNome(dto.getNome());
        usuario.setDataNasc(dto.getDataNasc());
        usuario.setGenero(dto.getGenero());
        usuario.setEmail(dto.getEmail());
        usuario.setAltura(dto.getAltura());
        usuario.setPeso(dto.getPeso());
        usuario.setTipoDiabetes(dto.getTipoDiabetes());
        usuario.setTipoInsulina(dto.getTipoInsulina());
        usuario.setComorbidades(dto.getComorbidades());
        usuario.setHorarios_afericao(dto.getHorarios_afericao());

        return this.usuarioRepository.save(usuario);
    }
}
