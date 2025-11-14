package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Territorio;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.Node;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.TerritorioRepository;

/**
 * Servicio que gestiona la lógica relacionada con los territorios del juego Risk.
 * <p>
 * Proporciona funcionalidades para obtener, asignar, reforzar y gestionar territorios,
 * así como verificar el control de continentes por parte de los jugadores.
 * </p>
 */
@Service
public class TerritorioService {

    @Autowired
    private TerritorioRepository territorioRepository;
    /**
     * Constructor por defecto de la clase {@code TerritorioService}.
     * Inicializa una instancia del servicio para gestionar territorios.
     */
    public TerritorioService() {
		// TODO Auto-generated constructor stub
	}

    /**
     * Obtiene todos los territorios del juego.
     *
     * @return {@code MyLinkedList<TerritorioDTO>} Lista enlazada de territorios en formato DTO.
     *         Nunca es {@code null}, pero puede estar vacía si no hay territorios registrados.
     */
    public MyLinkedList<TerritorioDTO> obtenerTodos() {
        MyLinkedList<TerritorioDTO> listaDTO = new MyLinkedList<>();
        for (Territorio t : territorioRepository.findAll()) {
            TerritorioDTO dto = new TerritorioDTO();
            dto.setId(t.getId());
            dto.setNombre(t.getNombre());
            dto.setTropas(t.getTropas());
            dto.setIdContinente(t.getIdContinente());
            dto.setIdJugador(t.getIdJugador());
            listaDTO.addLast(dto);
        }
        return listaDTO;
    }

    /**
     * Verifica si un jugador controla todos los territorios de un continente.
     * <p>
     * Este método verifica que <b>todos</b> los territorios del continente sean controlados
     * por el jugador especificado.
     * </p>
     *
     * @param idJugador   Identificador del jugador.
     * @param idContinente Identificador del continente.
     * @return {@code boolean} {@code true} si el jugador controla todos los territorios del continente,
     *         {@code false} en caso contrario.
     */
    public boolean controlaContinente(Long idJugador, Long idContinente) {
        MyLinkedList<TerritorioDTO> territorios = obtenerPorContinente(idContinente);
        for (int i = 0; i < territorios.size(); i++) {
            if (!territorios.getPos(i).getInfo().getIdJugador().equals(idJugador)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Cuenta la cantidad de territorios controlados por un jugador.
     *
     * @param idJugador Identificador del jugador.
     * @return {@code int} Cantidad de territorios controlados por el jugador.
     */
    public int contarTerritoriosDeJugador(Long idJugador) {
        return obtenerPorJugador(idJugador).size();
    }

    /**
     * Obtiene todos los territorios que pertenecen a un jugador.
     *
     * @param idJugador Identificador del jugador.
     * @return {@code MyLinkedList<TerritorioDTO>} Lista enlazada de territorios en formato DTO.
     *         Nunca es {@code null}, pero puede estar vacía si el jugador no controla territorios.
     */
    public MyLinkedList<TerritorioDTO> obtenerPorJugador(Long idJugador) {
        MyLinkedList<TerritorioDTO> lista = new MyLinkedList<>();
        for (Territorio t : territorioRepository.findAll()) {
            if (t.getIdJugador() != null && t.getIdJugador().equals(idJugador)) {
                TerritorioDTO dto = new TerritorioDTO();
                dto.setId(t.getId());
                dto.setNombre(t.getNombre());
                dto.setTropas(t.getTropas());
                dto.setIdContinente(t.getIdContinente());
                dto.setIdJugador(t.getIdJugador());
                lista.addLast(dto);
            }
        }
        return lista;
    }

    /**
     * Busca una entidad de territorio por su nombre.
     * <p>
     * Este método es privado y se utiliza internamente para buscar territorios por nombre.
     * </p>
     *
     * @param nombre Nombre del territorio.
     * @return {@code Territorio} La entidad del territorio encontrado, o {@code null} si no existe.
     */
    private Territorio buscarEntidadPorNombre(String nombre) {
        for (Territorio t : territorioRepository.findAll()) {
            if (t.getNombre().equalsIgnoreCase(nombre)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Obtiene todos los territorios que pertenecen a un continente.
     *
     * @param idContinente Identificador del continente.
     * @return {@code MyLinkedList<TerritorioDTO>} Lista enlazada de territorios en formato DTO.
     *         Nunca es {@code null}, pero puede estar vacía si el continente no tiene territorios.
     */
    public MyLinkedList<TerritorioDTO> obtenerPorContinente(Long idContinente) {
        MyLinkedList<TerritorioDTO> lista = new MyLinkedList<>();
        for (Territorio t : territorioRepository.findAll()) {
            if (t.getIdContinente().equals(idContinente)) {
                TerritorioDTO dto = new TerritorioDTO();
                dto.setId(t.getId());
                dto.setNombre(t.getNombre());
                dto.setTropas(t.getTropas());
                dto.setIdContinente(t.getIdContinente());
                dto.setIdJugador(t.getIdJugador());
                lista.addLast(dto);
            }
        }
        return lista;
    }

    /**
     * Asigna un jugador como propietario de un territorio.
     * <p>
     * Si el territorio existe, se actualiza su propietario con el identificador del jugador.
     * </p>
     *
     * @param idTerritorio Identificador del territorio.
     * @param idJugador    Identificador del jugador.
     */
    public void asignarJugador(Long idTerritorio, Long idJugador) {
        Territorio territorio = territorioRepository.findById(idTerritorio).orElse(null);
        if (territorio != null) {
            territorio.setIdJugador(idJugador);
            territorioRepository.save(territorio);
        }
    }

    /**
     * Añade tropas a un territorio.
     * <p>
     * Si el territorio existe, se incrementa la cantidad de tropas en el valor especificado.
     * </p>
     *
     * @param idTerritorio Identificador del territorio.
     * @param cantidad     Cantidad de tropas a añadir. Debe ser un número positivo.
     */
    public void reforzar(Long idTerritorio, int cantidad) {
        Territorio t = territorioRepository.findById(idTerritorio).orElse(null);
        if (t != null) {
            t.setTropas(t.getTropas() + cantidad);
            territorioRepository.save(t);
        }
    }

    /**
     * Quita tropas de un territorio.
     * <p>
     * Si el territorio existe, se decrementa la cantidad de tropas en el valor especificado,
     * asegurando que no queden menos de 0 tropas.
     * </p>
     *
     * @param idTerritorio Identificador del territorio.
     * @param cantidad     Cantidad de tropas a quitar. Debe ser un número positivo.
     */
    public void quitarTropas(Long idTerritorio, int cantidad) {
        Territorio territorio = territorioRepository.findById(idTerritorio).orElse(null);
        if (territorio != null) {
            int nuevasTropas = Math.max(0, territorio.getTropas() - cantidad);
            territorio.setTropas(nuevasTropas);
            territorioRepository.save(territorio);
        }
    }

    /**
     * Quita todas las tropas de un territorio.
     * <p>
     * Si el territorio existe, se establecen sus tropas a 0.
     * </p>
     *
     * @param idTerritorio Identificador del territorio.
     */
    public void quitarTodasLasTropas(Long idTerritorio) {
        Territorio territorio = territorioRepository.findById(idTerritorio).orElse(null);
        if (territorio != null) {
            territorio.setTropas(0);
            territorioRepository.save(territorio);
        }
    }

    /**
     * Reinicia todos los territorios, dejando sin jugador y sin tropas.
     * <p>
     * Este método establece todos los territorios a su estado inicial:
     * sin propietario y con 0 tropas.
     * </p>
     */
    public void reiniciarTodos() {
        for (Territorio t : territorioRepository.findAll()) {
            t.setIdJugador(0L);
            t.setTropas(0);
            territorioRepository.save(t);
        }
    }

    /**
     * Verifica si un jugador controla todos los territorios de un continente.
     * <p>
     * Este método cuenta los territorios del continente y verifica si todos son controlados
     * por el jugador especificado.
     * </p>
     *
     * @param idJugador   Identificador del jugador.
     * @param idContinente Identificador del continente.
     * @return {@code boolean} {@code true} si el jugador controla todos los territorios del continente,
     *         {@code false} en caso contrario.
     */
    public boolean jugadorControlaContinente(Long idJugador, Long idContinente) {
        int totalTerritorios = 0;
        int controladosPorJugador = 0;
        for (Territorio t : territorioRepository.findAll()) {
            if (t.getIdContinente().equals(idContinente)) {
                totalTerritorios++;
                if (idJugador.equals(t.getIdJugador())) {
                    controladosPorJugador++;
                }
            }
        }
        return totalTerritorios > 0 && controladosPorJugador == totalTerritorios;
    }

    /**
     * Obtiene un territorio por su identificador.
     *
     * @param idTerritorio Identificador del territorio.
     * @return {@code TerritorioDTO} El territorio en formato DTO, o {@code null} si no existe.
     */
    public TerritorioDTO obtenerPorId(Long idTerritorio) {
        Territorio t = territorioRepository.findById(idTerritorio).orElse(null);
        if (t == null) return null;
        TerritorioDTO dto = new TerritorioDTO();
        dto.setId(t.getId());
        dto.setNombre(t.getNombre());
        dto.setTropas(t.getTropas());
        dto.setIdContinente(t.getIdContinente());
        dto.setIdJugador(t.getIdJugador());
        return dto;
    }

    /**
     * Busca un territorio en una lista por su identificador.
     * <p>
     * Este método recorre una lista enlazada de territorios y busca el territorio
     * con el identificador especificado.
     * </p>
     *
     * @param lista Lista enlazada de territorios en formato DTO.
     * @param id    Identificador del territorio.
     * @return {@code TerritorioDTO} El territorio encontrado en la lista, o {@code null} si no existe.
     */
    public TerritorioDTO buscarEnLista(MyLinkedList<TerritorioDTO> lista, Long id) {
        Node<TerritorioDTO> n = lista.getFirst();
        while (n != null) {
            TerritorioDTO t = n.getInfo();
            if (t.getId().equals(id)) {
                return t;
            }
            n = n.getNext();
        }
        return null;
    }
}
