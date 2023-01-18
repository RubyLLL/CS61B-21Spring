package deque;

public class LinkedListDeque<T> {
    private final Node<T> sentinel;
    private int size;

    private static class Node<T> {
        public T item;
        public Node<T> next;
        public Node<T> prev;

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

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void addFirst(T item){
        Node<T> node = new Node<>(item, null, null);
        if (size == 0) sentinel.prev = node;
        Node<T> currentFront = sentinel.next; // null now
        node.next = currentFront == null ? sentinel : currentFront;
        node.prev = sentinel;
        sentinel.next = node;
        if(currentFront != null) currentFront.prev = node;
        size ++;
    }
    /**
     * @param item the item to be added to the end of the deque
     */
    public void addLast(T item){
        if(size == 0){
            addFirst(item);
            return;
        }
        Node<T> currentLast = sentinel.prev;
        Node<T> node = new Node<>(item, sentinel, currentLast);
        sentinel.prev = node;
        currentLast.next = node;
        size ++;
    }

    public int size(){ return size;}

    public void printDeque(){
        String rec = "";
        for (Node<T> node = sentinel.next; node!= sentinel; node = node.next){
            rec += node.item + " ";
        }
        System.out.println(rec);
    }

    public T removeFirst(){
        if (size == 0) return null;
        T item = sentinel.next.item; // current first item
        sentinel.next = sentinel.next.next;
        if (sentinel.next!= null) sentinel.next.prev = sentinel;
        size --;
        return item;
    }

    public T removeLast(){
        if(size == 0) return null;
        T item = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return item;
    }

    public T get(int index){
        if(index > size) return null;
        Node<T> curr = sentinel;
        while(index > 0){
            curr = curr.next;
            index--;
        }
        return curr.item;
    }

    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass()!= o.getClass()) return false;

        LinkedListDeque<?> linkedListDeque = (LinkedListDeque<?>) o;
        if (size!= linkedListDeque.size) return false;

        for(int i = 0; i < size; i++){
            if(!get(i).equals(linkedListDeque.get(i))) return false;
        }
        return true;
    }

    public T getRecursive(int index, Node<T> node){
        if(node == sentinel) return null;
        if(index == 1) return node.item;
        return getRecursive(index - 1, node.next);
    }

    public static void main(String[] args){
        LinkedListDeque<String> lld1 = new LinkedListDeque<>();
        lld1.addFirst("front");
        lld1.addFirst("frontmost");

        System.out.println(lld1.getRecursive(2, lld1.sentinel.next));
        System.out.println(lld1.getRecursive(1, lld1.sentinel.next));
        lld1.printDeque();
    }
}
