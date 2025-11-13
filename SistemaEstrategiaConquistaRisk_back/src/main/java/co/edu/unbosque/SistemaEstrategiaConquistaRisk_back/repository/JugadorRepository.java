package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
	Optional<Jugador> findById(Long id);

	Jugador findByNombre(String nombre);

	Jugador findByCorreo(String correo);

}
