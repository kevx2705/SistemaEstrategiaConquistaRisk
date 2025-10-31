package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Territorio;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.TerritorioRepository;

@Service
public class TerritorioService {

    @Autowired
    private TerritorioRepository territorioRepository;

    // Obtener todos los territorios como DTOs
    public MyLinkedList<TerritorioDTO> obtenerTodos() {
        MyLinkedList<TerritorioDTO> listaDTO = new MyLinkedList<>();

        for (Territorio t : territorioRepository.findAll()) {
            TerritorioDTO dto = new TerritorioDTO();
            dto.setId(t.getId()); // usa el id real
            dto.setNombre(t.getNombre());
            dto.setTropas(t.getTropas());
            dto.setIdContinente(t.getIdContinente());
            dto.setIdJugador(t.getIdJugador());
            listaDTO.addLast(dto);
        }

        return listaDTO;
    }

    // 🔹 Buscar entidad por nombre (uso interno del service)
    private Territorio buscarEntidadPorNombre(String nombre) {
        for (Territorio t : territorioRepository.findAll()) {
            if (t.getNombre().equalsIgnoreCase(nombre)) {
                return t;
            }
        }
        return null;
    }
 // ✅ Obtener territorios por continente
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

    // ✅ Asignar propietario a un territorio
    public void asignarJugador(Long idTerritorio, Long idJugador) {
        Territorio territorio = territorioRepository.findById(idTerritorio).orElse(null);
        if (territorio != null) {
            territorio.setIdJugador(idJugador);
            territorioRepository.save(territorio);
        }
    }

    // ✅ Reforzar (añadir tropas)
    public void reforzar(Long idTerritorio, int cantidad) {
        Territorio territorio = territorioRepository.findById(idTerritorio).orElse(null);
        if (territorio != null) {
            territorio.setTropas(territorio.getTropas() + cantidad);
            territorioRepository.save(territorio);
        }
    }

    // ✅ Quitar tropas (por ataque)
    public void quitarTropas(Long idTerritorio, int cantidad) {
        Territorio territorio = territorioRepository.findById(idTerritorio).orElse(null);
        if (territorio != null) {
            int nuevasTropas = Math.max(0, territorio.getTropas() - cantidad);
            territorio.setTropas(nuevasTropas);
            territorioRepository.save(territorio);
        }
    }
    public void quitarTodasLasTropas(Long idTerritorio) {
        Territorio territorio = territorioRepository.findById(idTerritorio).orElse(null);
        if (territorio != null) {
            territorio.setTropas(0);
            territorioRepository.save(territorio);
        }
    }
 // ✅ Reiniciar todos los territorios (sin jugador y sin tropas)
    public void reiniciarTodos() {
        for (Territorio t : territorioRepository.findAll()) {
            t.setIdJugador(0L);
            t.setTropas(0);
            territorioRepository.save(t);
        }
    }


    // ✅ Verificar si un jugador controla todos los territorios de un continente
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
}
