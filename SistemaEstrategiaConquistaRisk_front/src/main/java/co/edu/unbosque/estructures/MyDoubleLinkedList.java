package co.edu.unbosque.estructures;

import java.io.Serializable;
import java.util.Random;

/**
 * Implementación de una lista doblemente enlazada genérica.
 * Permite operaciones de inserción, eliminación, búsqueda y manipulación de nodos
 * de forma recursiva y iterativa.
 *
 * @param <E> Tipo genérico de los elementos almacenados en la lista.
 */
public class MyDoubleLinkedList<E> implements Serializable {

    /** Identificador de versión para la serialización. */
    private static final long serialVersionUID = 1L;

    /** Referencia al primer nodo de la lista. */
    protected DNode<E> head;

    /** Referencia al nodo actual (posición actual) en la lista. */
    protected DNode<E> currentPosition;

    /**
     * Constructor por defecto. Inicializa una lista doblemente enlazada vacía.
     */
    public MyDoubleLinkedList() {
        head = null;
        currentPosition = null;
    }

    /**
     * Avanza la posición actual en la lista el número de posiciones especificado.
     * Versión recursiva.
     *
     * @param numPositions Número de posiciones a avanzar. Debe ser mayor que 0.
     */
    public void forward(int numPositions) {
        if (numPositions <= 0 || head == null) {
            return;
        }
        if (currentPosition == null) {
            currentPosition = head;
            forward(numPositions - 1);
        } else if (currentPosition.getNext() != null && numPositions > 0) {
            currentPosition = currentPosition.getNext();
            forward(numPositions - 1);
        }
    }

    /**
     * Retrocede la posición actual en la lista el número de posiciones especificado.
     * Versión recursiva.
     *
     * @param numPositions Número de posiciones a retroceder. Debe ser mayor que 0.
     */
    public void back(int numPositions) {
        if (numPositions <= 0 || head == null || currentPosition == null) {
            return;
        }
        if (currentPosition.getPrevious() != null && numPositions > 0) {
            currentPosition = currentPosition.getPrevious();
            back(numPositions - 1);
        }
    }

    /**
     * Inserta un nuevo elemento en la posición actual de la lista.
     * Versión recursiva.
     *
     * @param data Elemento a insertar. No debe ser {@code null}.
     */
    public void insert(E data) {
        DNode<E> newNode = new DNode<>(data);
        insertRecursive(newNode, currentPosition);
        currentPosition = newNode;
    }

    /**
     * Método recursivo auxiliar para insertar un nodo en la lista.
     *
     * @param newNode Nodo a insertar.
     * @param current Nodo actual de referencia.
     */
    private void insertRecursive(DNode<E> newNode, DNode<E> current) {
        if (current == null) {
            newNode.setNext(head);
            if (head != null) {
                head.setPrevious(newNode);
            }
            head = newNode;
        } else {
            newNode.setNext(current.getNext());
            newNode.setPrevious(current);
            if (current.getNext() != null) {
                current.getNext().setPrevious(newNode);
            }
            current.setNext(newNode);
        }
    }

    /**
     * Devuelve una representación en cadena de la lista.
     * Versión recursiva.
     *
     * @return Cadena que representa los elementos de la lista.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringRecursive(head, sb);
        return sb.toString();
    }

    /**
     * Método recursivo auxiliar para generar la representación en cadena.
     *
     * @param node Nodo actual.
     * @param sb  StringBuilder para acumular la cadena.
     */
    private void toStringRecursive(DNode<E> node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        sb.append(node.getInfo());
        if (node.getNext() != null) {
            sb.append(" <-> ");
            toStringRecursive(node.getNext(), sb);
        }
    }

    /**
     * Extrae y devuelve el elemento en la posición actual.
     *
     * @return Elemento extraído, o {@code null} si la lista está vacía o no hay posición actual.
     */
    public E extract() {
        E info = null;
        if (currentPosition != null) {
            info = currentPosition.getInfo();
            if (head == currentPosition) {
                head = currentPosition.getNext();
            } else {
                currentPosition.getPrevious().setNext(currentPosition.getNext());
            }
            if (currentPosition.getNext() != null) {
                currentPosition.getNext().setPrevious(currentPosition.getPrevious());
            }
            currentPosition = currentPosition.getNext();
        }
        return info;
    }

    /**
     * Devuelve el número de elementos en la lista.
     * Versión recursiva.
     *
     * @return Número de elementos en la lista.
     */
    public int size() {
        return sizeRecursive(head);
    }

    /**
     * Método recursivo auxiliar para calcular el tamaño de la lista.
     *
     * @param node Nodo actual.
     * @return Tamaño de la lista desde el nodo actual.
     */
    private int sizeRecursive(DNode<E> node) {
        if (node == null) {
            return 0;
        }
        return 1 + sizeRecursive(node.getNext());
    }

    /**
     * Devuelve el nodo en la posición especificada.
     *
     * @param index Índice del nodo a obtener.
     * @return Nodo en la posición especificada, o {@code null} si el índice es inválido.
     */
    public DNode<E> getNodeAt(int index) {
        DNode<E> temp = currentPosition;
        currentPosition = head;
        DNode<E> result = getNodeAtRec(currentPosition, index);
        currentPosition = temp;
        return result;
    }

    /**
     * Método recursivo auxiliar para obtener el nodo en un índice específico.
     *
     * @param node  Nodo actual.
     * @param index Índice del nodo a obtener.
     * @return Nodo en el índice especificado.
     */
    private DNode<E> getNodeAtRec(DNode<E> node, int index) {
        if (index == 0) {
            return node;
        }
        return getNodeAtRec(node.getNext(), index - 1);
    }

    /**
     * Mezcla aleatoriamente los elementos de la lista.
     */
    public void shuffle() {
        int size = size();
        if (size <= 1)
            return;
        Random rand = new Random();
        DNode<E> temp = currentPosition;
        shuffleRecursive(size - 1, rand);
        currentPosition = temp;
    }

    /**
     * Método recursivo auxiliar para mezclar los elementos de la lista.
     *
     * @param index Índice actual.
     * @param rand  Generador de números aleatorios.
     */
    private void shuffleRecursive(int index, Random rand) {
        if (index <= 0) {
            return;
        }
        int j = rand.nextInt(index + 1);
        DNode<E> nodeI = getNodeAt(index);
        DNode<E> nodeJ = getNodeAt(j);
        E tempData = nodeI.getInfo();
        nodeI.setInfo(nodeJ.getInfo());
        nodeJ.setInfo(tempData);
        shuffleRecursive(index - 1, rand);
    }

    /**
     * Verifica si la lista contiene un elemento específico.
     * Versión recursiva.
     *
     * @param element Elemento a buscar.
     * @return {@code true} si el elemento está en la lista, {@code false} en caso contrario.
     */
    public boolean contains(E element) {
        return containsRecursive(head, element);
    }

    /**
     * Método recursivo auxiliar para verificar la existencia de un elemento.
     *
     * @param nodo    Nodo actual.
     * @param element Elemento a buscar.
     * @return {@code true} si el elemento está en la lista desde el nodo actual.
     */
    private boolean containsRecursive(DNode<E> nodo, E element) {
        if (nodo == null)
            return false;
        if (nodo.getInfo().equals(element))
            return true;
        return containsRecursive(nodo.getNext(), element);
    }

    /**
     * Agrega todos los elementos de otra lista al final de esta lista.
     *
     * @param otherList Lista cuyos elementos se agregarán. No debe ser {@code null}.
     */
    public void addAll(MyDoubleLinkedList<? extends E> otherList) {
        if (otherList == null || otherList.isEmpty()) {
            return;
        }
        DNode<E> temp = this.currentPosition;
        addAllRecursive(otherList.head);
        this.currentPosition = temp;
    }

    /**
     * Método recursivo auxiliar para agregar todos los elementos de otra lista.
     *
     * @param node Nodo actual de la lista a agregar.
     */
    private void addAllRecursive(DNode<? extends E> node) {
        if (node == null) {
            return;
        }
        this.insert((E) node.getInfo());
        addAllRecursive(node.getNext());
    }

    /**
     * Extrae y devuelve el elemento en la posición especificada.
     *
     * @param position Posición del elemento a extraer.
     * @return Elemento extraído, o {@code null} si la posición es inválida.
     */
    public E extract(int position) {
        if (position < 0 || position >= size() || head == null) {
            return null;
        }
        DNode<E> temp = currentPosition;
        DNode<E> target = getNodeAt(position);
        E data = target.getInfo();
        if (target == head) {
            head = target.getNext();
            if (head != null) {
                head.setPrevious(null);
            }
        } else {
            if (target.getPrevious() != null) {
                target.getPrevious().setNext(target.getNext());
            }
            if (target.getNext() != null) {
                target.getNext().setPrevious(target.getPrevious());
            }
        }
        currentPosition = temp;
        return data;
    }

    /**
     * Agrega un elemento al final de la lista.
     * Versión recursiva.
     *
     * @param data Elemento a agregar. No debe ser {@code null}.
     */
    public void addLast(E data) {
        if (head == null) {
            DNode<E> newNode = new DNode<>(data);
            head = newNode;
            currentPosition = newNode;
        } else {
            addLastRecursive(head, data);
        }
    }

    /**
     * Método recursivo auxiliar para agregar un elemento al final de la lista.
     *
     * @param node Nodo actual.
     * @param data Elemento a agregar.
     */
    private void addLastRecursive(DNode<E> node, E data) {
        if (node.getNext() == null) {
            DNode<E> newNode = new DNode<>(data);
            node.setNext(newNode);
            newNode.setPrevious(node);
        } else {
            addLastRecursive(node.getNext(), data);
        }
    }

    /**
     * Elimina un nodo específico de la lista.
     *
     * @param nodo Nodo a eliminar. No debe ser {@code null}.
     */
    public void deleteNode(DNode<E> nodo) {
        if (nodo == null)
            return;
        if (nodo == head) {
            head = nodo.getNext();
            if (head != null) {
                head.setPrevious(null);
            }
        } else {
            DNode<E> prev = nodo.getPrevious();
            DNode<E> next = nodo.getNext();
            if (prev != null) {
                prev.setNext(next);
            }
            if (next != null) {
                next.setPrevious(prev);
            }
        }
        if (currentPosition == nodo) {
            currentPosition = nodo.getNext() != null ? nodo.getNext() : nodo.getPrevious();
        }
        nodo.setNext(null);
        nodo.setPrevious(null);
    }

    /**
     * Devuelve el último nodo de la lista.
     *
     * @return Último nodo de la lista, o {@code null} si la lista está vacía.
     */
    public DNode<E> getTail() {
        if (head == null)
            return null;
        DNode<E> nodo = head;
        while (nodo.getNext() != null) {
            nodo = nodo.getNext();
        }
        return nodo;
    }

    /**
     * Elimina el último nodo de la lista.
     */
    public void deleteLast() {
        if (head == null)
            return;
        DNode<E> nodo = head;
        while (nodo.getNext() != null) {
            nodo = nodo.getNext();
        }
        if (nodo.getPrevious() != null) {
            nodo.getPrevious().setNext(null);
        } else {
            head = null;
        }
        if (currentPosition == nodo) {
            currentPosition = nodo.getPrevious();
        }
        nodo.setNext(null);
        nodo.setPrevious(null);
    }

    /**
     * Verifica si la lista está vacía.
     *
     * @return {@code true} si la lista está vacía, {@code false} en caso contrario.
     */
    public boolean isEmpty() {
        return head == null;
    }

    // Getters y Setters

    /**
     * Obtiene el primer nodo de la lista.
     *
     * @return Primer nodo de la lista.
     */
    public DNode<E> getHead() {
        return head;
    }

    /**
     * Establece el primer nodo de la lista.
     *
     * @param head Nuevo primer nodo.
     */
    public void setHead(DNode<E> head) {
        this.head = head;
    }

    /**
     * Obtiene la posición actual en la lista.
     *
     * @return Nodo actual.
     */
    public DNode<E> getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Establece la posición actual en la lista.
     *
     * @param currentPosition Nuevo nodo actual.
     */
    public void setCurrentPosition(DNode<E> currentPosition) {
        this.currentPosition = currentPosition;
    }
}
