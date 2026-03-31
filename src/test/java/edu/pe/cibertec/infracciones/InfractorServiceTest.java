package edu.pe.cibertec.infracciones;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Infractor;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.VehiculoRepository;
import edu.pe.cibertec.infracciones.service.impl.InfractorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InfractorServiceImpl - Test Unit")
public class InfractorServiceTest {

    @Mock
    private InfractorRepository infractorRepository;
    @Mock
    private MultaRepository multaRepository;
    @Mock
    private VehiculoRepository vehiculoRepository;
    @InjectMocks
    private InfractorServiceImpl service;


    @Test
    @DisplayName("Infractor con 2 multas VENCIDAS NO debe bloquearse")
    void given2MultasVencidas_whenVerificarBloqueo_thenNoDebeBloqueo(){

        Infractor infractor = new Infractor();
        infractor.setId(1L);
        infractor.setBloqueado(false);

        Multa vencida1 = new Multa();
        vencida1.setEstado(EstadoMulta.VENCIDA);

        Multa vencida2 = new Multa();
        vencida2.setEstado(EstadoMulta.VENCIDA);

        Multa pagada1 = new Multa();
        pagada1.setEstado(EstadoMulta.PAGADA);

        Multa pagada2 = new Multa();
        pagada2.setEstado(EstadoMulta.PAGADA);

        Multa pagada3 = new Multa();
        pagada3.setEstado(EstadoMulta.PAGADA);

        List<Multa> multasVencidas = new ArrayList<>(List.of(vencida1, vencida2));
        List<Multa> multasPagadas  = new ArrayList<>(List.of(pagada1, pagada2, pagada3));

        when(infractorRepository.findById(1L)).thenReturn(Optional.of(infractor));
        when(multaRepository.findByInfractor_IdAndEstado(1L, EstadoMulta.VENCIDA))
                .thenReturn(multasVencidas);

        service.verificarBloqueo(1L);

        assertFalse(infractor.isBloqueado());
        verify(infractorRepository, times(1)).findById(1L);
        verify(infractorRepository, never()).save(any());
    }


}
