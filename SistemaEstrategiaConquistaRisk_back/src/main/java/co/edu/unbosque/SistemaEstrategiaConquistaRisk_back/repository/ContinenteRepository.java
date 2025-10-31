package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Continente;

public interface ContinenteRepository extends JpaRepository<Continente, Long>{

	Optional<Continente> findByNombre(String nombre);

}
