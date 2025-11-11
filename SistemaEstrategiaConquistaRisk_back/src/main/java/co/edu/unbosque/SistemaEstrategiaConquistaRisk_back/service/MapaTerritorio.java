package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.Edge;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.Graph;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.Node;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.Vertex;
import jakarta.annotation.PostConstruct;

@Service
public class MapaTerritorio {

	private Graph grafo = new Graph();
	
	@Autowired
	private TerritorioService territorioService;
	
	public MapaTerritorio() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void inicializarMapa() {
		inicializarVertices();
		inicializarAristas();
	}

	// ============================================================
	// 1. Cargar todos los territorios como vértices
	// ============================================================
	private void inicializarVertices() {
		MyLinkedList<TerritorioDTO> territorios = territorioService.obtenerTodos();

		Node<TerritorioDTO> n = territorios.getFirst();
		while (n != null) {

			// ✅ Vertex SOLO es genérico — tus reglas se respetan
			Vertex<TerritorioDTO> v = new Vertex<>(n.getInfo());
			grafo.addVertex(v);

			n = n.getNext();
		}
	}

	// ============================================================
	// 2. Crear aristas según el mapa real de Risk
	// ============================================================
	private void inicializarAristas() {
		conectar("Alaska", "Alberta");
		conectar("Alaska", "Kamchatka");

		conectar("Alberta", "Alaska");
		conectar("Alberta", "Ontario");
		conectar("Alberta", "Terranova");
		conectar("Alberta", "Estados Unidos Occidentales");

		conectar("Ontario", "Alberta");
		conectar("Ontario", "Estados Unidos Occidentales");
		conectar("Ontario", "Estados Unidos Orientales");
		conectar("Ontario", "Quebec");
		conectar("Ontario", "Terranova");
		conectar("Ontario", "Groenlandia");

		conectar("Terranova", "Alberta");
		conectar("Terranova", "Ontario");
		conectar("Terranova", "Quebec");
		conectar("Terranova", "Groenlandia");

		conectar("Quebec", "Ontario");
		conectar("Quebec", "Terranova");
		conectar("Quebec", "Estados Unidos Orientales");

		conectar("Estados Unidos Occidentales", "Alberta");
		conectar("Estados Unidos Occidentales", "Ontario");
		conectar("Estados Unidos Occidentales", "Estados Unidos Orientales");
		conectar("Estados Unidos Occidentales", "América Central");

		conectar("Estados Unidos Orientales", "Estados Unidos Occidentales");
		conectar("Estados Unidos Orientales", "Ontario");
		conectar("Estados Unidos Orientales", "Quebec");
		conectar("Estados Unidos Orientales", "América Central");

		conectar("América Central", "Estados Unidos Orientales");
		conectar("América Central", "Estados Unidos Occidentales");
		conectar("América Central", "Venezuela");

		conectar("Groenlandia", "Terranova");
		conectar("Groenlandia", "Ontario");
		conectar("Groenlandia", "Quebec");
		conectar("Groenlandia", "Islandia");
		conectar("Islandia", "Groenlandia");
		conectar("Islandia", "Gran Bretaña");
		conectar("Islandia", "Escandinavia");

		conectar("Gran Bretaña", "Islandia");
		conectar("Gran Bretaña", "Escandinavia");
		conectar("Gran Bretaña", "Europa Occidental");
		conectar("Gran Bretaña", "Europa del Norte");

		conectar("Escandinavia", "Islandia");
		conectar("Escandinavia", "Gran Bretaña");
		conectar("Escandinavia", "Europa del Norte");
		conectar("Escandinavia", "Ucrania");

		conectar("Europa del Norte", "Gran Bretaña");
		conectar("Europa del Norte", "Escandinavia");
		conectar("Europa del Norte", "Europa Occidental");
		conectar("Europa del Norte", "Europa del Sur");
		conectar("Europa del Norte", "Ucrania");

		conectar("Europa Occidental", "Gran Bretaña");
		conectar("Europa Occidental", "Europa del Norte");
		conectar("Europa Occidental", "Europa del Sur");
		conectar("Europa Occidental", "África del Norte");

		conectar("Europa del Sur", "Europa Occidental");
		conectar("Europa del Sur", "Europa del Norte");
		conectar("Europa del Sur", "Ucrania");
		conectar("Europa del Sur", "Medio Oriente");
		conectar("Europa del Sur", "África del Norte");

		conectar("Ucrania", "Escandinavia");
		conectar("Ucrania", "Europa del Norte");
		conectar("Ucrania", "Europa del Sur");
		conectar("Ucrania", "Ural");
		conectar("Ucrania", "Afganistán");
		conectar("Ucrania", "Medio Oriente");
		conectar("África del Norte", "Brasil");
		conectar("África del Norte", "Europa Occidental");
		conectar("África del Norte", "Europa del Sur");
		conectar("África del Norte", "Egipto");
		conectar("África del Norte", "África Oriental");
		conectar("África del Norte", "Congo");

		conectar("Egipto", "África del Norte");
		conectar("Egipto", "Europa del Sur");
		conectar("Egipto", "Medio Oriente");
		conectar("Egipto", "África Oriental");

		conectar("África Oriental", "Egipto");
		conectar("África Oriental", "África del Norte");
		conectar("África Oriental", "Congo");
		conectar("África Oriental", "Sudáfrica");
		conectar("África Oriental", "Madagascar");

		conectar("Congo", "África del Norte");
		conectar("Congo", "África Oriental");
		conectar("Congo", "Sudáfrica");

		conectar("Sudáfrica", "Congo");
		conectar("Sudáfrica", "África Oriental");
		conectar("Sudáfrica", "Madagascar");

		conectar("Madagascar", "África Oriental");
		conectar("Madagascar", "Sudáfrica");
		conectar("Venezuela", "América Central");
		conectar("Venezuela", "Brasil");
		conectar("Venezuela", "Perú");

		conectar("Brasil", "Venezuela");
		conectar("Brasil", "Perú");
		conectar("Brasil", "Argentina");
		conectar("Brasil", "África del Norte");

		conectar("Perú", "Venezuela");
		conectar("Perú", "Brasil");
		conectar("Perú", "Argentina");

		conectar("Argentina", "Brasil");
		conectar("Argentina", "Perú");
		conectar("Ural", "Ucrania");
		conectar("Ural", "Siberia");
		conectar("Ural", "China");
		conectar("Ural", "Afganistán");

		conectar("Siberia", "Ural");
		conectar("Siberia", "Yakutsk");
		conectar("Siberia", "Irkutsk");
		conectar("Siberia", "Mongolia");
		conectar("Siberia", "China");

		conectar("Yakutsk", "Siberia");
		conectar("Yakutsk", "Irkutsk");
		conectar("Yakutsk", "Kamchatka");

		conectar("Irkutsk", "Siberia");
		conectar("Irkutsk", "Yakutsk");
		conectar("Irkutsk", "Kamchatka");
		conectar("Irkutsk", "Mongolia");

		conectar("Kamchatka", "Alaska");
		conectar("Kamchatka", "Yakutsk");
		conectar("Kamchatka", "Irkutsk");
		conectar("Kamchatka", "Mongolia");
		conectar("Kamchatka", "Japón");

		conectar("Japón", "Kamchatka");
		conectar("Japón", "Mongolia");

		conectar("Mongolia", "Irkutsk");
		conectar("Mongolia", "Siberia");
		conectar("Mongolia", "China");
		conectar("Mongolia", "Japón");
		conectar("Mongolia", "Kamchatka");

		conectar("China", "Ural");
		conectar("China", "Siberia");
		conectar("China", "Mongolia");
		conectar("China", "India");
		conectar("China", "Siam");

		conectar("India", "China");
		conectar("India", "Medio Oriente");
		conectar("India", "Afganistán");
		conectar("India", "Siam");

		conectar("Afganistán", "Ural");
		conectar("Afganistán", "China");
		conectar("Afganistán", "Medio Oriente");
		conectar("Afganistán", "India");
		conectar("Afganistán", "Ucrania");

		conectar("Medio Oriente", "Europa del Sur");
		conectar("Medio Oriente", "Egipto");
		conectar("Medio Oriente", "Afganistán");
		conectar("Medio Oriente", "India");
		conectar("Medio Oriente", "Ucrania");
		conectar("Indonesia", "Siam");
		conectar("Indonesia", "Nueva Guinea");
		conectar("Indonesia", "Australia Occidental");

		conectar("Nueva Guinea", "Indonesia");
		conectar("Nueva Guinea", "Australia Occidental");
		conectar("Nueva Guinea", "Australia Oriental");

		conectar("Australia Occidental", "Indonesia");
		conectar("Australia Occidental", "Nueva Guinea");
		conectar("Australia Occidental", "Australia Oriental");

		conectar("Australia Oriental", "Australia Occidental");
		conectar("Australia Oriental", "Nueva Guinea");

	}

	private void conectar(String nombreA, String nombreB) {
		Vertex<TerritorioDTO> a = findVertexByName(nombreA);
		Vertex<TerritorioDTO> b = findVertexByName(nombreB);

		if (a != null && b != null) {

			// ✅ Graph NO es genérico → insertEdge acepta Vertex sin tipo
			grafo.insertEdge(a, b, 1);
			grafo.insertEdge(b, a, 1);
		}
	}

	// ============================================================
	// 3. Búsqueda de vértices
	// ============================================================

	public Vertex<TerritorioDTO> findVertexById(Long id) {
		Node node = grafo.getListOfNodes().getFirst();

		while (node != null) {

			Vertex v = (Vertex) node.getInfo();
			TerritorioDTO dto = (TerritorioDTO) v.getInfo();

			if (dto.getId().equals(id)) {
				return v;
			}

			node = node.getNext();
		}

		return null;
	}

	private Vertex<TerritorioDTO> findVertexByName(String nombre) {

		Node node = grafo.getListOfNodes().getFirst();

		while (node != null) {

			Vertex<TerritorioDTO> v = (Vertex<TerritorioDTO>) node.getInfo();
			TerritorioDTO dto = v.getInfo();

			if (dto.getNombre().equalsIgnoreCase(nombre)) {
				return v;
			}

			node = node.getNext();
		}
		return null;
	}

	// ============================================================
	// 4. Obtener vecinos (para ataque)
	// ============================================================
	public MyLinkedList<Long> obtenerVecinos(Long id) {
		MyLinkedList<Long> vecinos = new MyLinkedList<>();

		Vertex<TerritorioDTO> v = findVertexById(id);
		if (v == null)
			return vecinos;

		Node<Edge> e = v.getAdyacentEdges().getFirst();
		while (e != null) {

			Vertex destino = e.getInfo().getDestination();
			TerritorioDTO dto = (TerritorioDTO) destino.getInfo();

			vecinos.addLast(dto.getId());

			e = e.getNext();
		}

		return vecinos;
	}

	public boolean sonVecinos(Long idA, Long idB) {
		MyLinkedList<Long> vecinos = obtenerVecinos(idA);

		Node<Long> n = vecinos.getFirst();
		while (n != null) {
			if (n.getInfo().equals(idB))
				return true;
			n = n.getNext();
		}
		return false;
	}

	// ============================================================
	// 5. BFS para verificar camino entre territorios del mismo jugador
	// ============================================================
	public boolean existeCamino(Long inicio, Long fin, Long jugadorId) {

		MyLinkedList<Long> visitados = new MyLinkedList<>();
		MyLinkedList<Long> cola = new MyLinkedList<>();

		cola.addLast(inicio);
		visitados.addLast(inicio);

		while (!cola.isEmpty()) {

			Long actual = cola.extract(); // ✅ AQUÍ VA extract()

			if (actual.equals(fin)) {
				return true;
			}

			MyLinkedList<Long> vecinos = obtenerVecinos(actual);
			Node<Long> n = vecinos.getFirst();

			while (n != null) {
				Long vecino = n.getInfo();

				TerritorioDTO dto = territorioService.obtenerPorId(vecino);

				if (dto != null && dto.getIdJugador().equals(jugadorId) && !visitados.contains(vecino)) {

					visitados.addLast(vecino);
					cola.addLast(vecino);
				}

				n = n.getNext();
			}
		}

		return false;
	}

}
