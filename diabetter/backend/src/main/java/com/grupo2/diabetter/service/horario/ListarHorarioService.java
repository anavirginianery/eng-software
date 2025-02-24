package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IListarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarHorarioService implements IListarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

@Override
public List<HorarioResponseDTO> listarHorario(Long userId){
    List<Horario> horarios = horarioRepository.findAllByUserId(userId);

    return horarios.stream()
        .map(horario -> new HorarioResponseDTO(
            horario.getUuid(),  
            horario.getValue(), 
            horario.getDate(),  
            horario.getUserId() 
        ))
    
        .collect(Collectors.toList());
}
}
