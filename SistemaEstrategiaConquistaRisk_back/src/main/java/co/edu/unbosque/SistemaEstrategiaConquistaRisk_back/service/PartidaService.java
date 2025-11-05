package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyDoubleLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.PartidaRepository;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final Gson gson = new Gson();

    public PartidaService(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    // ----------------------------------------------------------------------
    // ✅ Crear partida desde estructuras → guardar TODO como JSON
    // ----------------------------------------------------------------------
    @Transactional
    public Partida crearPartida(
            MyLinkedList<Long> ordenJugadores,
            MyLinkedList<?> territorios,
            MyDoubleLinkedList<?> mazoCartas) {

        Partida partida = new Partida();

        partida.setCodigoHash(UUID.randomUUID().toString().replace("-", ""));
        partida.setIniciada(true);
        partida.setFinalizada(false);
        partida.setGanadorId(null);
        partida.setFechaInicio(LocalDateTime.now());
        partida.setFechaFin(null);

        // Jugador actual = el primero del orden
        partida.setJugadorActualId(ordenJugadores.getPos(0).getInfo());

        // Serializar estructuras
        partida.setJugadoresOrdenTurnoJSON(gson.toJson(ordenJugadores));
        partida.setTerritoriosJSON(gson.toJson(territorios));
        partida.setMazoCartasJSON(gson.toJson(mazoCartas));

        return partidaRepository.save(partida);
    }

    // ----------------------------------------------------------------------
    // ✅ Deserializar JSON → MyLinkedList<Long> orden de jugadores
    // ----------------------------------------------------------------------
    public MyLinkedList<Long> cargarOrdenJugadores(Partida partida) {

        return gson.fromJson(
                partida.getJugadoresOrdenTurnoJSON(),
                new TypeToken<MyLinkedList<Long>>() {}.getType()
        );
    }

    // ----------------------------------------------------------------------
    // ✅ Deserializar territorios
    // ----------------------------------------------------------------------
    public <T> MyLinkedList<T> cargarTerritorios(Partida partida, Class<T> tipo) {

        return gson.fromJson(
                partida.getTerritoriosJSON(),
                TypeToken.getParameterized(MyLinkedList.class, tipo).getType()
        );
    }

    // ----------------------------------------------------------------------
    // ✅ Deserializar mazo
    // ----------------------------------------------------------------------
    public <T> MyDoubleLinkedList<T> cargarMazo(Partida partida, Class<T> tipo) {

        return gson.fromJson(
                partida.getMazoCartasJSON(),
                TypeToken.getParameterized(MyDoubleLinkedList.class, tipo).getType()
        );
    }

    // ----------------------------------------------------------------------
    // ✅ Guardar cambios
    // ----------------------------------------------------------------------
    @Transactional
    public Partida guardarPartida(Partida partida,
                                  MyLinkedList<?> territorios,
                                  MyLinkedList<Long> ordenJugadores,
                                  MyDoubleLinkedList<?> mazo) {

        partida.setTerritoriosJSON(gson.toJson(territorios));
        partida.setJugadoresOrdenTurnoJSON(gson.toJson(ordenJugadores));
        partida.setMazoCartasJSON(gson.toJson(mazo));

        return partidaRepository.save(partida);
    }

    // ----------------------------------------------------------------------
    // ✅ Avanzar turno circular
    // ----------------------------------------------------------------------
    @Transactional
    public Partida avanzarTurno(Long partidaId) {

        Partida partida = partidaRepository.findById(partidaId).orElse(null);
        if (partida == null) return null;

        MyLinkedList<Long> orden = cargarOrdenJugadores(partida);

        // Buscar índice del jugador actual
        long actual = partida.getJugadorActualId();
        int index = orden.indexOf(actual);

        // Siguiente
        index = (index + 1) % orden.size();

        partida.setJugadorActualId(orden.getPos(index).getInfo());

        // Guardar turno sin modificar estructuras
        return partidaRepository.save(partida);
    }

    // ----------------------------------------------------------------------
    // ✅ Finalizar partida
    // ----------------------------------------------------------------------
    @Transactional
    public Partida finalizarPartida(Long partidaId, Long ganadorId) {

        Partida partida = partidaRepository.findById(partidaId).orElse(null);
        if (partida == null) return null;

        partida.setFinalizada(true);
        partida.setGanadorId(ganadorId);
        partida.setFechaFin(LocalDateTime.now());

        return partidaRepository.save(partida);
    }

    // ----------------------------------------------------------------------
    // ✅ Obtener por hash
    // ----------------------------------------------------------------------
    public Partida cargarPorHash(String hash) {
        return partidaRepository.findByCodigoHash(hash).orElse(null);
    }

    // ----------------------------------------------------------------------
    // ✅ Listar partidas guardadas (Iterable — no usa List)
    // ----------------------------------------------------------------------
    public Iterable<Partida> listarPartidas() {
        return partidaRepository.findAll();
    }
}
