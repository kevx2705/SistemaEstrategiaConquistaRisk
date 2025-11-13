package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.CharacterException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.ExceptionCheker;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.MailException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.NumberException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.SymbolException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.TextException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.EmailService;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.JugadorService;

@RestController
@RequestMapping("/jugadores")
@CrossOrigin(origins = "*")
public class JugadorController {

	@Autowired
	private JugadorService jugadorService;

	@Autowired
	private EmailService emailService;

	// ==========================================================
	// ✅ CRUD BÁSICO
	// ==========================================================
	@PostMapping(path = "/crear")
	public ResponseEntity<String> crearJugador(@RequestParam String nombre, @RequestParam String correo,
	        @RequestParam String contrasena) {
	    try {
	        ExceptionCheker.checkerText(nombre);
	        ExceptionCheker.checkerMail(correo);
	        ExceptionCheker.checkerPasword(contrasena);

	        JugadorDTO dto = new JugadorDTO();
	        dto.setNombre(nombre);
	        dto.setCorreo(correo);
	        dto.setContrasena(contrasena);

	        int status = jugadorService.create(dto);
	        emailService.enviarCorreoRegistroHTML(dto.getCorreo(), dto.getNombre());

	        if (status == 0) {
	            emailService.enviarCorreoRegistroHTML(dto.getCorreo(), dto.getNombre());
	            return ResponseEntity.status(201).body("Jugador creado con éxito");
	        } else if (status == 1) {
	            return ResponseEntity.status(406).body("Error: el nombre ya está en uso");
	        } else if (status == 2) {
	            return ResponseEntity.status(406).body("Error: datos inválidos");
	        } else if (status == 3) {
	            return ResponseEntity.status(406).body("Error: el correo ya está en uso");
	        } else {
	            return ResponseEntity.status(406).body("Error desconocido al crear el jugador");
	        }
	    } catch (MailException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nCorreo inválido. Use gmail, hotmail, outlook o unbosque.edu.co");
	    } catch (TextException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("406\nEl nombre solo debe contener letras");
	    } catch (CharacterException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nLa contraseña debe tener al menos 8 caracteres");
	    } catch (NumberException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nLa contraseña debe contener al menos un número");
	    } catch (SymbolException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nLa contraseña debe tener al menos un símbolo especial");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("500\nError interno del servidor");
	    }
	}

	@GetMapping("/listar")
	public ResponseEntity<MyLinkedList<JugadorDTO>> listarJugadores() {
		MyLinkedList<JugadorDTO> jugadores = jugadorService.getAll();
		return ResponseEntity.ok(jugadores);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		int r = jugadorService.deleteById(id);
		if (r == 0)
			return ResponseEntity.ok("Jugador eliminado");
		return ResponseEntity.status(404).body("Jugador no encontrado");
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody JugadorDTO dto) {
		int r = jugadorService.updateById(id, dto);
		if (r == 0)
			return ResponseEntity.ok("Jugador actualizado");
		return ResponseEntity.status(404).body("Jugador no encontrado");
	}

	@GetMapping("/count")
	public ResponseEntity<Long> count() {
		return ResponseEntity.ok(jugadorService.count());
	}

	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> exists(@PathVariable Long id) {
		return ResponseEntity.ok(jugadorService.exist(id));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String correo, @RequestParam String contrasena) {
	    try {
	        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("400\nEl correo y la contraseña son obligatorios");
	        }

	        ExceptionCheker.checkerMail(correo);

	        JugadorDTO jugador = jugadorService.findByCorreoAndContrasena(correo, contrasena);

	        if (jugador != null) {
	            return ResponseEntity.ok("Login exitoso");
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("401\nCorreo o contraseña incorrectos");
	        }
	    } catch (MailException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nCorreo inválido. Use gmail, hotmail, outlook o unbosque.edu.co");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("500\nError interno del servidor");
	    }
	}

	
	// ✅ Agregar tropas
	@PutMapping("/{id}/agregar-tropas/{cantidad}")
	public ResponseEntity<?> agregarTropas(@PathVariable Long id, @PathVariable int cantidad) {
		jugadorService.agregarTropas(id, cantidad);
		return ResponseEntity.ok("Tropas agregadas");
	}

	// ✅ Quitar tropas
	@PutMapping("/{id}/quitar-tropas/{cantidad}")
	public ResponseEntity<?> quitarTropas(@PathVariable Long id, @PathVariable int cantidad) {
		jugadorService.quitarTropas(id, cantidad);
		return ResponseEntity.ok("Tropas quitadas");
	}

	// ✅ Reset tropas disponibles
	@PutMapping("/{id}/reset-tropas")
	public ResponseEntity<?> resetTropas(@PathVariable Long id) {
		jugadorService.resetTropas(id);
		return ResponseEntity.ok("Tropas reseteadas");
	}

	// ✅ Dar carta
	@PostMapping("/{id}/dar-carta")
	public ResponseEntity<?> darCarta(@PathVariable Long id, @RequestBody Carta carta) {
		jugadorService.darCarta(id, carta);
		return ResponseEntity.ok("Carta añadida");
	}

	// ✅ Quitar carta por índice
	@DeleteMapping("/{id}/quitar-carta/{index}")
	public ResponseEntity<?> quitarCarta(@PathVariable Long id, @PathVariable int index) {
		jugadorService.quitarCarta(id, index);
		return ResponseEntity.ok("Carta removida");
	}

	// ✅ Desactivar jugador
	@PutMapping("/{id}/desactivar")
	public ResponseEntity<?> desactivarJugador(@PathVariable Long id) {
		jugadorService.desactivarJugador(id);
		return ResponseEntity.ok("Jugador desactivado");
	}

	// ✅ Activar jugador
	@PutMapping("/{id}/activar")
	public ResponseEntity<?> activarJugador(@PathVariable Long id) {
		jugadorService.activarJugador(id);
		return ResponseEntity.ok("Jugador activado");
	}

	@PutMapping("/{id}/reset")
	public ResponseEntity<?> resetJugador(@PathVariable Long id) {
		jugadorService.resetJugadorPorId(id);
		return ResponseEntity.ok("Jugador reseteado");
	}

}
