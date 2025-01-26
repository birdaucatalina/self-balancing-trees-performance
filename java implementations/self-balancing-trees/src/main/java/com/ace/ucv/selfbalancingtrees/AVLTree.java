package com.ace.ucv.selfbalancingtrees;

public class AVLTree {
    private Node root;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    // Method to insert a key into the AVL tree
    public void insert(int key) {
        root = insert(root, key);
    }

    // Recursive method to insert a key and balance the tree
    public Node insert(Node node, int key) {
        if (node == null) {
            return new Node(key);
        }

        if (key < node.getKey()) {
            node.setLeft(insert(node.getLeft(), key));
        } else if (key > node.getKey()) {
            node.setRight(insert(node.getRight(), key));
        } else {
            // Duplicate keys are not allowed in AVL tree
            return node;
        }

        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));

        int balance = getBalance(node);

        // Balance the tree
        if (balance > 1 && key < node.getLeft().getKey()) {
            return rotateRight(node);
        }

        if (balance < -1 && key > node.getRight().getKey()) {
            return rotateLeft(node);
        }

        if (balance > 1 && key > node.getLeft().getKey()) {
            node.setLeft(rotateLeft(node.getLeft()));
            return rotateRight(node);
        }

        if (balance < -1 && key < node.getRight().getKey()) {
            node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateRight(Node y) {
        Node x = y.getLeft();
        Node T = x.getRight();

        x.setRight(y);
        y.setLeft(T);

        y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);
        x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.getRight();
        Node T = y.getLeft();

        y.setLeft(x);
        x.setRight(T);

        x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);
        y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);

        return y;
    }

    private int height(Node node) {
        return (node == null) ? 0 : node.getHeight();
    }

    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.getLeft()) - height(node.getRight());
    }

    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(Node node) {
        if (node != null) {
            inOrderTraversal(node.getLeft());
            System.out.print(node.getKey() + " ");
            inOrderTraversal(node.getRight());
        }
    }

    public void printTree(Node node, String prefix, boolean isLeft) {
        if (node != null) {
            System.out.println(prefix + (isLeft ? "|--- " : "|__ ") + node.getKey());
            printTree(node.getLeft(), prefix + (isLeft ? "|   " : "    "), true);
            printTree(node.getRight(), prefix + (isLeft ? "|   " : "    "), false);
        }
    }
    
    // Method to delete a key from the AVL tree
    public void delete(int key) {
        root = delete(root, key);
    }

    // Recursive method to delete a key and balance the tree
    public Node delete(Node node, int key) {
        // STEP 1: Perform normal BST delete
        if (node == null) {
            return node;
        }

        if (key < node.getKey()) {
            node.setLeft(delete(node.getLeft(), key));
        } else if (key > node.getKey()) {
            node.setRight(delete(node.getRight(), key));
        } else {
            // Node to be deleted is found

            // Node with only one child or no child
            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            }

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            node.setKey(minValue(node.getRight()));

            // Delete the inorder successor
            node.setRight(delete(node.getRight(), node.getKey()));
        }

        // STEP 2: Update the height of this node
        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));

        // STEP 3: Get the balance factor of this node
        int balance = getBalance(node);

        // STEP 4: Balance the tree if necessary

        // Left heavy case
        if (balance > 1 && getBalance(node.getLeft()) >= 0) {
            return rotateRight(node);
        }

        // Right heavy case
        if (balance < -1 && getBalance(node.getRight()) <= 0) {
            return rotateLeft(node);
        }

        // Left-right heavy case
        if (balance > 1 && getBalance(node.getLeft()) < 0) {
            node.setLeft(rotateLeft(node.getLeft()));
            return rotateRight(node);
        }

        // Right-left heavy case
        if (balance < -1 && getBalance(node.getRight()) > 0) {
            node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }

        return node;
    }

    // Helper function to get the minimum value node in the tree
    private int minValue(Node node) {
        int minValue = node.getKey();
        while (node.getLeft() != null) {
            minValue = node.getLeft().getKey();
            node = node.getLeft();
        }
        return minValue;
    }
    
    // Method to search for a key in the AVL tree
    public boolean search(int key) {
        return search(root, key);
    }

    // Recursive method to search for a key
    private boolean search(Node node, int key) {
        // Base case: node is null or the key is found
        if (node == null) {
            return false;
        }
        if (key == node.getKey()) {
            return true;
        }
        // Search left or right subtree based on comparison
        if (key < node.getKey()) {
            return search(node.getLeft(), key);
        } else {
            return search(node.getRight(), key);
        }
    }
}
