package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.JugadorRepository;

@Service
public class JugadorService implements CRUDOperation<JugadorDTO> {

    @Autowired
    private JugadorRepository jugadorRepo;

    @Autowired
    private ModelMapper modelMapper;

    public JugadorService() {}

    // ==========================================================
    // ✅ CRUD NORMAL (igual al AdminService)
    // ==========================================================

    @Override
    public int create(JugadorDTO newData) {
        Jugador entity = modelMapper.map(newData, Jugador.class);
        jugadorRepo.save(entity);
        return 0;
    }

    @Override
    public List<JugadorDTO> getAll() {
        List<Jugador> entityList = jugadorRepo.findAll();
        List<JugadorDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> {
            JugadorDTO dto = modelMapper.map(entity, JugadorDTO.class);
            dtoList.add(dto);
        });

        return dtoList;
    }

    @Override
    public int deleteById(Long id) {
        if (jugadorRepo.existsById(id)) {
            jugadorRepo.deleteById(id);
            return 0;
        }
        return 1;
    }

    @Override
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
            entity.setCartas(newData.getCartas());

            jugadorRepo.save(entity);
            return 0;
        }

        return 1;
    }

    @Override
    public Long count() {
        return (long) getAll().size();
    }

    @Override
    public boolean exist(Long id) {
        return jugadorRepo.existsById(id);
    }

    // ==========================================================
    // ✅ FUNCIONES ESPECIALES PARA EL JUEGO RISK
    // ==========================================================

    // ✅ Añadir tropas al jugador
    public void agregarTropas(Long idJugador, int cantidad) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.setTropasDisponibles(j.getTropasDisponibles() + cantidad);
            jugadorRepo.save(j);
        }
    }

    // ✅ Quitar tropas disponibles
    public void quitarTropas(Long idJugador, int cantidad) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            int nuevas = Math.max(0, j.getTropasDisponibles() - cantidad);
            j.setTropasDisponibles(nuevas);
            jugadorRepo.save(j);
        }
    }

    // ✅ Reiniciar tropas disponibles a 0
    public void resetTropas(Long idJugador) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.setTropasDisponibles(0);
            jugadorRepo.save(j);
        }
    }

    // ✅ Dar una carta al jugador
    public void darCarta(Long idJugador, Carta carta) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.getCartas().addLast(carta);
            jugadorRepo.save(j);
        }
    }

    // ✅ Quitar una carta por índice
    public void quitarCarta(Long idJugador, int index) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            if (index >= 0 && index < j.getCartas().size()) {
                j.getCartas().deleteAt(index);
                jugadorRepo.save(j);
            }
        }
    }

    // ✅ Desactivar jugador (cuando pierde)
    public void desactivarJugador(Long idJugador) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.setActivo(false);
            jugadorRepo.save(j);
        }
    }

    // ✅ Activar jugador (nueva partida)
    public void activarJugador(Long idJugador) {
        Jugador j = jugadorRepo.findById(idJugador).orElse(null);
        if (j != null) {
            j.setActivo(true);
            jugadorRepo.save(j);
        }
    }

    // ✅ Reset general (para iniciar nueva partida)
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

}
