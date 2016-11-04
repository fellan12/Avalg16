import java.util.*;

public class City {

	private double x;
	private double y;
	public int ptNumber;
	public ArrayList<Integer> neighbors = new ArrayList<Integer>();
	public City next; // currently next city in tour
	public City prev; // currently city before in tour
	public int degree = 0;
	
	public City(double x, double y, int ptNumber) {
		
		this.x = x;
		this.y = y;
		this.ptNumber = ptNumber;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void add_neighbors(int neighbor) {
		if (neighbors.size() < 110) {
			neighbors.add(neighbor);
		}
	}

}