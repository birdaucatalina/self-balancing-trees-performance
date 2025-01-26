package com.ace.ucv.selfbalancingtrees;

import java.io.IOException;

public class ApplicationMain {

	public static void main(String[] args) {
		System.out.println("AVL & Red-Black Tree Performance App is running...");
		String inputFileName = "input.txt";
		
        AVLTree tree = new AVLTree();

        try {
        	// Populate the AVL tree
            FileUtils.readFromResources(inputFileName, tree);
        } catch (IOException e) {
            System.err.println("Error while handling file: " + e.getMessage());
            return;
        }
        
        // Print AVL tree details
        System.out.println("\nIn-order traversal of the AVL tree:");
        tree.inOrderTraversal();
        System.out.println();
        
        // Measure search time and memory used
        int keyToSearch = 12;  
        long memoryBeforeSearch = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory used before search: " + memoryBeforeSearch / 1024 + " KB");
        long startTimeSearch = System.nanoTime();
        boolean found = tree.search(keyToSearch);
        long endTimeSearch = System.nanoTime();
        long memoryAfterSearch = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory used after search: " + memoryAfterSearch / 1024 + " KB");
        double searchTimeMicroseconds = (endTimeSearch - startTimeSearch) / 1000.0;
        System.out.printf("\nTime to search for %d: %.3f microseconds\n", keyToSearch, searchTimeMicroseconds);

        // Display if the key was found or not
        System.out.println("Key " + keyToSearch + " found: " + found);

        // Measure delete time and memory used
        int keyToDelete = 18;  
        long memoryBeforeDelete = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory used before delete: " + memoryBeforeDelete / 1024 + " KB");
        long startTimeDelete = System.nanoTime();
        tree.delete(keyToDelete);
        long endTimeDelete = System.nanoTime();
        long memoryAfterDelete = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory used after delete: " + memoryAfterDelete / 1024 + " KB");
        double deleteTimeMicroseconds = (endTimeDelete - startTimeDelete) / 1000.0;
        System.out.printf("\nTime to delete key %d: %.3f microseconds\n", keyToDelete, deleteTimeMicroseconds);

        // Print AVL tree details after deletion
        System.out.println("\nIn-order traversal of the AVL tree after deletion:");
        tree.inOrderTraversal();
        
//        // Print the tree structure
//        System.out.println("\n\nTree structure:");
//        tree.printTree(tree.getRoot(), "", true);
      
	}

}
