package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;
import java.io.Serializable;

import org.primefaces.PrimeFaces;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unbosque.estructures.MyLinkedList;
import co.edu.unbosque.model.Partida;
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

    private final String BASE_URL = "http://localhost:8081/partida";
    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        nombresJugadores = new MyLinkedList<>();
        partidaIniciada = false;
    }

    public void crearPartida() {
        try {
            if (anfitrionId == null) {
                showMessage("Error", "Debes ingresar el ID del anfitrión");
                return;
            }

            String[] nombresArray = nombresJugadores.toArray(new String[nombresJugadores.size()]);

            String json = mapper.writeValueAsString(nombresArray);

            String response = HttpClientUtil.post(BASE_URL + "/crear?anfitrionId=" + anfitrionId, json);

            partidaActual = mapper.readValue(response, Partida.class);

            showMessage("Éxito", "Partida creada con ID: " + partidaActual.getId());
            partidaIniciada = true;

        } catch (Exception e) {
            showMessage("Error", "No se pudo crear la partida: " + e.getMessage());
            e.printStackTrace();
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
}
