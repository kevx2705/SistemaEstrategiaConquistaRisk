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

    /** Inicializa el mazo desde la BD al arrancar la app */
    @PostConstruct
    public void inicializar() {
        inicializarMazo();
    }

    /** Inicializa el mazo con cartas disponibles de la BD y las baraja */
    public void inicializarMazo() {
        MyLinkedList<Carta> lista = new MyLinkedList<>();
        for (Carta c : cartaRepository.findAll()) {
            if (c.isDisponible()) {  // Solo cartas que no están robadas
                lista.addLast(c);
            }
        }
        barajarYCrearMazo(lista);
    }

    /** Baraja la lista y llena el mazo */
    private void barajarYCrearMazo(MyLinkedList<Carta> lista) {
        int size = lista.size();
        if (size == 0) return;

        // Convertir lista a arreglo de nodos para barajar sin alterar MyLinkedList
        Node<Carta>[] nodos = new Node[size];
        Node<Carta> current = lista.getFirst();
        for (int i = 0; i < size; i++) {
            nodos[i] = current;
            current = current.getNext();
        }

        // Barajar tipo Fisher-Yates
        for (int i = size - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Carta temp = nodos[i].getInfo();
            nodos[i].setInfo(nodos[j].getInfo());
            nodos[j].setInfo(temp);
        }

        // Llenar la pila mazo
        mazo = new StackImpl<>();
        current = lista.getFirst();
        while (current != null) {
            mazo.push(current.getInfo());
            current = current.getNext();
        }
    }

    /** Roba la carta superior */
    public CartaDTO robarCarta() {
        if (mazo.size() == 0) return null;
        Carta carta = mazo.pop();
        carta.setDisponible(false);
        cartaRepository.save(carta);
        return modelMapper.map(carta, CartaDTO.class);
    }

    /** Devuelve carta al mazo por ID */
    public void devolverCarta(Long idCarta) {
        if (idCarta == null) return;

        Optional<Carta> opt = cartaRepository.findById(idCarta);
        if (opt.isEmpty()) return;

        Carta carta = opt.get();
        carta.setDisponible(true);
        cartaRepository.save(carta);

        // Extraer mazo a lista temporal
        MyLinkedList<Carta> temp = new MyLinkedList<>();
        while (mazo.size() > 0) {
            temp.addLast(mazo.pop());
        }

        // Insertar carta devuelta en posición aleatoria
        int pos = random.nextInt(temp.size() + 1);
        if (pos == 0) {
            mazo.push(carta);
        } else if (pos == temp.size()) {
            temp.addLast(carta);
        } else {
            Node<Carta> nodo = temp.getPos(pos - 1);
            temp.insert(carta, nodo);
        }

        // Reconstruir mazo
        Node<Carta> current = temp.getFirst();
        while (current != null) {
            mazo.push(current.getInfo());
            current = current.getNext();
        }
    }

    /** Cantidad de cartas robadas (no disponibles) */
    public int contarCartasRobadas() {
        int count = 0;
        for (Carta c : cartaRepository.findAll()) {
            if (!c.isDisponible()) count++;
        }
        return count;
    }

    /** Estado del mazo y cartas robadas (debug) */
    public void imprimirEstadoMazo() {
        System.out.println("📦 Cartas en el mazo:");
        StackImpl<Carta> temp = new StackImpl<>();
        while (mazo.size() > 0) {
            Carta c = mazo.pop();
            System.out.println("- " + c.getId() + " [" + c.getTipo() + "]");
            temp.push(c);
        }
        while (temp.size() > 0) mazo.push(temp.pop());

        System.out.println("🎴 Cartas robadas:");
        for (Carta c : cartaRepository.findAll()) {
            if (!c.isDisponible()) {
                System.out.println("- " + c.getId() + " [" + c.getTipo() + "]");
            }
        }
    }

    /** Tamaño actual del mazo */
    public int tamañoMazo() {
        return mazo.size();
    }

    /** Resetea todas las cartas y baraja de nuevo */
    public void resetCartas() {
        MyLinkedList<Carta> lista = new MyLinkedList<>();
        for (Carta c : cartaRepository.findAll()) {
            c.setDisponible(true);
            cartaRepository.save(c);
            lista.addLast(c);
        }
        barajarYCrearMazo(lista);
    }
}
