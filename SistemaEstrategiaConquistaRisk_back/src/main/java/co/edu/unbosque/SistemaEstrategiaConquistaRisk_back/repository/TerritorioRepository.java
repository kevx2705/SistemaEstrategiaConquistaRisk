package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Territorio;

/**
 * Interfaz que define el repositorio para la entidad {@link Territorio}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos
 * y manejo de persistencia para la entidad Territorio.
 *
 * <p>El tipo de la clave primaria de la entidad es {@link Long}.</p>
 *
 * <p>Proporciona un método personalizado para buscar territorios por su nombre.</p>
 */
public interface TerritorioRepository extends JpaRepository<Territorio, Long> {

    /**
     * Busca un territorio por su nombre.
     *
     * @param nombre Nombre del territorio que se desea buscar.
     * @return Un objeto {@link Optional} que contiene el territorio encontrado,
     *         o vacío si no existe ningún territorio con el nombre especificado.
     */
    Optional<Territorio> findByNombre(String nombre);
}
