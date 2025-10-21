package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Admin;

/**
 * Repositorio JPA para la entidad {@code Admin}.
 * Proporciona operaciones CRUD y consultas personalizadas sobre la tabla de administradores.
 * Extiende {@link JpaRepository} para heredar métodos como {@code findAll()}, {@code save()}, {@code deleteById()}, etc.
 */
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
