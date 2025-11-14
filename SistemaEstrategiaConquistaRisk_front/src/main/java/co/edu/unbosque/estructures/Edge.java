package co.edu.unbosque.estructures;

/**
 * Clase que representa una arista en un grafo.
 * Una arista conecta dos vértices y puede tener un valor asociado.
 */
public class Edge {

    /**
     * Vértice de origen de la arista.
     */
    private Vertex<?> source;

    /**
     * Vértice de destino de la arista.
     */
    private Vertex<?> destination;

    /**
     * Valor asociado a la arista, puede representar peso, costo, distancia, etc.
     */
    private double value;

    /**
     * Constructor por defecto de la clase Edge.
     */
    public Edge() {
    }

    /**
     * Constructor que inicializa una arista con vértices de origen y destino, y un valor asociado.
     *
     * @param source      Vértice de origen.
     * @param destination Vértice de destino.
     * @param value       Valor asociado a la arista.
     */
    public Edge(Vertex<?> source, Vertex<?> destination, double value) {
        this.source = source;
        this.destination = destination;
        this.value = value;
    }

    /**
     * Obtiene el vértice de origen de la arista.
     *
     * @return Vértice de origen.
     */
    public Vertex<?> getSource() {
        return source;
    }

    /**
     * Establece el vértice de origen de la arista.
     *
     * @param source Vértice de origen a establecer.
     */
    public void setSource(Vertex<?> source) {
        this.source = source;
    }

    /**
     * Obtiene el vértice de destino de la arista.
     *
     * @return Vértice de destino.
     */
    public Vertex<?> getDestination() {
        return destination;
    }

    /**
     * Establece el vértice de destino de la arista.
     *
     * @param destination Vértice de destino a establecer.
     */
    public void setDestination(Vertex<?> destination) {
        this.destination = destination;
    }

    /**
     * Obtiene el valor asociado a la arista.
     *
     * @return Valor asociado a la arista.
     */
    public double getValue() {
        return value;
    }

    /**
     * Establece el valor asociado a la arista.
     *
     * @param value Valor a establecer.
     */
    public void setValue(double value) {
        this.value = value;
    }
}
