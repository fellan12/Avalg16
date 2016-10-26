import java.util.*;

public class TestCase {

	private static double min = 0.0;
	private static double max = 1000000.0;
	private static int points = 3000;

	public static void main (String[] args) {
		System.out.println(points);
		for (int i = 0; i < points; i++) {
			System.out.println(generateRandom() + " " + generateRandom());
		}
	}

	public static double generateRandom() {

		double random = new Random().nextDouble(); //generates random double between 0 and 1
		double result = min + (random * (max - min));
		return result;
	}

	
}