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
    public ResponseEntity<InsulinResponseDTO> criarInsulina(@RequestBody InsulinPostPutRequestDTO requestDTO) {
        InsulinResponseDTO response = criarInsulinService.criarInsulina(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsulinResponseDTO> recuperarInsulina(@PathVariable UUID id) {
        InsulinResponseDTO response = recuperarInsulinService.recuperarInsulina(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InsulinResponseDTO>> listarTodasInsulinas() {
        List<InsulinResponseDTO> response = listarInsulinService.listarTodasInsulinas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/horario/{horarioId}")
    public ResponseEntity<List<InsulinResponseDTO>> listarInsulinaPorHorario(@PathVariable UUID horarioId) {
        List<InsulinResponseDTO> response = listarInsulinService.listarInsulinaPorHorario(horarioId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsulinResponseDTO> atualizarInsulina(
            @PathVariable UUID id,
            @RequestBody InsulinPostPutRequestDTO requestDTO) {
        InsulinResponseDTO response = atualizarInsulinService.atualizarInsulina(id, requestDTO);
        return ResponseEntity.ok(response);
           
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<InsulinDeleteResponseDTO> deletarInsulin(@PathVariable UUID id) {
        // Isso deve ser verificação dentro do método do service
        
        // if (id == null) {
        //     throw new IllegalArgumentException("ID não pode ser nulo");
        // }
        InsulinDeleteResponseDTO response = deletarInsulinService.deletarInsulin(id);
        return ResponseEntity.ok(response);
    }
}