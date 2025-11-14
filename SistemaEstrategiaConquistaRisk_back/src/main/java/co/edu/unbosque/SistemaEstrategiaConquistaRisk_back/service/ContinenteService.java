package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ContinenteDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Continente;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.ContinenteRepository;
import org.modelmapper.ModelMapper;

/**
 * Servicio que gestiona la lógica relacionada con los continentes del juego Risk.
 * Proporciona funcionalidades para obtener, buscar y calcular bonificaciones de tropas
 * asociadas a los continentes.
 */
@Service
public class ContinenteService {

    @Autowired
    private ContinenteRepository continenteRepository;

    @Autowired
    private ModelMapper mapper;

    /**
     * Obtiene todos los continentes registrados en el sistema y los convierte a objetos DTO.
     *
     * @return {@code MyLinkedList<ContinenteDTO>} Lista enlazada de continentes en formato DTO.
     *         Nunca es {@code null}, pero puede estar vacía si no hay continentes registrados.
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
     * Obtiene el bonus de tropas asignado por conquistar un continente específico.
     * Los valores de bonus están basados en las reglas oficiales del juego Risk.
     *
     * @param nombreContinente Nombre del continente para el cual se desea obtener el bonus.
     * @return {@code int} Bonus de tropas correspondientes al continente.
     *         Retorna 0 si el nombre del continente no coincide con ninguno de los definidos.
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
     * Busca un continente por su nombre y lo retorna en formato DTO.
     *
     * @param nombre Nombre del continente a buscar. La búsqueda no es sensible a mayúsculas/minúsculas.
     * @return {@code ContinenteDTO} El continente encontrado en formato DTO,
     *         o {@code null} si no se encuentra ningún continente con el nombre especificado.
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
