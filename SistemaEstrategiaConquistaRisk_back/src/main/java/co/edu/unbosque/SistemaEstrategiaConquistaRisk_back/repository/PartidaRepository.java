package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;

import java.util.Optional;

public interface PartidaRepository extends CrudRepository<Partida, Long> {

    // Buscar una partida por su hash único
    Optional<Partida> findByCodigoHash(String codigoHash);

    // Buscar solo partidas iniciadas (Iterable, NO List)
    Iterable<Partida> findByIniciadaTrue();

    // Buscar solo partidas finalizadas
    Iterable<Partida> findByFinalizadaTrue();

    // Buscar partidas activas (iniciada = true, finalizada = false)
    Iterable<Partida> findByIniciadaTrueAndFinalizadaFalse();
}
