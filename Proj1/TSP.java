import java.lang.*;
import java.util.*;

/*
This is the main file.
When submitting this project to Kattis:
- TSP.java, City.java, Edge.java and Kattio.java needs to be inserted
- Use TSP as the main class.
*/

public class TSP {

	private static Kattio io = new Kattio(System.in, System.out);
	private static int numOfCities;
	private static ArrayList<Edge> edges; // list all edges in the complete graph, sorted in increasing order of weight
	private static int distance[][]; // stores the euclidian distance between two cities.
	private static City cities[]; // Array of all the cities

	public static void main (String[] args) {
		// Read input and store it in different data structures
		getInput();

		// Run shortest edge heurestic
		shortest_edge();

		// Run 2opt with bestgain
		for (int i = 0; i < 200; i++) {
			twoOpt(false);
		}

		// Print the final path
		printResult();
	}

	public static void shortest_edge() {
		int connected = 0; // number of cities in path

		for (Edge edge : edges) {
			// Check that degree of both cities won't become 3.
			if (edge.city1.degree+1 > 2 || edge.city2.degree+1 > 2) {
				continue;
			}
			// Check that the edge between city1 and city2 does not build a cycle (except for last edge).
			else if (cycleCheck(edge.city1, edge.city1, edge.city2) && connected+1 != numOfCities) {
				continue;
			}
			// Add the edge to the path
			else {
				connected++;
				edge.city1.degree++;
				edge.city2.degree++;
				if (edge.city1.prev == null) {
					edge.city1.prev = edge.city2;
				}
				else {
					edge.city1.next = edge.city2;
				}
				if (edge.city2.prev == null) {
					edge.city2.prev = edge.city1;
				}
				else {
					edge.city2.next = edge.city1;
				}
				// When all cities are covered we don't need to look at anymore edges.
				if (connected == numOfCities) {
					break;
				}
			}
		}

		// Fix so that all the next references goes in the same direction in the path
		// (and opposite direction for the prev reference)
		City start = cities[0];
		City c1 = cities[0];
		City c2;
		if (c1.next != null) {
			do {
				c2 = c1.next;
				if (c2.next.equals(c1)) {
					c2.next = c2.prev;
					c2.prev = c1;
				}
			} while ((c1 = c2) != start);
		}
	}

	public static boolean cycleCheck(City latestCity, City city1, City city2) {
		//Checks if connecting city1 with city2 will lead to a cycle.

		// Basecase 1: If one of the cities is not connected to an edge in the path we know for sure that
		// a cycle can't be created.
		if ((city1.next == null && city1.prev == null) || (city2.next == null && city2.prev == null)) {
			return false;
		}
		// Basecase 2: If you can walk from city1 to city2, then a cycle has been created.
		else if (city1.equals(city2)) {
			return true;
		}
		else {
		// Walk through all cities from city1 until you reach an end.
		// If that end is not city2, then it is not a cycle.
			if (city1.next != null && !city1.next.equals(latestCity)) {
				return (cycleCheck(city1, city1.next, city2));
			}
			else if (city1.prev != null && !city1.prev.equals(latestCity)) {
				return (cycleCheck(city1, city1.prev, city2));
			}
			return false;	
		}
		
	}
	public static void twoOpt(boolean greedy) {
		// If greedy is true, then it will run the greedy version of 2-opt.
		// Otherwise the bestgain version of 2-opt will be run.

		//2-opt is not possible unless there are at least 4 cities
		// (2 edges need to be able to swap)
		if (cities.length < 4) {
			return;			
		}

		int c1, c2, c3, c4; // c1-c2 and c3-c4 swaps to c1-c3 and c2-c4 (if it shortens the distance)
		int currc1 = -1, currc2 = -1, currc3 = -1, currc4 = -1; // Used to keep track of the best swaping cities in best gain
		int bestGain = 0;
		for(int i = 0; i < cities.length; i++) {
			c1 = i;
			c2 = cities[c1].next.ptNumber;

			for (int nb : cities[c1].neighbors) {
				c3 = nb;
				c4 = cities[c3].next.ptNumber;
				if (c2 == c3 || c2 == c4 || c1 == c3 || c1 == c4) {
					continue;
				}

				int gain = distance[c1][c2] + distance[c3][c4] - (distance[c1][c3] + distance[c2][c4]);

				if (greedy) {
					if (gain > 0) {
						twoOptSwap(c1, c2, c3, c4);
						break;
					}
					else {
						break;
					}
				}
				else {
					if (gain > bestGain) {
						bestGain = gain;
						currc1 = c1;
						currc2 = c2;
						currc3 = c3;
						currc4 = c4;
					}
				}
			}

		}
		if (!greedy && currc1 != -1) {
			twoOptSwap(currc1, currc2, currc3, currc4);
		}
	}

	public static void twoOptSwap(int c1, int c2, int c3, int c4) {
		// Performs a swap between cities c1, c2, c3 and c4
		// Previous edges: c1-c2 and c3-c4
		// New edges: c1-c3 and c2-c4
		cities[c3].next = cities[c3].prev;
		cities[c1].next = cities[c3];
		cities[c3].prev = cities[c1];

		cities[c2].prev = cities[c2].next;
		cities[c2].next = cities[c4];
		cities[c4].prev = cities[c2];

		// switch the next and prev references after the swap so that next goes 
		// in one direction in the path (opposite direction for prev)
	
		int a = c2;
		int b = c2;
		while((a = cities[a].prev.ptNumber) != c3) {
			cities[a].prev = cities[a].next;
			cities[a].next = cities[b];
			b = a;
		}
	}

	public static void getInput() { 
		// Reads the coordinates and saves them using three different structure
		// All these structure are later used in different context in order
		// to get as fast lookup as possible
		numOfCities = io.getInt();
		edges = new ArrayList<Edge>(); // used in the shortest edge heuristic
		distance = new int[numOfCities][numOfCities]; // used to get O(1) lookup for distance between 2 cities
		cities = new City[numOfCities]; //Stores all the cityobjects with relevant information about each city

		for (int i = 0; i < numOfCities; i++) {
			cities[i] = new City(io.getDouble(),io.getDouble(), i);
			for (int j = 0; j < i; j++) {
				distance[i][j] = dist(cities[i], cities[j]);
				distance[j][i] = distance[i][j];
				edges.add(new Edge(cities[i], cities[j]));
			}
		}

		Collections.sort(edges); // sorted edge list to be used in the shortest edge heuristic

		// add neighbors to city object (used in 2opt to find closest neighbour of a city)
		for (Edge edge : edges) {
			edge.city1.add_neighbors(edge.city2.ptNumber);
			edge.city2.add_neighbors(edge.city1.ptNumber);
		}

	}

	public static void printResult() { 
		// Prints the cities in the order of the path with help from next and prev references
		City city = cities[0];
		City latestCity = cities[0];
		for (int i = 0; i < cities.length; i++) {
			System.out.println(city.ptNumber);

			if (!(city.next == null) && city.next != latestCity) {
				latestCity = city;
				city = city.next;
			}
			else {
				latestCity = city;
				city = city.prev;
			}			
		}
	}

	public static int dist(City city1, City city2) { 
		// Calculate and return the euclidian distance between city 1 and city 2
		double x1 = city1.getX();
		double y1 = city1.getY();
		double x2 = city2.getX();
		double y2 = city2.getY();

		int dist = (int) Math.round(Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2)));

		return dist;
	}
}
