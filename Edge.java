import java.util.*;

public class Edge implements Comparable<Edge> {
	public City city1;
	public City city2;
	private int dist;


	public Edge(City city1, City city2) {
		this.dist = TSP.dist(city1, city2);
		this.city1 = city1;
		this.city2 = city2;

	}

	@Override 
	public int compareTo(Edge edge2) {
		return (this.dist - edge2.dist);
	}
}
	
