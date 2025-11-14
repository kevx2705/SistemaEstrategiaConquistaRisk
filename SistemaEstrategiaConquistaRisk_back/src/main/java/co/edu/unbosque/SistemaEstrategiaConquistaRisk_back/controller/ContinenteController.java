package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ContinenteDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Continente;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.ContinenteService;

/**
 * Controlador REST para la gestión de continentes en el juego Risk.
 * <p>
 * Proporciona endpoints para listar y obtener información sobre los continentes disponibles.
 * </p>
 */
@RestController
@RequestMapping("/api/continentes")
public class ContinenteController {

    /** Servicio para la gestión de continentes. */
    @Autowired
    private ContinenteService continenteService;

    /**
     * Constructor por defecto de la clase {@code ContinenteController}.
     * Inicializa una instancia del controlador para gestionar continentes.
     */
    public ContinenteController() {
        // Constructor por defecto
    }

    /**
     * Lista todos los continentes disponibles en el sistema.
     *
     * @return {@code ResponseEntity<MyLinkedList<ContinenteDTO>>} con la lista de continentes.
     *         Retorna {@code 200 OK} con la lista de continentes en formato DTO.
     */
    @GetMapping
    public ResponseEntity<MyLinkedList<ContinenteDTO>> listar() {
        return ResponseEntity.ok(continenteService.obtenerTodos());
    }
}
