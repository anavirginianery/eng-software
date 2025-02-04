package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.dto.insulin.InsulinDeleteRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.model.Insulin;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.InsulinServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsulinService implements InsulinServiceInterface {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public ResponseEntity<String> createInsulin(InsulinPostPutRequestDTO requestDTO) {
        Insulin insulin = new Insulin();
        insulin.setType(requestDTO.getType());
        insulin.setUnits(requestDTO.getUnits());
        insulin.setHorarioUuid(requestDTO.getHorarioUuid());
        insulinRepository.save(insulin);
        return new ResponseEntity<>("Insulin created successfully", HttpStatus.CREATED);
    }

    @Override
    public Insulin readInsulin(String id) {
        return insulinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insulin not found"));
    }

    @Override
    public List<Insulin> listAllInsulin() {
        return insulinRepository.findAll();
    }

    @Override
    public ResponseEntity<String> updateInsulin(String id, InsulinPostPutRequestDTO requestDTO) {
        Optional<Insulin> optionalInsulin = insulinRepository.findById(id);
        if (optionalInsulin.isPresent()) {
            Insulin insulin = optionalInsulin.get();
            insulin.setType(requestDTO.getType());
            insulin.setUnits(requestDTO.getUnits());
            insulin.setHorarioUuid(requestDTO.getHorarioUuid());
            insulinRepository.save(insulin);
            return new ResponseEntity<>("Insulin updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Insulin not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> disableInsulin(String id, InsulinDeleteRequestDTO requestDTO) {
        if (insulinRepository.existsById(id)) {
            insulinRepository.deleteById(id);
            return new ResponseEntity<>("Insulin disabled successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Insulin not found", HttpStatus.NOT_FOUND);
        }
    }
}

