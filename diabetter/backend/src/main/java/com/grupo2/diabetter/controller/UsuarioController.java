package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioChangePasswordDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import com.grupo2.diabetter.exception.InvalidPasswordException;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.service.usuario.CriarUsuarioService;
import com.grupo2.diabetter.service.usuario.interfaces.IAlterarSenhaService;
import com.grupo2.diabetter.service.usuario.interfaces.IAtualizarUsuarioService;
import com.grupo2.diabetter.service.usuario.interfaces.ICriarUsuarioService;
import com.grupo2.diabetter.service.usuario.interfaces.IDesativarUsuarioService;
import com.grupo2.diabetter.service.usuario.interfaces.IListarUsuarioService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private ICriarUsuarioService criarUsuarioService;
    @Autowired
    private IAtualizarUsuarioService atualizarUsuarioService;
    @Autowired
    private IAlterarSenhaService alterarSenhaService;
    @Autowired
    private IDesativarUsuarioService desativarUsuarioService;
    @Autowired
    private IListarUsuarioService listarUsuarioService;

   

    
    @PostMapping
    public Usuario criarUsuario(
            @Valid @RequestBody UsuarioPostPutRequestDTO dto
    ) {
        return this.criarUsuarioService.criarUsuario(dto);
    }

    @GetMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> listarUsuario(
            @PathVariable("id") UUID id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.listarUsuarioService.listarUsuario(id));
    }
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.listarUsuarioService.listarUsuarios());
    }
    
    @PutMapping("{id}")
    public Usuario atualizarUsuario(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UsuarioPostPutRequestDTO dto
    ) {
        return this.atualizarUsuarioService.atualizarUsuario(id, dto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> desativarUsuario(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UsuarioDeleteRequestDTO dto
    ) {
        return this.desativarUsuarioService.desativarUsuario(id, dto);
    }

    @PutMapping("{id}/alterar-senha")
    public ResponseEntity<?> alterarSenha(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UsuarioChangePasswordDTO dto
    ) {
        try {
            this.alterarSenhaService.alterarSenha(id, dto);
            return ResponseEntity.ok().body("Senha alterada com sucesso");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
    