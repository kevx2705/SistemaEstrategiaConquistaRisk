package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.util.List;
/**
 * Interfaz genérica para operaciones CRUD sobre objetos de tipo {@code D}.
 * Define los métodos básicos para crear, leer, actualizar, eliminar, contar y verificar existencia.
 *
 * @param <D> tipo de DTO que será gestionado por la implementación
 */
public interface CRUDOperation <D> {

	/**
	 * Crea una nueva instancia del DTO en el sistema.
	 *
	 * @param newData datos del nuevo objeto
	 * @return 0 si la operación fue exitosa, otro valor si hubo error
	 */
	public int create(D newData);

	/**
	 * Obtiene todos los objetos registrados.
	 *
	 * @return lista de objetos DTO
	 */
	public List<D> getAll();

	/**
	 * Elimina un objeto por su identificador.
	 *
	 * @param id identificador del objeto
	 * @return 0 si se eliminó correctamente, otro valor si no se encontró
	 */
	public int deleteById(Long id);

	/**
	 * Actualiza los datos de un objeto por su identificador.
	 *
	 * @param id identificador del objeto
	 * @param newData nuevos datos a aplicar
	 * @return 0 si se actualizó correctamente, otro valor si no se encontró
	 */
	public int updateById(Long id, D newData);

	/**
	 * Cuenta la cantidad total de objetos registrados.
	 *
	 * @return número total de objetos
	 */
	public Long count();

	/**
	 * Verifica si existe un objeto con el identificador dado.
	 *
	 * @param id identificador del objeto
	 * @return {@code true} si existe, {@code false} en caso contrario
	 */
	public boolean exist(Long id);
}
