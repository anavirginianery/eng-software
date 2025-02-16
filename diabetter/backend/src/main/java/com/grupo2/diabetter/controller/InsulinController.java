package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.insulin.InsulinDeleteResponseDTO;
import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import com.grupo2.diabetter.service.insulin.interfaces.ICriarInsulinService;
import com.grupo2.diabetter.service.insulin.interfaces.IRecuperarInsulinService;
import com.grupo2.diabetter.service.insulin.interfaces.IListarInsulinService;
import com.grupo2.diabetter.service.insulin.interfaces.IAtualizarInsulinService;
import com.grupo2.diabetter.service.insulin.interfaces.IDeletarInsulinService;
import com.grupo2.diabetter.service.insulin.interfaces.IListarInsulinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/insulin")
public class InsulinController {

    @Autowired
    private ICriarInsulinService criarInsulinService;

    @Autowired
    private IRecuperarInsulinService recuperarInsulinService;

    @Autowired
    private IListarInsulinService listarInsulinService;

    @Autowired
    private IAtualizarInsulinService atualizarInsulinService;

    @Autowired
    private IDeletarInsulinService deletarInsulinService;

    @PostMapping
    public ResponseEntity<InsulinResponseDTO> createInsulin(@RequestBody InsulinPostPutRequestDTO requestDTO) {
        InsulinResponseDTO response = criarInsulinService.criarInsulin(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsulinResponseDTO> readInsulin(@PathVariable UUID id) {
        InsulinResponseDTO response = recuperarInsulinService.recuperarInsulin(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InsulinResponseDTO>> listAllInsulin() {
        List<InsulinResponseDTO> response = listarInsulinService.listarTodasInsulinas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/horario/{horarioId}")
    public ResponseEntity<List<InsulinResponseDTO>> listInsulinByHorario(@PathVariable UUID horarioId) {
        List<InsulinResponseDTO> response = listarInsulinService.listarInsulin(horarioId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsulinResponseDTO> updateInsulin(
            @PathVariable UUID id,
            @RequestBody InsulinPostPutRequestDTO requestDTO) {
        InsulinResponseDTO response = atualizarInsulinService.atualizarInsulin(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<InsulinDeleteResponseDTO> deleteInsulin(@PathVariable UUID id) {
        InsulinDeleteResponseDTO response = deletarInsulinService.deletarInsulin(id);
        return ResponseEntity.ok(response);
    }
}