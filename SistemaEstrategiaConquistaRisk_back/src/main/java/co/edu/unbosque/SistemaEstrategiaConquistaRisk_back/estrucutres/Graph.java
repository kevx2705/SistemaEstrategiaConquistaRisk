package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres;

/**
 * Clase que representa un grafo utilizando una lista de vértices.
 * Permite agregar vértices y aristas entre ellos.
 * Cada vértice puede contener datos genéricos y estar conectado a otros vértices mediante aristas.
 */
public class Graph {

    /** Lista de vértices que componen el grafo. */
    private MyLinkedList<Vertex<?>> listOfNodes;

    /**
     * Constructor por defecto de la clase Graph.
     * Inicializa una lista vacía de vértices.
     */
    public Graph() {
        listOfNodes = new MyLinkedList<>();
    }

    /**
     * Agrega un vértice al grafo.
     *
     * @param v Vértice a agregar.
     */
    public void addVertex(Vertex<?> v) {
        listOfNodes.addLast(v);
    }

    /**
     * Inserta una arista entre dos vértices del grafo.
     * Si alguno de los vértices es nulo, no se realiza ninguna acción.
     *
     * @param a     Vértice de origen.
     * @param b     Vértice de destino.
     * @param value Valor asociado a la arista (peso, distancia, costo, etc.).
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
     * @return La lista de vértices del grafo.
     */
    public MyLinkedList<Vertex<?>> getListOfNodes() {
        return listOfNodes;
    }

    /**
     * Establece la lista de vértices del grafo.
     *
     * @param listOfNodes La lista de vértices a establecer.
     */
    public void setListOfNodes(MyLinkedList<Vertex<?>> listOfNodes) {
        this.listOfNodes = listOfNodes;
    }
}
