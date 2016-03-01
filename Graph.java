package apps;

import java.io.*;
import java.util.*;

import structures.Queue;

class Neighbor{
	public int vertexNum;
	public Neighbor next;
	public Neighbor(int vnum, Neighbor nbr){
		this.vertexNum = vnum;
		next = nbr;
	}
}
class Vertex {
	String name;
	String student;
	String school;
	Neighbor adjList;
	double distance = Double.POSITIVE_INFINITY;
	Vertex previous;
	int dfsnum;
	int back;

	Vertex(String name, String student, String school, Neighbor neighbors) {
		this.name = name;
		this.student = student;
		this.school = school;
		this.adjList = neighbors;
	}
}
public class Graph {
	public Vertex[] adjLists;
	public ArrayList<Vertex> AdjLists;

	public Graph(){
		this.AdjLists = new ArrayList<Vertex>();

	}

	//builds graph from file
	public Graph(String file) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(file));
		int size = 0;
		try{
			size = sc.nextInt();
		}catch(NoSuchElementException e){
			
		}
		this.adjLists = new Vertex[size];

		
		// read vertices
		for(int v = 0; v < adjLists.length; v++){
			String temp = sc.nextLine();
			String name = null, student = null, school = null;
			if(temp.indexOf("|") != -1){
				if(temp.indexOf("|") == temp.lastIndexOf("|")){
					name = temp.substring(0, temp.indexOf("|"));
					student = temp.substring(temp.indexOf("|") + 1, temp.length());
					school = null;
				}else{
					name = temp.substring(0, temp.indexOf("|"));
					student =  temp.substring(temp.indexOf("|") + 1, temp.lastIndexOf("|"));
					school = temp.substring(temp.lastIndexOf("|") + 1, temp.length());
				}
				adjLists[v] = new Vertex(name, student, school, null);
				//System.out.println(name + " " + student + " " + school);
			}else
				v--;
		}
		// read edges
		while(sc.hasNext()){
			StringTokenizer st = new StringTokenizer(sc.next(),"|");
			int v1 = 0;
			int v2 = 0;
			try{
				v1 = indexForName(st.nextToken());
				v2 = indexForName(st.nextToken());
			}catch(Exception e){

			}

			adjLists[v1].adjList = new Neighbor(v2, adjLists[v1].adjList);
			adjLists[v2].adjList = new Neighbor(v1, adjLists[v2].adjList);

		}
		sc.close();
	}

	public int indexForName(String name) {
		for (int v=0; v < adjLists.length; v++) {
			if (adjLists[v].name.equals(name)) {
				return v;
			}
		}
		return -1;
	}	
	public int IndexForName(String name){
		for(int v = 0; v < AdjLists.size(); v++){
			if(AdjLists.get(v).name.equals(name)){
				return v;
			}
		}
		return -1;
	}

	public void print() {
		System.out.println();
		for (int v=0; v < adjLists.length; v++) {
			System.out.print(adjLists[v].name + " " + "(" + adjLists[v].school + ")");
			for (Neighbor nbr=adjLists[v].adjList; nbr != null;nbr=nbr.next) {
				System.out.print(" --> " + adjLists[nbr.vertexNum].name+ " " + "(" + adjLists[nbr.vertexNum].school + ")");
			}
			System.out.println("\n");
		}
	}
	public void computePaths(Vertex source){

		source.distance = 0;
		source.previous = null;

		ArrayList<Vertex> fringe = new ArrayList<Vertex>();

		for(Neighbor temp = source.adjList; temp != null; temp = temp.next){
			adjLists[temp.vertexNum].distance = 1;
			adjLists[temp.vertexNum].previous = source;
			fringe.add(adjLists[temp.vertexNum]);
		}

		while(!fringe.isEmpty()){			
			double min = 0;
			int index = 0;
			if(fringe.size() == 1){
				min = fringe.get(0).distance;
				index = 0;
			}else{
				for(int i = 0; i < fringe.size(); i++){
					if(fringe.get(i).distance <= min){
						min = fringe.get(i).distance;
						index = i;
					}
				}
			}

			Vertex m = fringe.get(index);
			fringe.remove(index);

			for(Neighbor temp = m.adjList; temp != null; temp = temp.next){

				if(adjLists[temp.vertexNum].distance == Double.POSITIVE_INFINITY){
					adjLists[temp.vertexNum].distance = m.distance + 1;
					fringe.add(adjLists[temp.vertexNum]);
					adjLists[temp.vertexNum].previous = m;
				}else{
					adjLists[temp.vertexNum].distance = Math.min(adjLists[temp.vertexNum].distance, m.distance + 1);

				}
			}
		}

		/*
		for(Vertex v : adjLists){
			System.out.print(v.name + " " + v.distance);
			if(v.previous != null){
				System.out.print(" " + v.previous.name);
			}else
				System.out.print(" null");
			System.out.println();
		}
		 */

	}

	public void shortestPath(String s, String t){
		Vertex source = adjLists[indexForName(s)];
		Vertex target = adjLists[indexForName(t)];

		computePaths(source);

		Stack<Vertex> path = new Stack<Vertex>();

		Vertex temp = target;

		while(temp != source){
			path.push(temp);
			if(temp.previous != null){
				temp = temp.previous;
			}else
				break;

		}
		if(temp == source){
			System.out.print(source.name);
			while(!path.isEmpty()){
				System.out.print("--" + path.pop().name);
			}
			System.out.println();
		}else{
			System.out.println("A path does not exist between these two people.");
		}
		System.out.println();
	}
	public void findConnectors() {
		boolean[] visited = new boolean[adjLists.length];
		for (int v=0; v < visited.length; v++) {
			visited[v] = false;
		}
		ArrayList<Vertex> connectors = new ArrayList<Vertex>();
		for (int v=0; v < visited.length; v++) {
			int count = 1;
			if (!visited[v]) {
				adjLists[v].dfsnum = count;
				adjLists[v].back = count;
				count++;
				//System.out.println("Starting at " + adjLists[v].name);
				dfs(v, visited, count, connectors);
			}
		}

		if(adjLists.length == 1){
			System.out.println("Connectors: " +  adjLists[0].name);
			System.out.println();
		}else if(connectors.size() == 0){
			System.out.println("No connectors found.");
		}else{
			System.out.print("Connectors: " + connectors.get(0).name);
			for(int i = 1; i < connectors.size(); i ++){
				System.out.print(","+connectors.get(i).name);
			}
			System.out.println();
			System.out.println();
		}
	}

	// recursive dfs
	private void dfs(int v, boolean[] visited, int count, ArrayList<Vertex> connectors) {
		visited[v] = true;
		//System.out.println("visiting " + adjLists[v].name);
		for (Neighbor e=adjLists[v].adjList; e != null; e=e.next) {

			if (!visited[e.vertexNum]) {
				adjLists[e.vertexNum].dfsnum = count;
				adjLists[e.vertexNum].back = count;
				count++;


				//System.out.println(adjLists[v].name + "--" + adjLists[e.vertexNum].name + " " + count);

				dfs(e.vertexNum, visited, count, connectors);

				if(adjLists[v].dfsnum > adjLists[e.vertexNum].back){
					adjLists[v].back = Math.min(adjLists[v].back, adjLists[e.vertexNum].back);
				}
				if(v != 0){
					if(adjLists[v].dfsnum <= adjLists[e.vertexNum].back){
						if(!connectors.contains(adjLists[v]))
							connectors.add(adjLists[v]);
					}
				}
			}else{
				adjLists[v].back = Math.min(adjLists[v].back,adjLists[e.vertexNum].dfsnum);
			}	
		}
	}

	public void findCliques(String school){
		if(AdjLists.isEmpty()){
			System.out.println("No cliques found.");
			System.out.println();
			return;
		}
		Queue<Vertex> q = new Queue<Vertex>();
		boolean[] visited = new boolean[AdjLists.size()];
		for(int v = 0; v < visited.length; v++){
			visited[v] = false;
		}
		ArrayList<ArrayList<Vertex>> cliques = new ArrayList<ArrayList<Vertex>>();

		for(int i = 0; i < visited.length; i++){
			ArrayList<Vertex> subgraph = new ArrayList<Vertex>();
			Vertex temp = AdjLists.get(i);
			boolean addClique = false;
			if(!visited[i]){
				addClique = true;
				q.enqueue(temp);
				while(!q.isEmpty()){
					temp = q.dequeue();
					visited[IndexForName(temp.name)] = true;
					subgraph.add(temp);
					Neighbor e = temp.adjList;
					while(e != null){
						if(e.vertexNum != -1){
							if(!visited[e.vertexNum]){
								q.enqueue(AdjLists.get(e.vertexNum));
							}
						}
						e = e.next;
					}
				}
			}
			if(addClique){
				cliques.add(subgraph);
			}
		}

		for(int p = 0; p < cliques.size(); p++){
			ArrayList<Vertex> clique = cliques.get(p);
			System.out.println("Clique " + (p+1) + ":");
			System.out.println(cliques.get(p).size());
			for(int k = 0; k < clique.size(); k++){
				String name = clique.get(k).name;
				String sch = clique.get(k).school;
				System.out.println(name + "|" + "y" + "|" + sch);
			}
			boolean printed[] = new boolean[AdjLists.size()];
			for(int i = 0; i < printed.length; i++){
				printed[i] = false;
			}

			for(int k = 0; k < AdjLists.size(); k++){
				//printed[k] = true;
				for(Neighbor e = AdjLists.get(k).adjList; e != null; e = e.next){
					if(e.vertexNum != -1){
						if(clique.contains(AdjLists.get(k)) && !printed[e.vertexNum])	{
							System.out.println(AdjLists.get(k).name + "|" + AdjLists.get(e.vertexNum).name);
							printed[e.vertexNum] = true;
						}
					}
				}
			}
			System.out.println();

		}
	

	}
	public Graph subgraph(String school){
		Graph sub = new Graph();

		for(int i = 0; i < this.adjLists.length; i++){
			if(this.adjLists[i].school != null){
				if(this.adjLists[i].school.equalsIgnoreCase(school)){
					Vertex temp = this.adjLists[i];
					sub.AdjLists.add(temp); 
				}
			}
		}

		for(int k = 0; k < sub.AdjLists.size(); k++){
			Neighbor curr = sub.AdjLists.get(k).adjList;
			Neighbor prev = null;
			while(curr != null){		
				if(curr.vertexNum < 0){				
					if(prev == null){
						sub.AdjLists.get(k).adjList = curr.next;
					}else{
						prev.next = curr.next;
					}
				}else{
					curr.vertexNum = sub.IndexForName(adjLists[curr.vertexNum].name);
					prev = curr;
				}
				curr = curr.next;
			}
		}

		return sub;
	}
}

