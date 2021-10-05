package coloring;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import sojung2021.BasicTools;
import sojung2021.GraphReader;

/**
 * 
 *	Dsatur�� �Ϲ� coloring�� ����ϴ� �˰����̴�.
 *  saturation degree�� �������� �������� ��ĥ�� ������ �����ϴ� �˰����̴�.
 *  ��ĥ�� ������ ���õǸ� first-fit�� ����ؼ� coloring�� �����Ѵ�.
 *  
 *  saturation degree, sat(v) = the number of different colours assigned to adjacent vertices.
 *  ---> �� Ŭ������ dsat �Լ��� ������.
 *  ---> JgraphT�� saturationDegreeColoring�� dsatur�� �����Ѱ�.
 *  
 * 
 * 	���� L(2,1)-coloring ������ �� �˰����� ���������Ѵ�.
 * 	�켱�� �����ϰ� saturation degree�� ���Ǹ� L(2,1)������ �����Ѵ�.
 * 	saturation degress for L(2,1) , sat2,1(v) = the number of different colours assigned to N(v) and N(N(v))
 * 
 * 
 * 		<����> color ������ 0 ����
 * */
public class Dsatur {
	
	public static void dsat(byte[][] adj) {
		int n = adj.length;
		int[] deg = GraphReader.getDegreeVector(adj);
		
		ArrayList<Vertex> V = new ArrayList<>();
		for(int i=0; i<n; i++)
			V.add(new Vertex(i, deg[i]));
		
		int[] colors = new int[n]; Arrays.fill(colors, -1);
		
		while( ArrayUtils.contains(colors, -1) ) {
			ArrayList<Vertex> uncolored = (ArrayList<Vertex>) V.stream().filter(a -> a.colored == false).collect(Collectors.toList());
			ArrayList<Vertex> al1 = maximalDsat(uncolored);
			
			if(al1.size() == 1) {
				Vertex v = al1.get(0);
				int c = chooseColor(adj, colors, v);
				colors[v.v] = c;
				v.setColor(c);
			}else {
				ArrayList<Vertex> al2 = maximalDegree(al1);
				if( al2.size() == 1) {
					Vertex v = al2.get(0);
					int c = chooseColor(adj, colors, v);
					colors[v.v] = c;
					v.setColor(c);
				}else {
					int r = ThreadLocalRandom.current().nextInt(0, al2.size());
					Vertex v = al2.get(r);
					int c = chooseColor(adj, colors, v);
					colors[v.v] = c;
					v.setColor(c);
				}
			}
			
			for(int i=0, l=V.size(); i<l; i++)
				V.get(i).update(adj, colors);
			
			//System.out.println(Arrays.toString(colors));
		}
		System.out.println("[#] END --------------------------------");
		System.out.println(Arrays.toString(colors));
		System.out.println("max of color = "+ BasicTools.maxValue(colors));
	}
	
	// L(2,1)-coloring�� �°� ����.
	public static int chooseColor(byte[][] adj, int[] colors, Vertex v) {
		byte[][] adj2 = BasicTools.matrixMul(adj, adj);
		
		BasicTools.removeDiagonal(adj2);
		
		int vid = v.v;
		Set<Integer> usedColor = new HashSet<>();
		// �Ÿ��� 2�� �������� �� 
		for(int i=0, l=adj2[vid].length; i<l; i++ ) {
			if( adj2[vid][i] != 0 && colors[i] != -1 )
				usedColor.add(colors[i]);
		}
		
		for(int i=0, l=adj[vid].length; i<l; i++) {
			if( adj[vid][i] == 1 && colors[i] != -1 ) {
				int c = colors[i];
				usedColor.add(c-1);
				usedColor.add(c);
				usedColor.add(c+1);
			}
		}
		
		int c = 0;
		while(true) {
			if( !usedColor.contains(c) )
				break;
			else
				c++;
		}
		
		return c;
	}
	
	//al �� ���� ū dsat�� ������ ����(��) ArrayList�� �����ؼ� return
	public static ArrayList<Vertex> maximalDsat(ArrayList<Vertex> al) {
		int max = -1;
		ArrayList<Vertex> mdsat = new ArrayList<>();
		
		Iterator<Vertex> iter = al.iterator();
		while(iter.hasNext()) {
			Vertex v = iter.next();
			
			if(v.satur_deg == max)
				mdsat.add(v);
			else if( v.satur_deg > max) {
				mdsat.clear();
				mdsat.add(v);
				max = v.satur_deg;
			}
		}
		
		return mdsat;
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
	
	
	
	public static void selectVertex(ArrayList<Vertex> al) {
		ArrayList<Vertex> uncolored = (ArrayList<Vertex>) al.stream().filter(a -> a.colored == false).collect(Collectors.toList());
		
		if(uncolored.size() == 0) {}
			//return -1;
		
		//int max_sd = uncolored.stream().
		
		// decreasing order by saturation degree
		Collections.sort(uncolored, new Comparator<Vertex>() {
			@Override
			public int compare(Vertex v1, Vertex v2) {
				if( v1.satur_deg == v2.satur_deg) return 0;
				else if (v1.satur_deg > v2.satur_deg) return -1;
				else return 1;
			}
		});
	}
	
	public static void test1() {
		int n = 5;
		int[] deg = {2,1,3,2,1};
		ArrayList<Vertex> V = new ArrayList<>();
		for(int i=0; i<n; i++) {
			V.add(new Vertex(i, deg[i]));
			if( i%2 == 0 )
				V.get(i).setColored(true);
		}
		for(int i=0; i<V.size(); i++)
			System.out.println(V.get(i).toString());
		System.out.println();
		/*
		ArrayList<Vertex> uncolored = (ArrayList<Vertex>) V.stream().filter(a -> a.colored == false).collect(Collectors.toList());
		for(int i=0; i<uncolored.size(); i++)
			System.out.println(uncolored.get(i).toString());
		**/
		
		// decreasing order
		Collections.sort(V, new Comparator<Vertex>() {
			@Override
			public int compare(Vertex v1, Vertex v2) {
				if( v1.degree == v2.degree) return 0;
				else if (v1.degree > v2.degree) return -1;
				else return 1;
			}
		});
		
		for(int i=0; i<V.size(); i++)
			System.out.println(V.get(i).toString());
		System.out.println();
		
		// Increasing order
		Collections.sort(V, new Comparator<Vertex>() {
			@Override
			public int compare(Vertex v1, Vertex v2) {
				if( v1.degree == v2.degree) return 0;
				else if (v1.degree > v2.degree) return 1;
				else return -1;
			}
		});
		
		Vertex max = Collections.max(V, new Comparator<Vertex>() {
			@Override
			public int compare(Vertex v1, Vertex v2) {
				if( v1.degree == v2.degree) return 0;
				else if (v1.degree > v2.degree) return 1;
				else return -1;
			}
		});
		
		System.out.println(max);
	}
	
	
	// Q1. ������ ���� ���� ���� ã�� ����..!
	public static void main(String[] args) throws IOException {
		//String s = "D:\\Data\\DIMACS\\MY\\mGraph3.txt";
		String s = "D:\\Data\\DIMACS\\dia2\\C125.9.clq";
		byte[][] adj = GraphReader.adjacency(s, 1);
		System.out.println("# of vertex = "+adj.length);
		dsat(adj);
	}
}
