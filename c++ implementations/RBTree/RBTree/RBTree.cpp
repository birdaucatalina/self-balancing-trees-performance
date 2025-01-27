// RBTree.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <fstream>
#include <vector>
#include <chrono>
#include <iomanip>
#include <windows.h> 
#include <psapi.h>

using namespace std;

enum Color { RED, BLACK };

struct Node {
    int value;
    Node* left, * right, * parent;
    Color color;
    Node(int val) : value(val), left(nullptr), right(nullptr), parent(nullptr), color(RED) {}
};

class RedBlackTree {
public:
    Node* root;

    RedBlackTree() : root(nullptr) {}

    void leftRotate(Node* x);
    void rightRotate(Node* x);
    void fixInsert(Node* k);
    void insert(int value);
    Node* search(Node* root, int key);
    void inOrder(Node* root);
    void deleteNode(int key);
    void fixDelete(Node* x);
    Node* minimum(Node* node);
    void transplant(Node* u, Node* v);
    void deleteFix(Node* x);
};

// Function to get current memory usage
void printMemoryUsage(const string& message) {
    PROCESS_MEMORY_COUNTERS pmc;
    if (GetProcessMemoryInfo(GetCurrentProcess(), &pmc, sizeof(pmc))) {
        cout << message << " Memory used: " << pmc.WorkingSetSize / 1024 << " KB" << endl;
    }
    else {
        cerr << "Failed to get memory info" << endl;
    }
}

void RedBlackTree::leftRotate(Node* x) {
    Node* y = x->right;
    x->right = y->left;
    if (y->left != nullptr)
        y->left->parent = x;
    y->parent = x->parent;
    if (x->parent == nullptr)
        root = y;
    else if (x == x->parent->left)
        x->parent->left = y;
    else
        x->parent->right = y;
    y->left = x;
    x->parent = y;
}

void RedBlackTree::rightRotate(Node* x) {
    Node* y = x->left;
    x->left = y->right;
    if (y->right != nullptr)
        y->right->parent = x;
    y->parent = x->parent;
    if (x->parent == nullptr)
        root = y;
    else if (x == x->parent->right)
        x->parent->right = y;
    else
        x->parent->left = y;
    y->right = x;
    x->parent = y;
}

void RedBlackTree::fixInsert(Node* k) {
    while (k->parent != nullptr && k->parent->color == RED) {
        if (k->parent == k->parent->parent->left) {
            Node* u = k->parent->parent->right;
            if (u != nullptr && u->color == RED) {
                k->parent->color = BLACK;
                u->color = BLACK;
                k->parent->parent->color = RED;
                k = k->parent->parent;
            }
            else {
                if (k == k->parent->right) {
                    k = k->parent;
                    leftRotate(k);
                }
                k->parent->color = BLACK;
                k->parent->parent->color = RED;
                rightRotate(k->parent->parent);
            }
        }
        else {
            Node* u = k->parent->parent->left;
            if (u != nullptr && u->color == RED) {
                k->parent->color = BLACK;
                u->color = BLACK;
                k->parent->parent->color = RED;
                k = k->parent->parent;
            }
            else {
                if (k == k->parent->left) {
                    k = k->parent;
                    rightRotate(k);
                }
                k->parent->color = BLACK;
                k->parent->parent->color = RED;
                leftRotate(k->parent->parent);
            }
        }
        if (k == root)
            break;
    }
    root->color = BLACK;
}

void RedBlackTree::insert(int value) {
    Node* node = new Node(value);
    Node* y = nullptr;
    Node* x = root;

    while (x != nullptr) {
        y = x;
        if (node->value < x->value)
            x = x->left;
        else
            x = x->right;
    }
    node->parent = y;
    if (y == nullptr)
        root = node;
    else if (node->value < y->value)
        y->left = node;
    else
        y->right = node;
    fixInsert(node);
}

Node* RedBlackTree::search(Node* root, int key) {
    if (root == nullptr || root->value == key)
        return root;

    if (key < root->value)
        return search(root->left, key);
    else
        return search(root->right, key);
}

void RedBlackTree::inOrder(Node* root) {
    if (root != nullptr) {
        inOrder(root->left);
        cout << root->value << " ";
        inOrder(root->right);
    }
}

void RedBlackTree::deleteNode(int key) {
    Node* z = search(root, key);
    if (z == nullptr) {
        cout << "Key not found!" << endl;
        return;
    }

    Node* y = z;
    Node* x;
    Color originalColor = y->color;

    if (z->left == nullptr) {
        x = z->right;
        transplant(z, z->right);
    }
    else if (z->right == nullptr) {
        x = z->left;
        transplant(z, z->left);
    }
    else {
        y = minimum(z->right);
        originalColor = y->color;
        x = y->right;
        if (y->parent == z)
            x->parent = y;
        else {
            transplant(y, y->right);
            y->right = z->right;
            y->right->parent = y;
        }
        transplant(z, y);
        y->left = z->left;
        y->left->parent = y;
        y->color = z->color;
    }
    if (originalColor == BLACK)
        fixDelete(x);
}

void RedBlackTree::fixDelete(Node* x) {
    while (x != root && x->color == BLACK) {
        if (x == x->parent->left) {
            Node* w = x->parent->right;
            if (w->color == RED) {
                w->color = BLACK;
                x->parent->color = RED;
                leftRotate(x->parent);
                w = x->parent->right;
            }
            if (w->left->color == BLACK && w->right->color == BLACK) {
                w->color = RED;
                x = x->parent;
            }
            else {
                if (w->right->color == BLACK) {
                    w->left->color = BLACK;
                    w->color = RED;
                    rightRotate(w);
                    w = x->parent->right;
                }
                w->color = x->parent->color;
                x->parent->color = BLACK;
                w->right->color = BLACK;
                leftRotate(x->parent);
                x = root;
            }
        }
        else {
            Node* w = x->parent->left;
            if (w->color == RED) {
                w->color = BLACK;
                x->parent->color = RED;
                rightRotate(x->parent);
                w = x->parent->left;
            }
            if (w->right->color == BLACK && w->left->color == BLACK) {
                w->color = RED;
                x = x->parent;
            }
            else {
                if (w->left->color == BLACK) {
                    w->right->color = BLACK;
                    w->color = RED;
                    leftRotate(w);
                    w = x->parent->left;
                }
                w->color = x->parent->color;
                x->parent->color = BLACK;
                w->left->color = BLACK;
                rightRotate(x->parent);
                x = root;
            }
        }
    }
    x->color = BLACK;
}

Node* RedBlackTree::minimum(Node* node) {
    while (node->left != nullptr)
        node = node->left;
    return node;
}

void RedBlackTree::transplant(Node* u, Node* v) {
    if (u->parent == nullptr)
        root = v;
    else if (u == u->parent->left)
        u->parent->left = v;
    else
        u->parent->right = v;
    if (v != nullptr)
        v->parent = u->parent;
}

void RedBlackTree::deleteFix(Node* x) {
    // Placeholder for fix for Red-Black tree deletion
}

int main() {
    RedBlackTree tree;
    vector<int> values; // To store the numbers from the file

    int searchKey = 13032;
    int deleteKey = 13032;

    // Open the input file
    ifstream file("input.txt");
    if (!file.is_open()) {
        cerr << "Error: Could not open the file!" << endl;
        return 1;
    }

    int num;
    // Read integers from the file
    while (file >> num) {
        values.push_back(num);
    }

    file.close();

    // Insert numbers into the Red-Black tree
    printMemoryUsage("Before insertion:"); // Check memory usage before insertion
    auto startTimeInsert = chrono::high_resolution_clock::now(); // Start timing
    for (int value : values) {
        tree.insert(value);
    }
    auto endTimeInsert = chrono::high_resolution_clock::now(); // End timing
    printMemoryUsage("After insertion:"); // Check memory usage after insertion

    // Measure time taken to insert the numbers into the Red-Black tree
    double durationInsert = chrono::duration_cast<chrono::nanoseconds>(endTimeInsert - startTimeInsert).count();
    cout << fixed << setprecision(3) << "Time taken for insertion: " << durationInsert / 1000.0 << " microseconds" << endl;

    //// Display the tree (in-order traversal)
    //cout << "In-order traversal of the Red-Black tree: ";
    //tree.inOrder(tree.root);
    //cout << endl;

    // Search for a key
    printMemoryUsage("Before search:"); // Check memory usage before search
    auto startTimeSearch = chrono::high_resolution_clock::now(); // Start timing
    Node* result = tree.search(tree.root, searchKey);
    auto endTimeSearch = chrono::high_resolution_clock::now(); // End timing
    printMemoryUsage("After search:"); // Check memory usage after search

    // Measure time taken to search for a key
    double durationSearch = chrono::duration_cast<chrono::nanoseconds>(endTimeSearch - startTimeSearch).count();
    cout << fixed << setprecision(3) << "Time taken for searching a key: " << durationSearch / 1000.0 << " microseconds" << endl;

    // Display if the key was found or not
    if (result != nullptr)
        cout << "Key " << searchKey << " found!" << endl;
    else
        cout << "Key " << searchKey << " not found!" << endl;

    // Delete a key 
    printMemoryUsage("Before deletion:");
    auto startTimeDelete = chrono::high_resolution_clock::now(); // Start timing
    tree.deleteNode(deleteKey);
    auto endTimeDelete = chrono::high_resolution_clock::now(); // End timing
    printMemoryUsage("After deletion:");

    // Measure time taken to delete a key
    double durationDelete = chrono::duration_cast<chrono::nanoseconds>(endTimeDelete - startTimeDelete).count();
    cout << fixed << setprecision(3) << "Time taken for deletion of a key: " << durationDelete / 1000.0 << " microseconds" << endl;

    /* cout << "In-order traversal after deletion: ";
     tree.inOrder(tree.root);
     cout << endl;*/

    return 0;
}

