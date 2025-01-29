package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.glicemia.HorarioObject;
import com.grupo2.diabetter.dto.glicemia.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class HorarioService implements HorarioServiceInterface{

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public ResponseEntity<?> createHorario(HorarioPostPutRequestDTO dto) {
        Horario horario = new Horario();
        horario.setUuid(UUID.randomUUID());
        horario.setValue(dto.getValue());
        horario.setDate(dto.getDate());
        horarioRepository.save(horario);
        return ResponseEntity.ok("Horario created ssuccessfully");
    }

    @Override
    public HorarioObject readHorario(UUID uuid) {
        Optional<Horario> horario = horarioRepository.findById(uuid);
        return horario.map(h -> new HorarioObject(h.getUuid(), h.getValue(), h.getDate()))
                .orElseThrow(() -> new RuntimeException("Horario not found"));
    }

    @Override
    public ResponseEntity<?> updateHorario(UUID uuid, HorarioPostPutRequestDTO dto) {
        Optional<Horario> optionalHorario = horarioRepository.findById(uuid);
        if (optionalHorario.isPresent()) {
            Horario horario = optionalHorario.get();
            horario.setValue(dto.getValue());
            horario.setDate(dto.getDate());
            horarioRepository.save(horario);
            return ResponseEntity.ok("Horario updated successfully");
        } else {
            return ResponseEntity.status(404).body("Horario not found");
        }
    }

    @Override
    public ResponseEntity<?> disableHorario(UUID uuid) {
        if (horarioRepository.existsById(uuid)) {
            horarioRepository.deleteById(uuid);
            return ResponseEntity.ok("Horario disabled successfully");
        } else {
            return ResponseEntity.status(404).body("Horario not found");
        }
    }

}
