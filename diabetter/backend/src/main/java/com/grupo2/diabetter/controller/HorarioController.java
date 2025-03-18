package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.service.horario.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/horario")
public class HorarioController {


    @Autowired
    private ICriarHorarioService criarHorarioService;
    @Autowired
    private IAtualizarHorarioService atualizarHorarioService;
    @Autowired
    private IDeletarHorarioService deletarHorarioService;
    @Autowired
    private IListarHorarioService listarHorarioService;
    @Autowired
    private IRecuperarHorarioService recuperarHorarioService;

    @PostMapping
    public ResponseEntity<?> createHorario(@RequestBody HorarioPostPutRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(criarHorarioService.createHorario(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readHorario(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(recuperarHorarioService.recuperarHorario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHorario(@PathVariable UUID id, @RequestBody HorarioPostPutRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(atualizarHorarioService.updateHorario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> disableHorario(@PathVariable UUID id) {
        this.deletarHorarioService.deletarHorario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> listarHorarios(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(listarHorarioService.listarHorario(id));
    }

}
