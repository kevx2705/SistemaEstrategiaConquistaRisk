package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Territorio;

public interface TerritorioRepository extends JpaRepository<Territorio, Long>{
	Optional<Territorio> findByNombre(String nombre);
	

}
