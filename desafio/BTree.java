import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class BTree {
    public static final int T = 3; 
    public Node root;

    public BTree() {
        root = new Node(T);
    }

    public void insert(double key) {
        Node r = root;
        if (r.numKeys == 2 * T - 1) {
            Node s = new Node(T);
            root = s;
            s.isLeaf = false;
            s.children[0] = r;
            splitChild(s, 0);
            insertNonFull(s, key);
        } else {
            insertNonFull(r, key);
        }
    }

    private void insertNonFull(Node node, double key) {
        int i = node.numKeys - 1;
        if (node.isLeaf) {
            while (i >= 0 && key < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.numKeys++;
        } else {
            while (i >= 0 && key < node.keys[i]) {
                i--;
            }
            i++;
            Node child = node.children[i];
            if (child.numKeys == 2 * T - 1) {
                splitChild(node, i);
                if (key > node.keys[i]) {
                    i++;
                }
            }
            insertNonFull(node.children[i], key);
        }
    }

    private void splitChild(Node parent, int index) {
        Node fullChild = parent.children[index];
        Node newChild = new Node(T);
        newChild.isLeaf = fullChild.isLeaf;
        newChild.numKeys = T - 1;

        for (int i = 0; i < T - 1; i++) {
            newChild.keys[i] = fullChild.keys[i + T];
        }

        if (!fullChild.isLeaf) {
            for (int i = 0; i < T; i++) {
                newChild.children[i] = fullChild.children[i + T];
            }
        }

        fullChild.numKeys = T - 1;

        for (int i = parent.numKeys; i > index; i--) {
            parent.children[i + 1] = parent.children[i];
        }
        parent.children[index + 1] = newChild;

        for (int i = parent.numKeys - 1; i >= index; i--) {
            parent.keys[i + 1] = parent.keys[i];
        }
        parent.keys[index] = fullChild.keys[T - 1];
        parent.numKeys++;
    }

    public boolean search(double key) {
        return search(root, key);
    }

    private boolean search(Node node, double key) {
        int i = 0;
        while (i < node.numKeys && key > node.keys[i]) {
            i++;
        }
        if (i < node.numKeys && key == node.keys[i]) {
            return true;
        }
        if (node.isLeaf) {
            return false;
        }
        return search(node.children[i], key);
    }

    public void remove(double key) {
        remove(root, key);
        if (root.numKeys == 0) {
            if (root.isLeaf) {
                root = null;
            } else {
                root = root.children[0];
            }
        }
    }

    private void remove(Node node, double key) {
        int i = 0;
        while (i < node.numKeys && key > node.keys[i]) {
            i++;
        }

        if (i < node.numKeys && key == node.keys[i]) {
            if (node.isLeaf) {
                for (int j = i; j < node.numKeys - 1; j++) {
                    node.keys[j] = node.keys[j + 1];
                }
                node.numKeys--;
            } else {
                Node leftChild = node.children[i];
                Node rightChild = node.children[i + 1];
                if (leftChild.numKeys >= T) {
                    double predKey = getPredecessor(leftChild);
                    node.keys[i] = predKey;
                    remove(leftChild, predKey);
                } else if (rightChild.numKeys >= T) {
                    double succKey = getSuccessor(rightChild);
                    node.keys[i] = succKey;
                    remove(rightChild, succKey);
                } else {
                    merge(node, i);
                    remove(leftChild, key);
                }
            }
        } else if (!node.isLeaf) {
            Node child = node.children[i];
            if (child.numKeys == T - 1) {
                Node leftSibling = (i > 0) ? node.children[i - 1] : null;
                Node rightSibling = (i < node.numKeys) ? node.children[i + 1] : null;

                if (leftSibling != null && leftSibling.numKeys >= T) {
                    borrowFromLeft(node, i);
                } else if (rightSibling != null && rightSibling.numKeys >= T) {
                    borrowFromRight(node, i);
                } else {
                    if (leftSibling != null) {
                        merge(node, i - 1);
                        child = leftSibling;
                    } else {
                        merge(node, i);
                    }
                    remove(child, key);
                }
            } else {
                remove(child, key);
            }
        }
    }

    private double getPredecessor(Node node) {
        while (!node.isLeaf) {
            node = node.children[node.numKeys];
        }
        return node.keys[node.numKeys - 1];
    }

    private double getSuccessor(Node node) {
        while (!node.isLeaf) {
            node = node.children[0];
        }
        return node.keys[0];
    }

    private void borrowFromLeft(Node parent, int index) {
        Node child = parent.children[index];
        Node sibling = parent.children[index - 1];

        for (int i = child.numKeys - 1; i >= 0; i--) {
            child.keys[i + 1] = child.keys[i];
        }
        child.keys[0] = parent.keys[index - 1];
        child.numKeys++;

        parent.keys[index - 1] = sibling.keys[sibling.numKeys - 1];
        sibling.numKeys--;
    }

    private void borrowFromRight(Node parent, int index) {
        Node child = parent.children[index];
        Node sibling = parent.children[index + 1];

        child.keys[child.numKeys] = parent.keys[index];
        child.numKeys++;

        parent.keys[index] = sibling.keys[0];
        for (int i = 1; i < sibling.numKeys; i++) {
            sibling.keys[i - 1] = sibling.keys[i];
        }
        sibling.numKeys--;
    }

    private void merge(Node parent, int index) {
        Node leftChild = parent.children[index];
        Node rightChild = parent.children[index + 1];

        leftChild.keys[T - 1] = parent.keys[index];
        for (int i = 0; i < rightChild.numKeys; i++) {
            leftChild.keys[i + T] = rightChild.keys[i];
        }
        if (!leftChild.isLeaf) {
            for (int i = 0; i <= rightChild.numKeys; i++) {
                leftChild.children[i + T] = rightChild.children[i];
            }
        }
        for (int i = index + 1; i < parent.numKeys; i++) {
            parent.keys[i - 1] = parent.keys[i];
        }
        for (int i = index + 2; i <= parent.numKeys; i++) {
            parent.children[i - 1] = parent.children[i];
        }
        parent.numKeys--;
        leftChild.numKeys += rightChild.numKeys + 1;
    }

    public void writeToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writeNodeToFile(writer, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNodeToFile(BufferedWriter writer, Node node) throws IOException {
        for (int i = 0; i < node.numKeys; i++) {
            writer.write(node.keys[i] + " ");
        }
        writer.newLine();
        if (!node.isLeaf) {
            for (int i = 0; i <= node.numKeys; i++) {
                writeNodeToFile(writer, node.children[i]);
            }
        }
    }

    public void readFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split(" ");
                for (String num : numbers) {
                    insert(Double.parseDouble(num));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}