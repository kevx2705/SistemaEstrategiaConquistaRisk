package co.edu.unbosque.estructures;

import java.io.Serializable;

import co.edu.unbosque.model.Jugador;

/**
 * Implementación de una lista enlazada genérica usando solo métodos recursivos.
 *
 * @param <E> Tipo de los elementos almacenados en la lista.
 */
public class MyLinkedList<E> implements Serializable {

	private static final long serialVersionUID = 1L;
	protected Node<E> first;

	public MyLinkedList() {
		this.first = null;
	}

	public Node<E> getFirst() {
		return this.first;
	}

	public void setFirst(Node<E> first) {
		this.first = first;
	}

	public boolean isEmpty() {
		return (this.first == null);
	}

	public boolean contains(E info) {
		return get(info) != null;
	}

	public void add(E info) {
		Node<E> newNode = new Node<E>(info);
		newNode.setNext(this.first);
		first = newNode;
	}

	public E get(int index) {
		Node<E> node = getPos(index);
		return (node == null ? null : node.getInfo());
	}

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

	public void insert(E info, Node<E> previous) {
		if (previous != null) {
			Node<E> newNode = new Node<E>(info);
			newNode.setNext(previous.getNext());
			previous.setNext(newNode);
		}
	}

	public void addLast(E info) {
		if (isEmpty()) {
			this.first = new Node<E>(info);
		} else {
			getLastNode().setNext(new Node<E>(info));
		}
	}

	public E extract() {
		if (isEmpty()) {
			return null;
		}
		E data = this.first.getInfo();
		this.first = this.first.getNext();
		return data;
	}

	public E extract(Node<E> previous) {
		if (previous == null || previous.getNext() == null) {
			return null;
		}
		E data = previous.getNext().getInfo();
		previous.setNext(previous.getNext().getNext());
		return data;
	}

	public int size() {
		return sizeRecursivo(0, first);
	}

	private int sizeRecursivo(int counter, Node<E> current) {
		if (current == null) {
			return counter;
		}
		return sizeRecursivo(counter + 1, current.getNext());
	}

	public String print() {
		return toString();
	}

	public Node<E> get(E info) {
		return getInfoRecursivo(info, first);
	}

	private Node<E> getInfoRecursivo(E info, Node<E> currentNode) {
		if (currentNode == null) {
			return null;
		}
		if (currentNode.getInfo().equals(info)) {
			return currentNode;
		}
		return getInfoRecursivo(info, currentNode.getNext());
	}

	public Node<E> getPos(int n) {
		return getPosRecursivo(n, first, 0);
	}

	private Node<E> getPosRecursivo(int n, Node<E> currentNode, int counter) {
		if (currentNode == null) {
			return null;
		}
		if (counter == n) {
			return currentNode;
		}
		return getPosRecursivo(n, currentNode.getNext(), counter + 1);
	}

	public Node<E> getLastNode() {
		return getLastNodeRecursivo(first);
	}

	private Node<E> getLastNodeRecursivo(Node<E> current) {
		if (current == null || current.getNext() == null) {
			return current;
		}
		return getLastNodeRecursivo(current.getNext());
	}

	public int indexOf(E info) {
		return indexOfRecursivo(info, first, 0);
	}

	private int indexOfRecursivo(E info, Node<E> current, int infoPosicion) {
		if (current == null) {
			return -1;
		}
		if (current.getInfo().equals(info)) {
			return infoPosicion;
		}
		return indexOfRecursivo(info, current.getNext(), infoPosicion + 1);
	}

	public int numberOfOccurrences(E info) {
		return numberOfOccurrencesRecursivo(info, 0, first);
	}

	private int numberOfOccurrencesRecursivo(E info, int counter, Node<E> current) {
		if (current == null) {
			return counter;
		}
		if (current.getInfo().equals(info)) {
			counter++;
		}
		return numberOfOccurrencesRecursivo(info, counter, current.getNext());
	}

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

	public String print(int position) {
		return printRecursivo(position, first, 0, new StringBuilder()).toString();
	}

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

	@Override
	public String toString() {
		return toStringRecursivo(first, new StringBuilder()).toString();
	}

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
