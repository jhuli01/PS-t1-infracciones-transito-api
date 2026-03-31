package edu.pe.cibertec.infracciones;


import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.service.impl.MultaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MultaServiceImpl - Test Unit")
public class MultaServiceTest {

    @Mock
    private MultaRepository multaRepository;

    @InjectMocks
    private MultaServiceImpl multaService;

    @Test
    @DisplayName(" multa PENDIENTE con fechaVencimiento=2026-01-01 multa cambia su estado a VENCIDA")
    void givenMultaPendienteVencida_whenActualizarEstado_thenEstadoCambiaAVencido(){

        Multa multa = new Multa();
        multa.setEstado(EstadoMulta.PENDIENTE);
        multa.setFechaVencimiento(LocalDate.of(2026, 1, 1));

        when(multaRepository.findByEstado(EstadoMulta.PENDIENTE)).thenReturn(List.of(multa));

        multaService.actualizarEstados();

        assertEquals(EstadoMulta.VENCIDA, multa.getEstado());
        verify(multaRepository, times(1)).findByEstado(EstadoMulta.PENDIENTE);
        verify(multaRepository,times(1)).save(multa);
    }
}
