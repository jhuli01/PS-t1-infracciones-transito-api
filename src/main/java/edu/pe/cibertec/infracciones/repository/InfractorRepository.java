package edu.pe.cibertec.infracciones.repository;

import edu.pe.cibertec.infracciones.model.Infractor;
import edu.pe.cibertec.infracciones.model.Multa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InfractorRepository extends JpaRepository<Infractor, Long> {
    Optional<Infractor> findByDni(String dni);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);

}