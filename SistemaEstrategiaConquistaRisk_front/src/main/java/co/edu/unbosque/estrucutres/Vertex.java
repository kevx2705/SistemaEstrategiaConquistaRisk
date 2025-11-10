package co.edu.unbosque.estrucutres;


public class Vertex<T> {
	private T info;
	private MyLinkedList<Edge> adyacentEdges;

	public Vertex(T info) {
		this.info = info;
		adyacentEdges = new MyLinkedList<Edge>();
	}
	
	public void addEdge(Edge e) {
		adyacentEdges.addLast(e);
	}

	public T getInfo() {
		return info;
	}

	public void setInfo(T info) {
		this.info = info;
	}

	public MyLinkedList<Edge> getAdyacentEdges() {
		return adyacentEdges;
	}

	public void setAdyacentEdges(MyLinkedList<Edge> adyacentEdges) {
		this.adyacentEdges = adyacentEdges;
	}

	@Override
	public String toString() {
		return "\nVertex [info=" + info + ", adyacentEdges=" + adyacentEdges + "]";
	}
}
