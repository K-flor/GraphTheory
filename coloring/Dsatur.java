import java.io.*;
import java.util.*;

class Vertex{
	int id;
	int degree;
	int dsat;
	int adj_uncolor; // ��ĥ�ȵ� ������ ���� --> ( degree - dsat )
	
	int[] adj;
	
	boolean colored;
	int color;
	
	Vertex(int[] adj, int id){
		this.id = id;
		degree = adj.length;
		dsat = 0;
		adj_uncolor = degree;
		
		this.adj = Arrays.copyOf(adj, adj.length);
		colored = false;
		int color = 0;
	}
	
	boolean isColored(){
		return colored;
	}
	
	int getDegree() {
		return degree;
	}
	
	int getDsat() {
		return dsat;
	}
	
	void setDsat(int n) {
		dsat = n;
	}
	
	void update(int[] color) {
		int cnt=0; // colored�� adj
		
		for(int i: adj) {
			if(color[i] != 0)
				cnt++;
		}
		
		dsat = cnt;
		adj_uncolor = degree - dsat;
	}
	
	void setColor(int c) {
		color = c;
		colored = true;
	}

	@Override
	public String toString() {
		return "Vertex "+id +" [degree=" + degree + "], adj=" + Arrays.toString(adj);
	}
}

//color ������ 1 ����
public class Dsatur {
	
	//al �� ���� ū dsat�� ������ ����(��) ArrayList�� �����ؼ� return
	public static ArrayList<Vertex> maximalDsat(ArrayList<Vertex> al) {
		int max = -1;
		ArrayList<Vertex> mdsat = new ArrayList<>();
		
		Iterator<Vertex> iter = al.iterator();
		while(iter.hasNext()) {
			Vertex v = iter.next();
			
			if(v.dsat == max) {
				mdsat.add(v);
			}
			else if( v.dsat > max) {
				mdsat.clear();
				mdsat.add(v);
				max = v.dsat;
			}
		}
		
		return mdsat;
	}
	
	//al �� ������ uncolored ������ ���� ���� ����(��) ArrayList�� �����ؼ� return
	public static ArrayList<Vertex> maximalUncoloredAdj(ArrayList<Vertex> al){
		ArrayList<Vertex> uncolAdj = new ArrayList<>();
		int max = 0;
		
		Iterator<Vertex> iter = al.iterator();
		while( iter.hasNext() ) {
			Vertex v = iter.next();
			
			if(v.adj_uncolor == max)
				uncolAdj.add(v);
			else if( v.adj_uncolor > max ){
				uncolAdj.clear();
				uncolAdj.add(v);
				max = v.adj_uncolor;
			}
		}
		
		return uncolAdj;
	}
	
	// al �� degree�� ����ū ����(��) ArrayList�� �����ؼ� return
	public static ArrayList<Vertex> maximalDegree(ArrayList<Vertex> al){
		ArrayList<Vertex> maxDeg = new ArrayList<>();
		int max = 0;
		
		Iterator<Vertex> iter = al.iterator();
		while( iter.hasNext() ) {
			Vertex v = iter.next();
			
			if(v.degree == max)
				maxDeg.add(v);
			else if(v.degree > max) {
				maxDeg.clear();
				maxDeg.add(v);
				max = v.degree;
			}
		}
		
		return maxDeg;
	}
	
	// al���� 1���� ������ �ִ�. �� ������ ���� ���� color�� ���Ͽ� ���� ���� color�� ������ set
	public static int chooseColor(int[] color,  Vertex v) {
		int[] adj_color = new int[v.degree];
		for(int i=0; i<v.degree; i++) {
			adj_color[i] = color[v.adj[i]];
		}
		
		Arrays.sort(adj_color);
		
		int v_color = 1;
		for(int c : adj_color) {
			if(c == v_color)
				++v_color;
		}
		
		return v_color;
	}
	
	public static void dsatur(Vertex[] V) {
		int[] color = new int[V.length];
		
		//ArrayList<Vertex> colored = new ArrayList<>();
		ArrayList<Vertex> uncolored = new ArrayList<>();
		
		
		for(int i=0; i<V.length; i++) {
			uncolored.add(V[i]);
		}
		System.out.println(uncolored.size());
		
		while( !uncolored.isEmpty() ) {
			ArrayList<Vertex> al1 = maximalDsat(uncolored);
			
			if( al1.size() == 1 ) { //uncolored �� vertex �� dsat�� ���� ū vertex�� �Ѱ��ΰ�?
				Vertex v = al1.get(0);
				int c = chooseColor(color, v);
				color[v.id] = c;
				v.setColor(c);
				
				int index = uncolored.indexOf(v);
				uncolored.remove(index);
			}
			else {
				ArrayList<Vertex> al2 = maximalUncoloredAdj(al1);
				if( al2.size() == 1 ) { // dsat�� ���� ū vertex�� �� ������ uncolored vertex�� ���� ���� ������ �Ѱ��ΰ�? 
					Vertex v = al2.get(0);
					int c = chooseColor(color, v);
					color[v.id] = c;
					v.setColor(c);
					
					int index = uncolored.indexOf(v);
					uncolored.remove(index);
				}
				else {
					ArrayList<Vertex> al3 = maximalDegree(al2);
					
					Vertex v = al3.get(0);
					int c = chooseColor(color, v);
					color[v.id] = c;
					v.setColor(c);
					
					int index = uncolored.indexOf(v);
					uncolored.remove(index);
				}
			}

			for(int i=0; i<V.length; i++) {
				V[i].update(color);
			}
			//System.out.println(Arrays.toString(color));
		}
		
		System.out.println("\n=====Result====\n"+Arrays.toString(color));
		int[] c_clone = Arrays.copyOf(color, color.length);
		Arrays.sort(c_clone);
		System.out.println("best :"+c_clone[c_clone.length-1]);
	}
	
	public static void main(String[] args) throws IOException {
		String file = "C:/Users/user/Desktop/Graph/DIMACS/le450_15a.col";
		int v01 = 1;
		int[][] jagged = GraphReader.connectV2(file, v01);

		ArrayList<Vertex> uncolored = new ArrayList<>();
		Vertex[] V = new Vertex[jagged.length];
		
		for(int i=0; i<V.length; i++) {
			V[i] = new Vertex(jagged[i],i);
			uncolored.add(V[i]);
		}
		
		/*
		 * Vertex v = uncolored.get(0); System.out.println("Before : "+v.color);
		 * 
		 * V[0].setColor(3); System.out.println("After array V[0]'color : "+
		 * V[0].color); Vertex v1 = uncolored.get(0);
		 * System.out.println("After ArryaList 0 of uncolored'color : "+ v1.color);
		 */
		dsatur(V);
		
	}

}
