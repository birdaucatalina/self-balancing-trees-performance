// AVLTree.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <chrono> 
#include <iomanip>
#include <windows.h>
#include <psapi.h>

using namespace std;

// An AVL tree node 
struct Node {
    int key;
    Node* left;
    Node* right;
    int height;

    Node(int k) {
        key = k;
        left = nullptr;
        right = nullptr;
        height = 1;
    }
};

// A utility function to 
// get the height of the tree 
int height(Node* N) {
    if (N == nullptr)
        return 0;
    return N->height;
}

// A utility function to right 
// rotate subtree rooted with y 
Node* rightRotate(Node* y) {
    Node* x = y->left;
    Node* T2 = x->right;

    // Perform rotation 
    x->right = y;
    y->left = T2;

    // Update heights 
    y->height = 1 + max(height(y->left),
        height(y->right));
    x->height = 1 + max(height(x->left),
        height(x->right));

    // Return new root 
    return x;
}

// A utility function to left rotate 
// subtree rooted with x 
Node* leftRotate(Node* x) {
    Node* y = x->right;
    Node* T2 = y->left;

    // Perform rotation 
    y->left = x;
    x->right = T2;

    // Update heights 
    x->height = 1 + max(height(x->left),
        height(x->right));
    y->height = 1 + max(height(y->left),
        height(y->right));

    // Return new root 
    return y;
}

// Get balance factor of node N 
int getBalance(Node* N) {
    if (N == nullptr)
        return 0;
    return height(N->left) - height(N->right);
}

// Recursive function to insert a key in 
// the subtree rooted with node 
Node* insert(Node* node, int key) {

    // Perform the normal BST insertion
    if (node == nullptr)
        return new Node(key);

    if (key < node->key)
        node->left = insert(node->left, key);
    else if (key > node->key)
        node->right = insert(node->right, key);
    else // Equal keys are not allowed in BST 
        return node;

    // Update height of this ancestor node 
    node->height = 1 + max(height(node->left),
        height(node->right));

    // Get the balance factor of this ancestor node 
    int balance = getBalance(node);

    // If this node becomes unbalanced, 
    // then there are 4 cases 

    // Left Left Case 
    if (balance > 1 && key < node->left->key)
        return rightRotate(node);

    // Right Right Case 
    if (balance < -1 && key > node->right->key)
        return leftRotate(node);

    // Left Right Case 
    if (balance > 1 && key > node->left->key) {
        node->left = leftRotate(node->left);
        return rightRotate(node);
    }

    // Right Left Case 
    if (balance < -1 && key < node->right->key) {
        node->right = rightRotate(node->right);
        return leftRotate(node);
    }

    // Return the (unchanged) node pointer 
    return node;
}

// A utility function to print 
// preorder traversal of the tree 
void preOrder(Node* root) {
    if (root != nullptr) {
        cout << root->key << " ";
        preOrder(root->left);
        preOrder(root->right);
    }
}

// A utility function to print 
// inorder traversal of the tree 
void inOrder(Node* root) {
    if (root != nullptr) {
        inOrder(root->left);
        std::cout << root->key << " ";
        inOrder(root->right);
    }
}

// A utility function to get the node with minimum value
Node* minValueNode(Node* node) {
    Node* current = node;
    // Loop down to find the leftmost leaf
    while (current->left != nullptr)
        current = current->left;
    return current;
}

// Recursive function to delete a key from the subtree rooted with node
Node* deleteNode(Node* root, int key) {
    // STEP 1: Perform standard BST delete
    if (root == nullptr)
        return root;

    // If the key to be deleted is smaller than the root's key, then it lies in the left subtree
    if (key < root->key)
        root->left = deleteNode(root->left, key);

    // If the key to be deleted is greater than the root's key, then it lies in the right subtree
    else if (key > root->key)
        root->right = deleteNode(root->right, key);

    // If key is the same as root's key, then this is the node to be deleted
    else {
        // Node with only one child or no child
        if (root->left == nullptr) {
            Node* temp = root->right;
            delete root;
            return temp;
        }
        else if (root->right == nullptr) {
            Node* temp = root->left;
            delete root;
            return temp;
        }

        // Node with two children: Get the inorder successor (smallest in the right subtree)
        Node* temp = minValueNode(root->right);

        // Copy the inorder successor's content to this node
        root->key = temp->key;

        // Delete the inorder successor
        root->right = deleteNode(root->right, temp->key);
    }

    // STEP 2: Update height of the current node
    root->height = 1 + max(height(root->left), height(root->right));

    // STEP 3: Get the balance factor and perform rotations if necessary

    int balance = getBalance(root);

    // Left Left Case
    if (balance > 1 && getBalance(root->left) >= 0)
        return rightRotate(root);

    // Right Right Case
    if (balance < -1 && getBalance(root->right) <= 0)
        return leftRotate(root);

    // Left Right Case
    if (balance > 1 && getBalance(root->left) < 0) {
        root->left = leftRotate(root->left);
        return rightRotate(root);
    }

    // Right Left Case
    if (balance < -1 && getBalance(root->right) > 0) {
        root->right = rightRotate(root->right);
        return leftRotate(root);
    }

    return root;
}

// A utility function to search a key in the AVL tree
Node* search(Node* root, int key) {
    // Base case: root is null or key is present at the root
    if (root == nullptr || root->key == key)
        return root;

    // Key is greater than root's key, so search in the right subtree
    if (key > root->key)
        return search(root->right, key);

    // Key is smaller than root's key, so search in the left subtree
    return search(root->left, key);
}

void printMemoryUsage(const string& message) {
    PROCESS_MEMORY_COUNTERS pmc;

    // Obține informații despre utilizarea memoriei procesului
    if (GetProcessMemoryInfo(GetCurrentProcess(), &pmc, sizeof(pmc))) {
        cout << message << " Memory used: " << pmc.WorkingSetSize / 1024 << " KB" << endl;
    }
    else {
        cerr << "Failed to get memory info" << std::endl;
    }
}

int main() {
    Node* root = nullptr;
    vector<int> values; // To store the numbers from the file

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

    // Insert numbers into the AVL tree
    printMemoryUsage("Before insertion:"); // Check memory usage before insertion
    auto startTimeInsert = chrono::high_resolution_clock::now(); // Start timing
    for (int value : values) {
        root = insert(root, value);
    }
    auto endTimeInsert = chrono::high_resolution_clock::now(); // End timing
    printMemoryUsage("After insertion:"); // Check memory usage after insertion

    // Measure time taken to insert the numbers into the AVL tree
    double durationInsert = chrono::duration_cast<chrono::nanoseconds>(endTimeInsert - startTimeInsert).count();
    cout << fixed << setprecision(3) << "Time taken for insertion: " << durationInsert / 1000.0 << " microseconds" << endl;


    // Display the tree (in-order traversal)
    cout << "In-order traversal of the AVL tree: ";
    inOrder(root);
    cout << endl;

    // Search for a key
    int searchKey = 12;
    printMemoryUsage("Before search:"); // Check memory usage before search
    auto startTimeSearch = chrono::high_resolution_clock::now(); // Start timing
    Node* result = search(root, searchKey);
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
    int deleteKey = 18;
    printMemoryUsage("Before deletion:"); // Check memory usage before delete
    auto startTimeDelete = chrono::high_resolution_clock::now(); // Start timing
    root = deleteNode(root, deleteKey);
    auto endTimeDelete = chrono::high_resolution_clock::now(); // End timing
    printMemoryUsage("After deletion:"); // Check memory usage after delete

    // Measure time taken to delete a key
    double durationDelete = chrono::duration_cast<chrono::nanoseconds>(endTimeDelete - startTimeDelete).count();
    cout << fixed << setprecision(3) << "Time taken for deletion of a key: " << durationDelete / 1000.0 << " microseconds" << endl;

    cout << "In-order traversal after deletion: ";
    inOrder(root);
    cout << endl;
    
    return 0;
}
