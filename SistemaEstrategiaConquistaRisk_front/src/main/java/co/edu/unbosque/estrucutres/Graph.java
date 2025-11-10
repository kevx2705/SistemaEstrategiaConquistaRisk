package co.edu.unbosque.estrucutres;

public class Graph {
	private MyLinkedList<Vertex<?>> listOfNodes;

	public Graph() {
		listOfNodes = new MyLinkedList<>();
	}

	public void addVertex(Vertex<?> v) {
		listOfNodes.addLast(v);
	}

	public void insertEdge(Vertex<?> a, Vertex<?> b, double value) {
		if (a == null || b == null)
			return;
		Edge e = new Edge(a, b, value);
		a.addEdge(e);
	}

	public MyLinkedList<Vertex<?>> getListOfNodes() {
		return listOfNodes;
	}

	public void setListOfNodes(MyLinkedList<Vertex<?>> listOfNodes) {
		this.listOfNodes = listOfNodes;
	}
}
