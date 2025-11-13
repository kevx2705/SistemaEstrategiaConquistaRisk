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

@Named("territorioBean")
@SessionScoped
public class TerritorioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private MyDequeList<Territorio> listaTerritorios;
    private String apiBaseUrl = "http://localhost:8081/api/territorios"; 
    private ObjectMapper mapper;

    @PostConstruct
    public void init() {
        mapper = new ObjectMapper();
        listaTerritorios = new MyDequeList<>();
        cargarTerritorios();
    }

    public void cargarTerritorios() {
        try {
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

    public void asignarJugador(Long idTerritorio, Long idJugador) {
        ejecutarPost(apiBaseUrl + "/asignar-jugador/" + idTerritorio + "/" + idJugador);
    }

    public void reforzar(Long idTerritorio, int cantidad) {
        ejecutarPost(apiBaseUrl + "/reforzar/" + idTerritorio + "/" + cantidad);
    }

    public void quitarTropas(Long idTerritorio, int cantidad) {
        ejecutarPost(apiBaseUrl + "/quitar-tropas/" + idTerritorio + "/" + cantidad);
    }

    public void quitarTodasLasTropas(Long idTerritorio) {
        ejecutarPost(apiBaseUrl + "/quitar-todas/" + idTerritorio);
    }

    public void reiniciarTodos() {
        ejecutarPost(apiBaseUrl + "/reiniciar-todos");
        cargarTerritorios(); // Recarga después de reiniciar
    }

    public boolean jugadorControlaContinente(Long idJugador, Long idContinente) {
        try {
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

    // 🔧 Método auxiliar para POSTs sin cuerpo
    private void ejecutarPost(String endpoint) {
        try {
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
                System.err.println("⚠ Error en POST " + endpoint + ": " + code);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyDequeList<Territorio> getListaTerritorios() {
        return listaTerritorios;
    }

    public void setListaTerritorios(MyDequeList<Territorio> listaTerritorios) {
        this.listaTerritorios = listaTerritorios;
    }
}
