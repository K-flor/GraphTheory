import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import org.jgrapht.traverse.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleGraph1 {

	public static void main(String[] args) throws URISyntaxException  {
		Graph<URI, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
		
		URI google = new URI("http://www.google.com");
        URI wikipedia = new URI("http://www.wikipedia.org");
        URI jgrapht = new URI("http://www.jgrapht.org");
        
        g.addVertex(google);
        g.addVertex(wikipedia);
        g.addVertex(jgrapht);
        
        g.addEdge(jgrapht, wikipedia);
        g.addEdge(google, jgrapht);
        g.addEdge(google, wikipedia);
        g.addEdge(wikipedia, google);
        
        g.vertexSet();
	}

}
