public class Node {

    int T;
    double[] keys;
    Node[] children;
    int numKeys;
    boolean isLeaf;

    public Node(int T) {
        this.T = T;
        numKeys = 0;
        keys = new double[2 * T - 1];
        children = new Node[2 * T];
        isLeaf = true;
    }

    int findKey(double key) {
        int i = 0;
        while (i < numKeys && keys[i] < key) {
            i++;
        }
        return i;
    }
}