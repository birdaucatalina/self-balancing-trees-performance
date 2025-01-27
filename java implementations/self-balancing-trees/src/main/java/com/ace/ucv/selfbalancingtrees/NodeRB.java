package com.ace.ucv.selfbalancingtrees;

public class NodeRB {
    private int key;
    private boolean color;
    private NodeRB left, right, parent;

    // Constructor to initialize the node
    public NodeRB(int key) {
        this.key = key;
        this.color = true; // Initially, all nodes are red
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    // Getters and Setters
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public NodeRB getLeft() {
        return left;
    }

    public void setLeft(NodeRB left) {
        this.left = left;
    }

    public NodeRB getRight() {
        return right;
    }

    public void setRight(NodeRB right) {
        this.right = right;
    }

    public NodeRB getParent() {
        return parent;
    }

    public void setParent(NodeRB parent) {
        this.parent = parent;
    }
}
