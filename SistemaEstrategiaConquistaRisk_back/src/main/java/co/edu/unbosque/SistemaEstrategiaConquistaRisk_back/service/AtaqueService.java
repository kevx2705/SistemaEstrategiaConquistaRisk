package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ResultadoAtaqueDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.Node;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.PartidaRepository;

/**
 * Servicio que gestiona la lógica de los ataques entre territorios en una partida.
 * Proporciona métodos para realizar ataques, lanzar dados, ordenar resultados,
 * y buscar nodos específicos en listas enlazadas.
 */
@Service
public class AtaqueService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private MapaTerritorioService mapa;

    private final Gson gson = new Gson();

    /**
     * Realiza un ataque entre dos territorios en una partida.
     *
     * @param partidaId Identificador de la partida.
     * @param atacanteId Identificador del jugador atacante.
     * @param territorioAtacanteId Identificador del territorio atacante.
     * @param territorioDefensorId Identificador del territorio defensor.
     * @return ResultadoAtaqueDTO Resultado del ataque, incluyendo dados lanzados,
     *         bajas, y estado de conquista.
     * @throws RuntimeException Si la partida no existe, los datos son inválidos,
     *         el jugador no es dueño del territorio atacante, no hay tropas suficientes,
     *         o los territorios no son adyacentes.
     */
    public ResultadoAtaqueDTO atacar(Long partidaId, Long atacanteId, Long territorioAtacanteId,
            Long territorioDefensorId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("La partida no existe"));
        MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(),
                new TypeToken<MyLinkedList<TerritorioDTO>>() {
                }.getType());
        MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
                new TypeToken<MyLinkedList<JugadorDTO>>() {
                }.getType());
        Node<TerritorioDTO> nodoAtacante = getNodoPorId(territorios, territorioAtacanteId);
        Node<TerritorioDTO> nodoDefensor = getNodoPorId(territorios, territorioDefensorId);
        Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, atacanteId);
        if (nodoAtacante == null || nodoDefensor == null || nodoJugador == null)
            throw new RuntimeException("Datos inválidos");
        TerritorioDTO terrAtacante = nodoAtacante.getInfo();
        TerritorioDTO terrDefensor = nodoDefensor.getInfo();
        JugadorDTO jugador = nodoJugador.getInfo();
        if (!terrAtacante.getIdJugador().equals(atacanteId))
            throw new RuntimeException("No eres dueño del territorio atacante");
        if (terrAtacante.getTropas() <= 1)
            throw new RuntimeException("No tienes tropas suficientes para atacar");
        if (!mapa.sonVecinos(territorioAtacanteId, territorioDefensorId))
            throw new RuntimeException("Los territorios no son adyacentes");
        int dadosAtacante = Math.min(3, terrAtacante.getTropas() - 1);
        int dadosDefensor = Math.min(2, terrDefensor.getTropas());
        int[] dadosA = lanzarDados(dadosAtacante);
        int[] dadosD = lanzarDados(dadosDefensor);
        ordenarDesc(dadosA);
        ordenarDesc(dadosD);
        int comparaciones = Math.min(dadosA.length, dadosD.length);
        int bajasAtacante = 0;
        int bajasDefensor = 0;
        for (int i = 0; i < comparaciones; i++) {
            if (dadosA[i] > dadosD[i]) {
                terrDefensor.setTropas(terrDefensor.getTropas() - 1);
                bajasDefensor++;
            } else {
                terrAtacante.setTropas(terrAtacante.getTropas() - 1);
                bajasAtacante++;
            }
        }
        boolean conquista = false;
        if (terrDefensor.getTropas() <= 0) {
            terrDefensor.setIdJugador(atacanteId);
            int moverTropas = Math.max(1, dadosAtacante - bajasAtacante);
            terrDefensor.setTropas(moverTropas);
            terrAtacante.setTropas(terrAtacante.getTropas() - moverTropas);
            jugador.setTerritoriosControlados(jugador.getTerritoriosControlados() + 1);
            conquista = true;
        }
        partida.setTerritoriosJSON(gson.toJson(territorios));
        partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores));
        partidaRepository.save(partida);
        ResultadoAtaqueDTO resultado = new ResultadoAtaqueDTO();
        resultado.setDadosAtacante(dadosA);
        resultado.setDadosDefensor(dadosD);
        resultado.setPerdidasAtacante(bajasAtacante);
        resultado.setPerdidasDefensor(bajasDefensor);
        resultado.setConquista(conquista);
        resultado.setNuevoDueno(terrDefensor.getIdJugador());
        resultado.setTropasAtacanteRestantes(terrAtacante.getTropas());
        resultado.setTropasDefensorRestantes(terrDefensor.getTropas());
        return resultado;
    }

    /**
     * Lanza un conjunto de dados aleatorios.
     *
     * @param n Número de dados a lanzar.
     * @return Arreglo de enteros con los valores de los dados lanzados.
     */
    private int[] lanzarDados(int n) {
        int[] r = new int[n];
        for (int i = 0; i < n; i++)
            r[i] = (int) (Math.random() * 6) + 1;
        return r;
    }

    /**
     * Ordena un arreglo de enteros en orden descendente.
     *
     * @param arr Arreglo de enteros a ordenar.
     */
    private void ordenarDesc(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] > arr[i]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
    }

    /**
     * Busca un nodo en una lista enlazada por su identificador.
     *
     * @param <E> Tipo genérico de la información en la lista.
     * @param lista Lista enlazada donde buscar.
     * @param id Identificador del nodo a buscar.
     * @return Node<E> Nodo encontrado, o null si no se encuentra.
     */
    private <E> Node<E> getNodoPorId(MyLinkedList<E> lista, Long id) {
        Node<E> current = lista.getFirst();
        while (current != null) {
            E info = current.getInfo();
            if (info instanceof TerritorioDTO && ((TerritorioDTO) info).getId().equals(id))
                return current;
            if (info instanceof JugadorDTO && ((JugadorDTO) info).getId().equals(id))
                return current;
            current = current.getNext();
        }
        return null;
    }
}
