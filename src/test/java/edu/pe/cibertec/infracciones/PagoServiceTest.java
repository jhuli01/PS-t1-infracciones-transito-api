package edu.pe.cibertec.infracciones;


import edu.pe.cibertec.infracciones.dto.PagoResponseDTO;
import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.model.Pago;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.PagoRepository;
import edu.pe.cibertec.infracciones.service.impl.PagoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagoServiceImpl - Test Unit")
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private MultaRepository multaRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @Test
    @DisplayName("Multa PENDIENTE pagada el mismo día tiene descuento 20% y multa cambia a PAGADA")
    void givenMultaPendienteElMismoDia_whenProcesarPago_thenSeAgregaDescuentoYCambiaAPagada(){

        Multa multa = new Multa();
        multa.setId(1L);
        multa.setMonto(500.00);
        multa.setFechaEmision(LocalDate.now());
        multa.setFechaVencimiento(LocalDate.of(2026, 5, 5));
        multa.setEstado(EstadoMulta.PENDIENTE);

        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));

        PagoResponseDTO resultado = pagoService.procesarPago(1L);

        assertEquals(400.00, resultado.getMontoPagado());
        assertEquals(100.00, resultado.getDescuentoAplicado());
        assertEquals(EstadoMulta.PAGADA, multa.getEstado());

        verify(pagoRepository, times(1)).save(any(Pago.class));
        verify(multaRepository, times(1)).save(multa);

    }

    @Test
    @DisplayName("Multa vencida con 12 días desde emisión tiene recargo 15% y montoPagado=575.00")
    void givenMultaVencidaConMasDe5Dias_whenProcesarPago_thenAplicaRecargoDe15(){

        Multa multa = new Multa();
        multa.setId(1L);
        multa.setMonto(500.00);
        multa.setFechaEmision(LocalDate.now().minusDays(12));
        multa.setFechaVencimiento(LocalDate.now().minusDays(2));
        multa.setEstado(EstadoMulta.PENDIENTE);

        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));

        pagoService.procesarPago(1L);

        ArgumentCaptor<Pago> captor = ArgumentCaptor.forClass(Pago.class);

        verify(pagoRepository, times(1)).save(captor.capture());

        Pago pagoGuardado  = captor.getValue();

        assertEquals(0.00,   pagoGuardado.getDescuentoAplicado());
        assertEquals(75.00,  pagoGuardado.getRecargo());
        assertEquals(575.00, pagoGuardado.getMontoPagado());
        assertEquals(EstadoMulta.PAGADA, multa.getEstado());

    }
}
