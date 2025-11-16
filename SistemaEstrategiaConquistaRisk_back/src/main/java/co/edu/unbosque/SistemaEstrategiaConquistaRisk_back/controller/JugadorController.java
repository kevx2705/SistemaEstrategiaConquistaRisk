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
 * Controlador REST para la gestión de jugadores en el sistema.
 * Proporciona endpoints para crear, listar, actualizar, eliminar y gestionar
 * jugadores, así como manejar sus tropas y cartas.
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
     * @param nombre     Nombre del jugador. Solo debe contener letras.
     * @param correo     Correo electrónico del jugador. Debe ser un correo válido.
     * @param contrasena Contraseña del jugador. Debe cumplir con los requisitos de seguridad:
     *                   al menos 8 caracteres, un número y un símbolo especial.
     * @return {@code ResponseEntity<String>} con un mensaje de éxito o error.
     *         Códigos de estado posibles:
     *         <ul>
     *           <li>{@code 201} si el jugador fue creado con éxito.</li>
     *           <li>{@code 406} si los datos no son aceptables (nombre en uso, correo en uso, datos inválidos).</li>
     *           <li>{@code 500} si ocurre un error interno del servidor.</li>
     *         </ul>
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
     * Lista todos los jugadores registrados en el sistema.
     *
     * @return {@code ResponseEntity<MyLinkedList<JugadorDTO>>} con la lista de jugadores.
     *         El cuerpo de la respuesta contiene una lista enlazada de objetos {@link JugadorDTO}.
     */
    @GetMapping("/listar")
    public ResponseEntity<MyLinkedList<JugadorDTO>> listarJugadores() {
        MyLinkedList<JugadorDTO> jugadores = jugadorService.getAll();
        return ResponseEntity.ok(jugadores);
    }
    @GetMapping("/{id}/obtenerjugadorporid")
    public ResponseEntity<?> obtenerJugadorPorId(@PathVariable Long id) {

        JugadorDTO dto = jugadorService.obtenerJugadorPorIdDTO(id);

        if (dto == null) {
            return ResponseEntity.status(404)
                    .body("No existe el jugador con id " + id);
        }

        return ResponseEntity.ok(dto);
    }
    /**
     * Elimina un jugador por su identificador.
     *
     * @param id Identificador único del jugador a eliminar.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito o error.
     *         Retorna {@code 200 OK} si el jugador fue eliminado con éxito,
     *         o {@code 404 Not Found} si el jugador no fue encontrado.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        int r = jugadorService.deleteById(id);
        if (r == 0)
            return ResponseEntity.ok("Jugador eliminado");
        return ResponseEntity.status(404).body("Jugador no encontrado");
    }

    /**
     * Actualiza los datos de un jugador existente.
     *
     * @param id  Identificador único del jugador a actualizar.
     * @param dto Objeto {@link JugadorDTO} con los nuevos datos del jugador.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito o error.
     *         Retorna {@code 200 OK} si el jugador fue actualizado con éxito,
     *         o {@code 404 Not Found} si el jugador no fue encontrado.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody JugadorDTO dto) {
        int r = jugadorService.updateById(id, dto);
        if (r == 0)
            return ResponseEntity.ok("Jugador actualizado");
        return ResponseEntity.status(404).body("Jugador no encontrado");
    }

    /**
     * Cuenta la cantidad total de jugadores registrados en el sistema.
     *
     * @return {@code ResponseEntity<Long>} con el número total de jugadores.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(jugadorService.count());
    }

    /**
     * Verifica si existe un jugador con el identificador proporcionado.
     *
     * @param id Identificador único del jugador.
     * @return {@code ResponseEntity<Boolean>} con el resultado de la verificación.
     *         Retorna {@code true} si el jugador existe, o {@code false} en caso contrario.
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
     * @return {@code ResponseEntity<?>} con el identificador del jugador si las credenciales son correctas,
     *         o un mensaje de error si no lo son.
     *         Retorna {@code 200 OK} si el inicio de sesión es exitoso,
     *         o {@code 401 Unauthorized} si las credenciales son incorrectas.
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
     * Agrega una cantidad específica de tropas a un jugador.
     *
     * @param id       Identificador único del jugador.
     * @param cantidad Cantidad de tropas a agregar. Debe ser un número positivo.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito.
     */
    @PutMapping("/{id}/agregar-tropas/{cantidad}")
    public ResponseEntity<?> agregarTropas(@PathVariable Long id, @PathVariable int cantidad) {
        jugadorService.agregarTropas(id, cantidad);
        return ResponseEntity.ok("Tropas agregadas");
    }

    /**
     * Quita una cantidad específica de tropas a un jugador.
     *
     * @param id       Identificador único del jugador.
     * @param cantidad Cantidad de tropas a quitar. Debe ser un número positivo y no exceder las tropas actuales del jugador.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito.
     */
    @PutMapping("/{id}/quitar-tropas/{cantidad}")
    public ResponseEntity<?> quitarTropas(@PathVariable Long id, @PathVariable int cantidad) {
        jugadorService.quitarTropas(id, cantidad);
        return ResponseEntity.ok("Tropas quitadas");
    }

    /**
     * Resetea las tropas de un jugador a cero.
     *
     * @param id Identificador único del jugador.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito.
     */
    @PutMapping("/{id}/reset-tropas")
    public ResponseEntity<?> resetTropas(@PathVariable Long id) {
        jugadorService.resetTropas(id);
        return ResponseEntity.ok("Tropas reseteadas");
    }

    /**
     * Asigna una carta a un jugador.
     *
     * @param id    Identificador único del jugador.
     * @param carta Objeto {@link Carta} a asignar al jugador.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito.
     */
    @PostMapping("/{id}/dar-carta")
    public ResponseEntity<?> darCarta(@PathVariable Long id, @RequestBody Carta carta) {
        jugadorService.darCarta(id, carta);
        return ResponseEntity.ok("Carta añadida");
    }

    /**
     * Quita una carta de un jugador según su índice en la lista de cartas.
     *
     * @param id    Identificador único del jugador.
     * @param index Índice de la carta a quitar en la lista de cartas del jugador.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito.
     */
    @DeleteMapping("/{id}/quitar-carta/{index}")
    public ResponseEntity<?> quitarCarta(@PathVariable Long id, @PathVariable int index) {
        jugadorService.quitarCarta(id, index);
        return ResponseEntity.ok("Carta removida");
    }

    /**
     * Desactiva un jugador, impidiendo que pueda participar en nuevas partidas.
     *
     * @param id Identificador único del jugador.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito.
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarJugador(@PathVariable Long id) {
        jugadorService.desactivarJugador(id);
        return ResponseEntity.ok("Jugador desactivado");
    }

    /**
     * Activa un jugador previamente desactivado.
     *
     * @param id Identificador único del jugador.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito.
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarJugador(@PathVariable Long id) {
        jugadorService.activarJugador(id);
        return ResponseEntity.ok("Jugador activado");
    }

    /**
     * Resetea todos los datos de un jugador a sus valores iniciales.
     * Esto incluye tropas, cartas y cualquier otro atributo modificable.
     *
     * @param id Identificador único del jugador.
     * @return {@code ResponseEntity<?>} con un mensaje de éxito.
     */
    @PutMapping("/{id}/reset")
    public ResponseEntity<?> resetJugador(@PathVariable Long id) {
        jugadorService.resetJugadorPorId(id);
        return ResponseEntity.ok("Jugador reseteado");
    }
}
