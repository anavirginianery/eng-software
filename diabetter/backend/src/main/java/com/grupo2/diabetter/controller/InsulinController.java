package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.insulina.InsulinDeleteResponseDTO;
import com.grupo2.diabetter.dto.insulina.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import com.grupo2.diabetter.dto.insulina.IntervaloDataDTO;
import com.grupo2.diabetter.service.insulina.interfaces.ICriarInsulinaService;
import com.grupo2.diabetter.service.insulina.interfaces.IRecuperarInsulinaService;
import com.grupo2.diabetter.service.insulina.interfaces.IListarInsulinaService;
import com.grupo2.diabetter.service.insulina.interfaces.IAtualizarInsulinaService;
import com.grupo2.diabetter.service.insulina.interfaces.IDeletarInsulinaService;
import com.grupo2.diabetter.service.insulina.interfaces.IListarInsulinaPorIntervalo;
import jakarta.validation.Valid;
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


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/insulin")
public class InsulinController {

    @Autowired
    private ICriarInsulinaService criarInsulinService;

    @Autowired
    private IRecuperarInsulinaService recuperarInsulinService;

    @Autowired
    private IListarInsulinaService listarInsulinService;

    @Autowired
    private IAtualizarInsulinaService atualizarInsulinService;

    @Autowired
    private IDeletarInsulinaService deletarInsulinService;

    @Autowired
    private IListarInsulinaPorIntervalo listarInsulinasPorIntervalo;

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

    @PostMapping("/insulinas-por-intervalo")
    public Map<LocalDate, Double> listarInsulinas(@RequestBody @Valid IntervaloDataDTO dto) {
        return listarInsulinasPorIntervalo.listarInsulinasPorIntervalo(dto);
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
        InsulinDeleteResponseDTO response = deletarInsulinService.deletarInsulina(id);
        return ResponseEntity.ok(response);
    }
}