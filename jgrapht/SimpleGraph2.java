import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

public class SimpleGraph2 {

	public static void main(String[] args) {
		Graph<String, DefaultEdge> G = new SimpleGraph<>(DefaultEdge.class);
		//Graph<String, DefaultEdge> G = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class); // 이렇게 작성하면 에러가 발생한다.
		
		
		G.addVertex("a"); G.addVertex("b");
		G.addVertex("c"); G.addVertex("d");
		G.addVertex("e"); G.addVertex("f");
		
		G.addEdge("a", "b");G.addEdge("c", "b");
		G.addEdge("c", "d");G.addEdge("e", "d");
		G.addEdge("e", "f");
		
		System.out.println("G : " + G);
		System.out.println(G.degreeOf("c"));
		//G.removeVertex("c");
		//System.out.println("G : " + G);
		
		GraphPath<String, DefaultEdge> path = DijkstraShortestPath.findPathBetween(G, "a", "f");
		// a 와 f 사이의 path가 없으면 null을 리턴
		
		System.out.println("Path between a and f : " + path);
		
		Graph<Integer, DefaultEdge> G2 = new SimpleGraph<>(DefaultEdge.class);
		G2.addVertex(1);G2.addVertex(2);G2.addVertex(3);
		G2.addVertex(5);G2.addVertex(4);G2.addVertex(6);G2.addVertex(7);
		
		G2.addEdge(1, 2);G2.addEdge(1, 3);G2.addEdge(1, 7);
		G2.addEdge(2, 3);G2.addEdge(3, 5);G2.addEdge(4, 7);
		G2.addEdge(5, 6); G2.addEdge(2, 1);
		
		System.out.println("G2 : "+G2);
		
		GraphPath<Integer, DefaultEdge> path2 = DijkstraShortestPath.findPathBetween(G2, 1, 6);
		System.out.println("path 1 to 6 " + path2);
	}
}
