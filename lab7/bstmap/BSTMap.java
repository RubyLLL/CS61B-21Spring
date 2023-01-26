package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<Key extends Comparable<Key>, Value> implements Map61B<Key, Value> {
    BSTNode root;
    int size;

    public BSTMap() {
        size = 0;
        root = null;
    }

    public BSTMap(BSTNode root) {
        size = 1;
        root = root;
    }

    class BSTNode {
        Key key;
        Value value;
        BSTNode left, right;

        public BSTNode(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Key key) {
        return get(key) != null;
    }

    private Value get(BSTNode root, Key key) {
        if (root == null) {
            return null;
        }
        if (key.compareTo(root.key) < 0) {
            return get(root.left, key);
        } else if (key.compareTo(root.key) > 0) {
            return get(root.right, key);
        } else {
            return root.value;
        }
    }

    @Override
    public Value get(Key key) {
        return get(root, key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(Key key, Value value) {
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode root, Key key, Value value) {

        if (root == null) {
            return new BSTNode(key, value);
        }
        if (key.compareTo(root.key) < 0) {
            root.left = put(root.left, key, value);
        } else if (key.compareTo(root.key) > 0) {
            root.right = put(root.right, key, value);
        } else {
            root.value = value;
        }
        return root;
    }

    @Override
    public Set<Key> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value remove(Key key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value remove(Key key, Value value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Key> iterator() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("hi", null);
    }
}
