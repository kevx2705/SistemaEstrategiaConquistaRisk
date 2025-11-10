package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ContinenteDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Continente;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.ContinenteService;

@RestController
@RequestMapping("/api/continentes")
public class ContinenteController {

	@Autowired
	private ContinenteService continenteService;

	@GetMapping
	public ResponseEntity<MyLinkedList<ContinenteDTO>> listar() {
		return ResponseEntity.ok(continenteService.obtenerTodos());
	}
}
