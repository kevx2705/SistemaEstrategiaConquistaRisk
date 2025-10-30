package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Continente;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.ContinenteRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ContinenteService {

    @Autowired
    private ContinenteRepository continenteRepository;

    // Obtener todos los continentes (por ejemplo, para inicializar el mapa)
    public MyLinkedList<Continente> obtenerTodos() {
        MyLinkedList<Continente> lista = new MyLinkedList<>();
        for (Continente c : continenteRepository.findAll()) {
            lista.addLast(c);
        }
        return lista;
    }

    // Buscar un continente por su nombre
    public Continente buscarPorNombre(String nombre) {
        for (Continente c : continenteRepository.findAll()) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return c;
            }
        }
        return null;
    }
}
