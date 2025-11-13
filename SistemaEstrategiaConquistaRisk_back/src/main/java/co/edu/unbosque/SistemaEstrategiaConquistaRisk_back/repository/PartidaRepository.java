package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import java.util.Optional;

/**
 * Interfaz que define el repositorio para la entidad {@link Partida}.
 * Extiende {@link CrudRepository} para proporcionar métodos CRUD básicos
 * y manejo de persistencia para la entidad Partida.
 *
 * <p>El tipo de la clave primaria de la entidad es {@link Long}.</p>
 *
 * <p>Proporciona métodos personalizados para buscar partidas por su código hash,
 * estado de inicio y finalización.</p>
 */
public interface PartidaRepository extends CrudRepository<Partida, Long> {

    /**
     * Busca una partida por su código hash único.
     *
     * @param codigoHash Código hash único de la partida.
     * @return Un objeto {@link Optional} que contiene la partida encontrada,
     *         o vacío si no existe ninguna partida con el código hash especificado.
     */
    Optional<Partida> findByCodigoHash(String codigoHash);

    /**
     * Busca todas las partidas que han sido iniciadas.
     *
     * @return Un {@link Iterable} de partidas iniciadas.
     */
    Iterable<Partida> findByIniciadaTrue();

    /**
     * Busca todas las partidas que han sido finalizadas.
     *
     * @return Un {@link Iterable} de partidas finalizadas.
     */
    Iterable<Partida> findByFinalizadaTrue();

    /**
     * Busca todas las partidas que están activas (iniciadas pero no finalizadas).
     *
     * @return Un {@link Iterable} de partidas activas.
     */
    Iterable<Partida> findByIniciadaTrueAndFinalizadaFalse();

    /**
     * Busca la primera partida activa (iniciada pero no finalizada).
     *
     * @return La primera partida activa encontrada.
     * @throws javax.persistence.NoResultException si no se encuentra ninguna partida activa.
     */
    Partida findFirstByIniciadaTrueAndFinalizadaFalse();
}
