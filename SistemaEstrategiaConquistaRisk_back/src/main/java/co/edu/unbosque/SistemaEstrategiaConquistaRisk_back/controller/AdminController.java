//package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;
//
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.AdminDTO;
//import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.AdminService;
//
///**
// * Controlador REST para gestionar operaciones relacionadas con administradores.
// * Expone endpoints para crear, actualizar, eliminar y consultar administradores.
// * Utiliza {@link AdminService} para delegar la lógica de negocio.
// */
//@RestController
//@CrossOrigin(origins = { "*" })
//@RequestMapping(path = { "/admin" })
//public class AdminController {
//
//	/** Servicio que gestiona la lógica de negocio para administradores. */
//	@Autowired
//	private AdminService adminService;
//
//	/**
//	 * Crea un nuevo administrador a partir de parámetros individuales.
//	 *
//	 * @param nombre     nombre del administrador
//	 * @param correo     correo electrónico
//	 * @param edad       edad del administrador
//	 * @param contrasena contraseña del administrador
//	 * @return respuesta HTTP con mensaje de éxito o error
//	 */
//	@PostMapping(path = "/crear")
//	public ResponseEntity<String> crear(@RequestParam String nombre, String correo, int edad, String contrasena) {
//		AdminDTO nuevo = new AdminDTO(nombre, correo, edad, contrasena);
//		int status = adminService.create(nuevo);
//		if (status == 0) {
//			return new ResponseEntity<>("Usuario creado con exito", HttpStatus.CREATED);
//		} else {
//			return new ResponseEntity<>("Error creando su cuenta, nombre o correo ya en uso.", HttpStatus.NOT_ACCEPTABLE);
//		}
//	}
//
//	/**
//	 * Crea un nuevo administrador a partir de un objeto JSON.
//	 *
//	 * @param newUser DTO con los datos del nuevo administrador
//	 * @return respuesta HTTP con mensaje de éxito o error
//	 */
//	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	ResponseEntity<String> createNewWithJSON(@RequestBody AdminDTO newUser) {
//		int status = adminService.create(newUser);
//		if (status == 0) {
//			return new ResponseEntity<>("Usuario creado con exito", HttpStatus.CREATED);
//		} else {
//			return new ResponseEntity<>("Error creando su cuenta, nombre o correo ya en uso.", HttpStatus.NOT_ACCEPTABLE);
//		}
//	}
//
//	/**
//	 * Elimina un administrador por su ID.
//	 *
//	 * @param id identificador del administrador
//	 * @return respuesta HTTP con mensaje de éxito o error
//	 */
//	@DeleteMapping(path = "/eliminar")
//	public ResponseEntity<String> eliminar(@RequestParam Long id) {
//		int status = adminService.deleteById(id);
//		if (status == 0) {
//			return new ResponseEntity<>("Eliminado con exito", HttpStatus.NO_CONTENT);
//		} else {
//			return new ResponseEntity<>("Admin no encontrado", HttpStatus.NOT_FOUND);
//		}
//	}
//
//	/**
//	 * Actualiza los datos de un administrador por su ID.
//	 *
//	 * @param id         identificador del administrador
//	 * @param nombre     nuevo nombre
//	 * @param correo     nuevo correo
//	 * @param edad       nueva edad
//	 * @param contrasena nueva contraseña
//	 * @return respuesta HTTP con mensaje de éxito o error
//	 */
//	@PutMapping(path = "/actualizar")
//	public ResponseEntity<String> actualizar(@RequestParam Long id, String nombre, String correo, int edad, String contrasena) {
//		AdminDTO data = new AdminDTO(nombre, correo, edad, contrasena);
//		int status = adminService.updateById(id, data);
//		if (status == 0) {
//			return new ResponseEntity<>("Admin actualizado", HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>("Error al actualizar", HttpStatus.NOT_ACCEPTABLE);
//		}
//	}
//
//	/**
//	 * Muestra todos los administradores en formato de texto plano.
//	 *
//	 * @return respuesta HTTP con listado de administradores o mensaje de vacío
//	 */
//	@GetMapping(path = "/mostrar")
//	public ResponseEntity<String> mostrar() {
//		List<AdminDTO> lista = adminService.getAll();
//		if (lista.isEmpty()) {
//			return new ResponseEntity<>("Aun no existe ningun admin", HttpStatus.NOT_FOUND);
//		} else {
//			StringBuilder sb = new StringBuilder();
//			lista.forEach(dto -> sb.append(dto.toString()).append("\n"));
//			return new ResponseEntity<>("Admins:\n" + sb.toString(), HttpStatus.ACCEPTED);
//		}
//	}
//
//	/**
//	 * Obtiene todos los administradores en formato JSON.
//	 *
//	 * @return respuesta HTTP con lista de administradores o estado vacío
//	 */
//	@GetMapping("/getall")
//	ResponseEntity<List<AdminDTO>> getAll() {
//		List<AdminDTO> users = adminService.getAll();
//		if (users.isEmpty()) {
//			return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
//		} else {
//			return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
//		}
//	}
//}