package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;
import java.io.Serializable;
import org.primefaces.PrimeFaces;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.edu.unbosque.estructures.MyLinkedList;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.Partida;
import co.edu.unbosque.model.UsuarioActual;
import co.edu.unbosque.util.HttpClientUtil;

@Named("partidabean")
@SessionScoped
public class PartidaBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Partida partidaActual;
	private MyLinkedList<String> nombresJugadores;
	private String codigoPartida;
	private Long anfitrionId;
	private boolean partidaIniciada;
	private int cantidadJugadores;
	private final String BASE_URL = "http://localhost:8081/partida";
	private ObjectMapper mapper = new ObjectMapper();

	@PostConstruct
	public void init() {
		nombresJugadores = new MyLinkedList<>();
		partidaIniciada = false;
	}

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
			System.out.println("ID del anfitrión: " + anfitrionId); // Log 1

			nombresJugadores.clear();
			for (int i = 1; i <= cantidadJugadores; i++) {
				nombresJugadores.add("Jugador " + i);
			}
			String[] nombresArray = nombresJugadores.toArray(new String[nombresJugadores.size()]);
			String json = mapper.writeValueAsString(nombresArray);
			System.out.println("JSON enviado: " + json); // Log 2

			String url = BASE_URL + "/crear?anfitrionId=" + anfitrionId;
			System.out.println("URL del backend: " + url); // Log 3
			String response = HttpClientUtil.post(url, json);
			System.out.println("Respuesta del backend: " + response); // Log 4

			partidaActual = mapper.readValue(response, Partida.class);
			showMessage("Éxito", "Partida creada con ID: " + partidaActual.getId());
			partidaIniciada = true;

			inicializarJuego();

			irAlTablero();

		} catch (Exception e) {
			showMessage("Error", "No se pudo crear la partida: " + e.getMessage());
			e.printStackTrace();
		}
	}

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

	        String url = BASE_URL + "/reclamar?partidaId=" + partidaId +
	                     "&jugadorId=" + jugadorId +
	                     "&territorioId=" + territorioId;

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
	
	public void reclamarTerritorioDesdeBoton(Long territorioId) {
	    Long partidaId = partidaActual != null ? partidaActual.getId() : null;
	    Long jugadorId = UsuarioActual.getUsuarioActual().getId();

	    reclamarTerritorio(partidaId, jugadorId, territorioId);
	}

	
	private Long obtenerAnfitrion(Long partidaId) {
	    try {
	        String response = HttpClientUtil.get(BASE_URL + "/" + partidaId + "/anfitrion");
	        return mapper.readValue(response, Long.class);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	private Long obtenerJugadorActual(Long partidaId) {
	    try {
	        String response = HttpClientUtil.get(BASE_URL + "/" + partidaId + "/jugador-actual");
	        return mapper.readValue(response, Long.class);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

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

	public String irAlTablero() {
		return "tablero.xhtml?faces-redirect=true";
	}

	private void showMessage(String titulo, String detalle) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalle);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public Partida getPartidaActual() {
		return partidaActual;
	}

	public void setPartidaActual(Partida partidaActual) {
		this.partidaActual = partidaActual;
	}

	public MyLinkedList<String> getNombresJugadores() {
		return nombresJugadores;
	}

	public void setNombresJugadores(MyLinkedList<String> nombresJugadores) {
		this.nombresJugadores = nombresJugadores;
	}

	public String getCodigoPartida() {
		return codigoPartida;
	}

	public void setCodigoPartida(String codigoPartida) {
		this.codigoPartida = codigoPartida;
	}

	public Long getAnfitrionId() {
		return anfitrionId;
	}

	public void setAnfitrionId(Long anfitrionId) {
		this.anfitrionId = anfitrionId;
	}

	public boolean isPartidaIniciada() {
		return partidaIniciada;
	}

	public void setPartidaIniciada(boolean partidaIniciada) {
		this.partidaIniciada = partidaIniciada;
	}

	public int getCantidadJugadores() {
		return cantidadJugadores;
	}

	public void setCantidadJugadores(int cantidadJugadores) {
		this.cantidadJugadores = cantidadJugadores;
	}
}
