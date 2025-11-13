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

/**
 * Controlador REST para la gestión de jugadores.
 * Proporciona endpoints para crear, listar, actualizar, eliminar y gestionar jugadores,
 * así como manejar sus tropas y cartas.
 */
@RestController
@RequestMapping("/jugadores")
@CrossOrigin(origins = "*")
public class JugadorController {

    /** Servicio para la gestión de jugadores. */
    @Autowired
    private JugadorService jugadorService;

    /** Servicio para el envío de correos electrónicos. */
    @Autowired
    private EmailService emailService;

    /**
     * Crea un nuevo jugador con los datos proporcionados.
     *
     * @param nombre     Nombre del jugador.
     * @param correo     Correo electrónico del jugador.
     * @param contrasena Contraseña del jugador.
     * @return ResponseEntity con un mensaje de éxito o error.
     *         Códigos de estado posibles: 201 (creado), 406 (datos no aceptables), 500 (error interno).
     * @throws MailException       Si el correo no es válido.
     * @throws TextException       Si el nombre contiene caracteres no permitidos.
     * @throws CharacterException  Si la contraseña no cumple con la longitud mínima.
     * @throws NumberException     Si la contraseña no contiene números.
     * @throws SymbolException     Si la contraseña no contiene símbolos especiales.
     */
    @PostMapping(path = "/crear")
    public ResponseEntity<String> crearJugador(
            @RequestParam String nombre,
            @RequestParam String correo,
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
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("406\nEl nombre solo debe contener letras");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("500\nError interno del servidor");
        }
    }

    /**
     * Lista todos los jugadores registrados.
     *
     * @return ResponseEntity con la lista de jugadores.
     */
    @GetMapping("/listar")
    public ResponseEntity<MyLinkedList<JugadorDTO>> listarJugadores() {
        MyLinkedList<JugadorDTO> jugadores = jugadorService.getAll();
        return ResponseEntity.ok(jugadores);
    }

    /**
     * Elimina un jugador por su identificador.
     *
     * @param id Identificador del jugador a eliminar.
     * @return ResponseEntity con un mensaje de éxito o error.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        int r = jugadorService.deleteById(id);
        if (r == 0)
            return ResponseEntity.ok("Jugador eliminado");
        return ResponseEntity.status(404).body("Jugador no encontrado");
    }

    /**
     * Actualiza los datos de un jugador.
     *
     * @param id  Identificador del jugador a actualizar.
     * @param dto Objeto JugadorDTO con los nuevos datos.
     * @return ResponseEntity con un mensaje de éxito o error.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody JugadorDTO dto) {
        int r = jugadorService.updateById(id, dto);
        if (r == 0)
            return ResponseEntity.ok("Jugador actualizado");
        return ResponseEntity.status(404).body("Jugador no encontrado");
    }

    /**
     * Cuenta la cantidad total de jugadores registrados.
     *
     * @return ResponseEntity con el número total de jugadores.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(jugadorService.count());
    }

    /**
     * Verifica si existe un jugador con el identificador proporcionado.
     *
     * @param id Identificador del jugador.
     * @return ResponseEntity con el resultado de la verificación.
     */
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.exist(id));
    }

    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param correo     Correo electrónico del jugador.
     * @param contrasena Contraseña del jugador.
     * @return ResponseEntity con un mensaje de éxito o error.
     * @throws MailException Si el correo no es válido.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String correo, @RequestParam String contrasena) {
        JugadorDTO jugador = jugadorService.findByCorreoAndContrasena(correo, contrasena);

        if (jugador != null) {
            return ResponseEntity.ok(jugador.getId());  
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos");
        }
    }


    /**
     * Agrega tropas a un jugador.
     *
     * @param id       Identificador del jugador.
     * @param cantidad Cantidad de tropas a agregar.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @PutMapping("/{id}/agregar-tropas/{cantidad}")
    public ResponseEntity<?> agregarTropas(@PathVariable Long id, @PathVariable int cantidad) {
        jugadorService.agregarTropas(id, cantidad);
        return ResponseEntity.ok("Tropas agregadas");
    }

    /**
     * Quita tropas a un jugador.
     *
     * @param id       Identificador del jugador.
     * @param cantidad Cantidad de tropas a quitar.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @PutMapping("/{id}/quitar-tropas/{cantidad}")
    public ResponseEntity<?> quitarTropas(@PathVariable Long id, @PathVariable int cantidad) {
        jugadorService.quitarTropas(id, cantidad);
        return ResponseEntity.ok("Tropas quitadas");
    }

    /**
     * Resetea las tropas de un jugador a cero.
     *
     * @param id Identificador del jugador.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @PutMapping("/{id}/reset-tropas")
    public ResponseEntity<?> resetTropas(@PathVariable Long id) {
        jugadorService.resetTropas(id);
        return ResponseEntity.ok("Tropas reseteadas");
    }

    /**
     * Asigna una carta a un jugador.
     *
     * @param id    Identificador del jugador.
     * @param carta Objeto Carta a asignar.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @PostMapping("/{id}/dar-carta")
    public ResponseEntity<?> darCarta(@PathVariable Long id, @RequestBody Carta carta) {
        jugadorService.darCarta(id, carta);
        return ResponseEntity.ok("Carta añadida");
    }

    /**
     * Quita una carta de un jugador según su índice.
     *
     * @param id    Identificador del jugador.
     * @param index Índice de la carta a quitar.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @DeleteMapping("/{id}/quitar-carta/{index}")
    public ResponseEntity<?> quitarCarta(@PathVariable Long id, @PathVariable int index) {
        jugadorService.quitarCarta(id, index);
        return ResponseEntity.ok("Carta removida");
    }

    /**
     * Desactiva un jugador.
     *
     * @param id Identificador del jugador.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarJugador(@PathVariable Long id) {
        jugadorService.desactivarJugador(id);
        return ResponseEntity.ok("Jugador desactivado");
    }

    /**
     * Activa un jugador.
     *
     * @param id Identificador del jugador.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarJugador(@PathVariable Long id) {
        jugadorService.activarJugador(id);
        return ResponseEntity.ok("Jugador activado");
    }

    /**
     * Resetea todos los datos de un jugador.
     *
     * @param id Identificador del jugador.
     * @return ResponseEntity con un mensaje de éxito.
     */
    @PutMapping("/{id}/reset")
    public ResponseEntity<?> resetJugador(@PathVariable Long id) {
        jugadorService.resetJugadorPorId(id);
        return ResponseEntity.ok("Jugador reseteado");
    }
}
