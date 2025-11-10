package co.edu.unbosque.estrucutres;

import java.io.Serializable;

/**
 * Clase que representa un nodo doblemente enlazado para ser usado en estructuras de datos como listas doblemente enlazadas.
 * Implementa {@link Serializable} para permitir la serialización del nodo.
 *
 * @param <E> Tipo genérico de la información almacenada en el nodo.
 */
public class DNode<E> implements Serializable {

    /** Identificador de versión para la serialización. */
    private static final long serialVersionUID = 1L;

    /** Referencia al siguiente nodo en la estructura. */
    private DNode<E> next;

    /** Referencia al nodo anterior en la estructura. */
    private DNode<E> previous;

    /** Información almacenada en el nodo. */
    private E info;

    /**
     * Constructor que inicializa el nodo con el siguiente nodo y la información especificada.
     *
     * @param next Referencia al siguiente nodo.
     * @param info Información a almacenar en el nodo.
     */
    public DNode(DNode<E> next, E info) {
        this.next = next;
        this.info = info;
    }

    /**
     * Constructor que inicializa el nodo solo con la información especificada.
     * El siguiente y anterior nodo se establecen a {@code null}.
     *
     * @param elem Información a almacenar en el nodo.
     */
    public DNode(E elem) {
        this(null, elem);
    }

    /**
     * Constructor por defecto. Crea un nodo vacío con referencias y información nulas.
     */
    public DNode() {
        this.next = null;
        this.previous = null;
        this.info = null;
    }

    /**
     * Obtiene el siguiente nodo en la estructura.
     *
     * @return Referencia al siguiente nodo.
     */
    public DNode<E> getNext() {
        return next;
    }

    /**
     * Establece el siguiente nodo en la estructura.
     *
     * @param next Referencia al siguiente nodo.
     */
    public void setNext(DNode<E> next) {
        this.next = next;
    }

    /**
     * Obtiene el nodo anterior en la estructura.
     *
     * @return Referencia al nodo anterior.
     */
    public DNode<E> getPrevious() {
        return previous;
    }

    /**
     * Establece el nodo anterior en la estructura.
     *
     * @param previous Referencia al nodo anterior.
     */
    public void setPrevious(DNode<E> previous) {
        this.previous = previous;
    }

    /**
     * Obtiene la información almacenada en el nodo.
     *
     * @return Información almacenada.
     */
    public E getInfo() {
        return info;
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
     * Devuelve una representación en cadena del nodo.
     * Si la información es nula, devuelve {@code null}.
     *
     * @return Representación en cadena de la información almacenada.
     */
    @Override
    public String toString() {
        return info != null ? info.toString() : null;
    }
}
