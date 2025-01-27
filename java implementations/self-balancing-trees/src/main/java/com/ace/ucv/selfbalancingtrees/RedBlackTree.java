package com.ace.ucv.selfbalancingtrees;

public class RedBlackTree implements SelfBalancingTree {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private NodeRB root;
    private NodeRB TNULL; // Null node to replace leaves

    // Constructor to initialize the tree
    public RedBlackTree() {
        TNULL = new NodeRB(0);
        TNULL.setColor(BLACK);
        root = TNULL;
    }

    // Function to perform a left rotation
    private void leftRotate(NodeRB x) {
        NodeRB y = x.getRight();
        x.setRight(y.getLeft());
        if (y.getLeft() != TNULL) {
            y.getLeft().setParent(x);
        }
        y.setParent(x.getParent());
        if (x.getParent() == null) {
            root = y;
        } else if (x == x.getParent().getLeft()) {
            x.getParent().setLeft(y);
        } else {
            x.getParent().setRight(y);
        }
        y.setLeft(x);
        x.setParent(y);
    }

    // Function to perform a right rotation
    private void rightRotate(NodeRB x) {
        NodeRB y = x.getLeft();
        x.setLeft(y.getRight());
        if (y.getRight() != TNULL) {
            y.getRight().setParent(x);
        }
        y.setParent(x.getParent());
        if (x.getParent() == null) {
            root = y;
        } else if (x == x.getParent().getRight()) {
            x.getParent().setRight(y);
        } else {
            x.getParent().setLeft(y);
        }
        y.setRight(x);
        x.setParent(y);
    }

    // Method to insert a key into the Red-Black Tree
    public void insert(int key) {
        NodeRB node = new NodeRB(key);
        node.setParent(null);
        node.setKey(key);
        node.setLeft(TNULL);
        node.setRight(TNULL);
        node.setColor(RED);

        NodeRB y = null;
        NodeRB x = this.root;

        while (x != TNULL) {
            y = x;
            if (node.getKey() < x.getKey()) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }

        node.setParent(y);
        if (y == null) {
            root = node;
        } else if (node.getKey() < y.getKey()) {
            y.setLeft(node);
        } else {
            y.setRight(node);
        }

        if (node.getParent() == null) {
            node.setColor(BLACK);
            return;
        }

        if (node.getParent().getParent() == null) {
            return;
        }

        balanceInsert(node);
    }

    // Balancing the tree after insertion
    private void balanceInsert(NodeRB k) {
        NodeRB u;
        while (k.getParent().getColor() == RED) {
            if (k.getParent() == k.getParent().getParent().getRight()) {
                u = k.getParent().getParent().getLeft(); // Uncle node
                if (u.getColor() == RED) {
                    u.setColor(BLACK);
                    k.getParent().setColor(BLACK);
                    k.getParent().getParent().setColor(RED);
                    k = k.getParent().getParent();
                } else {
                    if (k == k.getParent().getLeft()) {
                        k = k.getParent();
                        rightRotate(k);
                    }
                    k.getParent().setColor(BLACK);
                    k.getParent().getParent().setColor(RED);
                    leftRotate(k.getParent().getParent());
                }
            } else {
                u = k.getParent().getParent().getRight(); // Uncle node
                if (u.getColor() == RED) {
                    u.setColor(BLACK);
                    k.getParent().setColor(BLACK);
                    k.getParent().getParent().setColor(RED);
                    k = k.getParent().getParent();
                } else {
                    if (k == k.getParent().getRight()) {
                        k = k.getParent();
                        leftRotate(k);
                    }
                    k.getParent().setColor(BLACK);
                    k.getParent().getParent().setColor(RED);
                    rightRotate(k.getParent().getParent());
                }
            }
            if (k == root) {
                break;
            }
        }
        root.setColor(BLACK);
    }

    // In-order traversal of the tree
    public void inOrder(NodeRB node) {
        if (node != TNULL) {
            inOrder(node.getLeft());
            System.out.print(node.getKey() + " ");
            inOrder(node.getRight());
        }
    }

    // Get the root of the tree
    public NodeRB getRoot() {
        return this.root;
    }
    
    // Method to search for a key in the Red-Black Tree
    public boolean search(int key) {
        return searchTreeHelper(this.root, key);
    }

    private boolean searchTreeHelper(NodeRB node, int key) {
        if (node == TNULL || key == node.getKey()) {
            return node != TNULL;
        }
        if (key < node.getKey()) {
            return searchTreeHelper(node.getLeft(), key);
        }
        return searchTreeHelper(node.getRight(), key);
    }

    // Method to delete a key from the Red-Black Tree
    public void delete(int key) {
        deleteNodeHelper(this.root, key);
    }

    private void deleteNodeHelper(NodeRB node, int key) {
        NodeRB z = TNULL;
        NodeRB x, y;

        while (node != TNULL) {
            if (node.getKey() == key) {
                z = node;
            }
            if (node.getKey() <= key) {
                node = node.getRight();
            } else {
                node = node.getLeft();
            }
        }

        if (z == TNULL) {
            System.out.println("Key not found in the tree");
            return;
        }

        y = z;
        boolean yOriginalColor = y.getColor();
        if (z.getLeft() == TNULL) {
            x = z.getRight();
            rbTransplant(z, z.getRight());
        } else if (z.getRight() == TNULL) {
            x = z.getLeft();
            rbTransplant(z, z.getLeft());
        } else {
            y = minimum(z.getRight());
            yOriginalColor = y.getColor();
            x = y.getRight();
            if (y.getParent() == z) {
                x.setParent(y);
            } else {
                rbTransplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }

            rbTransplant(z, y);
            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
            y.setColor(z.getColor());
        }

        if (yOriginalColor == BLACK) {
            balanceDelete(x);
        }
    }

    private void rbTransplant(NodeRB u, NodeRB v) {
        if (u.getParent() == null) {
            root = v;
        } else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v);
        } else {
            u.getParent().setRight(v);
        }
        v.setParent(u.getParent());
    }

    private void balanceDelete(NodeRB x) {
        NodeRB s;
        while (x != root && x.getColor() == BLACK) {
            if (x == x.getParent().getLeft()) {
                s = x.getParent().getRight();
                if (s.getColor() == RED) {
                    s.setColor(BLACK);
                    x.getParent().setColor(RED);
                    leftRotate(x.getParent());
                    s = x.getParent().getRight();
                }

                if (s.getLeft().getColor() == BLACK && s.getRight().getColor() == BLACK) {
                    s.setColor(RED);
                    x = x.getParent();
                } else {
                    if (s.getRight().getColor() == BLACK) {
                        s.getLeft().setColor(BLACK);
                        s.setColor(RED);
                        rightRotate(s);
                        s = x.getParent().getRight();
                    }

                    s.setColor(x.getParent().getColor());
                    x.getParent().setColor(BLACK);
                    s.getRight().setColor(BLACK);
                    leftRotate(x.getParent());
                    x = root;
                }
            } else {
                s = x.getParent().getLeft();
                if (s.getColor() == RED) {
                    s.setColor(BLACK);
                    x.getParent().setColor(RED);
                    rightRotate(x.getParent());
                    s = x.getParent().getLeft();
                }

                if (s.getRight().getColor() == BLACK && s.getLeft().getColor() == BLACK) {
                    s.setColor(RED);
                    x = x.getParent();
                } else {
                    if (s.getLeft().getColor() == BLACK) {
                        s.getRight().setColor(BLACK);
                        s.setColor(RED);
                        leftRotate(s);
                        s = x.getParent().getLeft();
                    }

                    s.setColor(x.getParent().getColor());
                    x.getParent().setColor(BLACK);
                    s.getLeft().setColor(BLACK);
                    rightRotate(x.getParent());
                    x = root;
                }
            }
        }
        x.setColor(BLACK);
    }

    private NodeRB minimum(NodeRB node) {
        while (node.getLeft() != TNULL) {
            node = node.getLeft();
        }
        return node;
    }

}