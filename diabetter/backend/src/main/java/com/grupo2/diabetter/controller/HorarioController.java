package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.glicemia.HorarioObject;
import com.grupo2.diabetter.dto.glicemia.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.service.horario.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/horario")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @PostMapping
    public ResponseEntity<?> createHorario(@RequestBody HorarioPostPutRequestDTO dto){
        return horarioService.createHorario(dto);
    }

    @GetMapping("/{id}")
    public HorarioObject readHorario(@PathVariable UUID uuid) {
        return horarioService.readHorario(uuid);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHorario(@PathVariable UUID uuid, @RequestBody HorarioPostPutRequestDTO dto){
        return horarioService.updateHorario(uuid, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> disableHorario(@PathVariable UUID uuid) {
        return horarioService.disableHorario(uuid);
    }

}
