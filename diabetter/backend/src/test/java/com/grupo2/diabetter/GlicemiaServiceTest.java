package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.AtualizarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.CriarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.DeletarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.ListarGlicemiasService;
import com.grupo2.diabetter.service.glicemia.RecuperarGlicemiaService;

public class GlicemiaServiceTest {
    @Mock
    private GlicemiaRepository glicemiaRepository;

    @InjectMocks
    private CriarGlicemiaService criarGlicemiaService;

    @InjectMocks
    private ListarGlicemiasService listarGlicemiasService;

    @InjectMocks
    private AtualizarGlicemiaService atualizarGlicemiaService;

    @InjectMocks
    private DeletarGlicemiaService  deletarGlicemiaService;

    @InjectMocks
    private RecuperarGlicemiaService recuperarGlicemiaService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

@Nested
@DisplayName("Conjunto de casos de criar Glicemia ")
class UsuarioCriacao {
        @Test
        @DisplayName("Cria uma glicemia com dados válidos")
        void testCreateBloodSugarWithValidData() {
            
            GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
            dto.setMeasurement(120.0f); 

            Glicemia glicemiaSalva = Glicemia.builder()
                    .id(UUID.randomUUID())
                    .measurement(dto.getMeasurement())
                    .build();

            
            when(glicemiaRepository.save(any(Glicemia.class))).thenReturn(glicemiaSalva);

            Glicemia resultado = criarGlicemiaService.executar(dto);

            
            assertNotNull(resultado); 
            assertNotNull(resultado.getId()); 
            assertEquals(dto.getMeasurement(), resultado.getMeasurement(), 0.001); 
        }

    @Test
    @DisplayName("Tenta criar uma glicemia com dados inválidos")
    void testCreateBloodSugarWithInvalidData() {
    
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(-10.0f); 
    
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            criarGlicemiaService.executar(dto);
        });
    
        assertEquals("Medição de glicemia inválida", exception.getMessage()); 
    }

}

@Nested
@DisplayName("Conjunto de casos de lê glicemia")
class  leituraGlicemia{
    @Test
    @DisplayName("Lista glicemias com dados válidos (banco não vazio)")
    void testReadBloodSugarWithValidData() {
        
        Glicemia glicemia1 = Glicemia.builder()
                .id(UUID.randomUUID())
                .measurement(120.0f)
                .build();

        Glicemia glicemia2 = Glicemia.builder()
                .id(UUID.randomUUID())
                .measurement(130.0f)
                .build();

        List<Glicemia> glicemias = List.of(glicemia1, glicemia2);

        when(glicemiaRepository.findAll()).thenReturn(glicemias);

        List<Glicemia> resultado = listarGlicemiasService.executar();

 
        assertNotNull(resultado); 
        assertEquals(2, resultado.size());
        assertEquals(glicemia1.getMeasurement(), resultado.get(0).getMeasurement(), 0.001); 
        assertEquals(glicemia2.getMeasurement(), resultado.get(1).getMeasurement(), 0.001); 
    }

    @Test
    @DisplayName("Lista glicemias com banco vazio")
    void testReadBloodSugarWithEmptyDatabase() {

        when(glicemiaRepository.findAll()).thenReturn(List.of()); 

        List<Glicemia> resultado = listarGlicemiasService.executar();

        assertNotNull(resultado); 
        assertTrue(resultado.isEmpty()); 
    }
}

@Nested
@DisplayName("Conjunto de casos de atualizar  Glicemia")
class GlicemiaUpdate {
    @Test
    @DisplayName("Atualiza glicemia com dados válidos")
    void testUpdateWithValidData() {
  
        UUID id = UUID.randomUUID();
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(150.0f); 

        Glicemia glicemiaExistente = Glicemia.builder()
                .id(id)
                .measurement(120.0f) 
                .build();

        Glicemia glicemiaAtualizada = Glicemia.builder()
                .id(id)
                .measurement(dto.getMeasurement()) 
                .build();


        when(glicemiaRepository.findById(id)).thenReturn(Optional.of(glicemiaExistente));
        when(glicemiaRepository.save(any(Glicemia.class))).thenReturn(glicemiaAtualizada);


        Glicemia resultado = atualizarGlicemiaService.executar(id, dto);

  
        assertNotNull(resultado); 
        assertEquals(id, resultado.getId()); 
        assertEquals(dto.getMeasurement(), resultado.getMeasurement(), 0.001); 
    }

    @Test
    @DisplayName("Tenta atualizar glicemia com ID inválido")
    void testUpdateWithInvalidId() {
      
        UUID id = UUID.randomUUID();
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(150.0f);

        when(glicemiaRepository.findById(id)).thenReturn(Optional.empty());


        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            atualizarGlicemiaService.executar(id, dto);
        });

        assertEquals("Glicemia não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Atualiza glicemia sem alterações")
    void testUpdateBloodSugarWithoutChange() {

        UUID id = UUID.randomUUID();
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(120.0f); 

        Glicemia glicemiaExistente = Glicemia.builder()
                .id(id)
                .measurement(120.0f) 
                .build();

        when(glicemiaRepository.findById(id)).thenReturn(Optional.of(glicemiaExistente));
        when(glicemiaRepository.save(any(Glicemia.class))).thenReturn(glicemiaExistente);

        Glicemia resultado = atualizarGlicemiaService.executar(id, dto);

        assertNotNull(resultado); 
        assertEquals(id, resultado.getId()); 
        assertEquals(dto.getMeasurement(), resultado.getMeasurement(), 0.001); 
    }


}

@Nested
@DisplayName("Conjunto de casos de deletar glicemia")
class DeletarGlicemia {

    @Test
    @DisplayName("Desativa glicemia com sucesso")
    void testDisableBloodSugarSuccessfuly() {

        UUID id = UUID.randomUUID();
        Glicemia glicemia = Glicemia.builder()
                .id(id)
                .measurement(120.0f)
                .build();
    
        when(glicemiaRepository.findById(id)).thenReturn(Optional.of(glicemia));
    
        doNothing().when(glicemiaRepository).deleteById(id);
    
        deletarGlicemiaService.executar(id);
    
      
        verify(glicemiaRepository, times(1)).findById(id); 
        verify(glicemiaRepository, times(1)).deleteById(id);    
    }
    @Test
    @DisplayName("Tenta desativar glicemia com ID inválido")
    void testDisableBloodSugarWithInvalidId() {
       
        UUID id = UUID.randomUUID();

        
        when(glicemiaRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            deletarGlicemiaService.executar(id);
        });

        assertEquals("Glicemia não encontrada", exception.getMessage()); 
    }

    @Test
    @DisplayName("Tenta desativar glicemia já desativada")
    void testDisableBloodSugarJaDesativado() {
        
        UUID id = UUID.randomUUID();
        Glicemia glicemia = Glicemia.builder()
                .id(id)
                .measurement(120.0f)
                .build();

        when(glicemiaRepository.findById(id)).thenReturn(Optional.of(glicemia));

        deletarGlicemiaService.executar(id);

        when(glicemiaRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            deletarGlicemiaService.executar(id);
        });

        assertEquals("Glicemia não encontrada", exception.getMessage()); 
    }
}


}
