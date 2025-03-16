import java.util.*;

class Btree {
    private BTreeNode root;
    private int t;

    public Btree(int t) {
        this.root = new BTreeNode(t, true);
        this.t = t;
    }

    public void insert(double key) {
        if (root.n == 2 * t - 1) {
            BTreeNode s = new BTreeNode(t, false);
            s.children[0] = root;
            s.splitChild(0, root);
            int i = (s.keys[0] < key) ? 1 : 0;
            s.children[i].insertNonFull(key);
            root = s;
        } else {
            root.insertNonFull(key);
        }
    }

    public boolean search(double key) {
        return root.search(key) != null;
    }

    public void remove(double key) {
        root.remove(key);
        if (root.n == 0) {
            if (!root.leaf) {
                root = root.children[0];
            } else {
                root = new BTreeNode(t, true);
            }
        }
    }

    public List<Double> getSortedElements() {
        List<Double> sortedList = new ArrayList<>();
        root.traverse(sortedList);
        return sortedList;
    }
}