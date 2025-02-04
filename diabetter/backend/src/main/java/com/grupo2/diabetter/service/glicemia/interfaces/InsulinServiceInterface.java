package com.grupo2.diabetter.service.glicemia.interfaces;

import com.grupo2.diabetter.dto.insulin.InsulinDeleteRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.model.Insulin;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface InsulinServiceInterface {
    ResponseEntity<String> createInsulin(InsulinPostPutRequestDTO requestDTO);
    Insulin readInsulin(String id);
    List<Insulin> listAllInsulin();
    ResponseEntity<String> updateInsulin(String id, InsulinPostPutRequestDTO requestDTO);
    ResponseEntity<String> disableInsulin(String id, InsulinDeleteRequestDTO requestDTO);
}

