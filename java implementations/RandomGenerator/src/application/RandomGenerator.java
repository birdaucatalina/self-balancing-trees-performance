package application;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class RandomGenerator {

	public static void main(String[] args) {
		String inputFileName = "random_output.txt";
        int numElements = 1000; 
        int bound = 10000;
        
        try {
			saveRandomData(inputFileName, numElements, bound);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Save random integers to a file in resources
    public static void saveRandomData(String filename, int size, int bound) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter("src/" + filename))) {
            Random random = new Random();
            for (int i = 0; i < size; i++) {
                pw.print(random.nextInt(bound) + " ");
            }
        }
        System.out.println("Random data saved to " + filename);
    }

}
