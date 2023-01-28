package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
public class BSTMap<K, V extends Comparable> implements Iterable<K>, Serializable {
    private BSTNode root;
    private int size;

    private class BSTNode {
        K key;
        V value;
        BSTNode left, right;

        BSTNode() {}

        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public void saveFiles(File targetFolder) {
        //TODO: this should return a hash code to signal where we have put this file
    }
    @Override
    public Iterator<K> iterator() {
        return null;
    }
}
