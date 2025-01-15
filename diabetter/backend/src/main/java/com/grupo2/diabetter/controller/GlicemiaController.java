package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.service.glicemia.interfaces.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/glicemia")
public class GlicemiaController {

    @Autowired
    private ICriarGlicemiaService criarClienteService;
    @Autowired
    private IListarGlicemiasService listarClientesService;
    @Autowired
    private IAtualizarGlicemiaService atualizarClienteService;
    @Autowired
    private IRecuperarGlicemiaService recuperarClienteService;
    @Autowired
    private IDeletarGlicemiaService deletarClienteService;
    @Autowired


    @GetMapping
    ResponseEntity<List<Glicemia>> listarClientes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.listarClientesService.executar());
    }

    @PutMapping("{id}")
    ResponseEntity<Glicemia> atualizarCliente(
            @PathVariable("id") Long id,
            @Valid @RequestBody GlicemiaPostPutRequestDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.atualizarClienteService.executar(id, dto));
    }

    @GetMapping("{id}")
    ResponseEntity<Glicemia> recuperarCliente(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.recuperarClienteService.executar(id));
    }

    @PostMapping
    ResponseEntity<Glicemia> criarCliente(
            @Valid @RequestBody GlicemiaPostPutRequestDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.criarClienteService.executar(dto));
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> removerCliente(
            @PathVariable("id") Long id
            ) {
        this.deletarClienteService.executar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
