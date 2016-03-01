//Ian de la Cruz and Kerules Fareg

package apps;
import java.util.*;
import java.io.*;

public class Friends {

	static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
	public static void main(String[] args) throws IOException {

		System.out.print("Enter graph file: ");
		String file = keyboard.readLine();
		Graph friendGraph = new Graph(file);
		
		while(friendGraph.adjLists.length == 0 || friendGraph.adjLists == null){
			System.out.print("Graph is empty. Enter new graph file: ");
			file = keyboard.readLine();
			friendGraph = new Graph(file);
		}
		
		friendGraph.print();
		while(true){
			friendGraph = new Graph(file);
		
			System.out.println("Enter a number to choose an option or quit to exit: ");
			System.out.println("1: Shortest Intro Path");
			System.out.println("2: Cliques");
			System.out.println("3: Connectors");
			String input = keyboard.readLine();


			if(input.equals("1")){
				
				while(true){
					System.out.print("Enter two names separated by a comma. (i.e. jack,jill): ");
					StringTokenizer st = new StringTokenizer(keyboard.readLine(), ",");
					if(st.countTokens() == 2){
						String name1 = st.nextToken();
						String name2 = st.nextToken();
						shortestPath(friendGraph, name1, name2);
						break;
					}else
						System.out.println("Illegal arguemt.");
				}
			}else if(input.equals("2")){
				System.out.print("Enter a school to search through: ");
				String school = keyboard.readLine();
				
				Graph sub = friendGraph.subgraph(school);
				sub.findCliques(school);

			}else if(input.equals("3")){
				friendGraph.findConnectors();
				/*
				for(int i = 0; i < friendGraph.adjLists.length;i++){
					System.out.println(friendGraph.adjLists[i].name + " " + 
									   friendGraph.adjLists[i].dfsnum + "/" + 
									   friendGraph.adjLists[i].back);
				}
				 */
			}else if(input.equalsIgnoreCase("quit")){
				break;
			}
		}
	}
	static void shortestPath(Graph friend, String name1, String name2){
		boolean found1 = false; 
		boolean found2 = false;
		for(int i = 0; i < friend.adjLists.length; i++){
			if(friend.adjLists[i].name.equals(name1.trim())){
				found1 = true;
				break;
			}
		}
		if(found1){
			for(int i = 0; i < friend.adjLists.length; i++){
				if(friend.adjLists[i].name.equals(name2.trim())){
					found2 = true;
					break;
				}
			}
		}
		if(found1 && found2){
			friend.shortestPath(name1.trim(), name2.trim());
		}else if(!found1 || !found2){
			System.out.println("Names could not be found.");
		}
	}

}
