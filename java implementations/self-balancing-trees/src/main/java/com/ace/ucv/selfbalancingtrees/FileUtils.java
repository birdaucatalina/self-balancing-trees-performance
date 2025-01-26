package com.ace.ucv.selfbalancingtrees;

import java.io.*;
import java.util.Random;

public class FileUtils {

    // Read integers from a file in resources and insert them into the AVL tree
    public static void readFromResources(String filename, AVLTree tree) throws IOException {
        try (InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(filename)) {
            if (is == null) {
                throw new FileNotFoundException("Resource file not found: " + filename);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\s+");
                
                long memoryBeforeInsert = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                System.out.println("Memory used before insert: " + memoryBeforeInsert / 1024 + " KB");
                long startTime = System.nanoTime();
                
                for (String value : values) {
                    try {
                    	tree.insert(Integer.parseInt(value)); 
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number in file: " + value);
                    }
                }          
                long endTime = System.nanoTime();
                double insertTimeMicroseconds = (endTime - startTime) / 1000.0;
                System.out.printf("\nTime to insert the values: %.3f microseconds\n", insertTimeMicroseconds);
                
                long memoryAfterInsert = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                System.out.println("Memory used after insert: " + memoryAfterInsert / 1024 + " KB"); 
            }
        }
        System.out.println("Data successfully loaded into AVL tree from " + filename);
    }
}
