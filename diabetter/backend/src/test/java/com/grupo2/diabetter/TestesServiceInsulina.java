package com.grupo2.diabetter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.grupo2.diabetter.controller.InsulinController;
import com.grupo2.diabetter.dto.insulin.InsulinDeleteResponseDTO;
import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import com.grupo2.diabetter.exception.CommerceException;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulin.AtualizarInsulinService;
import com.grupo2.diabetter.service.insulin.CriarInsulinService;
import com.grupo2.diabetter.service.insulin.DeletarInsulinService;
import com.grupo2.diabetter.service.insulin.ListarInsulinService;
import com.grupo2.diabetter.service.insulin.RecuperarInsulinService;

@SpringBootTest
public class TestesServiceInsulina {

    private MockMvc mockmcv;

    @Mock
    private AtualizarInsulinService atualizarInsulinService;

    @InjectMocks
    private CriarInsulinService criarInsulinService;

    @InjectMocks
    private DeletarInsulinService deletarInsulinService;

    @InjectMocks
    private ListarInsulinService listarInsulinService;

    @InjectMocks
    private RecuperarInsulinService recuperarInsulinService;

    @Mock
    private InsulinRepository insulinRepository;

    @Mock
    private InsulinController insulinController;



    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

@Nested
@DisplayName("Conjunto de casos de criação de insulina")
class createInsulin{
     @Test
    @DisplayName("Cria insulina com dados válidos")
    void testCreateInsulinWithValidData() {
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                .type("Novorapido")
                .units(10)
                .horarioId(horarioId)
                .build();

        Insulina insulinSalva = Insulina.builder()
                .uuid(UUID.randomUUID())
                .type(requestDTO.getType())
                .units(requestDTO.getUnits())
                .horario(requestDTO.getHorarioId())
                .build();

        when(insulinRepository.save(any(Insulina.class))).thenReturn(insulinSalva);

        InsulinResponseDTO responseDTO = criarInsulinService.criarInsulin(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(insulinSalva.getUuid(), responseDTO.getUuid());
        assertEquals(insulinSalva.getType(), responseDTO.getType());
        assertEquals(insulinSalva.getUnits(), responseDTO.getUnits());
        assertEquals(insulinSalva.getHorario(), responseDTO.getHorarioId());
    }

     @Test
    @DisplayName("Cria insulina com tipo inválido")
    void testCreateInsulinWithInvalidType() {
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                .type("") 
                .units(10)
                .horarioId(horarioId)
                .build();

        CommerceException exception = assertThrows(CommerceException.class, () -> {
            criarInsulinService.criarInsulin(requestDTO);
        });

        assertEquals("Tipo de insulina inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Cria insulina com unidades inválidas")
    void testCreateInsulinWithInvalidUnits() {
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                .type("Novorapido")
                .units(-5) 
                .horarioId(horarioId)
                .build();

        CommerceException exception = assertThrows(CommerceException.class, () -> {
            criarInsulinService.criarInsulin(requestDTO);
        });

        
        assertEquals("Unidades de insulina inválidas", exception.getMessage());
    }

    @Test
    @DisplayName("Cria insulina com erro interno")
    void testCreateInsulinWithInternalError() {
        UUID horarioId = UUID.randomUUID();

        InsulinPostPutRequestDTO requestDTO = InsulinPostPutRequestDTO.builder()
                .type("Novorapido")
                .units(10)
                .horarioId(horarioId)
                .build();

        when(insulinRepository.save(any(Insulina.class)))
                .thenThrow(new RuntimeException("Erro interno no servidor"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            criarInsulinService.criarInsulin(requestDTO);
        });

        assertEquals("Erro interno no servidor", exception.getMessage());
    }
}

@Nested
@DisplayName("Testes de leitura de insulina")
class readInsulin{
    @Test
        @DisplayName("Leitura de insulina com ID válido")
        void testReadInsulinWithValidId() {
            UUID horarioId = UUID.randomUUID();
            UUID validId = UUID.randomUUID();

            Insulina insulin = new Insulina();
            insulin.setUuid(validId);
            insulin.setType("Novorapido");
            insulin.setUnits(10);
            insulin.setHorario(UUID.randomUUID());

            when(insulinRepository.findById(validId)).thenReturn(Optional.of(insulin));

            
            InsulinResponseDTO responseDTO = recuperarInsulinService.recuperarInsulin(validId);

       
            assertNotNull(responseDTO);
            assertEquals(validId, responseDTO.getUuid());
            assertEquals("Novorapido", responseDTO.getType());
            assertEquals(10, responseDTO.getUnits());
            assertEquals(insulin.getHorario(), responseDTO.getHorarioId());

            verify(insulinRepository, times(1)).findById(validId);
        }

        @Test
        @DisplayName("Leitura de insulina com ID inválido")
        void testReadInsulinWithInvalidId() {
            UUID horarioId = UUID.randomUUID();
            UUID invalidId = UUID.randomUUID();
            
            when(insulinRepository.findById(invalidId)).thenReturn(Optional.empty());

       
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                recuperarInsulinService.recuperarInsulin(invalidId);
            });

            assertEquals("Insulin não encontrada", exception.getMessage());

            verify(insulinRepository, times(1)).findById(invalidId);
        }
    }
@Nested
@DisplayName("Casos de listar insulina")
class ListarInsulina {

        @Test
        @DisplayName("Listar insulinas com registros")
        void testListAllInsulinWithRecords() {
        
            UUID horarioId1 = UUID.randomUUID();
            UUID horarioId2 = UUID.randomUUID();

            Insulina insulin1 = new Insulina();
            insulin1.setUuid(UUID.randomUUID());
            insulin1.setType("Novorapido");
            insulin1.setUnits(10);
            insulin1.setHorario(horarioId1);

            Insulina insulin2 = new Insulina();
            insulin2.setUuid(UUID.randomUUID());
            insulin2.setType("Lantus");
            insulin2.setUnits(20);
            insulin2.setHorario(horarioId2);

            List<Insulina> insulinas = List.of(insulin1, insulin2);

            when(insulinRepository.findAll()).thenReturn(insulinas);

         
            List<InsulinResponseDTO> responseDTOs = listarInsulinService.listarTodasInsulinas();

            assertNotNull(responseDTOs);
            assertEquals(2, responseDTOs.size());
            assertEquals(horarioId1, responseDTOs.get(0).getHorarioId());
            assertEquals(horarioId2, responseDTOs.get(1).getHorarioId());

            verify(insulinRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Listar insulinas sem registros")
        void testListAllInsulinWithoutRecords() {
  
            when(insulinRepository.findAll()).thenReturn(Collections.emptyList());

     
            List<InsulinResponseDTO> responseDTOs = listarInsulinService.listarTodasInsulinas();

            assertNotNull(responseDTOs);
            assertTrue(responseDTOs.isEmpty());

            verify(insulinRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Listar insulinas com erro interno")
        void testListAllInsulinWithInternalError() {
         
            when(insulinRepository.findAll()).thenThrow(new RuntimeException("Erro interno no servidor"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                listarInsulinService.listarTodasInsulinas();
            });

            assertEquals("Erro interno no servidor", exception.getMessage());

            verify(insulinRepository, times(1)).findAll();
        }
    }

@Nested
@DisplayName("Casos de teste de deletar a insulina")
class DeletarInsulina {

    @Test
    @DisplayName("Desativa insulina com sucesso")
    void testDisableInsulinWithSuccess() {
        UUID validId = UUID.randomUUID();

        when(insulinRepository.existsById(validId)).thenReturn(true);

        InsulinDeleteResponseDTO responseDTO = deletarInsulinService.deletarInsulin(validId);

        assertNotNull(responseDTO);
        assertEquals("Insulin deletada com sucesso", responseDTO.getMensagem());

        verify(insulinRepository, times(1)).existsById(validId);
        verify(insulinRepository, times(1)).deleteById(validId);
    }

    @Test
    @DisplayName("Desativa insulina com ID inválido")
    void testDisableInsulinWithInvalidId() {
        UUID invalidId = UUID.randomUUID();

        when(insulinRepository.existsById(invalidId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deletarInsulinService.deletarInsulin(invalidId);
        });

        assertEquals("Insulin não encontrada", exception.getMessage());

        verify(insulinRepository, times(1)).existsById(invalidId);
        verify(insulinRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Desativa insulina com dados inválidos")
    void testDisableInsulinWithInvalidData() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deletarInsulinService.deletarInsulin(null);
        });

        assertEquals("ID não pode ser nulo", exception.getMessage());

        verify(insulinRepository, never()).existsById(any());
        verify(insulinRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Desativa insulina já desativada")
    void testDisableInsulinAlreadyDisabled() {
        UUID validId = UUID.randomUUID();

        when(insulinRepository.existsById(validId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deletarInsulinService.deletarInsulin(validId);
        });

        assertEquals("Insulin não encontrada", exception.getMessage());

        verify(insulinRepository, times(1)).existsById(validId);
        verify(insulinRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Desativa insulina com erro interno")
    void testDisableInsulinWithInternalError() {
        UUID validId = UUID.randomUUID();

        when(insulinRepository.existsById(validId)).thenReturn(true);
        doThrow(new RuntimeException("Erro interno no servidor")).when(insulinRepository).deleteById(validId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deletarInsulinService.deletarInsulin(validId);
        });

        assertEquals("Erro interno no servidor", exception.getMessage());

        verify(insulinRepository, times(1)).existsById(validId);
        verify(insulinRepository, times(1)).deleteById(validId);
    }
}


    

}
