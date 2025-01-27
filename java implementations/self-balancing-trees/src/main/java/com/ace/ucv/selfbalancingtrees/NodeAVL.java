package com.ace.ucv.selfbalancingtrees;

public class NodeAVL {
    private int key;
    private int height;
    private NodeAVL left, right;

    public NodeAVL(int key) {
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

    public NodeAVL getLeft() {
        return left;
    }

    public void setLeft(NodeAVL left) {
        this.left = left;
    }

    public NodeAVL getRight() {
        return right;
    }

    public void setRight(NodeAVL right) {
        this.right = right;
    }
}
