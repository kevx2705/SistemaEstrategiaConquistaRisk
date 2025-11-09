package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.CartaDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.Node;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.JugadorRepository;

@Service
public class JugadorService {

	@Autowired
	private JugadorRepository jugadorRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TerritorioService territorioService;


	public JugadorService() {
	}

	// ==========================================================
	// ✅ CRUD NORMAL (igual al AdminService)
	// ==========================================================

	public int create(JugadorDTO newData) {
		Jugador entity = modelMapper.map(newData, Jugador.class);
		jugadorRepo.save(entity);
		return 0;
	}

	public Jugador obtenerJugadorPorId(Long idJugador) {
		return jugadorRepo.findById(idJugador).orElse(null);
	}

	public MyLinkedList<JugadorDTO> getAll() {
		MyLinkedList<JugadorDTO> lista = new MyLinkedList<>();

		for (Jugador entity : jugadorRepo.findAll()) {
			lista.addLast(modelMapper.map(entity, JugadorDTO.class));
		}
		return lista;
	}

	public int deleteById(Long id) {
		if (jugadorRepo.existsById(id)) {
			jugadorRepo.deleteById(id);
			return 0;
		}
		return 1;
	}

	public int updateById(Long id, JugadorDTO newData) {
		Optional<Jugador> opt = jugadorRepo.findById(id);

		if (opt.isPresent()) {
			Jugador entity = opt.get();

			entity.setNombre(newData.getNombre());
			entity.setCorreo(newData.getCorreo());
			entity.setColor(newData.getColor());
			entity.setTropasDisponibles(newData.getTropasDisponibles());
			entity.setTerritoriosControlados(newData.getTerritoriosControlados());
			entity.setActivo(newData.isActivo());

			// --- Convertir CartaDTO a Carta ---
			MyLinkedList<Carta> cartasConvertidas = new MyLinkedList<>();
			if (newData.getCartas() != null) {
				Node<CartaDTO> nodo = newData.getCartas().getFirst();
				while (nodo != null) {
					CartaDTO dto = nodo.getInfo();
					Carta carta = new Carta();
					carta.setId(dto.getId()); // si tienes id en DTO
					carta.setTipo(dto.getTipo());
					carta.setDisponible(dto.isDisponible());
					cartasConvertidas.add(carta);
					nodo = nodo.getNext();
				}
			}
			entity.setCartas(cartasConvertidas);

			jugadorRepo.save(entity);
			return 0;
		}

		return 1;
	}

	public Long count() {
		return (long) getAll().size();
	}

	public boolean exist(Long id) {
		return jugadorRepo.existsById(id);
	}

	public Jugador crearJugadorTemporal(String nombre, String color) {
		Jugador j = new Jugador();
		j.setNombre(nombre);
		j.setColor(color);
		j.setActivo(true);
		jugadorRepo.save(j);
		return j;
	}

	public boolean jugadorControlaContinente(Long idContinente, Long idJugador,
			MyLinkedList<TerritorioDTO> todosTerritorios) {

		boolean controla = true;

		for (int i = 0; i < todosTerritorios.size(); i++) {
			TerritorioDTO t = todosTerritorios.getPos(i).getInfo(); // ✅ CORRECTO

			if (t.getIdContinente().equals(idContinente)) {
				if (!t.getIdJugador().equals(idJugador)) {
					controla = false;
					break;
				}
			}
		}

		return controla;
	}

	// ==========================================================
	// ✅ FUNCIONES ESPECIALES PARA EL JUEGO RISK
	// ==========================================================

	// ✅ Añadir tropas al jugador
	public void agregarTropas(Long idJugador, int cantidad) {
		Jugador j = jugadorRepo.findById(idJugador).orElse(null);
		if (j != null) {
			j.setTropasDisponibles(j.getTropasDisponibles() + cantidad);
			jugadorRepo.save(j);
		}
	}

	public void agregarTerritorio(Long idJugador) {
		Jugador j = jugadorRepo.findById(idJugador).orElse(null);
		if (j != null) {
			j.setTerritoriosControlados(j.getTerritoriosControlados() + 1);
			jugadorRepo.save(j);
		}
	}

	// Colocar tropas en un territorio específico
	public void colocarTropasEnTerritorio(Long idJugador, Long idTerritorio, int cantidad) {
		if (cantidad <= 0)
			return;

		Jugador jugador = jugadorRepo.findById(idJugador).orElse(null);
		if (jugador == null)
			return;

		if (jugador.getTropasDisponibles() < cantidad) {
			throw new RuntimeException("No tienes suficientes tropas disponibles");
		}

		// Validar que el territorio sea del jugador
		TerritorioDTO territorio = territorioService.obtenerPorId(idTerritorio);
		if (!idJugador.equals(territorio.getIdJugador())) {
			throw new RuntimeException("No puedes colocar tropas en un territorio que no controlas");
		}

		// Colocar tropas
		territorioService.reforzar(idTerritorio, cantidad);

		// Restar tropas disponibles
		jugador.setTropasDisponibles(jugador.getTropasDisponibles() - cantidad);
		jugadorRepo.save(jugador);
	}

	// ✅ Quitar tropas disponibles
	public void quitarTropas(Long idJugador, int cantidad) {
		Jugador j = jugadorRepo.findById(idJugador).orElse(null);
		if (j != null) {
			int nuevas = Math.max(0, j.getTropasDisponibles() - cantidad);
			j.setTropasDisponibles(nuevas);
			jugadorRepo.save(j);
		}
	}

	// ✅ Reiniciar tropas disponibles a 0
	public void resetTropas(Long idJugador) {
		Jugador j = jugadorRepo.findById(idJugador).orElse(null);
		if (j != null) {
			j.setTropasDisponibles(0);
			jugadorRepo.save(j);
		}
	}

	// ✅ Dar una carta al jugador
	public void darCarta(Long idJugador, Carta carta) {
		Jugador j = jugadorRepo.findById(idJugador).orElse(null);
		if (j != null) {
			j.getCartas().addLast(carta);
			jugadorRepo.save(j);
		}
	}

	// ✅ Quitar una carta por índice
	public void quitarCarta(Long idJugador, int index) {
		Jugador j = jugadorRepo.findById(idJugador).orElse(null);
		if (j != null) {
			if (index >= 0 && index < j.getCartas().size()) {
				j.getCartas().deleteAt(index);
				jugadorRepo.save(j);
			}
		}
	}

	// ✅ Desactivar jugador (cuando pierde)
	public void desactivarJugador(Long idJugador) {
		Jugador j = jugadorRepo.findById(idJugador).orElse(null);
		if (j != null) {
			j.setActivo(false);
			jugadorRepo.save(j);
		}
	}

	// ✅ Activar jugador (nueva partida)
	public void activarJugador(Long idJugador) {
		Jugador j = jugadorRepo.findById(idJugador).orElse(null);
		if (j != null) {
			j.setActivo(true);
			jugadorRepo.save(j);
		}
	}

	// ✅ Reset general (para iniciar nueva partida)
	public void resetJugadorPorId(Long id) {
		Jugador j = jugadorRepo.findById(id).orElse(null);

		if (j != null) {
			j.setTropasDisponibles(0);
			j.setTerritoriosControlados(0);
			j.setActivo(true);
			j.setCartas(new MyLinkedList<>());
			jugadorRepo.save(j);
		}
	}

}
