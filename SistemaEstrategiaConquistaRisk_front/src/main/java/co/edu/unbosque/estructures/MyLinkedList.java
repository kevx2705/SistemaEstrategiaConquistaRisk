package co.edu.unbosque.estructures;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Implementación de una lista enlazada genérica usando solo métodos recursivos.
 *
 * @param <E> Tipo de los elementos almacenados en la lista.
 */
public class MyLinkedList<E> implements Serializable, Iterable<E> {

    /**
     * Versión serial para la serialización de la clase.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nodo inicial de la lista enlazada.
     */
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
     * @return Primer nodo de la lista.
     */
    public Node<E> getFirst() {
        return this.first;
    }

    /**
     * Establece el primer nodo de la lista.
     *
     * @param first Primer nodo a establecer.
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
     * @param info Elemento a buscar.
     * @return true si el elemento está en la lista, false en caso contrario.
     */
    public boolean contains(E info) {
        return get(info) != null;
    }

    /**
     * Añade un elemento al inicio de la lista.
     *
     * @param info Elemento a añadir.
     */
    public void add(E info) {
        Node<E> newNode = new Node<E>(info);
        newNode.setNext(this.first);
        first = newNode;
    }

    /**
     * Obtiene el elemento en la posición especificada.
     *
     * @param index Posición del elemento.
     * @return Elemento en la posición especificada, o null si no existe.
     */
    public E get(int index) {
        Node<E> node = getPos(index);
        return (node == null ? null : node.getInfo());
    }

    /**
     * Elimina la primera ocurrencia de un elemento en la lista.
     *
     * @param info Elemento a eliminar.
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
     * Elimina el elemento en la posición especificada.
     *
     * @param index Posición del elemento a eliminar.
     * @return Elemento eliminado, o null si no existe.
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
     * Inserta un elemento después del nodo especificado.
     *
     * @param info     Elemento a insertar.
     * @param previous Nodo después del cual se insertará el nuevo elemento.
     */
    public void insert(E info, Node<E> previous) {
        if (previous != null) {
            Node<E> newNode = new Node<E>(info);
            newNode.setNext(previous.getNext());
            previous.setNext(newNode);
        }
    }

    /**
     * Añade un elemento al final de la lista.
     *
     * @param info Elemento a añadir.
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
     * @return Primer elemento de la lista, o null si la lista está vacía.
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
     * Extrae y elimina el elemento siguiente al nodo especificado.
     *
     * @param previous Nodo anterior al elemento a extraer.
     * @return Elemento extraído, o null si no existe.
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
     * @return Tamaño de la lista.
     */
    public int size() {
        return sizeRecursivo(0, first);
    }

    /**
     * Método recursivo para calcular el tamaño de la lista.
     *
     * @param counter Contador actual.
     * @param current Nodo actual.
     * @return Tamaño de la lista.
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
     * Obtiene el nodo que contiene el elemento especificado.
     *
     * @param info Elemento a buscar.
     * @return Nodo que contiene el elemento, o null si no existe.
     */
    public Node<E> get(E info) {
        return getInfoRecursivo(info, first);
    }

    /**
     * Método recursivo para obtener el nodo que contiene el elemento especificado.
     *
     * @param info        Elemento a buscar.
     * @param currentNode Nodo actual.
     * @return Nodo que contiene el elemento, o null si no existe.
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
     * Obtiene el nodo en la posición especificada.
     *
     * @param n Posición del nodo.
     * @return Nodo en la posición especificada, o null si no existe.
     */
    public Node<E> getPos(int n) {
        return getPosRecursivo(n, first, 0);
    }

    /**
     * Método recursivo para obtener el nodo en la posición especificada.
     *
     * @param n         Posición del nodo.
     * @param currentNode Nodo actual.
     * @param counter   Contador actual.
     * @return Nodo en la posición especificada, o null si no existe.
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
     * @return Último nodo de la lista.
     */
    public Node<E> getLastNode() {
        return getLastNodeRecursivo(first);
    }

    /**
     * Método recursivo para obtener el último nodo de la lista.
     *
     * @param current Nodo actual.
     * @return Último nodo de la lista.
     */
    private Node<E> getLastNodeRecursivo(Node<E> current) {
        if (current == null || current.getNext() == null) {
            return current;
        }
        return getLastNodeRecursivo(current.getNext());
    }

    /**
     * Obtiene la posición del elemento especificado en la lista.
     *
     * @param info Elemento a buscar.
     * @return Posición del elemento, o -1 si no existe.
     */
    public int indexOf(E info) {
        return indexOfRecursivo(info, first, 0);
    }

    /**
     * Método recursivo para obtener la posición del elemento especificado.
     *
     * @param info         Elemento a buscar.
     * @param current      Nodo actual.
     * @param infoPosicion Posición actual.
     * @return Posición del elemento, o -1 si no existe.
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
     * Cuenta el número de ocurrencias de un elemento en la lista.
     *
     * @param info Elemento a contar.
     * @return Número de ocurrencias del elemento.
     */
    public int numberOfOccurrences(E info) {
        return numberOfOccurrencesRecursivo(info, 0, first);
    }

    /**
     * Método recursivo para contar el número de ocurrencias de un elemento.
     *
     * @param info    Elemento a contar.
     * @param counter Contador actual.
     * @param current Nodo actual.
     * @return Número de ocurrencias del elemento.
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
     * @return Último elemento de la lista, o null si la lista está vacía.
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
     * Imprime la lista a partir de una posición especificada.
     *
     * @param position Posición inicial para imprimir.
     * @return Representación en cadena de la lista desde la posición especificada.
     */
    public String print(int position) {
        return printRecursivo(position, first, 0, new StringBuilder()).toString();
    }

    /**
     * Método recursivo para imprimir la lista a partir de una posición especificada.
     *
     * @param position Posición inicial para imprimir.
     * @param current  Nodo actual.
     * @param counter  Contador actual.
     * @param sb       StringBuilder para construir la cadena.
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
     * Método recursivo para construir la representación en cadena de la lista.
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

    /**
     * Implementación del iterador para MyLinkedList.
     *
     * @return Iterador para la lista.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = first;

            /**
             * Verifica si hay más elementos en la lista.
             *
             * @return true si hay más elementos, false en caso contrario.
             */
            @Override
            public boolean hasNext() {
                return current != null;
            }

            /**
             * Obtiene el siguiente elemento de la lista.
             *
             * @return Siguiente elemento de la lista.
             */
            @Override
            public E next() {
                E data = current.getInfo();
                current = current.getNext();
                return data;
            }
        };
    }

    /**
     * Limpia la lista, eliminando todos sus elementos.
     */
    public void clear() {
        first = null;
    }

    /**
     * Convierte la lista a un arreglo de cadenas.
     *
     * @param array Arreglo de cadenas para almacenar los elementos.
     * @return Arreglo de cadenas con los elementos de la lista.
     */
    public String[] toArray(String[] array) {
        int size = size();
        String[] result = (array.length >= size) ? array : new String[size];
        Node<E> current = first;
        int i = 0;
        while (current != null) {
            result[i++] = current.getInfo().toString();
            current = current.getNext();
        }
        return result;
    }
}
