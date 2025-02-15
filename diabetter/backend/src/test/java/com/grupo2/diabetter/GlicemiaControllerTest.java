package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.parser.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.grupo2.diabetter.controller.GlicemiaController;
import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.exception.ErrorHandlingControllerAdvice;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.AtualizarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.CriarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.DeletarGlicemiaService;
import com.grupo2.diabetter.service.glicemia.ListarGlicemiasService;
import com.grupo2.diabetter.service.glicemia.RecuperarGlicemiaService;

import jakarta.validation.constraints.Min;

@SpringBootTest
class GlicemiaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GlicemiaRepository glicemiaRepository; 

	@Mock
    private CriarGlicemiaService criarGlicemiaService;

    @Mock
    private DeletarGlicemiaService deletarGlicemiaService;

    @Mock
    private ListarGlicemiasService listarGlicemiasService;

    @Mock
    private AtualizarGlicemiaService atualizarGlicemiaService;

    @Mock
    private RecuperarGlicemiaService recuperarGlicemiaService;

    @InjectMocks
    private GlicemiaController glicemiaController;

    @BeforeEach
    public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(glicemiaController)
            .setControllerAdvice(new ErrorHandlingControllerAdvice())
            .build();
}

@Nested
@DisplayName("Conjunto de casos de criar glicemia")
    class UsuarioCriacao {
      

    @Test
	void testCreateBloodSugarWithValidData() throws Exception{
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
    dto.setMeasurement(120.0f);

    Glicemia glicemia = new Glicemia();
    glicemia.setId(UUID.randomUUID());
    glicemia.setMeasurement(dto.getMeasurement());

    
    when(criarGlicemiaService.executar(any(GlicemiaPostPutRequestDto.class))).thenReturn(glicemia);

    ResponseEntity<Glicemia> response = glicemiaController.criarGlicemia(dto);

   
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(glicemia.getId(), response.getBody().getId());
    assertEquals(glicemia.getMeasurement(), response.getBody().getMeasurement(), 0.001);
}
    @Test
    @DisplayName("Tenta criar uma glicemia e ocorre um erro interno")
    void testCreateBloodSugarWithInternalError() {
  
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(120.0f);
    
        when(criarGlicemiaService.executar(any(GlicemiaPostPutRequestDto.class)))
            .thenThrow(new RuntimeException("Erro interno ao criar glicemia"));
    
      
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            glicemiaController.criarGlicemia(dto);
        });
    
        assertEquals("Erro interno ao criar glicemia", exception.getMessage());
        verify(criarGlicemiaService, times(1)).executar(any(GlicemiaPostPutRequestDto.class));
    }
     @Test
    @DisplayName("Tenta criar uma glicemia com dados inválidos")
    void testCreateBloodSugarWithInvalidData() throws Exception {
        
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(-10.0f); 
    
            glicemiaController.criarGlicemia(dto);
        }
}

@Nested
@DisplayName("Conjunto de casos de lê glicemia")
class  leituraGlicemia {	
    @Test
    @DisplayName("Recupera uma glicemia com ID válido")
    void testReadBloodSugarWithValidId() {
        
        UUID validId = UUID.randomUUID();
        Glicemia glicemia = new Glicemia();
        glicemia.setId(validId);
        glicemia.setMeasurement(120.0f);
    
        when(recuperarGlicemiaService.executar(validId)).thenReturn(glicemia);
    
        ResponseEntity<Glicemia> response = glicemiaController.recuperarGlicemia(validId);
    

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(validId, response.getBody().getId());
        assertEquals(120.0f, response.getBody().getMeasurement(), 0.001);
    }
    @Test
    @DisplayName("Tenta recuperar uma glicemia e ocorre um erro interno")
    void testReadBloodSugarWithInternalError() {
 
        UUID validId = UUID.randomUUID();
    
        when(recuperarGlicemiaService.executar(validId))
            .thenThrow(new RuntimeException("Erro interno ao recuperar glicemia"));
    
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            glicemiaController.recuperarGlicemia(validId);
        });
    
        assertEquals("Erro interno ao recuperar glicemia", exception.getMessage());
        verify(recuperarGlicemiaService, times(1)).executar(validId);
    }
    
    @Test
    @DisplayName("Tenta recuperar uma glicemia com ID inválido")
    void testReadBloodSugarWithInvalidId() {
       
        UUID invalidId = UUID.randomUUID();
    
        when(recuperarGlicemiaService.executar(invalidId)).thenThrow(new RuntimeException("Glicemia não encontrada"));
    
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            glicemiaController.recuperarGlicemia(invalidId);
        });
    
        assertEquals("Glicemia não encontrada", exception.getMessage());
    }
}

@Nested
@DisplayName("Conjunto de casos de atualizar glicemia")
class GlicemiaUpdate {
    @Test
    @DisplayName("Atualiza uma glicemia com dados válidos")
    void testUpdateWithValidData() {
        
        UUID validId = UUID.randomUUID();
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(130.0f);
    
        Glicemia glicemiaAtualizada = new Glicemia();
        glicemiaAtualizada.setId(validId);
        glicemiaAtualizada.setMeasurement(dto.getMeasurement());
    
        when(atualizarGlicemiaService.executar(validId, dto)).thenReturn(glicemiaAtualizada);
    
        
        ResponseEntity<Glicemia> response = glicemiaController.atualizarGlicemia(validId, dto);
    
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(validId, response.getBody().getId());
        assertEquals(130.0f, response.getBody().getMeasurement(), 0.001);
    }
    @Test
    @DisplayName("Tenta atualizar uma glicemia com ID inválido")
    void testUpdateWithInvalidId() {
        
        UUID invalidId = UUID.randomUUID();
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(130.0f);
    
        when(atualizarGlicemiaService.executar(invalidId, dto))
                .thenThrow(new NotFoundException("Glicemia não encontrada"));
    
        
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            glicemiaController.atualizarGlicemia(invalidId, dto);
        });
    
        assertEquals("Glicemia não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Tenta atualizar uma glicemia e ocorre um erro interno")
    void testUpdateBloodSugarWithInternalError() {

        UUID validId = UUID.randomUUID();
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(130.0f);
    
        when(atualizarGlicemiaService.executar(validId, dto))
            .thenThrow(new RuntimeException("Erro interno ao atualizar glicemia"));
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            glicemiaController.atualizarGlicemia(validId, dto);
        });
    
        assertEquals("Erro interno ao atualizar glicemia", exception.getMessage());
        verify(atualizarGlicemiaService, times(1)).executar(validId, dto);
    }


    @Test
    @DisplayName("Atualiza uma glicemia sem alterar o valor da medição")
    void testUpdateBloodSugarWithoutChange() {
        
        UUID validId = UUID.randomUUID();
        GlicemiaPostPutRequestDto dto = new GlicemiaPostPutRequestDto();
        dto.setMeasurement(120.0f); 
    
        Glicemia glicemiaAtualizada = new Glicemia();
        glicemiaAtualizada.setId(validId);
        glicemiaAtualizada.setMeasurement(dto.getMeasurement());
    
        when(atualizarGlicemiaService.executar(validId, dto)).thenReturn(glicemiaAtualizada);
    
        
        ResponseEntity<Glicemia> response = glicemiaController.atualizarGlicemia(validId, dto);
    
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(validId, response.getBody().getId());
        assertEquals(120.0f, response.getBody().getMeasurement(), 0.001);
    }
}

@Nested
@DisplayName("Conjunto de casos de deletar o glicemia")
class DeletarGlicemia {
    @Test
    @DisplayName("Desativa uma glicemia com sucesso")
    void testDisableBloodSugarSuccessfully() {
        
        UUID validId = UUID.randomUUID();
    
        doNothing().when(deletarGlicemiaService).executar(validId);
    
        
        ResponseEntity<?> response = glicemiaController.removerGlicemia(validId);
    
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); 
        verify(deletarGlicemiaService, times(1)).executar(validId); 
    }

    @Test
    @DisplayName("Tenta desativar uma glicemia com ID inválido")
    void testDisableBloodSugarWithInvalidId() {
    
        UUID invalidId = UUID.randomUUID();
    
        doThrow(new NotFoundException("Glicemia não encontrada")).when(deletarGlicemiaService).executar(invalidId);
    
        
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            glicemiaController.removerGlicemia(invalidId);
        });
    
        assertEquals("Glicemia não encontrada", exception.getMessage()); 
        verify(deletarGlicemiaService, times(1)).executar(invalidId); 
    }

    @Test
    @DisplayName("Tenta desativar uma glicemia e ocorre um erro interno")
    void testDisableBloodSugarWithInternalError() {

        UUID validId = UUID.randomUUID();
    
        doThrow(new RuntimeException("Erro interno ao desativar glicemia"))
            .when(deletarGlicemiaService).executar(validId);
    
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            glicemiaController.removerGlicemia(validId);
        });
    
        assertEquals("Erro interno ao desativar glicemia", exception.getMessage());
        verify(deletarGlicemiaService, times(1)).executar(validId);
    }

    @Test
    @DisplayName("Tenta desativar uma glicemia que já foi desativada")
    void testDisableBloodSugarJaDesativado() {
   
        UUID validId = UUID.randomUUID();
    
        
        doThrow(new NotFoundException("Glicemia já desativada")).when(deletarGlicemiaService).executar(validId);
    
        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            glicemiaController.removerGlicemia(validId);
        });
    
        assertEquals("Glicemia já desativada", exception.getMessage()); 
        verify(deletarGlicemiaService, times(1)).executar(validId); 
    }
 
}






    
    







}
