package com.grupo2.diabetter.service.usuario.interfaces;

import java.util.UUID;

import com.grupo2.diabetter.dto.usuario.UsuarioChangePasswordDTO;
import com.grupo2.diabetter.exception.InvalidPasswordException;
import com.grupo2.diabetter.exception.NotFoundException;

public interface IAlterarSenhaService {
    void alterarSenha(UUID id, UsuarioChangePasswordDTO dto) throws NotFoundException, InvalidPasswordException;
};

