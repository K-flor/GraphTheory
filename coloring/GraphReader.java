import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.apache.commons.lang3.StringUtils;

public class GraphReader {
	/***
	tsp file�� �о� vertex������ �Ÿ��� double[][]�� �����ش�
	*/
	public static double[][] tspDouble(String tspfile)throws IOException {
		double[][] vertex = new double[1][1];
		int size=1;
		EuclideanDistance distance = new EuclideanDistance();

		Scanner sc = new Scanner(new File(tspfile));

		while(sc.hasNextLine()){
			String s = sc.nextLine();
			StringTokenizer line = new StringTokenizer(s);

			String[] tokens = new String[line.countTokens()];
			int index = 0;
			while(line.hasMoreTokens()){
				tokens[index] = line.nextToken();
				index++;
			}

			if(tokens.length == 0) 
				continue;
			else if (tokens[0].equals("EOF"))
				break;
			else if(tokens[0].equals("DIMENSION")) {
				size = Integer.parseInt(tokens[2]);
				vertex = new double[size][2];
				continue;
			}
			else if(tokens[0].equals("DIMENSION:")){
				size = Integer.parseInt(tokens[1]);
				vertex = new double[size][2];
				continue;
			}

			for(int i=1;i<=size;i++){
				if( tokens[0].equals(Integer.toString(i)) ){
					vertex[i-1][0] =  Double.parseDouble(tokens[1]);
					vertex[i-1][1] =  Double.parseDouble(tokens[2]);
				}
			}
		}
		sc.close();
		
		double[][] adj_M = new double[size][size];

		for (int i=0; i<adj_M.length; i++){
			for(int j=i+1; j<adj_M[0].length; j++)
				adj_M[i][j]= adj_M[j][i] = distance.compute(vertex[i],vertex[j]);
		}

		return adj_M;
	}
/*
tspDouble�Լ��� ������ ������ double[][]�� ���������� ���Լ��� Math.round()�� �̿��Ͽ�
int[][]�� ��ȯ�Ѵ�
*/
	public static int[][] tspInt(String tspfile) throws IOException{
		double[][] adj = tspDouble(tspfile);
		int[][] Adj = new int[adj.length][adj[0].length];

		for(int i=0; i<adj.length; i++){
			for(int j=0; j<adj[0].length; j++)
				Adj[i][j] = (int)Math.round(adj[i][j]);
		}
		return Adj;
	}

/*
	�־��� Dimac������ Adjacency�� 2�� byte �迭�� �����ش�.
	[����] �Լ��� �Ű����� v01�� ���� ������.
*/
	public static byte[][] adjacency(String file, int v01)throws IOException{
		int size=0;
		byte[][] G;
		Scanner scan = new Scanner(new File(file));
		
		while(scan.hasNextLine()){
			String s = scan.nextLine();
			String[] word = s.split("\\s+");
			if(word[0].equals("p")) size = Integer.parseInt(word[2]);
		}
		G = new byte[size][size];
		scan.close();

		scan = new Scanner(new File(file));

		if(v01 == 1){ // ������ ���� ��ȣ�� 1���� �����ϴ� ���
			while(scan.hasNextLine()){
				String s = scan.nextLine();
				String[] vertex = s.split(" ");

				if(vertex[0].equals("e")){
					int a = Integer.parseInt(vertex[1])-1;
					int b = Integer.parseInt(vertex[2])-1;
					G[a][b] = G[b][a]=1;
				}
			}
			scan.close();
		}
		else { // ������ ���� ��ȣ�� 0���� �����ϴ� ���
				while(scan.hasNextLine()){
				String s = scan.nextLine();
				String[] vertex = s.split(" ");

				if(vertex[0].equals("e")){
					int a = Integer.parseInt(vertex[1]);
					int b = Integer.parseInt(vertex[2]);
					G[a][b] = G[b][a]=1;
				}
			}
			scan.close();
		}
		return G;
	}
/*
	����ġ���ִ� �׷���(����ġ�� int��)�� �о� 2�� �迭�� �����ش� 
*/
	public static int[][] weightAdj(String file)throws IOException {
		int size;
		int u, v, w;
		int[][] G = new int[1][1];
		Scanner scan = new Scanner(new File(file));

		while (scan.hasNextLine()) {
			String str = scan.nextLine();

			if (StringUtils.startsWith(str, "c") || str.isEmpty())
				continue;

			String[] token = str.split("\\s+");

			if (token[0].equals("p")) {
				size = Integer.parseInt(token[2]);
				G = new int[size][size];
			}

			if (token[0].equals("e")) {
				u = Integer.parseInt(token[1]);
				v = Integer.parseInt(token[2]);
				w = Integer.parseInt(token[3]);
				G[u][v] = G[v][u] = w;
			}
		}
		scan.close();
		return G;
	}

/*
 �Լ� adjacency�� ���� ��������� �̿��Ͽ�
 �� ������ �̾��� ������ jagged array�� ��ȯ�Ѵ�
	connectV1�� ��� �� 0��° �࿡�� ����� ������ + ����� ������ �����ϰ��ִ�.
	connectV2�� ����� ������ ����.
*/
	public static int[][] connectV1(String file, int v01)throws IOException{
		byte[][] G = adjacency(file,v01);
		int[][] jag = new int[G.length][];
		
		for(int i=0; i<G.length;i++){
			int count = 0;
			for(int j=0; j<G[0].length;j++)
				if(G[i][j]==1) count++;
			jag[i] = new int[count+1];
			jag[i][0] = count;
		}

		for(int i=0; i<G.length; i++){
			int k=1;
			for(int j=0; j<G[0].length; j++){
				if(G[i][j]==1) {
					jag[i][k] = j;
					k++;
				}
			}
		}
		return jag;
	}

	public static int[][] connectV2(String file, int v01)throws IOException{
		byte[][] G = adjacency(file, v01);
		int[][] jag = new int[G.length][];
		
		for(int i=0; i<G.length;i++){
			int count = 0;
			for(int j=0; j<G[0].length;j++)
				if(G[i][j]==1) count++;
			jag[i] = new int[count];
		}

		for(int i=0; i<G.length; i++){
			int k=0;
			for(int j=0; j<G[0].length; j++){
				if(G[i][j]==1) {
					jag[i][k] = j;
					k++;
				}
			}
		}
		return jag;
	}
	
	public static Graph<Integer, DefaultEdge> AdjtoJGT(String file, int v01) throws IOException {
		byte[][] adj = adjacency(file, v01);
		int v = adj.length;
		
		Graph<Integer, DefaultEdge> G = new SimpleGraph<>(DefaultEdge.class);
		
		for(int i=0; i < v; i++)
			G.addVertex(i);
		
		for(int i=0; i<v; i++) {
			for(int j = i; j<adj[i].length; j++) {
				if(adj[i][j] == 1)
					G.addEdge(i, j);
			}
		}
		
		return G;
	}
}