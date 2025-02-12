package com.grupo2.diabetter.service.insulin.interfaces;

import com.grupo2.diabetter.dto.insulin.InsulinDeleteRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.model.Insulin;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;

public interface InsulinServiceInterface {
    ResponseEntity<String> createInsulin(InsulinPostPutRequestDTO requestDTO);
    Insulin readInsulin(UUID id);
    List<Insulin> listAllInsulin();
    ResponseEntity<String> updateInsulin(UUID id, InsulinPostPutRequestDTO requestDTO);
    ResponseEntity<String> disableInsulin(UUID id, InsulinDeleteRequestDTO requestDTO);
}