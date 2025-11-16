package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.CartaDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.Node;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.JugadorRepository;

/**
 * Servicio que gestiona la lógica relacionada con los jugadores del juego.
 * Proporciona funcionalidades para crear, actualizar, eliminar y consultar jugadores,
 * así como gestionar sus tropas, territorios y cartas.
 */
@Service
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TerritorioService territorioService;

    /**
     * Constructor por defecto de la clase JugadorService.
     */
    public JugadorService() {
    }

    /**
     * Crea un nuevo jugador en la base de datos.
     *
     * @param newData DTO con los datos del nuevo jugador.
     * @return int 0 si la operación fue exitosa.
     */
    public int create(JugadorDTO newData) {
        Jugador entity = modelMapper.map(newData, Jugador.class);
        jugadorRepo.save(entity);
        return 0;
    }

    /**
     * Obtiene un jugador por su identificador.
     *
     * @param idJugador Identificador del jugador.
     * @return Jugador El jugador encontrado, o null si no existe.
     */
    public Jugador obtenerJugadorPorId(Long idJugador) {
        return jugadorRepo.findById(idJugador).orElse(null);
    }

    public JugadorDTO obtenerJugadorPorIdDTO(Long idJugador) {
        Jugador jugador = jugadorRepo.findById(idJugador).orElse(null);
        if (jugador == null) return null;

        return modelMapper.map(jugador, JugadorDTO.class);
    }


    /**
     * Obtiene todos los jugadores registrados.
     *
     * @return MyLinkedList<JugadorDTO> Lista enlazada de jugadores en formato DTO.
     */
    public MyLinkedList<JugadorDTO> getAll() {
        MyLinkedList<JugadorDTO> lista = new MyLinkedList<>();
        for (Jugador entity : jugadorRepo.findAll()) {
            lista.addLast(modelMapper.map(entity, JugadorDTO.class));
        }
        return lista;
    }

    /**
     * Elimina un jugador por su identificador.
     *
     * @param id Identificador del jugador a eliminar.
     * @return int 0 si la eliminación fue exitosa, 1 si el jugador no existe.
     */
    public int deleteById(Long id) {
        if (jugadorRepo.existsById(id)) {
            jugadorRepo.deleteById(id);
            return 0;
        }
        return 1;
    }

    /**
     * Actualiza los datos de un jugador por su identificador.
     *
     * @param id Identificador del jugador a actualizar.
     * @param newData DTO con los nuevos datos del jugador.
     * @return int 0 si la actualización fue exitosa, 1 si el jugador no existe.
     */
    public int updateById(Long id, JugadorDTO newData) {
        Optional<Jugador> opt = jugadorRepo.findById(id);
        if (opt.isPresent()) {
            Jugador entity = opt.get();
            entity.setNombre(newData.getNombre());
            entity.setCorreo(newData.getCorreo());
            entity.setColor(newData.getColor());
            entity.setTropasDisponibles(newData.getTropasDisponibles());
            entity.setTerritoriosControlados(newData.getTerritoriosControlados());
            entity.setActivo(newData.isActivo());
            MyLinkedList<Carta> cartasConvertidas = new MyLinkedList<>();
            if (newData.getCartas() != null) {
                Node<CartaDTO> nodo = newData.getCartas().getFirst();
                while (nodo != null) {
                    CartaDTO dto = nodo.getInfo();
                    Carta carta = new Carta();
                    carta.setId(dto.getId());
                    carta.setTipo(dto.getTipo());
                    carta.setDisponible(dto.isDisponible());
                    cartasConvertidas.add(carta);
                    nodo = nodo.getNext();
                }
            }
            entity.setCartas(cartasConvertidas);
            jugadorRepo.save(entity);
            return 0;
        }
        return 1;
    }

    /**
     * Cuenta la cantidad de jugadores registrados.
     *
     * @return Long Cantidad de jugadores registrados.
     */
    public Long count() {
        return (long) getAll().size();
    }

    /**
     * Verifica si existe un jugador con el identificador dado.
     *
     * @param id Identificador del jugador.
     * @return boolean Verdadero si el jugador existe, falso en caso contrario.
     */
    public boolean exist(Long id) {
        return jugadorRepo.existsById(id);
    }

    /**
     * Crea un jugador temporal con nombre y color.
     *
     * @param nombre Nombre del jugador.
     * @param color Color del jugador.
     * @return Jugador El jugador temporal creado.
     */
    public Jugador crearJugadorTemporal(String nombre, String color) {
        Jugador j = new Jugador();
        j.setNombre(nombre);
        j.setColor(color);
        j.setActivo(true);
        jugadorRepo.save(j);
        return j;
    }

    /**
     * Verifica si un jugador controla un continente específico.
     *
     * @param idContinente Identificador del continente.
     * @param idJugador Identificador del jugador.
     * @param todosTerritorios Lista de todos los territorios.
     * @return boolean Verdadero si el jugador controla el continente, falso en caso contrario.
     */
    public boolean jugadorControlaContinente(Long idContinente, Long idJugador,
            MyLinkedList<TerritorioDTO> todosTerritorios) {
        boolean controla = true;
        for (int i = 0; i < todosTerritorios.size(); i++) {
            TerritorioDTO t = todosTerritorios.getPos(i).getInfo();
            if (t.getIdContinente().equals(idContinente)) {
                if (!t.getIdJugador().equals(idJugador)) {
                    controla = false;
                    break;
                }
            }
        }
        return controla;
    }

    /**
     * Agrega tropas disponibles a un jugador.
     *
     * @param idJugador Identificador del jugador.
     * @param cantidad Cantidad de tropas a agregar.
     */
    public void agregarTropas(Long idJugador, int cantidad) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.setTropasDisponibles(j.getTropasDisponibles() + cantidad);
            jugadorRepo.save(j);
        }
    }

    /**
     * Agrega un territorio controlado a un jugador.
     *
     * @param idJugador Identificador del jugador.
     */
    public void agregarTerritorio(Long idJugador) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.setTerritoriosControlados(j.getTerritoriosControlados() + 1);
            jugadorRepo.save(j);
        }
    }

    /**
     * Coloca tropas en un territorio específico.
     *
     * @param idJugador Identificador del jugador.
     * @param idTerritorio Identificador del territorio.
     * @param cantidad Cantidad de tropas a colocar.
     * @throws RuntimeException Si el jugador no tiene suficientes tropas disponibles o no controla el territorio.
     */
    public void colocarTropasEnTerritorio(Long idJugador, Long idTerritorio, int cantidad) {
        if (cantidad <= 0)
            return;
        Jugador jugador = jugadorRepo.findById(idJugador).orElse(null);
        if (jugador == null)
            return;
        if (jugador.getTropasDisponibles() < cantidad) {
            throw new RuntimeException("No tienes suficientes tropas disponibles");
        }
        TerritorioDTO territorio = territorioService.obtenerPorId(idTerritorio);
        if (!idJugador.equals(territorio.getIdJugador())) {
            throw new RuntimeException("No puedes colocar tropas en un territorio que no controlas");
        }
        territorioService.reforzar(idTerritorio, cantidad);
        jugador.setTropasDisponibles(jugador.getTropasDisponibles() - cantidad);
        jugadorRepo.save(jugador);
    }

    /**
     * Quita tropas disponibles a un jugador.
     *
     * @param idJugador Identificador del jugador.
     * @param cantidad Cantidad de tropas a quitar.
     */
    public void quitarTropas(Long idJugador, int cantidad) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            int nuevas = Math.max(0, j.getTropasDisponibles() - cantidad);
            j.setTropasDisponibles(nuevas);
            jugadorRepo.save(j);
        }
    }

    /**
     * Reinicia las tropas disponibles de un jugador a cero.
     *
     * @param idJugador Identificador del jugador.
     */
    public void resetTropas(Long idJugador) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.setTropasDisponibles(0);
            jugadorRepo.save(j);
        }
    }

    /**
     * Asigna una carta a un jugador.
     *
     * @param idJugador Identificador del jugador.
     * @param carta Carta a asignar.
     */
    public void darCarta(Long idJugador, Carta carta) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.getCartas().addLast(carta);
            jugadorRepo.save(j);
        }
    }

    /**
     * Quita una carta de la mano de un jugador.
     *
     * @param idJugador Identificador del jugador.
     * @param index Índice de la carta a quitar.
     */
    public void quitarCarta(Long idJugador, int index) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            if (index >= 0 && index < j.getCartas().size()) {
                j.getCartas().deleteAt(index);
                jugadorRepo.save(j);
            }
        }
    }

    /**
     * Desactiva un jugador.
     *
     * @param idJugador Identificador del jugador.
     */
    public void desactivarJugador(Long idJugador) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.setActivo(false);
            jugadorRepo.save(j);
        }
    }

    /**
     * Activa un jugador.
     *
     * @param idJugador Identificador del jugador.
     */
    public void activarJugador(Long idJugador) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.setActivo(true);
            jugadorRepo.save(j);
        }
    }

    /**
     * Reinicia los datos de un jugador a valores por defecto.
     *
     * @param id Identificador del jugador.
     */
    public void resetJugadorPorId(Long id) {
        Jugador j = jugadorRepo.findById(id).orElse(null);
        if (j != null) {
            j.setTropasDisponibles(0);
            j.setTerritoriosControlados(0);
            j.setActivo(true);
            j.setCartas(new MyLinkedList<>());
            jugadorRepo.save(j);
        }
    }

    /**
     * Busca un jugador por su nombre.
     *
     * @param nombre Nombre del jugador.
     * @return JugadorDTO El jugador encontrado en formato DTO, o null si no existe.
     */
    public JugadorDTO findByName(String nombre) {
        MyLinkedList<JugadorDTO> lista = getAll();
        for (int i = 0; i < lista.size(); i++) {
            JugadorDTO jugador = lista.get(i);
            if (jugador.getNombre().equals(nombre)) {
                return jugador;
            }
        }
        return null;
    }

    /**
     * Convierte una entidad Jugador a un DTO.
     *
     * @param jugador Entidad Jugador a convertir.
     * @return JugadorDTO DTO del jugador.
     */
    private JugadorDTO convertToDTO(Jugador jugador) {
        return modelMapper.map(jugador, JugadorDTO.class);
    }

    /**
     * Convierte un DTO Jugador a una entidad.
     *
     * @param jugadorDTO DTO del jugador a convertir.
     * @return Jugador Entidad Jugador.
     */
    @SuppressWarnings("unused")
    private Jugador convertToEntity(JugadorDTO jugadorDTO) {
        return modelMapper.map(jugadorDTO, Jugador.class);
    }

    /**
     * Busca un jugador por correo y contraseña.
     *
     * @param correo Correo del jugador.
     * @param contrasena Contraseña del jugador.
     * @return JugadorDTO El jugador encontrado en formato DTO, o null si no existe o la contraseña no coincide.
     */
    public JugadorDTO findByCorreoAndContrasena(String correo, String contrasena) {
        Jugador jugador = jugadorRepo.findByCorreo(correo);
        if (jugador != null && jugador.getContrasena().equals(contrasena)) {
            return convertToDTO(jugador);
        }
        return null;
    }
}
