package co.edu.unbosque.estructures;

/**
 * Clase que representa un vértice en un grafo.
 * Cada vértice contiene información genérica y una lista de aristas adyacentes.
 *
 * @param <T> Tipo de la información almacenada en el vértice.
 */
public class Vertex<T> {

    /**
     * Información almacenada en el vértice.
     */
    private T info;

    /**
     * Lista de aristas adyacentes al vértice.
     */
    private MyLinkedList<Edge> adyacentEdges;

    /**
     * Constructor que inicializa un vértice con la información proporcionada.
     * También inicializa una lista vacía de aristas adyacentes.
     *
     * @param info Información a almacenar en el vértice.
     */
    public Vertex(T info) {
        this.info = info;
        adyacentEdges = new MyLinkedList<Edge>();
    }

    /**
     * Añade una arista a la lista de aristas adyacentes del vértice.
     *
     * @param e Arista a añadir.
     */
    public void addEdge(Edge e) {
        adyacentEdges.addLast(e);
    }

    /**
     * Obtiene la información almacenada en el vértice.
     *
     * @return Información almacenada en el vértice.
     */
    public T getInfo() {
        return info;
    }

    /**
     * Establece la información almacenada en el vértice.
     *
     * @param info Información a establecer.
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
     * Establece la lista de aristas adyacentes al vértice.
     *
     * @param adyacentEdges Lista de aristas adyacentes a establecer.
     */
    public void setAdyacentEdges(MyLinkedList<Edge> adyacentEdges) {
        this.adyacentEdges = adyacentEdges;
    }

    /**
     * Devuelve una representación en cadena del vértice, incluyendo su información
     * y sus aristas adyacentes.
     *
     * @return Representación en cadena del vértice.
     */
    @Override
    public String toString() {
        return "\nVertex [info=" + info + ", adyacentEdges=" + adyacentEdges + "]";
    }
}
