package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.service.horario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/horario")
public class HorarioController {


    @Autowired
    private CriarHorarioService criarHorarioService;
    @Autowired
    private AtualizarHorarioService atualizarHorarioService;
    @Autowired
    private DeletarHorarioService deletarHorarioService;
    @Autowired
    private ListarHorarioService listarHorarioService;
    @Autowired
    private RecuperarHorarioService recuperarHorarioService;

    @PostMapping
    public ResponseEntity<?> createHorario(@RequestBody HorarioPostPutRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(criarHorarioService.createHorario(dto));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> readHorario(@PathVariable UUID uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(recuperarHorarioService.recuperarHorario(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateHorario(@PathVariable UUID uuid, @RequestBody HorarioPostPutRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(atualizarHorarioService.updateHorario(uuid, dto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> disableHorario(@PathVariable UUID uuid) {
        this.deletarHorarioService.deletarHorario(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listarHorarios(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(listarHorarioService.listarHorario(userId));
    }

}
