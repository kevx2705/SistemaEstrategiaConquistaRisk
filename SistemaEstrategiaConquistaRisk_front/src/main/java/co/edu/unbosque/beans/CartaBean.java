package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import co.edu.unbosque.model.Carta;

/**
 * Bean de sesión que gestiona las operaciones relacionadas con el mazo de cartas.
 * Permite inicializar el mazo, robar cartas, devolver cartas y reiniciar el mazo.
 */
@Named("cartaBean")
@SessionScoped
public class CartaBean implements Serializable {

    /**
     * Versión serial para la serialización de la clase.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Almacena la última carta robada del mazo.
     */
    private Carta cartaRobada;

    /**
     * Cantidad de cartas disponibles en el mazo.
     */
    private int cartasDisponibles;

    /**
     * Cantidad de cartas que han sido robadas del mazo.
     */
    private int cartasRobadas;

    /**
     * Instancia de RestTemplate para realizar peticiones HTTP al backend.
     */
    private RestTemplate restTemplate;

    /**
     * URL base para los endpoints del servicio de cartas.
     */
    private final String BASE_URL = "http://localhost:8081/carta";

    /**
     * Inicializa el bean y actualiza el estado del mazo al crearse.
     */
    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        actualizarEstadoMazo();
    }

	/** constructor vacio */
    public CartaBean() {
		// TODO Auto-generated constructor stub
	}
    
    /**
     * Inicializa el mazo en el backend.
     * Realiza una petición POST al endpoint "/inicializar" para preparar el mazo.
     */
    public void inicializarMazo() {
        try {
            restTemplate.postForEntity(BASE_URL + "/inicializar", null, String.class);
            actualizarEstadoMazo();
            System.out.println("Mazo inicializado correctamente");
        } catch (Exception e) {
            System.err.println(" Error al inicializar mazo: " + e.getMessage());
        }
    }

    /**
     * Roba una carta del mazo.
     * Realiza una petición GET al endpoint "/robar" para obtener una carta.
     * Si la operación es exitosa, almacena la carta robada y actualiza el estado del mazo.
     */
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

    /**
     * Devuelve una carta al mazo.
     * @param idCarta El ID de la carta que se desea devolver al mazo.
     */
    public void devolverCarta(Long idCarta) {
        try {
            restTemplate.postForEntity(BASE_URL + "/devolver", idCarta, String.class);
            System.out.println("Carta con ID " + idCarta + " devuelta al mazo");
            actualizarEstadoMazo();
        } catch (Exception e) {
            System.err.println(" Error al devolver carta: " + e.getMessage());
        }
    }

    /**
     * Reinicia todas las cartas del mazo.
     * Realiza una petición PUT al endpoint "/reset" para reiniciar el mazo.
     */
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

    /**
     * Actualiza el estado del mazo (cartas disponibles y robadas).
     * Consulta los endpoints "/tamano" y "/robadas" para obtener la cantidad de cartas disponibles y robadas, respectivamente.
     */
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

    /**
     * Obtiene la carta robada actualmente.
     * @return La carta robada.
     */
    public Carta getCartaRobada() {
        return cartaRobada;
    }

    /**
     * Establece la carta robada actualmente.
     * @param cartaRobada La carta robada a establecer.
     */
    public void setCartaRobada(Carta cartaRobada) {
        this.cartaRobada = cartaRobada;
    }

    /**
     * Obtiene la cantidad de cartas disponibles en el mazo.
     * @return La cantidad de cartas disponibles.
     */
    public int getCartasDisponibles() {
        return cartasDisponibles;
    }

    /**
     * Establece la cantidad de cartas disponibles en el mazo.
     * @param cartasDisponibles La cantidad de cartas disponibles a establecer.
     */
    public void setCartasDisponibles(int cartasDisponibles) {
        this.cartasDisponibles = cartasDisponibles;
    }

    /**
     * Obtiene la cantidad de cartas robadas del mazo.
     * @return La cantidad de cartas robadas.
     */
    public int getCartasRobadas() {
        return cartasRobadas;
    }

    /**
     * Establece la cantidad de cartas robadas del mazo.
     * @param cartasRobadas La cantidad de cartas robadas a establecer.
     */
    public void setCartasRobadas(int cartasRobadas) {
        this.cartasRobadas = cartasRobadas;
    }
}
