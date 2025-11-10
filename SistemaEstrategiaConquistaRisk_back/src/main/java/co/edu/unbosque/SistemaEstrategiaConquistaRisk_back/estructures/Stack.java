package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures;

/**
 * Interfaz que define el comportamiento básico de una pila (LIFO: Last-In-First-Out).
 * Una pila es una estructura de datos lineal donde los elementos se insertan y eliminan
 * por el mismo extremo, conocido como la cima de la pila.
 *
 * @param <E> Tipo de los elementos almacenados en la pila.
 */
public interface Stack<E> {

    /**
     * Inserta un elemento en la cima de la pila.
     *
     * @param info Elemento a insertar en la pila.
     */
    void push(E info);

    /**
     * Elimina y devuelve el elemento en la cima de la pila.
     *
     * @return El elemento eliminado, o {@code null} si la pila está vacía.
     */
    E pop();

    /**
     * Devuelve el número de elementos en la pila.
     *
     * @return Número de elementos en la pila.
     */
    int size();
}
