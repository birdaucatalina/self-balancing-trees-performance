package com.ace.ucv.selfbalancingtrees;

import java.io.IOException;

public class ApplicationMain {

	public static void main(String[] args) {
		System.out.println("AVL & Red-Black Tree Performance App is running...");
		String inputFileName = "input.txt";
		int keyToSearch = 13032;
		int keyToDelete = 13032;
		
		// AVL tree
		System.out.println("AVL Tree: ");
        AVLTree treeAVL = new AVLTree();

        try {
        	// Populate the AVL tree
            FileUtils.readFromResourcesAndInsert(inputFileName, treeAVL);
        } catch (IOException e) {
            System.err.println("Error while handling file: " + e.getMessage());
            return;
        }
        
//        // Print AVL tree details
//        System.out.println("\nIn-order traversal of the AVL tree:");
//        treeAVL.inOrderTraversal();
//        System.out.println();
        
        // Measure search time and memory usage for AVL Tree
        measureSearchPerformance(treeAVL, keyToSearch, "AVL Tree");

        // Measure delete time and memory usage for AVL Tree  
        measureDeletePerformance(treeAVL, keyToDelete, "AVL Tree");

//        // Print AVL tree details after deletion
//        System.out.println("\nIn-order traversal of the AVL tree after deletion:");
//        treeAVL.inOrderTraversal();
//        System.out.println();
        
//        // Print the tree structure
//        System.out.println("\n\nTree structure:");
//        tree.printTree(tree.getRoot(), "", true);
        
        
        // Red-Black tree
        RedBlackTree treeRB = new RedBlackTree();
        System.out.println("Red-Black Tree: ");
        
        try {
            // Populate the Red-Black Tree
            FileUtils.readFromResourcesAndInsert(inputFileName, treeRB);
        } catch (IOException e) {
            System.err.println("Error while handling file: " + e.getMessage());
            return;
        }

//        // Print Red-Black Tree details
//        System.out.println("\nIn-order traversal of the Red-Black Tree:");
//        treeRB.inOrder(treeRB.getRoot());
//        System.out.println();

        // Measure search time and memory usage for Red-Black Tree
        measureSearchPerformance(treeRB, keyToSearch, "Red-Black Tree");

        // Measure delete time and memory usage for Red-Black Tree
        measureDeletePerformance(treeRB, keyToDelete, "Red-Black Tree");

//        // Print Red-Black Tree after deletion
//        System.out.println("\nIn-order traversal of the Red-Black Tree after deletion:");
//        treeRB.inOrder(treeRB.getRoot());
//        System.out.println();
      
	}
    
    // Method to measure search performance for both AVL and Red-Black Trees
    public static void measureSearchPerformance(SelfBalancingTree tree, int key, String treeType) {
        long memoryBeforeSearch = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(treeType + " - Memory used before search: " + memoryBeforeSearch / 1024 + " KB");
        long startTimeSearch = System.nanoTime();
        boolean found = tree.search(key);
        long endTimeSearch = System.nanoTime();
        long memoryAfterSearch = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(treeType + " - Memory used after search: " + memoryAfterSearch / 1024 + " KB");
        double searchTimeMicroseconds = (endTimeSearch - startTimeSearch) / 1000.0;
        System.out.printf("\n%s - Time to search for %d: %.3f microseconds\n", treeType, key, searchTimeMicroseconds);
        System.out.println(treeType + " - Key " + key + " found: " + found);
    }

    // Method to measure delete performance for both AVL and Red-Black Trees
    public static void measureDeletePerformance(SelfBalancingTree tree, int key, String treeType) {
        long memoryBeforeDelete = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(treeType + " - Memory used before delete: " + memoryBeforeDelete / 1024 + " KB");
        long startTimeDelete = System.nanoTime();
        tree.delete(key);
        long endTimeDelete = System.nanoTime();
        long memoryAfterDelete = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(treeType + " - Memory used after delete: " + memoryAfterDelete / 1024 + " KB");
        double deleteTimeMicroseconds = (endTimeDelete - startTimeDelete) / 1000.0;
        System.out.printf("\n%s - Time to delete key %d: %.3f microseconds\n", treeType, key, deleteTimeMicroseconds);
    }

}
