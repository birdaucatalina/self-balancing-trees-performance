package com.ace.ucv.selfbalancingtrees;

public class Node {
    private int key;
    private int height;
    private Node left, right;

    public Node(int key) {
        this.key = key;
        this.height = 1; // Initially, the height of the node is 1
        this.left = null;
        this.right = null;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
