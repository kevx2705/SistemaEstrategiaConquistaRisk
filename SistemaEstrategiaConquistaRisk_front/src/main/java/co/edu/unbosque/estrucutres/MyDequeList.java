package co.edu.unbosque.estrucutres;

import java.io.Serializable;

/**
 * Implementación de una cola doble (deque) basada en una lista doblemente enlazada.
 * Permite inserciones y eliminaciones eficientes por ambos extremos.
 *
 * @param <E> Tipo genérico de los elementos almacenados en la cola doble.
 */
public class MyDequeList<E> extends MyDoubleLinkedList<E> implements Dqueue<E>, Serializable {

    /** Identificador de versión para la serialización. */
    private static final long serialVersionUID = 1L;

    /** Referencia al último nodo de la cola doble. */
    private DNode<E> tail;

    /** Cantidad de elementos en la cola doble. */
    private int size;

    /**
     * Constructor por defecto. Inicializa una cola doble vacía.
     */
    public MyDequeList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Inserta un elemento al inicio de la cola doble.
     *
     * @param info Elemento a insertar. No debe ser {@code null}.
     */
    @Override
    public void insertFirst(E info) {
        DNode<E> node = new DNode<>();
        node.setInfo(info);
        if (size == 0) {
            head = tail = node;
        } else {
            node.setNext(head);
            head.setPrevious(node);
            head = node;
        }
        size++;
    }

    /**
     * Inserta un elemento al final de la cola doble.
     *
     * @param info Elemento a insertar. No debe ser {@code null}.
     */
    @Override
    public void insertLast(E info) {
        DNode<E> node = new DNode<>();
        node.setInfo(info);
        if (size == 0) {
            head = tail = node;
        } else {
            tail.setNext(node);
            node.setPrevious(tail);
            tail = node;
        }
        size++;
    }

    /**
     * Elimina y devuelve el primer elemento de la cola doble.
     *
     * @return El elemento eliminado, o {@code null} si la cola está vacía.
     */
    @Override
    public E removeFirst() {
        if (head == null)
            return null;
        E val = head.getInfo();
        head = head.getNext();
        if (head != null) {
            head.setPrevious(null);
        } else {
            tail = null;
        }
        size--;
        return val;
    }

    /**
     * Elimina y devuelve el último elemento de la cola doble.
     *
     * @return El elemento eliminado, o {@code null} si la cola está vacía.
     */
    @Override
    public E removeLast() {
        if (tail == null)
            return null;
        E val = tail.getInfo();
        tail = tail.getPrevious();
        if (tail != null) {
            tail.setNext(null);
        } else {
            head = null;
        }
        size--;
        return val;
    }

    /**
     * Devuelve la cantidad de elementos en la cola doble.
     *
     * @return Número de elementos en la cola.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Obtiene el primer nodo de la cola doble.
     *
     * @return Referencia al primer nodo.
     */
    public DNode<E> getHead() {
        return head;
    }

    /**
     * Establece el primer nodo de la cola doble.
     *
     * @param head Referencia al nuevo primer nodo.
     */
    public void setHead(DNode<E> head) {
        this.head = head;
    }

    /**
     * Obtiene el último nodo de la cola doble.
     *
     * @return Referencia al último nodo.
     */
    public DNode<E> getTail() {
        return tail;
    }

    /**
     * Establece el último nodo de la cola doble.
     *
     * @param tail Referencia al nuevo último nodo.
     */
    public void setTail(DNode<E> tail) {
        this.tail = tail;
    }

    /**
     * Devuelve una representación en cadena de la cola doble.
     * Muestra los elementos desde la cabeza hasta la cola.
     *
     * @return Cadena que representa la cola doble.
     */
    @Override
    public String toString() {
        return "head [" + toStringRec(head) + "] tail";
    }

    /**
     * Método recursivo auxiliar para generar la representación en cadena.
     *
     * @param nodo Nodo actual.
     * @return Cadena con la información del nodo y sus siguientes.
     */
    private String toStringRec(DNode<E> nodo) {
        if (nodo == null) {
            return "";
        }
        String s = nodo.getInfo().toString();
        if (nodo.getNext() != null) {
            s += "-" + toStringRec(nodo.getNext());
        }
        return s;
    }
}
