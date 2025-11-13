package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres;

import java.io.Serializable;

/**
 * Implementación de una lista enlazada genérica usando métodos recursivos.
 * Esta lista permite almacenar elementos de cualquier tipo y realizar operaciones
 * como inserción, eliminación, búsqueda y recorrido de manera recursiva.
 *
 * @param <E> Tipo de los elementos almacenados en la lista.
 */
public class MyLinkedList<E> implements Serializable {

    /** Versión de serialización para garantizar la compatibilidad. */
    private static final long serialVersionUID = 1L;

    /** Primer nodo de la lista enlazada. */
    protected Node<E> first;

    /**
     * Constructor por defecto de la clase MyLinkedList.
     * Inicializa una lista vacía.
     */
    public MyLinkedList() {
        this.first = null;
    }

    /**
     * Obtiene el primer nodo de la lista.
     *
     * @return El primer nodo de la lista.
     */
    public Node<E> getFirst() {
        return this.first;
    }

    /**
     * Establece el primer nodo de la lista.
     *
     * @param first El nodo a establecer como primer elemento.
     */
    public void setFirst(Node<E> first) {
        this.first = first;
    }

    /**
     * Verifica si la lista está vacía.
     *
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        return (this.first == null);
    }

    /**
     * Verifica si la lista contiene un elemento específico.
     *
     * @param info El elemento a buscar.
     * @return true si el elemento está en la lista, false en caso contrario.
     */
    public boolean contains(E info) {
        return get(info) != null;
    }

    /**
     * Agrega un elemento al inicio de la lista.
     *
     * @param info El elemento a agregar.
     */
    public void add(E info) {
        Node<E> newNode = new Node<E>(info);
        newNode.setNext(this.first);
        first = newNode;
    }

    /**
     * Obtiene el elemento en una posición específica de la lista.
     *
     * @param index La posición del elemento a obtener.
     * @return El elemento en la posición especificada, o null si no existe.
     */
    public E get(int index) {
        Node<E> node = getPos(index);
        return (node == null ? null : node.getInfo());
    }

    /**
     * Elimina un elemento específico de la lista.
     *
     * @param info El elemento a eliminar.
     * @return true si el elemento fue eliminado, false en caso contrario.
     */
    public boolean delete(E info) {
        if (isEmpty())
            return false;
        // Caso especial: el primero es el que se elimina
        if (first.getInfo().equals(info)) {
            extract();
            return true;
        }
        Node<E> prev = first;
        Node<E> current = first.getNext();
        while (current != null) {
            if (current.getInfo().equals(info)) {
                extract(prev); // elimina usando tu método
                return true;
            }
            prev = current;
            current = current.getNext();
        }
        return false; // no encontrado
    }

    /**
     * Elimina el elemento en una posición específica de la lista.
     *
     * @param index La posición del elemento a eliminar.
     * @return El elemento eliminado, o null si no existe.
     */
    public E deleteAt(int index) {
        if (isEmpty() || index < 0 || index >= size()) {
            return null;
        }
        // Caso especial: eliminar primer nodo
        if (index == 0) {
            return extract();
        }
        Node<E> prev = getPos(index - 1);
        return extract(prev);
    }

    /**
     * Inserta un elemento después de un nodo específico.
     *
     * @param info     El elemento a insertar.
     * @param previous El nodo después del cual se insertará el nuevo elemento.
     */
    public void insert(E info, Node<E> previous) {
        if (previous != null) {
            Node<E> newNode = new Node<E>(info);
            newNode.setNext(previous.getNext());
            previous.setNext(newNode);
        }
    }

    /**
     * Agrega un elemento al final de la lista.
     *
     * @param info El elemento a agregar.
     */
    public void addLast(E info) {
        if (isEmpty()) {
            this.first = new Node<E>(info);
        } else {
            getLastNode().setNext(new Node<E>(info));
        }
    }

    /**
     * Extrae y elimina el primer elemento de la lista.
     *
     * @return El elemento eliminado, o null si la lista está vacía.
     */
    public E extract() {
        if (isEmpty()) {
            return null;
        }
        E data = this.first.getInfo();
        this.first = this.first.getNext();
        return data;
    }

    /**
     * Extrae y elimina el nodo siguiente al nodo especificado.
     *
     * @param previous El nodo anterior al nodo a eliminar.
     * @return El elemento eliminado, o null si no existe.
     */
    public E extract(Node<E> previous) {
        if (previous == null || previous.getNext() == null) {
            return null;
        }
        E data = previous.getNext().getInfo();
        previous.setNext(previous.getNext().getNext());
        return data;
    }

    /**
     * Obtiene el tamaño de la lista.
     *
     * @return El tamaño de la lista.
     */
    public int size() {
        return sizeRecursivo(0, first);
    }

    /**
     * Método recursivo para calcular el tamaño de la lista.
     *
     * @param counter Contador actual.
     * @param current Nodo actual.
     * @return El tamaño de la lista.
     */
    private int sizeRecursivo(int counter, Node<E> current) {
        if (current == null) {
            return counter;
        }
        return sizeRecursivo(counter + 1, current.getNext());
    }

    /**
     * Imprime la lista como una cadena.
     *
     * @return Representación en cadena de la lista.
     */
    public String print() {
        return toString();
    }

    /**
     * Obtiene el nodo que contiene un elemento específico.
     *
     * @param info El elemento a buscar.
     * @return El nodo que contiene el elemento, o null si no se encuentra.
     */
    public Node<E> get(E info) {
        return getInfoRecursivo(info, first);
    }

    /**
     * Método recursivo para obtener el nodo que contiene un elemento específico.
     *
     * @param info        El elemento a buscar.
     * @param currentNode Nodo actual.
     * @return El nodo que contiene el elemento, o null si no se encuentra.
     */
    private Node<E> getInfoRecursivo(E info, Node<E> currentNode) {
        if (currentNode == null) {
            return null;
        }
        if (currentNode.getInfo().equals(info)) {
            return currentNode;
        }
        return getInfoRecursivo(info, currentNode.getNext());
    }

    /**
     * Obtiene el nodo en una posición específica de la lista.
     *
     * @param n La posición del nodo a obtener.
     * @return El nodo en la posición especificada, o null si no existe.
     */
    public Node<E> getPos(int n) {
        return getPosRecursivo(n, first, 0);
    }

    /**
     * Método recursivo para obtener el nodo en una posición específica.
     *
     * @param n         La posición del nodo a obtener.
     * @param currentNode Nodo actual.
     * @param counter    Contador actual.
     * @return El nodo en la posición especificada, o null si no existe.
     */
    private Node<E> getPosRecursivo(int n, Node<E> currentNode, int counter) {
        if (currentNode == null) {
            return null;
        }
        if (counter == n) {
            return currentNode;
        }
        return getPosRecursivo(n, currentNode.getNext(), counter + 1);
    }

    /**
     * Obtiene el último nodo de la lista.
     *
     * @return El último nodo de la lista.
     */
    public Node<E> getLastNode() {
        return getLastNodeRecursivo(first);
    }

    /**
     * Método recursivo para obtener el último nodo de la lista.
     *
     * @param current Nodo actual.
     * @return El último nodo de la lista.
     */
    private Node<E> getLastNodeRecursivo(Node<E> current) {
        if (current == null || current.getNext() == null) {
            return current;
        }
        return getLastNodeRecursivo(current.getNext());
    }

    /**
     * Obtiene la posición de un elemento específico en la lista.
     *
     * @param info El elemento a buscar.
     * @return La posición del elemento, o -1 si no se encuentra.
     */
    public int indexOf(E info) {
        return indexOfRecursivo(info, first, 0);
    }

    /**
     * Método recursivo para obtener la posición de un elemento específico.
     *
     * @param info          El elemento a buscar.
     * @param current       Nodo actual.
     * @param infoPosicion  Posición actual.
     * @return La posición del elemento, o -1 si no se encuentra.
     */
    private int indexOfRecursivo(E info, Node<E> current, int infoPosicion) {
        if (current == null) {
            return -1;
        }
        if (current.getInfo().equals(info)) {
            return infoPosicion;
        }
        return indexOfRecursivo(info, current.getNext(), infoPosicion + 1);
    }

    /**
     * Cuenta el número de ocurrencias de un elemento específico en la lista.
     *
     * @param info El elemento a contar.
     * @return El número de ocurrencias del elemento.
     */
    public int numberOfOccurrences(E info) {
        return numberOfOccurrencesRecursivo(info, 0, first);
    }

    /**
     * Método recursivo para contar el número de ocurrencias de un elemento específico.
     *
     * @param info    El elemento a contar.
     * @param counter Contador actual.
     * @param current Nodo actual.
     * @return El número de ocurrencias del elemento.
     */
    private int numberOfOccurrencesRecursivo(E info, int counter, Node<E> current) {
        if (current == null) {
            return counter;
        }
        if (current.getInfo().equals(info)) {
            counter++;
        }
        return numberOfOccurrencesRecursivo(info, counter, current.getNext());
    }

    /**
     * Extrae y elimina el último elemento de la lista.
     *
     * @return El elemento eliminado, o null si la lista está vacía.
     */
    public E extractLast() {
        if (isEmpty()) {
            return null;
        }
        if (size() == 1) {
            E info = first.getInfo();
            first = null;
            return info;
        }
        Node<E> previousLastNode = getPos(size() - 2);
        return extract(previousLastNode);
    }

    /**
     * Imprime la lista desde una posición específica.
     *
     * @param position Posición desde la cual se imprimirá la lista.
     * @return Representación en cadena de la lista desde la posición especificada.
     */
    public String print(int position) {
        return printRecursivo(position, first, 0, new StringBuilder()).toString();
    }

    /**
     * Método recursivo para imprimir la lista desde una posición específica.
     *
     * @param position Posición desde la cual se imprimirá la lista.
     * @param current  Nodo actual.
     * @param counter   Contador actual.
     * @param sb        StringBuilder para construir la cadena.
     * @return StringBuilder con la representación en cadena de la lista.
     */
    private StringBuilder printRecursivo(int position, Node<E> current, int counter, StringBuilder sb) {
        if (current == null) {
            return sb;
        }
        if (counter >= position) {
            sb.append(current.getInfo().toString());
            if (current.getNext() != null && counter >= position) {
                sb.append(" -> ");
            }
        }
        return printRecursivo(position, current.getNext(), counter + 1, sb);
    }

    /**
     * Devuelve una representación en cadena de la lista.
     *
     * @return Representación en cadena de la lista.
     */
    @Override
    public String toString() {
        return toStringRecursivo(first, new StringBuilder()).toString();
    }

    /**
     * Método recursivo para construir una representación en cadena de la lista.
     *
     * @param current Nodo actual.
     * @param sb      StringBuilder para construir la cadena.
     * @return StringBuilder con la representación en cadena de la lista.
     */
    private StringBuilder toStringRecursivo(Node<E> current, StringBuilder sb) {
        if (current == null) {
            return sb;
        }
        sb.append(current.getInfo().toString());
        if (current.getNext() != null) {
            sb.append(" -> ");
        }
        return toStringRecursivo(current.getNext(), sb);
    }
}
