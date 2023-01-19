package deque;

public interface Deque<T> {
    public void addFirst(T item);
    public void addLast(T item);
    public T removeFirst();
    public T removeLast();
    public default boolean isEmpty(){
        if(size() == 0) return true;
        return false;
    }
    public int size();
    public void printDeque();
    public T get(int index);
}
