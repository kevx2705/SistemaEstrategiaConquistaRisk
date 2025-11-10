package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres;

import java.io.Serializable;

/**
 * Clase que representa un nodo de una lista enlazada genérica.
 * Cada nodo almacena un elemento de tipo genérico y una referencia al siguiente nodo.
 *
 * @param <E> Tipo de dato almacenado en el nodo.
 */
public class Node<E> implements Serializable {

    /** Versión de serialización para la compatibilidad de objetos serializados. */
    private static final long serialVersionUID = 1L;

    /** Información almacenada en el nodo. */
    private E info;

    /** Referencia al siguiente nodo en la lista. */
    private Node<E> next;

    /**
     * Constructor para crear un nodo con información y referencia al siguiente nodo.
     *
     * @param info Información a almacenar en el nodo.
     * @param next Referencia al siguiente nodo.
     */
    public Node(E info, Node<E> next) {
        this.info = info;
        this.next = next;
    }

    /**
     * Constructor para crear un nodo con información y sin referencia al siguiente nodo.
     *
     * @param info Información a almacenar en el nodo.
     */
    public Node(E info) {
        this(info, null);
    }

    /**
     * Constructor por defecto.
     * Crea un nodo sin información y sin referencia al siguiente nodo.
     */
    public Node() {
        this(null, null);
    }

    /**
     * Obtiene la información almacenada en el nodo.
     *
     * @return La información almacenada en el nodo.
     */
    public E getInfo() {
        return this.info;
    }

    /**
     * Obtiene la referencia al siguiente nodo.
     *
     * @return Referencia al siguiente nodo.
     */
    public Node<E> getNext() {
        return this.next;
    }

    /**
     * Establece la información a almacenar en el nodo.
     *
     * @param info Información a almacenar.
     */
    public void setInfo(E info) {
        this.info = info;
    }

    /**
     * Establece la referencia al siguiente nodo.
     *
     * @param next Referencia al siguiente nodo.
     */
    public void setNext(Node<E> next) {
        this.next = next;
    }

    /**
     * Devuelve una representación en cadena del nodo.
     * Si la información es {@code null}, devuelve {@code null}.
     *
     * @return Representación en cadena de la información almacenada en el nodo.
     */
    @Override
    public String toString() {
        if (info != null) {
            return info.toString();
        } else {
            return null;
        }
    }
}
