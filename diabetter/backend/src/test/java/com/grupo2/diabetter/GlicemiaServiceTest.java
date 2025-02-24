package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
    @Test
    @DisplayName("Tenta criar uma glicemia e ocorre um erro interno")
    void testCreateBloodSugarWithInternalError() {
 
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(120.0f);
    
        when(glicemiaRepository.save(any(Glicemia.class)))
            .thenThrow(new RuntimeException("Erro interno ao criar glicemia"));
    
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            criarGlicemiaService.executar(dto);
        });
    
        assertEquals("Erro interno ao criar glicemia", exception.getMessage());
        verify(glicemiaRepository, times(1)).save(any(Glicemia.class));
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

    @Test
    @DisplayName("Tenta recuperar uma glicemia e ocorre um erro interno")
    void testReadBloodSugarWithInternalError() {

        UUID validId = UUID.randomUUID();
    
        when(glicemiaRepository.findById(validId))
            .thenThrow(new RuntimeException("Erro interno ao recuperar glicemia"));
    
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recuperarGlicemiaService.executar(validId);
        });
    
        assertEquals("Erro interno ao recuperar glicemia", exception.getMessage());
        verify(glicemiaRepository, times(1)).findById(validId);
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

    @Test
    @DisplayName("Tenta atualizar uma glicemia e ocorre um erro interno")
    void testUpdateBloodSugarWithInternalError() {
 
        UUID validId = UUID.randomUUID();
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(130.0f);
    
        when(glicemiaRepository.findById(validId)).thenReturn(Optional.of(new Glicemia()));
        when(glicemiaRepository.save(any(Glicemia.class)))
            .thenThrow(new RuntimeException("Erro interno ao atualizar glicemia"));
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            atualizarGlicemiaService.executar(validId, dto);
        });
    
        assertEquals("Erro interno ao atualizar glicemia", exception.getMessage());
        verify(glicemiaRepository, times(1)).save(any(Glicemia.class));
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

    @Test
    @DisplayName("Tenta desativar uma glicemia e ocorre um erro interno")
    void testDisableBloodSugarWithInternalError() {

        UUID validId = UUID.randomUUID();
    
        when(glicemiaRepository.findById(validId)).thenReturn(Optional.of(new Glicemia()));
        doThrow(new RuntimeException("Erro interno ao desativar glicemia"))
            .when(glicemiaRepository).deleteById(validId);
    
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deletarGlicemiaService.executar(validId);
        });
    
        assertEquals("Erro interno ao desativar glicemia", exception.getMessage());
        verify(glicemiaRepository, times(1)).deleteById(validId);
    }
}


}
