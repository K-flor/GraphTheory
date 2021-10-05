package coloring;

public class Vertex{
	int v; // vertex name
	
	int degree;
	int satur_deg;
	
	boolean colored;
	int color;
	
	Vertex(int v, int degree){
		this.v = v; this.degree = degree;
		this.colored = false;
		this.satur_deg = 0;
	}
	
	void update(byte[][] adj, int[] colors) {
		int new_sd = 0;
		for(int i=0; i<adj.length; i++) {
			if(adj[v][i] == 1 && colors[i] != -1)
				new_sd += 1;
		}
		
		setSatur_deg(new_sd);
	}

	void setSatur_deg(int satur_deg) {
		this.satur_deg = satur_deg;
	}

	boolean isColored() {
		return colored;
	}
	
	void setColor(int color) {
		this.color = color;
		this.colored = true;
	}
	

	public void setColored(boolean colored) {
		this.colored = colored;
	}

	@Override
	public String toString() {
		return "Vertex v=" + v + " [ degree=" + degree + ", colored=" + colored + " ]";
	}
	
	
}