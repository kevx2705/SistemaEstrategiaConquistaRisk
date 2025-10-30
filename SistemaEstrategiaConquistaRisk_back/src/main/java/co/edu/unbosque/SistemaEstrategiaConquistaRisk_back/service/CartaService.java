package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.CartaDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.Node;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.StackImpl;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.CartaRepository;
import org.modelmapper.ModelMapper;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class CartaService {

    @Autowired
    private CartaRepository cartaRepository;

    @Autowired
    private ModelMapper modelMapper;

    private StackImpl<Carta> mazo;
    private Random random;

    public CartaService() {
        mazo = new StackImpl<>();
        random = new Random();
    }
    @PostConstruct
    public void inicializar() {
        if (cartaRepository.count() == 0) {
            System.out.println("Inicializando cartas del mazo...");
            crearCartasIniciales();
        }
        inicializarMazo();
    }
    /** 🔹 Crea las 44 cartas oficiales de Risk una sola vez */
    private void crearCartasIniciales() {
        System.out.println("⚔️ Creando mazo inicial de cartas Risk...");

        // INFANTERÍA
        crearCarta("Alaska", "Infantería");
        crearCarta("Ontario", "Infantería");
        crearCarta("Venezuela", "Infantería");
        crearCarta("Islandia", "Infantería");
        crearCarta("Europa del Norte", "Infantería");
        crearCarta("Congo", "Infantería");
        crearCarta("Oriente Medio", "Infantería");
        crearCarta("Afganistán", "Infantería");
        crearCarta("Siam", "Infantería");
        crearCarta("Siberia", "Infantería");
        crearCarta("Australia Occidental", "Infantería");
        crearCarta("Gran Bretaña", "Infantería");
        crearCarta("Quebec", "Infantería");
        crearCarta("África del Norte", "Infantería");

        // CABALLERÍA
        crearCarta("Alberta", "Caballería");
        crearCarta("Terranova", "Caballería");
        crearCarta("Brasil", "Caballería");
        crearCarta("Escandinavia", "Caballería");
        crearCarta("Egipto", "Caballería");
        crearCarta("África del Este", "Caballería");
        crearCarta("India", "Caballería");
        crearCarta("China", "Caballería");
        crearCarta("Mongolia", "Caballería");
        crearCarta("Yakutsk", "Caballería");
        crearCarta("Kamchatka", "Caballería");
        crearCarta("Japón", "Caballería");
        crearCarta("Indonesia", "Caballería");
        crearCarta("Europa del Sur", "Caballería");

        // ARTILLERÍA
        crearCarta("Groenlandia", "Artillería");
        crearCarta("Oeste de EE.UU.", "Artillería");
        crearCarta("Este de EE.UU.", "Artillería");
        crearCarta("América Central", "Artillería");
        crearCarta("Argentina", "Artillería");
        crearCarta("Europa Occidental", "Artillería");
        crearCarta("África del Sur", "Artillería");
        crearCarta("Ural", "Artillería");
        crearCarta("Irkutsk", "Artillería");
        crearCarta("Nueva Guinea", "Artillería");
        crearCarta("Australia Oriental", "Artillería");
        crearCarta("Perú", "Artillería");
        crearCarta("Madagascar", "Artillería");
        crearCarta("Ucrania", "Artillería");


        // COMODINES
        crearCarta("Comodín 1", "Comodín");
        crearCarta("Comodín 2", "Comodín");

        System.out.println("✅ Mazo de cartas creado correctamente (44 cartas).");
    }
    private void crearCarta(String territorio, String tipo) {
        Carta carta = new Carta();
        carta.setTerritorio(territorio);
        carta.setTipo(tipo);
        carta.setDisponible(true);
        cartaRepository.save(carta);
    }

    /** Inicializa el mazo desde la BD y lo baraja */
    public void inicializarMazo() {
        MyLinkedList<Carta> todas = new MyLinkedList<>();
        for (Carta c : cartaRepository.findAll()) {
            if (c.isDisponible()) { // Solo cartas disponibles
                todas.add(c);
            }
        }
        barajarYCrearMazo(todas);
    }

    /** Baraja una lista de cartas y las pone en la pila mazo */
    private void barajarYCrearMazo(MyLinkedList<Carta> lista) {
        int total = lista.size();
        while (total > 0) {
            int pos = random.nextInt(total);
            Node<Carta> nodo = lista.getPos(pos);
            Carta carta = nodo.getInfo();
            mazo.push(carta);
            lista.extract(nodo);
            total--;
        }
    }

    /** Roba la carta superior del mazo */
    public CartaDTO robarCarta() {
        if (mazo.size() == 0) return null;
        Carta carta = mazo.pop();
        carta.setDisponible(false);          // Marcar como no disponible
        cartaRepository.save(carta);         // Guardar cambio en BD
        return modelMapper.map(carta, CartaDTO.class);
    }

    public void devolverCarta(Long idCarta) {
        if (idCarta == null) return;

        Optional<Carta> optionalCarta = cartaRepository.findById(idCarta);
        if (optionalCarta.isEmpty()) {
            System.out.println("⚠️ No se encontró la carta con ID: " + idCarta);
            return;
        }

        Carta carta = optionalCarta.get();
        carta.setDisponible(true);
        cartaRepository.save(carta);

        // Extraer cartas actuales del mazo
        MyLinkedList<Carta> temp = new MyLinkedList<>();
        while (mazo.size() > 0) {
            temp.addLast(mazo.pop());
        }

        // Insertar la carta devuelta en una posición aleatoria
        int pos = random.nextInt(temp.size() + 1);
        if (pos == 0) {
            mazo.push(carta);
        } else if (pos == temp.size()) {
            temp.addLast(carta);
        } else {
            Node<Carta> nodo = temp.getPos(pos - 1);
            temp.insert(carta, nodo);
        }

        // Reconstruir el mazo
        Node<Carta> current = temp.getFirst();
        while (current != null) {
            mazo.push(current.getInfo());
            current = current.getNext();
        }

        System.out.println("♻️ Carta '" + carta.getTerritorio() + "' devuelta al mazo.");
    }
    /** 
     * Retorna la cantidad de cartas actualmente robadas (no disponibles).
     */
    public int contarCartasRobadas() {
        int contador = 0;
        for (Carta c : cartaRepository.findAll()) {
            if (!c.isDisponible()) {
                contador++;
            }
        }
        return contador;
    }


    /** Devuelve la cantidad de cartas en el mazo */
    public int tamañoMazo() {
        return mazo.size();
    }

    /** Resetea todas las cartas a disponible y reconstruye el mazo */
    public void resetCartas() {
        MyLinkedList<Carta> todas = new MyLinkedList<>();
        for (Carta c : cartaRepository.findAll()) {
            c.setDisponible(true);
            cartaRepository.save(c);
            todas.add(c);
        }
        mazo = new StackImpl<>();
        barajarYCrearMazo(todas);
    }
}
