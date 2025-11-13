package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;

/**
 * Interfaz que define el repositorio para la entidad {@link Jugador}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos
 * y manejo de persistencia para la entidad Jugador.
 *
 * <p>El tipo de la clave primaria de la entidad es {@link Long}.</p>
 *
 * <p>Proporciona métodos personalizados para buscar jugadores por su ID, nombre o correo.</p>
 */
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    /**
     * Busca un jugador por su identificador único.
     *
     * @param id Identificador único del jugador.
     * @return Un objeto {@link Optional} que contiene el jugador encontrado,
     *         o vacío si no existe un jugador con el ID especificado.
     */
    Optional<Jugador> findById(Long id);

    /**
     * Busca un jugador por su nombre.
     *
     * @param nombre Nombre del jugador que se desea buscar.
     * @return El jugador encontrado.
     * @throws javax.persistence.NoResultException si no se encuentra ningún jugador con el nombre especificado.
     */
    Jugador findByNombre(String nombre);

    /**
     * Busca un jugador por su correo electrónico.
     *
     * @param correo Correo electrónico del jugador que se desea buscar.
     * @return El jugador encontrado.
     * @throws javax.persistence.NoResultException si no se encuentra ningún jugador con el correo especificado.
     */
    Jugador findByCorreo(String correo);
}
