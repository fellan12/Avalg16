import java.lang.*;
import java.util.*;

public class TSP {

	private static Kattio io = new Kattio(System.in, System.out);
	private static int numOfPoints;
	private static double points[][];
	private static int tour[];
	private static boolean used[];
	private static ArrayList<Edge> edges; // listar alla kantobjekt, sorterat m.a.p. kortaste kanten
	private static int distance[][]; // sparar varje nods avstånd till alla andra
	private static City cities[];
	//private static ArrayList<Edge> solution;
	private static final boolean DEBUG = false;
	private static final boolean PRINTSCORE = false;

	public static void debug(String str) {
		if (DEBUG) {
			System.out.println(str);
		}
	}

	public static void twoOpt(boolean greedy) {
		debug("startar twoopt");
		//City start = cities[0];
		//City c1 = cities[0];
		//City c2;
		//do {
		if (cities.length < 4) {
			return; //2-opt kan inte köras om det är mindre än 4 städer
		}
		int c1, c2, c3, c4;
		int currc1 = -1, currc2 = -1, currc3 = -1, currc4 = -1;
		int bestGain = 0;
		for(int i = 0; i < cities.length; i++) {
			c1 = i;
			c2 = cities[c1].next.ptNumber;
			debug("kollar grannar och testar swap för varje stad, nuvarande stad: " + c1);

			for (int nb : cities[c1].neighbors) {
				debug("stadens grannar: " + nb);
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
		//} while (!(c1 = c1.next).equals(start));
	}

	public static void twoOptSwap(int c1, int c2, int c3, int c4) {
		debug("kör swap:");
		debug("c1: " + c1 + " c2: " + c2 + " c3: " + c3 + " c4: " + c4);
		cities[c3].next = cities[c3].prev;
		cities[c1].next = cities[c3];
		cities[c3].prev = cities[c1];
		debug("c3.next: " + cities[c3].next.ptNumber + "c3.prev: " + cities[c3].prev.ptNumber);
		debug("c1.next: " + cities[c1].next.ptNumber + "c1.prev: " + cities[c1].prev.ptNumber);



		cities[c2].prev = cities[c2].next;
		cities[c2].next = cities[c4];
		cities[c4].prev = cities[c2];
		debug("c2.next: " + cities[c2].next.ptNumber + "c2.prev: " + cities[c2].prev.ptNumber);
		debug("c4.next: " + cities[c4].next.ptNumber + "c4.prev: " + cities[c4].prev.ptNumber);

		// städa upp prev och next efter man swappat eftersom halva cykeln byter håll.
	

		int a = c2;
		int b = c2;
		while((a = cities[a].prev.ptNumber) != c3) {
			cities[a].prev = cities[a].next;
			cities[a].next = cities[b];
			b = a;
		}

		// do {
		// 	b = a.next;
		// 	if (b.next.equals(a)) {
		// 		b.next = b.prev;
		// 		b.prev = a;
		// 	}
		// } while (!(a = b).equals(c2));
	}

	public static void main (String[] args) {
		long startTime = System.nanoTime();
		getInput();
		greedy2();

		for (int i = 0; i < 10; i++) {
			twoOpt(false);
		}
		for (int i = 0; i < 100; i++) {
			twoOpt(true);
		}
		
		// Optimize greedy
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		printResult();
		if (PRINTSCORE){
			System.err.println("Totalt avstånd: " + calcRoute());
			System.err.println("Tid: " + totalTime/1000000);
		}

	}

	public static int calcRoute() {
		int totDist = 0;
		int a;
		int b;
		for (int i = 0; i < cities.length; i++) {
			a = i;
			b = cities[a].next.ptNumber;
			debug("a: " + a + "b: " + b + "abdist: " + distance[a][b]);
			totDist += distance[a][b];

		}
		return totDist;
	}


	public static void getInput() { // läser in allt och lagrar de i alla format vi vill ha
		numOfPoints = io.getInt();
		edges = new ArrayList<Edge>();
		distance = new int[numOfPoints][numOfPoints];
		cities = new City[numOfPoints];

		for (int i = 0; i < numOfPoints; i++) {
			cities[i] = new City(io.getDouble(),io.getDouble(), i);
			for (int j = 0; j < i; j++) {
				distance[i][j] = dist(cities[i], cities[j]);
				distance[j][i] = distance[i][j];
				edges.add(new Edge(cities[i], cities[j]));
			}
		}

		Collections.sort(edges);

		// add neighbors to city object;
		for (Edge edge : edges) {
			edge.city1.add_neighbors(edge.city2.ptNumber);
			edge.city2.add_neighbors(edge.city1.ptNumber);
		}

	}

	public static void greedy2() {
		int connected = 0; // antal städer som läggs till i path
		//solution = new ArrayList<Edge>();

		for (Edge edge : edges) {

			// Check that degree of both cities won't become 3.
			debug("Edge between: " + edge.city1.ptNumber + " and " + edge.city2.ptNumber);
			if (edge.city1.degree+1 > 2 || edge.city2.degree+1 > 2) {
				debug("will lead to degree bigger than 3");
				continue;
			}
			else if (cycleCheck(edge.city1, edge.city1, edge.city2) && connected+1 != numOfPoints) {
				continue;
			}
			else {
				debug("did not found any cycle or degree, add to solution");
				//sololution.add(edge);
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
			
				if (connected == numOfPoints) {
					break;
				}
			}
		}

		// städa rutt, rätta till next och prev
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
		debug("cycleCheck " + city1.ptNumber + " " + city2.ptNumber);
		if ((city1.next == null && city1.prev == null) || (city2.next == null && city2.prev == null)) {
			debug("null, ret false");
			return false;
		}
		else if (city1.equals(city2)) {
			debug("cycle detected true");
			return true;
		}
		else {
			if (city1.next != null && !city1.next.equals(latestCity)) {
				debug("go for next, " + city1.next.ptNumber + " != ");
				return (cycleCheck(city1, city1.next, city2));
			}
			else if (city1.prev != null && !city1.prev.equals(latestCity)) {
				debug("go for prev, " + city1.prev.ptNumber + " != ");
				return (cycleCheck(city1, city1.prev, city2));
			}
			debug("else, no cycle, false");
			return false;	
		}
		
	}

	public static void printResult() {
		City city = cities[0];
		//debug(city.getX());
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
		double x1 = city1.getX();
		double y1 = city1.getY();
		double x2 = city2.getX();
		double y2 = city2.getY();

		int dist = (int) Math.round(Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2)));

		return dist;
	}
}