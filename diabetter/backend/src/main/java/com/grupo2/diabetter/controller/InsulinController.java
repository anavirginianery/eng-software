package com.grupo2.diabetter.controller;

import com.grupo2.diabetter.dto.insulin.InsulinDeleteRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.model.Insulin;
import com.grupo2.diabetter.service.glicemia.interfaces.InsulinServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insulin")
public class InsulinController {

    @Autowired
    private InsulinServiceInterface insulinService;

    @PostMapping
    public ResponseEntity<String> createInsulin(@RequestBody InsulinPostPutRequestDTO requestDTO) {
        return insulinService.createInsulin(requestDTO);
    }

    @GetMapping("/{id}")
    public Insulin readInsulin(@PathVariable String id) {
        return insulinService.readInsulin(id);
    }

    @GetMapping
    public List<Insulin> listAllInsulin() {
        return insulinService.listAllInsulin();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateInsulin(
            @PathVariable String id,
            @RequestBody InsulinPostPutRequestDTO requestDTO) {
        return insulinService.updateInsulin(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> disableInsulin(
            @PathVariable String id,
            @RequestBody InsulinDeleteRequestDTO requestDTO) {
        return insulinService.disableInsulin(id, requestDTO);
    }
}

