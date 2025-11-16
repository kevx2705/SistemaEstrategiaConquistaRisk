package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.primefaces.PrimeFaces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import co.edu.unbosque.estructures.MyLinkedList;
import co.edu.unbosque.estructures.Node;
import co.edu.unbosque.model.Carta;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.Partida;
import co.edu.unbosque.model.UsuarioActual;
import co.edu.unbosque.model.persistence.JugadorDTO;
import co.edu.unbosque.model.persistence.TerritorioDTO;
import co.edu.unbosque.util.HttpClientUtil;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 * Bean de sesión que gestiona la creación, inicialización y acciones de una
 * partida. Permite crear partidas, reclamar territorios y navegar al tablero de
 * juego.
 */
@Named("partidabean")
@SessionScoped
public class PartidaBean implements Serializable {

	/**
	 * Versión serial para la serialización de la clase.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Partida actualmente en curso.
	 */
	private Partida partidaActual;

	/**
	 * Lista de nombres de los jugadores en la partida.
	 */
	private MyLinkedList<String> nombresJugadores;

	/**
	 * Código único de la partida.
	 */
	private String codigoPartida;

	/**
	 * ID del jugador anfitrión de la partida.
	 */
	private Long anfitrionId;

	/**
	 * Indica si la partida ha sido iniciada.
	 */
	private boolean partidaIniciada;

	/**
	 * Cantidad de jugadores en la partida.
	 */
	private int cantidadJugadores;

	/**
	 * ID del territorio actualmente seleccionado.
	 */
	private Long territorioSeleccionadoId;
	/**
	 * URL base para las solicitudes al backend relacionadas con partidas.
	 */
	private final String BASE_URL = "http://localhost:8081/partida";
	private MyLinkedList<TerritorioDTO> territoriosDisponibles; // lista original
	private TerritorioDTO[] territoriosDisponiblesArray; // array para el selectOneMenu
	private TerritorioDTO territorioSeleccionado; // objeto seleccionado
	private List<TerritorioDTO> territoriosDisponiblesList;
	private Long jugadorActualId;
	private JugadorDTO jugadorActualDTO;
	private Gson gson =  new Gson();


	/**
	 * Instancia de ObjectMapper para manejar la serialización y deserialización de
	 * JSON.
	 */
	private ObjectMapper mapper = new ObjectMapper();
	private MyLinkedList<Jugador> jugadoresEnPartida;

	/**
	 * Inicializa el bean y prepara las estructuras de datos necesarias.
	 */
	@PostConstruct
	public void init() {
		nombresJugadores = new MyLinkedList<>();
		jugadoresEnPartida = new MyLinkedList<>();
		partidaIniciada = false;
	}

	/** constructor vacio */
	public PartidaBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Crea una nueva partida con la cantidad de jugadores especificada. Valida la
	 * cantidad de jugadores y envía la solicitud al backend.
	 */
	public void crearPartida() {
		try {
			if (cantidadJugadores < 2 || cantidadJugadores > 5) {
				showMessage("Error", "La cantidad de jugadores debe estar entre 2 y 5");
				return;
			}

			Jugador usuarioActual = UsuarioActual.getUsuarioActual();
			if (usuarioActual == null || usuarioActual.getId() == null) {
				showMessage("Error", "No se pudo obtener el ID del usuario actual");
				return;
			}

			this.anfitrionId = usuarioActual.getId();
			System.out.println("ID del anfitrión: " + anfitrionId);

			nombresJugadores.clear();
			for (int i = 1; i <= cantidadJugadores; i++) {
				nombresJugadores.add("Jugador " + i);
			}

			String[] nombresArray = nombresJugadores.toArray(new String[0]);
			String json = mapper.writeValueAsString(nombresArray);
			System.out.println("JSON enviado: " + json);

			String url = BASE_URL + "/crear?anfitrionId=" + anfitrionId;
			String response = HttpClientUtil.post(url, json);

			partidaActual = mapper.readValue(response, Partida.class);
			showMessage("Éxito", "Partida creada con ID: " + partidaActual.getId());
			partidaIniciada = true;

			jugadoresEnPartida = new MyLinkedList<>();

			if (partidaActual.getJugadoresOrdenTurnoJSON() != null) {
				Map<String, Object> mapWrapper = mapper.readValue(partidaActual.getJugadoresOrdenTurnoJSON(),
						new TypeReference<Map<String, Object>>() {
						});

				Object elementsObj = mapWrapper.get("elements");
				if (elementsObj instanceof List) {
					List<?> listaMap = (List<?>) elementsObj;

					for (Object obj : listaMap) {
						if (obj instanceof Map) {
							Map<String, Object> map = (Map<String, Object>) obj;

							Jugador j = new Jugador();
							j.setId(map.get("id") != null ? Long.valueOf(map.get("id").toString()) : null);
							j.setNombre((String) map.get("nombre"));
							j.setColor((String) map.get("color"));
							j.setTropasDisponibles(map.get("tropasDisponibles") != null
									? Integer.parseInt(map.get("tropasDisponibles").toString())
									: 0);
							j.setTerritoriosControlados(map.get("territoriosControlados") != null
									? Integer.parseInt(map.get("territoriosControlados").toString())
									: 0);

							Object cartasObj = map.get("cartas");
							MyLinkedList<Carta> cartas = new MyLinkedList<>();
							if (cartasObj instanceof List) {
								List<?> cartasList = (List<?>) cartasObj;
								for (Object cObj : cartasList) {
									if (cObj instanceof Map) {
										Carta c = mapper.convertValue(cObj, Carta.class);
										cartas.addLast(c);
									}
								}
							}
							j.setCartas(cartas);

							jugadoresEnPartida.addLast(j);
							jugadorActualId = partidaActual.getJugadorActualId();
							System.out.println("Jugador actual id" + partidaActual);
							System.out.println(jugadoresEnPartida.toString());
						}
					}
				}
			}

			inicializarJuego();
			irAlTablero();
			cargarTerritoriosDisponibles();
			cargarJugadorActual();
			obtenerJugadoresDePartida(partidaActual.getId());
			System.out.println(territoriosDisponiblesList);

		} catch (Exception e) {
			showMessage("Error", "No se pudo crear la partida: " + e.getMessage());
			e.printStackTrace();
		}
	}
	public void obtenerJugadoresDePartida(Long partidaId) {
		try {
			if (partidaId == null) {
				showMessage("Error", "ID de partida no válido.");
				return;
			}
			
			String url = BASE_URL + "/" + partidaId + "/jugadores";
			
			String response = HttpClientUtil.get(url);
			
			if (response.contains("Partida no encontrada")) {
				showMessage("Error", "La partida no existe.");
			} else if (response.contains("Error al obtener jugadores")) {
				showMessage("Error", "No se pudieron obtener los jugadores.");
			} else {
				MyLinkedList<JugadorDTO> jugadores = parsearJugadoresDesdeJson(response);
				mostrarJugadoresEnUI(jugadores);
			}
		} catch (Exception e) {
			showMessage("Error", "No se pudieron obtener los jugadores: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public List<Jugador> getJugadoresParaFront() {
		List<Jugador> lista = new ArrayList<>();
		for (int i = 0; i < jugadoresEnPartida.size(); i++) {
			lista.add(jugadoresEnPartida.get(i));
		}
		return lista;
	}

	public Jugador getJugador1() {
		return jugadoresEnPartida.size() > 0 ? jugadoresEnPartida.get(0) : null;
	}

	public Jugador getJugador2() {
		return jugadoresEnPartida.size() > 1 ? jugadoresEnPartida.get(1) : null;
	}

	public Jugador getJugador3() {
		return jugadoresEnPartida.size() > 2 ? jugadoresEnPartida.get(2) : null;
	}

	public Jugador getJugador4() {
		return jugadoresEnPartida.size() > 3 ? jugadoresEnPartida.get(3) : null;
	}

	public Jugador getJugador5() {
		return jugadoresEnPartida.size() > 4 ? jugadoresEnPartida.get(4) : null;
	}
	

	private MyLinkedList<JugadorDTO> parsearJugadoresDesdeJson(String jsonResponse) {
	    return new MyLinkedList<>();
	}

	private void mostrarJugadoresEnUI(MyLinkedList<JugadorDTO> jugadores) {
	    System.out.println("Jugadores obtenidos: " + jugadores);
	}

	/**
	 * Reclama un territorio para un jugador en una partida específica.
	 * 
	 * @param partidaId    ID de la partida.
	 * @param jugadorId    ID del jugador que reclama el territorio.
	 * @param territorioId ID del territorio a reclamar.
	 */
	public void reclamarTerritorio(Long territorioId) {
		try {
			if (partidaActual == null) {
				showMessage("Error", "No hay partida activa.");
				return;
			}

			Long partidaId = partidaActual.getId();
			Long jugadorActualId = obtenerJugadorActual(partidaId);

			if (jugadorActualId == null) {
				showMessage("Error", "No se pudo obtener el jugador actual.");
				return;
			}

			// Debug real
			System.out.println("➡ Jugador actual (backend): " + jugadorActualId);
			System.out.println("➡ Territorio que quiero reclamar: " + territorioId);

			String url = BASE_URL + "/" + partidaId + "/reclamar" + "?jugadorId=" + jugadorActualId + "&territorioId="
					+ territorioId;

			String response = HttpClientUtil.post(url, null);

			// No revises status manualmente. Solo revisa errores controlados.
			if (response.contains("Territorio ya asignado")) {
				showMessage("Error", "Este territorio ya fue reclamado.");
			} else if (response.contains("No es el turno de este jugador")) {
				showMessage("Error", "Debes esperar tu turno.");
			} else if (response.contains("Territorio no encontrado")) {
				showMessage("Error", "Territorio inválido.");
			} else {
				showMessage("Éxito", "Territorio reclamado.");
				cargarTerritoriosDisponibles();
				cargarJugadorActual();
			}

		} catch (Exception e) {
			showMessage("Error", "No se pudo reclamar: " + e.getMessage());
			e.printStackTrace();
		}
	}
	public void cargarJugadorActual() {
	    try {
	        Long jugadorId = obtenerJugadorActual(partidaActual.getId());

	        String url = "http://localhost:8081/jugadores/"+jugadorId+"/obtenerjugadorporid";
	        String json = HttpClientUtil.get(url);

	        jugadorActualDTO = gson.fromJson(json, JugadorDTO.class);
	        System.out.println("id jugador actualdto"+jugadorActualDTO.getId());
	        System.out.println("nombre jugador actual dto"+jugadorActualDTO.getNombre());
	    } catch (Exception e) {
	        e.printStackTrace();
	        showMessage("Error", "No se pudo obtener el jugador actual");
	    }
	}

	/**
	 * Reclama el territorio actualmente seleccionado para el jugador actual.
	 */
	public void reclamarTerritorioSeleccionado() {
		System.out.println("Territorio seleccionado: " + territorioSeleccionadoId);
		reclamarTerritorio(territorioSeleccionadoId);
	}

	/**
	 * Obtiene el ID del anfitrión de una partida específica.
	 * 
	 * @param partidaId ID de la partida.
	 * @return ID del anfitrión, o null si ocurre un error.
	 */
	private Long obtenerAnfitrion(Long partidaId) {
		try {
			String response = HttpClientUtil.get(BASE_URL + "/" + partidaId + "/anfitrion");
			return mapper.readValue(response, Long.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Obtiene el ID del jugador actual de una partida específica.
	 * 
	 * @param partidaId ID de la partida.
	 * @return ID del jugador actual, o null si ocurre un error.
	 */
	public Long obtenerJugadorActual(Long partidaId) {
		try {
			String url = BASE_URL + "/" + partidaId + "/jugador-actual";
			String response = HttpClientUtil.get(url);

			return Long.parseLong(response.trim());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Inicializa el juego para la partida actual.
	 */
	public void inicializarJuego() {
		try {
			if (partidaActual == null) {
				showMessage("Error", "No hay partida activa");
				return;
			}
			HttpClientUtil.post(BASE_URL + "/" + partidaActual.getId() + "/inicializar", null);
			showMessage("Éxito", "La partida ha sido inicializada correctamente");
		} catch (Exception e) {
			showMessage("Error", "No se pudo inicializar: " + e.getMessage());
		}
	}

	public void cargarTerritoriosDisponibles() {
		try {
			if (partidaActual == null) {
				showMessage("Error", "No hay partida activa");
				return;
			}

			String url = BASE_URL + "/" + partidaActual.getId() + "/territorios/disponibles";
			String response = HttpClientUtil.get(url);

			TerritorioDTO[] array = mapper.readValue(response, TerritorioDTO[].class);

			this.territoriosDisponiblesList = Arrays.asList(array);

			System.out.println("Territorios cargados: " + territoriosDisponiblesList.size());

			// Actualizar componente
			PrimeFaces.current().ajax().update("gameform:territoriosJugadorRefuerzos");

		} catch (Exception e) {
			showMessage("Error", "No se pudieron cargar los territorios: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Redirige al tablero de juego.
	 * 
	 * @return Ruta al tablero de juego.
	 */
	public String irAlTablero() {
		return "tablero.xhtml?faces-redirect=true";
	}

	/**
	 * Muestra un mensaje emergente al usuario.
	 * 
	 * @param titulo  Título del mensaje.
	 * @param detalle Contenido del mensaje.
	 */
	private void showMessage(String titulo, String detalle) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalle);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	/**
	 * Obtiene la partida actual.
	 * 
	 * @return Partida actual.
	 */
	public Partida getPartidaActual() {
		return partidaActual;
	}

	/**
	 * Establece la partida actual.
	 * 
	 * @param partidaActual Partida a establecer.
	 */
	public void setPartidaActual(Partida partidaActual) {
		this.partidaActual = partidaActual;
	}

	public MyLinkedList<Jugador> getJugadoresEnPartida() {
		return jugadoresEnPartida;
	}

	public void setJugadoresEnPartida(MyLinkedList<Jugador> jugadoresEnPartida) {
		this.jugadoresEnPartida = jugadoresEnPartida;
	}

	/**
	 * Obtiene la lista de nombres de los jugadores.
	 * 
	 * @return Lista de nombres de jugadores.
	 */
	public MyLinkedList<String> getNombresJugadores() {
		return nombresJugadores;
	}

	/**
	 * Establece la lista de nombres de los jugadores.
	 * 
	 * @param nombresJugadores Lista de nombres de jugadores.
	 */
	public void setNombresJugadores(MyLinkedList<String> nombresJugadores) {
		this.nombresJugadores = nombresJugadores;
	}

	/**
	 * Obtiene el código de la partida.
	 * 
	 * @return Código de la partida.
	 */
	public String getCodigoPartida() {
		return codigoPartida;
	}

	/**
	 * Establece el código de la partida.
	 * 
	 * @param codigoPartida Código de la partida.
	 */
	public void setCodigoPartida(String codigoPartida) {
		this.codigoPartida = codigoPartida;
	}

	/**
	 * Obtiene el ID del anfitrión de la partida.
	 * 
	 * @return ID del anfitrión.
	 */
	public Long getAnfitrionId() {
		return anfitrionId;
	}

	/**
	 * Establece el ID del anfitrión de la partida.
	 * 
	 * @param anfitrionId ID del anfitrión.
	 */
	public void setAnfitrionId(Long anfitrionId) {
		this.anfitrionId = anfitrionId;
	}

	/**
	 * Verifica si la partida ha sido iniciada.
	 * 
	 * @return true si la partida ha sido iniciada, false en caso contrario.
	 */
	public boolean isPartidaIniciada() {
		return partidaIniciada;
	}

	/**
	 * Establece si la partida ha sido iniciada.
	 * 
	 * @param partidaIniciada Estado de la partida.
	 */
	public void setPartidaIniciada(boolean partidaIniciada) {
		this.partidaIniciada = partidaIniciada;
	}

	/**
	 * Obtiene la cantidad de jugadores en la partida.
	 * 
	 * @return Cantidad de jugadores.
	 */
	public int getCantidadJugadores() {
		return cantidadJugadores;
	}

	/**
	 * Establece la cantidad de jugadores en la partida.
	 * 
	 * @param cantidadJugadores Cantidad de jugadores.
	 */
	public void setCantidadJugadores(int cantidadJugadores) {
		this.cantidadJugadores = cantidadJugadores;
	}

	/**
	 * Obtiene el ID del territorio actualmente seleccionado.
	 * 
	 * @return ID del territorio seleccionado.
	 */
	public Long getTerritorioSeleccionadoId() {
		return territorioSeleccionadoId;
	}

	/**
	 * Establece el ID del territorio seleccionado y lo reclama para el jugador
	 * actual.
	 * 
	 * @param territorioSeleccionadoId ID del territorio seleccionado.
	 */
	public void setTerritorioSeleccionadoId(Long territorioSeleccionadoId) {
		this.territorioSeleccionadoId = territorioSeleccionadoId;

	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public MyLinkedList<TerritorioDTO> getTerritoriosDisponibles() {
		return territoriosDisponibles;
	}

	public void setTerritoriosDisponibles(MyLinkedList<TerritorioDTO> territoriosDisponibles) {
		this.territoriosDisponibles = territoriosDisponibles;
	}

	public TerritorioDTO[] getTerritoriosDisponiblesArray() {
		return territoriosDisponiblesArray;
	}

	public void setTerritoriosDisponiblesArray(TerritorioDTO[] territoriosDisponiblesArray) {
		this.territoriosDisponiblesArray = territoriosDisponiblesArray;
	}

	public TerritorioDTO getTerritorioSeleccionado() {
		return territorioSeleccionado;
	}

	public void setTerritorioSeleccionado(TerritorioDTO territorioSeleccionado) {
		this.territorioSeleccionado = territorioSeleccionado;
	}

	public List<TerritorioDTO> getTerritoriosDisponiblesList() {
		if (territoriosDisponiblesList == null) {
			territoriosDisponiblesList = new ArrayList<>();
		}
		return territoriosDisponiblesList;
	}

	public void setTerritoriosDisponiblesList(List<TerritorioDTO> territoriosDisponiblesList) {
		this.territoriosDisponiblesList = territoriosDisponiblesList;
	}

	public Long getJugadorActualId() {
		return jugadorActualId;
	}

	public void setJugadorActualId(Long jugadorActualId) {
		this.jugadorActualId = jugadorActualId;
	}

	public JugadorDTO getJugadorActualDTO() {
		return jugadorActualDTO;
	}

	public void setJugadorActualDTO(JugadorDTO jugadorActualDTO) {
		this.jugadorActualDTO = jugadorActualDTO;
	}

}
