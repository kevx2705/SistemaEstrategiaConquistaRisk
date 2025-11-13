package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;

/**
 * Interfaz que define el repositorio para la entidad {@link Carta}.
 * Extiende {@link JpaRepository} para proporcionar métodos CRUD básicos
 * y manejo de persistencia para la entidad Carta.
 *
 * <p>El tipo de la clave primaria de la entidad es {@link Long}.</p>
 */
public interface CartaRepository extends JpaRepository<Carta, Long> {
    // Spring Data JPA proporciona automáticamente los métodos CRUD básicos.
    // Se pueden agregar métodos personalizados de consulta aquí si son necesarios.
}
