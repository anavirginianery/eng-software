package com.grupo2.diabetter.service.usuario;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo2.diabetter.dto.usuario.UsuarioChangePasswordDTO;
import com.grupo2.diabetter.exception.InvalidPasswordException;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.interfaces.IAlterarSenhaService;

@Service
public class AlterarSenhaService implements IAlterarSenhaService {
    
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public void alterarSenha(UUID id, UsuarioChangePasswordDTO dto) throws NotFoundException, InvalidPasswordException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        
        //espaço reservado para a lógica de construção de um password válido, ou criptografia/encoding de um novo password.
        if (!usuario.getPassword().equals(dto.getSenhaAtual())) {
            throw new InvalidPasswordException("Senha atual inválida");
        }
    
       
        usuario.setPassword(dto.getNovaSenha());
    
       
    
        usuarioRepository.save(usuario);
    }
    
}
