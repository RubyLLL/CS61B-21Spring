package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import static org.junit.Assert.assertTrue;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
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
        if (root == null || root.key == null) {
            size++;
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

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(BSTNode root) {
        if (root == null) {
            return;
        }
        printInOrder(root.left);
        System.out.print(root.key + " ");
        printInOrder(root.right);
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new java.util.HashSet<>();
        Iterator<K> it = iterator();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return set;
    }

    @Override
    public V remove(K key) {
        return remove(key, root);
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    private V remove(K key, BSTNode node) {
        V result = null;
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            result = remove(key, node.left);
        } else if (key.compareTo(node.key) > 0) {
            result = remove(key, node.right);
        } else {
            result = node.value;
            size--;
            result = node.value;
            if (node.left != null && node.right != null) {
                BSTNode max = findMax(node.left);
                node.key = max.key;
                node.value = max.value;
                remove(max.key, max);
            } else if (node.left == null && node.right != null) {
                node.key = node.right.key;
                node.value = node.right.value;
                node.right = node.right.right;
                node.right = null;
            } else if (node.right == null && node.left != null) {
                node.key = node.left.key;
                node.value = node.left.value;
                node.left = node.left.left;
                node.left = null;
            } else {
                node.key = null;
                node.value = null;
            }

        }
        return result;
    }

    /**
     * Returns the largest node in the left subt
     * @return the subtree after having the largest node removed
     */
    private BSTNode findMax(BSTNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
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

    public static void main(String[] args) {
        BSTMap rightChild = new BSTMap();
        rightChild.put('A', 1);
        rightChild.put('B', 2);
        Integer result = (Integer) rightChild.remove('A');
        assertTrue(result.equals(Integer.valueOf(1)));
    }
}
