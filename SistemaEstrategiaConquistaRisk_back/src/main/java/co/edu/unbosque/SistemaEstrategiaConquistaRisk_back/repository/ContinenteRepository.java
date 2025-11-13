package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Continente;

/**
 * Interfaz que define el repositorio para la entidad {@link Continente}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos
 * y manejo de persistencia para la entidad Continente.
 *
 * <p>El tipo de la clave primaria de la entidad es {@link Long}.</p>
 *
 * <p>Proporciona métodos personalizados para buscar continentes por su nombre.</p>
 */
public interface ContinenteRepository extends JpaRepository<Continente, Long> {

    /**
     * Busca un continente por su nombre.
     *
     * @param nombre Nombre del continente que se desea buscar.
     * @return Un objeto {@link Optional} que contiene el continente encontrado,
     *         o vacío si no se encontró ningún continente con el nombre especificado.
     */
    Optional<Continente> findByNombre(String nombre);
}
