package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ContinenteDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Continente;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.ContinenteRepository;
import org.modelmapper.ModelMapper;

/**
 * Servicio que gestiona la lógica relacionada con los continentes del juego.
 * Proporciona funcionalidades para obtener todos los continentes,
 * buscar un continente por su nombre y obtener el bonus de tropas por continente.
 */
@Service
public class ContinenteService {

    @Autowired
    private ContinenteRepository continenteRepository;

    @Autowired
    private ModelMapper mapper;

    /**
     * Obtiene todos los continentes y los convierte a DTO.
     *
     * @return MyLinkedList<ContinenteDTO> Lista enlazada de continentes en formato DTO.
     */
    public MyLinkedList<ContinenteDTO> obtenerTodos() {
        MyLinkedList<ContinenteDTO> listaDTO = new MyLinkedList<>();
        for (Continente c : continenteRepository.findAll()) {
            ContinenteDTO dto = mapper.map(c, ContinenteDTO.class);
            listaDTO.addLast(dto);
        }
        return listaDTO;
    }

    /**
     * Obtiene el bonus de tropas por conquistar un continente específico.
     *
     * @param nombreContinente Nombre del continente.
     * @return int Bonus de tropas correspondientes al continente.
     */
    public int getBonusPorContinente(String nombreContinente) {
        return switch (nombreContinente) {
            case "Oceanía", "América del Sur" -> 2;
            case "África" -> 3;
            case "Europa", "América del Norte" -> 5;
            case "Asia" -> 7;
            default -> 0;
        };
    }

    /**
     * Busca un continente por su nombre.
     *
     * @param nombre Nombre del continente a buscar.
     * @return ContinenteDTO El continente encontrado en formato DTO, o null si no se encuentra.
     */
    public ContinenteDTO buscarPorNombre(String nombre) {
        for (Continente c : continenteRepository.findAll()) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return mapper.map(c, ContinenteDTO.class);
            }
        }
        return null;
    }
}
