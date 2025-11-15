package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.PrimeFaces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.edu.unbosque.estructures.MyLinkedList;
import co.edu.unbosque.model.Carta;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.Partida;
import co.edu.unbosque.model.UsuarioActual;
import co.edu.unbosque.util.HttpClientUtil;

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
							System.out.println(jugadoresEnPartida.toString());
						}
					}
				}
			}

			inicializarJuego();
			irAlTablero();

		} catch (Exception e) {
			showMessage("Error", "No se pudo crear la partida: " + e.getMessage());
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

	/**
	 * Reclama un territorio para un jugador en una partida específica.
	 * 
	 * @param partidaId    ID de la partida.
	 * @param jugadorId    ID del jugador que reclama el territorio.
	 * @param territorioId ID del territorio a reclamar.
	 */
	public void reclamarTerritorio(Long partidaId, Long jugadorId, Long territorioId) {
		try {
			if (partidaId == null || jugadorId == null || territorioId == null) {
				showMessage("Error", "Datos incompletos para reclamar el territorio.");
				return;
			}
			Long anfitrionId = obtenerAnfitrion(partidaId);
			Long jugadorActualId = obtenerJugadorActual(partidaId);
			if (jugadorActualId == null) {
				showMessage("Error", "No se pudo obtener el jugador actual de la partida.");
				return;
			}
			if (!jugadorActualId.equals(jugadorId) && !anfitrionId.equals(jugadorId)) {
				showMessage("Error", "Solo el anfitrión o el jugador actual pueden reclamar territorios.");
				return;
			}
			String url = BASE_URL + "/reclamar?partidaId=" + partidaId + "&jugadorId=" + jugadorId + "&territorioId="
					+ territorioId;
			String response = HttpClientUtil.post(url, null);
			if (response.contains("\"status\":200")) {
				showMessage("Éxito", "Territorio reclamado con éxito.");
			} else if (response.contains("\"status\":400")) {
				showMessage("Error", "Solicitud inválida. Verifica los datos.");
			} else if (response.contains("\"status\":403")) {
				showMessage("Error", "No tienes permiso para reclamar este territorio.");
			} else if (response.contains("\"status\":404")) {
				showMessage("Error", "Partida, jugador o territorio no encontrado.");
			} else if (response.contains("\"status\":500")) {
				showMessage("Error", "Error interno del servidor al reclamar el territorio.");
			} else {
				showMessage("Error", "No se pudo reclamar el territorio: " + response);
			}
		} catch (Exception e) {
			showMessage("Error", "No se pudo reclamar el territorio: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Reclama el territorio actualmente seleccionado para el jugador actual.
	 */
	public void reclamarTerritorioSeleccionado() {
		System.out.println("Territorio seleccionado: " + territorioSeleccionadoId);
		Long jugadorId = UsuarioActual.getUsuarioActual().getId();
		reclamarTerritorio(partidaActual.getId(), jugadorId, territorioSeleccionadoId);
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
	private Long obtenerJugadorActual(Long partidaId) {
		try {
			String response = HttpClientUtil.get(BASE_URL + "/" + partidaId + "/jugador-actual");
			return mapper.readValue(response, Long.class);
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
		if (territorioSeleccionadoId != null && partidaActual != null) {
			Long jugadorId = UsuarioActual.getUsuarioActual().getId();
			reclamarTerritorio(partidaActual.getId(), jugadorId, territorioSeleccionadoId);
		}
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

}
