package by.it.group551003.paskal.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        int height;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;
    

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;
        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;
        y.left = x;
        x.right = t2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node rebalance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);

        if (bf > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }


    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key is null");
        Node[] oldValueHolder = new Node[1];
        root = insert(root, key, value, oldValueHolder);
        if (oldValueHolder[0] == null) {

        }
        return oldValueHolder[0] == null ? null : oldValueHolder[0].value;
    }

    private Node insert(Node node, int key, String value, Node[] oldValueHolder) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = insert(node.left, key, value, oldValueHolder);
        } else
            if (key > node.key) {
                node.right = insert(node.right, key, value, oldValueHolder);
        } else {
            oldValueHolder[0] = new Node(node.key, node.value);
            node.value = value;
            return node;
        }
        return rebalance(node);
    }


    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node cur = root;
        int k = (Integer) key;
        while (cur != null) {
            if (k < cur.key) cur = cur.left;
            else if (k > cur.key) cur = cur.right;
            else return cur.value;
        }
        return null;
    }


    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        Node cur = root;
        int k = (Integer) key;
        while (cur != null) {
            if (k < cur.key) cur = cur.left;
            else if (k > cur.key) cur = cur.right;
            else return true;
        }
        return false;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        if (!containsKey(k)) return null;
        Node[] removed = new Node[1];
        root = delete(root, k, removed);
        size--;
        return removed[0].value;
    }

    private Node delete(Node node, int key, Node[] removed) {
        if (node == null) return null;
        if (key < node.key) {
            node.left = delete(node.left, key, removed);
        } else if (key > node.key) {
            node.right = delete(node.right, key, removed);
        } else {
            removed[0] = new Node(node.key, node.value);
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node min = node.right;
            while (min.left != null) min = min.left;
            node.key = min.key;
            node.value = min.value;
            node.right = deleteMin(node.right);
        }
        return rebalance(node);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        return rebalance(node);
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append('}');
        return sb.toString();
    }

    private void inorder(Node node, StringBuilder sb) {
        if (node == null) return;
        inorder(node.left, sb);
        sb.append(node.key).append('=').append(node.value).append(", ");
        inorder(node.right, sb);
    }


    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}