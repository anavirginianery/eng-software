package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.service.glicemia.interfaces.ICriarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.interfaces.IAtualizarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.interfaces.IDeletarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.interfaces.IListarGlicemiasService;
import com.grupo2.diabetter.service.glicemia.interfaces.IRecuperarGlicemiaService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping
    public ResponseEntity<List<GlicemiaResponseDTO>> listarGlicemias() {
        List<GlicemiaResponseDTO> glicemias = listarGlicemiaService.executar();
        return ResponseEntity.status(HttpStatus.OK).body(glicemias);
    }

    @GetMapping("/by-horario/{horarioId}")
    public ResponseEntity<List<GlicemiaResponseDTO>> listarGlicemiaByHorario(@PathVariable UUID horarioId) {
        List<GlicemiaResponseDTO> glicemias = listarGlicemiaService.listarGlicemiaByHorario(horarioId);
        return ResponseEntity.status(HttpStatus.OK).body(glicemias);
    }


    // Não deveria retornar um DTO?
    @PostMapping
    public ResponseEntity<GlicemiaResponseDTO> criarGlicemia(
            @Valid @RequestBody GlicemiaPostPutRequestDto dto
    ) {
        GlicemiaResponseDTO createdGlicemia = criarGlicemiaService.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGlicemia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlicemiaResponseDTO> atualizarGlicemia(
            @PathVariable UUID id,
            @Valid @RequestBody GlicemiaPostPutRequestDto dto
    ) {
        GlicemiaResponseDTO updatedGlicemia = atualizarGlicemiaService.executar(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedGlicemia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlicemiaResponseDTO> recuperarGlicemia(
            @PathVariable UUID id
    ) {
        GlicemiaResponseDTO glicemia = recuperarGlicemiaService.executar(id);
        return ResponseEntity.status(HttpStatus.OK).body(glicemia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerGlicemia(
            @PathVariable UUID id
    ) {
        deletarGlicemiaService.executar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}