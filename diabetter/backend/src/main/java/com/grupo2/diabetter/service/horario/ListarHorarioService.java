package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IListarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarHorarioService implements IListarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public List<Horario> listarHorario(Long userId){
        return horarioRepository.findAllByUserId(userId);
    }
}
