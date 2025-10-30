package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres;

/**
 * Interfaz que define las operaciones básicas de una cola doble (deque).
 * Permite inserciones y eliminaciones eficientes por ambos extremos de la estructura.
 *
 * @param <E> Tipo genérico de los elementos almacenados en la cola doble.
 */
public interface Dqueue<E> {

    /**
     * Inserta un elemento al final de la cola doble.
     *
     * @param info Elemento a insertar. No debe ser {@code null}.
     */
    public void insertLast(E info);

    /**
     * Elimina y devuelve el primer elemento de la cola doble.
     *
     * @return El elemento eliminado.
     * @throws java.util.NoSuchElementException si la cola doble está vacía.
     */
    public E removeFirst();

    /**
     * Elimina y devuelve el último elemento de la cola doble.
     *
     * @return El elemento eliminado.
     * @throws java.util.NoSuchElementException si la cola doble está vacía.
     */
    public E removeLast();

    /**
     * Devuelve la cantidad de elementos almacenados en la cola doble.
     *
     * @return Número de elementos en la cola doble.
     */
    public int size();

    /**
     * Inserta un elemento al inicio de la cola doble.
     *
     * @param info Elemento a insertar. No debe ser {@code null}.
     */
    public void insertFirst(E info);
}
