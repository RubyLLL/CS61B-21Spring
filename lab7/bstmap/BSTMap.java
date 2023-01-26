package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>, Iterable<K> {
    private BSTNode root;
    private int size;

    public BSTMap() {
        size = 0;
        root = null;
    }

    private class BSTNode {
        K key;
        V value;
        BSTNode left, right;

        BSTNode(K key, V value) {
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
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    private V get(BSTNode root, K key) {
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
    public V get(K key) {
        return get(root, key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode root, K key, V value) {

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
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new myBSTMaoKeyIterator(root);
    }

    private class myBSTMaoKeyIterator implements Iterator<K> {
        Stack<BSTNode> stack;
        BSTNode cur;

        myBSTMaoKeyIterator(BSTNode root) {
            this.cur = root;
            this.stack = new Stack<BSTNode>();
        }

        @Override
        public boolean hasNext() {
            return cur != null || !stack.isEmpty();
        }

        @Override
        public K next() {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            BSTNode node = cur;
            cur = cur.right;
            return node.key;
        }
    }
}
