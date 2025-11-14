package co.edu.unbosque.estructures;

/**
 * Clase que representa un grafo.
 * Un grafo está compuesto por una lista de vértices, donde cada vértice puede estar conectado
 * a otros mediante aristas.
 */
public class Graph {

    /**
     * Lista de vértices que componen el grafo.
     */
    private MyLinkedList<Vertex<?>> listOfNodes;

    /**
     * Constructor por defecto de la clase Graph.
     * Inicializa una lista vacía de vértices.
     */
    public Graph() {
        listOfNodes = new MyLinkedList<>();
    }

    /**
     * Añade un vértice al grafo.
     *
     * @param v Vértice a añadir.
     */
    public void addVertex(Vertex<?> v) {
        listOfNodes.addLast(v);
    }

    /**
     * Inserta una arista entre dos vértices del grafo con un valor asociado.
     * Si alguno de los vértices es nulo, la operación no se realiza.
     *
     * @param a     Vértice de origen.
     * @param b     Vértice de destino.
     * @param value Valor asociado a la arista.
     */
    public void insertEdge(Vertex<?> a, Vertex<?> b, double value) {
        if (a == null || b == null)
            return;
        Edge e = new Edge(a, b, value);
        a.addEdge(e);
    }

    /**
     * Obtiene la lista de vértices del grafo.
     *
     * @return Lista de vértices.
     */
    public MyLinkedList<Vertex<?>> getListOfNodes() {
        return listOfNodes;
    }

    /**
     * Establece la lista de vértices del grafo.
     *
     * @param listOfNodes Lista de vértices a establecer.
     */
    public void setListOfNodes(MyLinkedList<Vertex<?>> listOfNodes) {
        this.listOfNodes = listOfNodes;
    }
}
