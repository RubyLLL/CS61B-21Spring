package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private Node<T> sentinel;
    private int size;

    private static class Node<T> {
        private T item;
        private Node<T> next;
        private Node<T> prev;

        public Node(T item, Node<T> next, Node<T> prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }

    }

    public LinkedListDeque() {
        this.sentinel = new Node(42, null, null);
        this.size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node<T> node = new Node<>(item, null, null);
        if (size == 0) {
            sentinel.prev = node;
        }
        Node<T> currentFront = sentinel.next; // null now
        node.next = currentFront == null ? sentinel : currentFront;
        node.prev = sentinel;
        sentinel.next = node;
        if (currentFront != null) currentFront.prev = node;
        size++;
    }

    /**
     * @param item the item to be added to the end of the deque
     */
    @Override
    public void addLast(T item) {
        if (size == 0) {
            addFirst(item);
            return;
        }
        Node<T> currentLast = sentinel.prev;
        Node<T> node = new Node<>(item, sentinel, currentLast);
        sentinel.prev = node;
        currentLast.next = node;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        String rec = "";
        for (Node<T> node = sentinel.next; node != sentinel; node = node.next) {
            rec += node.item + " ";
        }
        System.out.println(rec);
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = sentinel.next.item; // current first item
        sentinel.next = sentinel.next.next;
        if (sentinel.next != null) {
            sentinel.next.prev = sentinel;
        }
        size--;
        return item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return item;
    }

    @Override
    public T get(int index) {
        if (index > size) {
            return null;
        }
        Node<T> curr = sentinel.next;
        while (index > 0) {
            curr = curr.next;
            index--;
        }
        return curr.item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Deque)) {
            return false;
        }
        Deque<?> deque = (Deque<?>) o;
        if (size != deque.size()) {
            return false;
        }

        for(int i = 0; i < size; i++) {
            if (!get(i).equals(deque.get(i))) {
                return false;
            }
        }
        return true;
    }

   class MyLLIterator implements Iterator<T> {
        private Node<T> curr;

        public MyLLIterator() {
            curr = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return curr != sentinel && curr != null;
        }

        @Override
        public T next() {
            T item = curr.item;
            curr = curr.next;
            return item;
        }
    }

    public Iterator<T> iterator() {
        return new MyLLIterator();
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        if (index == 0) {
            return sentinel.next.item;
        } else{
            sentinel = sentinel.next;
        }
        return getRecursive(index - 1);
    }
}
