package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.edu.unbosque.model.Territorio;
import co.edu.unbosque.estructures.MyDequeList;
import java.io.OutputStream;

/**
 * Bean de sesión que gestiona las operaciones relacionadas con los territorios del juego.
 * Permite cargar, asignar, reforzar y quitar tropas de los territorios,
 * así como verificar el control de continentes por parte de los jugadores.
 */
@Named("territorioBean")
@SessionScoped
public class TerritorioBean implements Serializable {

    /**
     * Versión serial para la serialización de la clase.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lista de territorios cargados desde el backend.
     */
    private MyDequeList<Territorio> listaTerritorios;

    /**
     * URL base de la API para las operaciones relacionadas con territorios.
     */
    private String apiBaseUrl = "http://localhost:8081/api/territorios";

    /**
     * Instancia de ObjectMapper para manejar la serialización y deserialización de JSON.
     */
    private ObjectMapper mapper;

    /**
     * Inicializa el bean, configura el ObjectMapper y carga los territorios al crearse.
     */
    @PostConstruct
    public void init() {
        mapper = new ObjectMapper();
        listaTerritorios = new MyDequeList<>();
        cargarTerritorios();
    }
	/** constructor vacio */
    public TerritorioBean() {
		// TODO Auto-generated constructor stub
	}
    
    /**
     * Carga la lista de territorios desde el backend.
     */
    public void cargarTerritorios() {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(apiBaseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                Scanner sc = new Scanner(url.openStream());
                StringBuilder json = new StringBuilder();
                while (sc.hasNext()) {
                    json.append(sc.nextLine());
                }
                sc.close();
                listaTerritorios = new MyDequeList<>();
                for (Territorio t : mapper.readValue(json.toString(), new TypeReference<java.util.List<Territorio>>(){})) {
                    listaTerritorios.insertLast(t);
                }
            } else {
                System.err.println("Error al cargar territorios: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Asigna un jugador a un territorio específico.
     * @param idTerritorio ID del territorio a asignar.
     * @param idJugador ID del jugador al que se asignará el territorio.
     */
    public void asignarJugador(Long idTerritorio, Long idJugador) {
        ejecutarPost(apiBaseUrl + "/asignar-jugador/" + idTerritorio + "/" + idJugador);
    }

    /**
     * Reforce un territorio con una cantidad específica de tropas.
     * @param idTerritorio ID del territorio a reforzar.
     * @param cantidad Número de tropas a añadir.
     */
    public void reforzar(Long idTerritorio, int cantidad) {
        ejecutarPost(apiBaseUrl + "/reforzar/" + idTerritorio + "/" + cantidad);
    }

    /**
     * Quita una cantidad específica de tropas de un territorio.
     * @param idTerritorio ID del territorio del que se quitarán tropas.
     * @param cantidad Número de tropas a quitar.
     */
    public void quitarTropas(Long idTerritorio, int cantidad) {
        ejecutarPost(apiBaseUrl + "/quitar-tropas/" + idTerritorio + "/" + cantidad);
    }

    /**
     * Quita todas las tropas de un territorio.
     * @param idTerritorio ID del territorio del que se quitarán todas las tropas.
     */
    public void quitarTodasLasTropas(Long idTerritorio) {
        ejecutarPost(apiBaseUrl + "/quitar-todas/" + idTerritorio);
    }

    /**
     * Reinicia todos los territorios a su estado inicial.
     */
    public void reiniciarTodos() {
        ejecutarPost(apiBaseUrl + "/reiniciar-todos");
        cargarTerritorios();
    }

    /**
     * Verifica si un jugador controla un continente específico.
     * @param idJugador ID del jugador.
     * @param idContinente ID del continente a verificar.
     * @return true si el jugador controla el continente, false en caso contrario.
     */
    public boolean jugadorControlaContinente(Long idJugador, Long idContinente) {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(apiBaseUrl + "/controla-continente/" + idJugador + "/" + idContinente);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                Scanner sc = new Scanner(url.openStream());
                boolean resultado = Boolean.parseBoolean(sc.nextLine());
                sc.close();
                return resultado;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Ejecuta una petición POST al endpoint especificado.
     * @param endpoint URL del endpoint al que se realizará la petición.
     */
    private void ejecutarPost(String endpoint) {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write("".getBytes());
            os.flush();
            os.close();
            int code = conn.getResponseCode();
            if (code != 200) {
                System.err.println("Error en POST " + endpoint + ": " + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la lista de territorios.
     * @return Lista de territorios.
     */
    public MyDequeList<Territorio> getListaTerritorios() {
        return listaTerritorios;
    }

    /**
     * Establece la lista de territorios.
     * @param listaTerritorios Lista de territorios a establecer.
     */
    public void setListaTerritorios(MyDequeList<Territorio> listaTerritorios) {
        this.listaTerritorios = listaTerritorios;
    }
}
