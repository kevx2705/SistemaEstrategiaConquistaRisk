package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.AdminDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Admin;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.AdminRepository;


/**
 * Servicio que gestiona operaciones CRUD para la entidad {@code Admin} usando {@code AdminDTO}.
 * Implementa la interfaz {@code CRUDOperation<AdminDTO>} y utiliza {@code ModelMapper} para la conversión entre entidades y DTOs.
 */
@Service
public class AdminService implements CRUDOperation<AdminDTO> {

	/** Repositorio JPA para acceder a datos de administradores. */
	@Autowired
	private AdminRepository adminRepo;

	/** Utilidad para mapear entre entidades y DTOs. */
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Constructor vacío requerido por Spring.
	 */
	public AdminService() {

	}

	/**
	 * Crea un nuevo administrador en la base de datos.
	 * 
	 * @param newData DTO con los datos del nuevo administrador
	 * @return 0 si la operación fue exitosa
	 */
	@Override
	public int create(AdminDTO newData) {
		Admin entity = modelMapper.map(newData, Admin.class);
		adminRepo.save(entity);
		return 0;
	}

	/**
	 * Obtiene todos los administradores registrados.
	 * 
	 * @return lista de {@code AdminDTO}
	 */
	@Override
	public List<AdminDTO> getAll() {
		List<Admin> entityList = adminRepo.findAll();
		List<AdminDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			AdminDTO dto = modelMapper.map(entity, AdminDTO.class);
			dtoList.add(dto);
		});
		return dtoList;
	}

	/**
	 * Elimina un administrador por su ID.
	 * 
	 * @param id identificador del administrador
	 * @return 0 si se eliminó correctamente, 1 si no existe
	 */
	@Override
	public int deleteById(Long id) {
		if (adminRepo.existsById(id)) {
			adminRepo.deleteById(id);
			return 0;
		}
		return 1;
	}

	/**
	 * Actualiza los datos de un administrador por su ID.
	 * 
	 * @param id      identificador del administrador
	 * @param newData DTO con los nuevos datos
	 * @return 0 si se actualizó correctamente, 1 si no se encontró
	 */
	@Override
	public int updateById(Long id, AdminDTO newData) {
		Optional<Admin> opt = adminRepo.findById(id);
		if (opt.isPresent()) {
			Admin entity = opt.get();
			entity.setNombre(newData.getNombre());
			entity.setCorreo(newData.getCorreo());
			entity.setEdad(newData.getEdad());
			entity.setContrasena(newData.getContrasena());
			adminRepo.save(entity);
			return 0;
		}
		return 1;
	}

	/**
	 * Cuenta la cantidad total de administradores registrados.
	 * 
	 * @return número total de administradores
	 */
	@Override
	public Long count() {
		Long tamano = (long) getAll().size();
		return tamano;
	}

	/**
	 * Verifica si existe un administrador por su ID.
	 * 
	 * @param id identificador del administrador
	 * @return {@code false} (implementación pendiente)
	 */
	@Override
	public boolean exist(Long id) {
		// TODO Auto-generated method stub
		return false;
	}
}
