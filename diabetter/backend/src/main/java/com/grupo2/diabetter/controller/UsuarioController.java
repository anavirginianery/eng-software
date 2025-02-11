package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import com.grupo2.diabetter.service.usuario.interfaces.UsuarioServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioServiceInterface usuarioService;

    @PostMapping
    ResponseEntity<?> criarUsuario(
            @Valid @RequestBody UsuarioPostPutRequestDTO dto
    ) {
        return this.usuarioService.criarUsuario(dto);
    }

    @GetMapping("{id}")
    ResponseEntity<UsuarioResponseDTO> listarUsuario(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.usuarioService.listarUsuario(id));
    }

    @PutMapping("{id}")
    ResponseEntity<?> atualizarUsuario(
            @PathVariable("id") Long id,
            @Valid @RequestBody UsuarioPostPutRequestDTO dto
    ) {
        return this.usuarioService.atualizarUsuario(id, dto);
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> desativarUsuario(
            @PathVariable("id") Long id,
            @Valid @RequestBody UsuarioDeleteRequestDTO dto
    ) {
        return this.usuarioService.desativarUsuario(id, dto);
    }
}
    