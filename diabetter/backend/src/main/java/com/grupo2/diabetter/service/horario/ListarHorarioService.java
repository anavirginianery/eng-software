package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IListarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ListarHorarioService implements IListarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public ResponseEntity<?> listarHorario(Long userId){
        return ResponseEntity.ok(horarioRepository.findAllByUserId(userId));
    }
}
