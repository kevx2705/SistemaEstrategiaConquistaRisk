package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementación de una pila (LIFO) utilizando una lista doble enlazada ({@code MyDequeList}).
 * Esta clase permite operaciones básicas de pila como apilar, desapilar y obtener el tamaño.
 *
 * @param <E> Tipo de los elementos almacenados en la pila.
 * @see Stack
 * @see MyDequeList
 */
public class StackImpl<E> implements Stack<E> {

    /** Estructura interna que almacena los elementos de la pila. */
    private MyDequeList<E> data;

    /**
     * Constructor por defecto.
     * Crea una pila vacía.
     */
    public StackImpl() {
        data = new MyDequeList<E>();
        Queue<String> q = new LinkedList<String>(); // Nota: Esta línea no se utiliza en la implementación.
    }

    /**
     * Inserta un elemento en la parte superior de la pila.
     *
     * @param info Elemento a insertar en la pila.
     */
    @Override
    public void push(E info) {
        data.insertFirst(info);
    }

    /**
     * Elimina y devuelve el elemento en la parte superior de la pila.
     *
     * @return El elemento eliminado, o {@code null} si la pila está vacía.
     */
    @Override
    public E pop() {
        return data.removeFirst();
    }

    /**
     * Devuelve el número de elementos en la pila.
     *
     * @return Número de elementos en la pila.
     */
    @Override
    public int size() {
        return data.size();
    }

    /**
     * Devuelve una representación en cadena de la pila.
     *
     * @return Cadena que representa los elementos de la pila.
     */
    @Override
    public String toString() {
        return data.toString();
    }
}
