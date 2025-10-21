package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Usuario;

/**
 * Clase AdminDTO que extiende de Usuario 
 * Es un Data Transfer Object para la entidad Admin
 */
public class AdminDTO extends Usuario {
	
	/**
	 * Constructor vacio de la clase AdminDTO
	 */
	public AdminDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con parametros (atributos) heredados de Usuario
	 * @param nombre
	 * @param correo
	 * @param edad
	 * @param contrasena
	 */
	public AdminDTO(String nombre, String correo, int edad, String contrasena) {
		super(nombre, correo, edad, contrasena);
	}
	
}
