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
import java.util.UUID;

@RestController
@RequestMapping("/glicemia")
public class GlicemiaController {

    @Autowired
    private ICriarGlicemiaService criarGlicemiaService;
    @Autowired
    private IListarGlicemiasService listarGlicemiaService;
    @Autowired
    private IAtualizarGlicemiaService atualizarGlicemiaService;
    @Autowired
    private IRecuperarGlicemiaService recuperarGlicemiaService;
    @Autowired
    private IDeletarGlicemiaService deletarGlicemiaService;
    @Autowired


    @GetMapping
    public ResponseEntity<List<Glicemia>> listarGlicemias() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.listarGlicemiaService.executar());
    }

    @PutMapping("{id}")
    public ResponseEntity<Glicemia> atualizarGlicemia(
            @PathVariable("id") UUID id,
            @Valid @RequestBody GlicemiaPostPutRequestDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.atualizarGlicemiaService.executar(id, dto));
    }

    @GetMapping("{id}")
    public ResponseEntity<Glicemia> recuperarGlicemia(
            @PathVariable("id") UUID id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.recuperarGlicemiaService.executar(id));
    }

    @PostMapping
    public ResponseEntity<Glicemia> criarGlicemia(
            @Valid @RequestBody GlicemiaPostPutRequestDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.criarGlicemiaService.executar(dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> removerGlicemia(
            @PathVariable("id") UUID id
            ) {
        this.deletarGlicemiaService.executar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
