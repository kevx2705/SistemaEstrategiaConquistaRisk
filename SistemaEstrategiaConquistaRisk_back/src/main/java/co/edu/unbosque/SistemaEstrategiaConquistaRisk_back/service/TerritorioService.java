package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Territorio;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.Node;
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
    public boolean controlaContinente(Long idJugador, Long idContinente) {
        MyLinkedList<TerritorioDTO> territorios = obtenerPorContinente(idContinente);
        
        for (int i = 0; i < territorios.size(); i++) {
            if (!territorios.getPos(i).getInfo().getIdJugador().equals(idJugador)) {
                return false;
            }
        }
        return true;
    }

    public int contarTerritoriosDeJugador(Long idJugador) {
        return obtenerPorJugador(idJugador).size();
    }
 // ✅ Obtener todos los territorios que pertenecen a un jugador
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
        Territorio t = territorioRepository.findById(idTerritorio).orElse(null);
        if (t != null) {
            t.setTropas(t.getTropas() + cantidad);
            territorioRepository.save(t);
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
 // 🔹 Buscar un TerritorioDTO dentro de una lista por su id
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
