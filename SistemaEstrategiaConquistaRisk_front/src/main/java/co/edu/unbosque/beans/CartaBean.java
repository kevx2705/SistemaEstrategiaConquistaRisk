package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import co.edu.unbosque.model.Carta;

@Named("cartaBean")
@SessionScoped
public class CartaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Carta cartaRobada;      
    private int cartasDisponibles;    
    private int cartasRobadas;         

    private RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8081/carta"; 

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        actualizarEstadoMazo();
    }

    /** Inicializa el mazo en el backend */
    public void inicializarMazo() {
        try {
            restTemplate.postForEntity(BASE_URL + "/inicializar", null, String.class);
            actualizarEstadoMazo();
            System.out.println("Mazo inicializado correctamente");
        } catch (Exception e) {
            System.err.println(" Error al inicializar mazo: " + e.getMessage());
        }
    }

    /** Roba una carta del mazo */
    public void robarCarta() {
        try {
            ResponseEntity<Carta> response = restTemplate.getForEntity(BASE_URL + "/robar", Carta.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                cartaRobada = response.getBody();
                System.out.println(" Carta robada: " + cartaRobada.getNombre());
            } else {
                cartaRobada = null;
                System.out.println(" No hay cartas disponibles para robar");
            }
            actualizarEstadoMazo();
        } catch (Exception e) {
            System.err.println(" Error al robar carta: " + e.getMessage());
            cartaRobada = null;
        }
    }

    /** Devuelve una carta al mazo */
    public void devolverCarta(Long idCarta) {
        try {
            restTemplate.postForEntity(BASE_URL + "/devolver", idCarta, String.class);
            System.out.println("Carta con ID " + idCarta + " devuelta al mazo");
            actualizarEstadoMazo();
        } catch (Exception e) {
            System.err.println(" Error al devolver carta: " + e.getMessage());
        }
    }

    /** Reinicia todas las cartas */
    public void resetCartas() {
        try {
            restTemplate.put(BASE_URL + "/reset", null);
            cartaRobada = null;
            actualizarEstadoMazo();
            System.out.println(" Mazo reiniciado completamente");
        } catch (Exception e) {
            System.err.println(" Error al reiniciar mazo: " + e.getMessage());
        }
    }

    /** Actualiza el estado del mazo (disponibles y robadas) */
    public void actualizarEstadoMazo() {
        try {
            cartasDisponibles = restTemplate.getForObject(BASE_URL + "/tamano", Integer.class);
            cartasRobadas = restTemplate.getForObject(BASE_URL + "/robadas", Integer.class);
        } catch (Exception e) {
            cartasDisponibles = 0;
            cartasRobadas = 0;
            System.err.println("No se pudo obtener el estado del mazo");
        }
    }

    public Carta getCartaRobada() {
        return cartaRobada;
    }

    public void setCartaRobada(Carta cartaRobada) {
        this.cartaRobada = cartaRobada;
    }

    public int getCartasDisponibles() {
        return cartasDisponibles;
    }

    public void setCartasDisponibles(int cartasDisponibles) {
        this.cartasDisponibles = cartasDisponibles;
    }

    public int getCartasRobadas() {
        return cartasRobadas;
    }

    public void setCartasRobadas(int cartasRobadas) {
        this.cartasRobadas = cartasRobadas;
    }
}
