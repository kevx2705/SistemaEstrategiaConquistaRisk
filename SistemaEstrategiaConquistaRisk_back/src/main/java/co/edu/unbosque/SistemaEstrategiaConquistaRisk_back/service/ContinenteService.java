package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ContinenteDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Continente;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.ContinenteRepository;
import org.modelmapper.ModelMapper;

@Service
public class ContinenteService {

    @Autowired
    private static ContinenteRepository continenteRepository;

    @Autowired
    private static ModelMapper mapper;

    // Obtener todos los continentes como DTOs
    public static MyLinkedList<ContinenteDTO> obtenerTodos() {
        MyLinkedList<ContinenteDTO> listaDTO = new MyLinkedList<>();

        for (Continente c : continenteRepository.findAll()) {
            ContinenteDTO dto = mapper.map(c, ContinenteDTO.class);
            listaDTO.addLast(dto);
        }

        return listaDTO;
    }
    public static int getBonusPorContinente(String nombreContinente) {
        return switch(nombreContinente) {
            case "Oceanía", "América del Sur" -> 2;
            case "África" -> 3;
            case "Europa", "América del Norte" -> 5;
            case "Asia" -> 7;
            default -> 0;
        };
    }


    // Buscar un continente por su nombre y devolverlo como DTO
    public ContinenteDTO buscarPorNombre(String nombre) {
        for (Continente c : continenteRepository.findAll()) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return mapper.map(c, ContinenteDTO.class);
            }
        }
        return null;
    }
}
