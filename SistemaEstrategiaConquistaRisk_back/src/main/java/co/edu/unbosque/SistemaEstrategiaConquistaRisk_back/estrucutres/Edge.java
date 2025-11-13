package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres;

/**
 * Clase que representa una arista en una estructura de grafo.
 * Una arista conecta dos vértices y tiene un valor asociado,
 * que puede representar peso, distancia, costo, etc.
 *
 * @param <T> Tipo genérico del dato almacenado en los vértices conectados.
 */
public class Edge<T> {

    /** Vértice de origen de la arista. */
    private Vertex<?> source;

    /** Vértice de destino de la arista. */
    private Vertex<?> destination;

    /** Valor asociado a la arista (peso, distancia, costo, etc.). */
    private double value;

    /**
     * Constructor por defecto de la clase Edge.
     */
    public Edge() {
    }

    /**
     * Constructor parametrizado de la clase Edge.
     *
     * @param source      Vértice de origen de la arista.
     * @param destination Vértice de destino de la arista.
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
     * @return El vértice de origen.
     */
    public Vertex<?> getSource() {
        return source;
    }

    /**
     * Establece el vértice de origen de la arista.
     *
     * @param source El vértice de origen a establecer.
     */
    public void setSource(Vertex<?> source) {
        this.source = source;
    }

    /**
     * Obtiene el vértice de destino de la arista.
     *
     * @return El vértice de destino.
     */
    public Vertex<?> getDestination() {
        return destination;
    }

    /**
     * Establece el vértice de destino de la arista.
     *
     * @param destination El vértice de destino a establecer.
     */
    public void setDestination(Vertex<?> destination) {
        this.destination = destination;
    }

    /**
     * Obtiene el valor asociado a la arista.
     *
     * @return El valor asociado a la arista.
     */
    public double getValue() {
        return value;
    }

    /**
     * Establece el valor asociado a la arista.
     *
     * @param value El valor a establecer.
     */
    public void setValue(double value) {
        this.value = value;
    }
}
