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

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.grupo2.diabetter.controller.InsulinController;
import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.dto.insulin.InsulinDeleteResponseDTO;
import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import com.grupo2.diabetter.exception.CommerceException;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.model.Insulin;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulin.AtualizarInsulinService;
import com.grupo2.diabetter.service.insulin.CriarInsulinService;
import com.grupo2.diabetter.service.insulin.DeletarInsulinService;
import com.grupo2.diabetter.service.insulin.ListarInsulinService;
import com.grupo2.diabetter.service.insulin.RecuperarInsulinService;

@SpringBootTest
public class TestesControllerInsulina {

    private MockMvc mockmcv;

    @Mock
    private AtualizarInsulinService atualizarInsulinService;

    @Mock
    private CriarInsulinService criarInsulinService;

    @Mock
    private DeletarInsulinService deletarInsulinService;

    @Mock
    private ListarInsulinService listarInsulinService;

    @Mock
    private RecuperarInsulinService recuperarInsulinService;

    @Mock
    private InsulinRepository insulinRepository;

    @InjectMocks
    private InsulinController insulinController;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

@Nested
@DisplayName("Caso de testes de criação de insulina")
class CriarInsulina{
    @Test
    @DisplayName("Cria insulina com dados válidos")
    void testCreateInsulinWithValidData(){
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .type("Novorapido")
                .units(10)
                .horarioId(horarioId)
                .build();
    
        InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
                .uuid(UUID.randomUUID()) 
                .type(dto.getType())
                .units(dto.getUnits())
                .horarioId(dto.getHorarioId())
                .build();
    
       
        when(criarInsulinService.criarInsulin(any(InsulinPostPutRequestDTO.class))).thenReturn(responseDTO);
    
  
        ResponseEntity<InsulinResponseDTO> response = insulinController.createInsulin(dto);
    
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(horarioId, response.getBody().getHorarioId()); 

    }
    @Test
    @DisplayName("Cria insulina com tipo inválido")
    void testCreateInsulinWithInvalidType() {
        UUID horarioId = UUID.randomUUID();
    
        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .type("") 
                .units(10)
                .horarioId(horarioId)
                .build();
    
        when(criarInsulinService.criarInsulin(any(InsulinPostPutRequestDTO.class)))
                .thenThrow(new CommerceException("Tipo de insulina inválido"));
    
        CommerceException exception = assertThrows(CommerceException.class, () -> {
            insulinController.createInsulin(dto);
        });
    
        assertEquals("Tipo de insulina inválido", exception.getMessage());
    }
    @Test
    @DisplayName("Cria insulina com unidades inválidas")
    void testCreateInsulinWithInvalidUnits() {
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .type("Novorapido")
                .units(-5) // Unidades inválidas (negativas)
                .horarioId(horarioId)
                .build();

        when(criarInsulinService.criarInsulin(any(InsulinPostPutRequestDTO.class)))
                .thenThrow(new CommerceException("Unidades de insulina inválidas"));

        CommerceException exception = assertThrows(CommerceException.class, () -> {
            insulinController.createInsulin(dto);
        });

        assertEquals("Unidades de insulina inválidas", exception.getMessage());
    }

    @Test
    @DisplayName("Cria insulina sem horário")
    void testCreateInsulinWithoutSchedule() {
        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .type("Novorapido")
                .units(10)
                .horarioId(null) // Horário inválido (nulo)
                .build();

        when(criarInsulinService.criarInsulin(any(InsulinPostPutRequestDTO.class)))
                .thenThrow(new CommerceException("Horário de insulina inválido"));

        CommerceException exception = assertThrows(CommerceException.class, () -> {
            insulinController.createInsulin(dto);
        });

        assertEquals("Horário de insulina inválido", exception.getMessage());
    }
    @Test
    @DisplayName("Cria insulina com erro interno")
    void testCreateInsulinWithInternalError() {
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .type("Novorapido")
                .units(10)
                .horarioId(horarioId)
                .build();

        when(criarInsulinService.criarInsulin(any(InsulinPostPutRequestDTO.class)))
                .thenThrow(new RuntimeException("Erro interno no servidor"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insulinController.createInsulin(dto);
        });

        assertEquals("Erro interno no servidor", exception.getMessage());
    }
}
@Nested
@DisplayName("Conjunto de casos de lê insulina")
class  leituraInsulina{
    @Test
    @DisplayName("lê insulina com dados válidos")
    void testReadInsulinWithValidId(){
        UUID horarioId = UUID.randomUUID();
        UUID insulinId = UUID.randomUUID();

        InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
        .uuid(insulinId)
        .type("Novorapido")
        .units(10)
        .horarioId(horarioId)
        .build();
               
               
        when(recuperarInsulinService.recuperarInsulin(insulinId)).thenReturn(responseDTO);

        ResponseEntity<InsulinResponseDTO> response = insulinController.readInsulin(insulinId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(insulinId, response.getBody().getUuid());
        assertEquals(horarioId, response.getBody().getHorarioId());
    }

    @Test
    @DisplayName("Leitura de insulina com dados invalidos")
    void testReadInsulinWithInternalError(){
        UUID horarioId = UUID.randomUUID();
        UUID insulinId = UUID.randomUUID();

        InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
        .uuid(null)
        .type("Novorapido")
        .units(10)
        .horarioId(horarioId)
        .build();

        when(recuperarInsulinService.recuperarInsulin(insulinId)).thenThrow(new RuntimeException("Insulina não encontrada"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insulinController.readInsulin(insulinId);
        });
        assertEquals("Insulina não encontrada", exception.getMessage());
    



    }
}

@Nested
@DisplayName("Casos de listar insulina")
class ListarInsulina{
    @Test
    @DisplayName("Listar insulinas registradas")
    void testListAllInsulinWithRecords(){
        UUID horarioId1 = UUID.randomUUID();
        UUID horarioId2 = UUID.randomUUID();
    
        List<InsulinResponseDTO> insulinList = List.of(
                InsulinResponseDTO.builder()
                        .uuid(UUID.randomUUID())
                        .type("Novorapido")
                        .units(10)
                        .horarioId(horarioId1)
                        .build(),
                InsulinResponseDTO.builder()
                        .uuid(UUID.randomUUID())
                        .type("Lantus")
                        .units(20)
                        .horarioId(horarioId2)
                        .build()
        );
    
        when(listarInsulinService.listarTodasInsulinas()).thenReturn(insulinList);
    
        ResponseEntity<List<InsulinResponseDTO>> response = insulinController.listAllInsulin();
    
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(horarioId1, response.getBody().get(0).getHorarioId());
        assertEquals(horarioId2, response.getBody().get(1).getHorarioId());
    }

    @Test
    @DisplayName("Listar todas as insulinas, quando nãos e tem registro")
    void testListAllInsulinWithotRecords(){
        when(listarInsulinService.listarTodasInsulinas())
        .thenThrow(new NotFoundException("Não existem registros"));


        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            insulinController.listAllInsulin();
        });

        assertEquals("Não existem registros", exception.getMessage());
    }

    @Test
    @DisplayName("le insulina com erro interno")
    void testListAllInsulinWithInternalError(){
    UUID horarioId = UUID.randomUUID();
    UUID insulinId = UUID.randomUUID();

    InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
    .uuid(insulinId)
    .type("Novorapido")
    .units(10)
    .horarioId(horarioId)
    .build();
           
           
    when(listarInsulinService.listarTodasInsulinas()).thenThrow(new RuntimeException("Erro interno no servidor"));

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        insulinController.listAllInsulin();
    });

    assertEquals("Erro interno no servidor", exception.getMessage());

    }
    }

@Nested
@DisplayName("Conjunto de casos de atualização da insulina")
class updateInsulin{
    @Test
    @DisplayName("Atualizar insulina")
    void testUpdateInsulinWithValidData(){
        
        UUID insulinId = UUID.randomUUID();
        UUID horarioId = UUID.randomUUID();

        
        InsulinPostPutRequestDTO dto = InsulinPostPutRequestDTO.builder()
                .type("Rapida")
                .units(15)
                .horarioId(horarioId)
                .build();

        InsulinResponseDTO responseDTO = InsulinResponseDTO.builder()
                .uuid(insulinId)
                .type("Basal")
                .units(15)
                .horarioId(horarioId)
                .build();

        when(atualizarInsulinService.atualizarInsulin(insulinId, dto)).thenReturn(responseDTO);

        ResponseEntity<InsulinResponseDTO> response = insulinController.updateInsulin(insulinId, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(insulinId, response.getBody().getUuid());
        assertEquals("Basal", response.getBody().getType());
        assertEquals(15, response.getBody().getUnits(), 0.001);
        assertEquals(horarioId, response.getBody().getHorarioId());

    }
    
    
}
@Nested
@DisplayName("Casos de teste de deletar a insulina")
class DeletarInsulina{
    @Test
    void testDisableInsulinWithSuccess() {
        UUID id = UUID.randomUUID();
        Insulin insulin = new Insulin();
        insulin.setUuid(id);

        when(insulinRepository.existsById(id)).thenReturn(true);
        doNothing().when(insulinRepository).deleteById(id);

        InsulinDeleteResponseDTO response = deletarInsulinService.deletarInsulin(id);

        assertNotNull(response);
        assertEquals("Insulin deletada com sucesso", response.getMensagem());
        verify(insulinRepository, times(1)).deleteById(id);
    }

    @Test
    void testDisableInsulinWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(insulinRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            deletarInsulinService.deletarInsulin(id);
        });

        verify(insulinRepository, never()).deleteById(id);
    }

    @Test
    void testDisableInsulinWithInvalidData() {
        UUID id = null;

        assertThrows(IllegalArgumentException.class, () -> {
            deletarInsulinService.deletarInsulin(id);
        });

        verify(insulinRepository, never()).deleteById(any());
    }

    @Test
    void testDisableInsulinAlreadyDisabled() {
        UUID id = UUID.randomUUID();

        when(insulinRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            deletarInsulinService.deletarInsulin(id);
        });

        verify(insulinRepository, never()).deleteById(id);
    }

    @Test
    void testDisableInsulinWithInternalError() {
        UUID id = UUID.randomUUID();

        when(insulinRepository.existsById(id)).thenReturn(true);
        doThrow(new EmptyResultDataAccessException(1)).when(insulinRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> {
            deletarInsulinService.deletarInsulin(id);
        });

        verify(insulinRepository, times(1)).deleteById(id);
    }
}
}


