package co.edu.unbosque.estructures;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementación de una pila (LIFO) utilizando una lista doble enlazada
 * ({@code MyDequeList}). Esta clase permite operaciones básicas de pila como
 * apilar, desapilar y obtener el tamaño.
 *
 * @param <E> Tipo de los elementos almacenados en la pila.
 * @see Stack
 * @see MyDequeList
 */
public class StackImpl<E> implements Stack<E> {

    /**
     * Estructura interna que almacena los elementos de la pila.
     */
    private MyDequeList<E> data;

    /**
     * Constructor por defecto. Crea una pila vacía.
     */
    public StackImpl() {
        data = new MyDequeList<E>();
        Queue<String> q = new LinkedList<String>();
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
     * Verifica si la pila está vacía.
     *
     * @return {@code true} si la pila está vacía, {@code false} en caso contrario.
     */
    public boolean isEmpty() {
        return data.size() == 0;
    }

    /**
     * Crea y devuelve una copia exacta de la pila actual.
     * La pila original no se modifica durante el proceso.
     *
     * @return Una nueva pila con los mismos elementos que la pila original.
     */
    public StackImpl<E> copiar() {
        StackImpl<E> temp = new StackImpl<>();
        StackImpl<E> copia = new StackImpl<>();
        while (!this.isEmpty()) {
            temp.push(this.pop());
        }
        while (!temp.isEmpty()) {
            E elem = temp.pop();
            this.push(elem);
            copia.push(elem);
        }
        return copia;
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
