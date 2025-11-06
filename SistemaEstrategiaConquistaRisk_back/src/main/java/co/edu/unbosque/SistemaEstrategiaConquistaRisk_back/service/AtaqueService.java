package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ResultadoAtaqueDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.CartaDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.Node;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.PartidaRepository;

@Service
public class AtaqueService {

	@Autowired
	private PartidaRepository partidaRepository;

	@Autowired
	private CartaService cartaService;

	@Autowired
	private MapaTerritorio mapa;

	@Autowired
	private ModelMapper modelMapper;

	private final Gson gson = new Gson();

	// ============================================================
	// MÉTODO PRINCIPAL DE ATAQUE
	// ============================================================
	public ResultadoAtaqueDTO atacar(Long partidaId, Long atacanteId, Long territorioAtacanteId,
			Long territorioDefensorId, int dadosAtacante, int dadosDefensor) {

// -------------------------------
// 1. Cargar partida
// -------------------------------
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("La partida no existe"));

// -------------------------------
// 2. Reconstruir territorios y jugadores desde JSON
// -------------------------------
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(),
				new TypeToken<MyLinkedList<TerritorioDTO>>() {
				}.getType());

		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<JugadorDTO>>() {
				}.getType());

// -------------------------------
// 3. Obtener territorios y jugador actual
// -------------------------------
		Node<TerritorioDTO> nodoAtacante = getNodoPorId(territorios, territorioAtacanteId);
		Node<TerritorioDTO> nodoDefensor = getNodoPorId(territorios, territorioDefensorId);
		Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, atacanteId);

		if (nodoAtacante == null || nodoDefensor == null || nodoJugador == null)
			throw new RuntimeException("Datos inválidos");

		TerritorioDTO terrAtacante = nodoAtacante.getInfo();
		TerritorioDTO terrDefensor = nodoDefensor.getInfo();
		JugadorDTO jugador = nodoJugador.getInfo();

// -------------------------------
// 4. Validaciones
// -------------------------------
		if (!terrAtacante.getIdJugador().equals(atacanteId))
			throw new RuntimeException("No eres dueño del territorio atacante");
		if (terrAtacante.getTropas() <= 1)
			throw new RuntimeException("No tienes tropas suficientes para atacar");
		if (!mapa.sonVecinos(territorioAtacanteId, territorioDefensorId))
			throw new RuntimeException("Los territorios no son adyacentes");

// -------------------------------
// 5. Lanzar dados
// -------------------------------
		int[] dadosA = lanzarDados(dadosAtacante);
		int[] dadosD = lanzarDados(dadosDefensor);

		ordenarDesc(dadosA);
		ordenarDesc(dadosD);

// -------------------------------
// 6. Resolver enfrentamiento
// -------------------------------
		int comparaciones = Math.min(dadosA.length, dadosD.length);
		int bajasAtacante = 0;
		int bajasDefensor = 0;

		for (int i = 0; i < comparaciones; i++) {
			if (dadosA[i] > dadosD[i]) {
				terrDefensor.setTropas(terrDefensor.getTropas() - 1);
				bajasDefensor++;
			} else {
				terrAtacante.setTropas(terrAtacante.getTropas() - 1);
				bajasAtacante++;
			}
		}

// -------------------------------
// 7. Si el defensor queda sin tropas → conquista
// -------------------------------
		boolean conquista = false;
		if (terrDefensor.getTropas() <= 0) {
			terrDefensor.setIdJugador(atacanteId);

// Mover al menos 1 tropa del atacante al nuevo territorio
			int moverTropas = Math.max(1, dadosAtacante - bajasAtacante);
			terrDefensor.setTropas(moverTropas);
			terrAtacante.setTropas(terrAtacante.getTropas() - moverTropas);

// Actualizar territorios controlados
			jugador.setTerritoriosControlados(jugador.getTerritoriosControlados() + 1);
			conquista = true;

// -------------------------------
// 7b. Robar carta tras conquista
// -------------------------------
			CartaDTO nuevaCarta = cartaService.robarCarta();
			if (nuevaCarta != null) {
				if (jugador.getCartas() == null) {
					jugador.setCartas(new MyLinkedList<>());
				}
				jugador.getCartas().addLast(modelMapper.map(nuevaCarta, Carta.class));
			}
		}

// -------------------------------
// 8. Guardar cambios en la partida
// -------------------------------
		partida.setTerritoriosJSON(gson.toJson(territorios));
		partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores));
		partidaRepository.save(partida);

// -------------------------------
// 9. Construir DTO de resultado
// -------------------------------
		ResultadoAtaqueDTO resultado = new ResultadoAtaqueDTO();
		resultado.setDadosAtacante(dadosA);
		resultado.setDadosDefensor(dadosD);
		resultado.setPerdidasAtacante(bajasAtacante);
		resultado.setPerdidasDefensor(bajasDefensor);
		resultado.setConquista(conquista);
		resultado.setNuevoDueno(terrDefensor.getIdJugador());
		resultado.setTropasAtacanteRestantes(terrAtacante.getTropas());
		resultado.setTropasDefensorRestantes(terrDefensor.getTropas());

		return resultado;
	}

	// ============================================================
	// MÉTODOS AUXILIARES
	// ============================================================

	private int[] lanzarDados(int n) {
		int[] r = new int[n];
		for (int i = 0; i < n; i++)
			r[i] = (int) (Math.random() * 6) + 1;
		return r;
	}

	private void ordenarDesc(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[j] > arr[i]) {
					int tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
				}
			}
		}
	}

	// Obtener nodo por id en MyLinkedList
	private <E> Node<E> getNodoPorId(MyLinkedList<E> lista, Long id) {
		Node<E> current = lista.getFirst();
		while (current != null) {
			E info = current.getInfo();
			if (info instanceof TerritorioDTO && ((TerritorioDTO) info).getId().equals(id))
				return current;
			if (info instanceof JugadorDTO && ((JugadorDTO) info).getId().equals(id))
				return current;
			current = current.getNext();
		}
		return null;
	}
}
