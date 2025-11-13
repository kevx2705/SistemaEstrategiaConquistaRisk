package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres;

/**
 * Clase que representa un vértice en una estructura de grafo.
 * Cada vértice contiene información genérica y una lista de aristas adyacentes.
 *
 * @param <T> Tipo genérico de la información almacenada en el vértice.
 */
public class Vertex<T> {
    private T info;
    private MyLinkedList<Edge> adyacentEdges;

    /**
     * Constructor para crear un nuevo vértice con la información especificada.
     *
     * @param info Información genérica que se almacenará en el vértice.
     */
    public Vertex(T info) {
        this.info = info;
        adyacentEdges = new MyLinkedList<Edge>();
    }

    /**
     * Agrega una arista a la lista de aristas adyacentes del vértice.
     *
     * @param e Arista que se agregará a la lista de adyacentes.
     */
    public void addEdge(Edge e) {
        adyacentEdges.addLast(e);
    }

    /**
     * Obtiene la información almacenada en el vértice.
     *
     * @return Información genérica almacenada en el vértice.
     */
    public T getInfo() {
        return info;
    }

    /**
     * Establece la información del vértice.
     *
     * @param info Nueva información genérica que se almacenará en el vértice.
     */
    public void setInfo(T info) {
        this.info = info;
    }

    /**
     * Obtiene la lista de aristas adyacentes al vértice.
     *
     * @return Lista de aristas adyacentes.
     */
    public MyLinkedList<Edge> getAdyacentEdges() {
        return adyacentEdges;
    }

    /**
     * Establece la lista de aristas adyacentes del vértice.
     *
     * @param adyacentEdges Nueva lista de aristas adyacentes.
     */
    public void setAdyacentEdges(MyLinkedList<Edge> adyacentEdges) {
        this.adyacentEdges = adyacentEdges;
    }

    /**
     * Devuelve una representación en cadena del vértice,
     * incluyendo su información y las aristas adyacentes.
     *
     * @return Cadena que representa al vértice.
     */
    @Override
    public String toString() {
        return "\nVertex [info=" + info + ", adyacentEdges=" + adyacentEdges + "]";
    }
}
