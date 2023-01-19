package deque;

public class MaxArrayDeque<T> implements Deque<T> {
    // Comparator<T> comparator;
    /**
     * creates a MaxArrayDeque with the given Comparator
     * @param
     */
    // public MaxArrayDeque(Comparator<T> c){}

    @Override
    public void addFirst(T item){}

    @Override
    public void addLast(T item){}
    @Override
    public T removeFirst() { return null; }

    @Override
    public T removeLast(){ return null; }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public int size() {return 0; }

    @Override
    public void printDeque(){}

    @Override
    public T get(int index){ return null; }

    /**
     * @return the maximum element in the deque as governed by the previously given Comparator.
     * If the MaxArrayDeque is empty, simply return null.
     */
    public T max(){ return null; }

    /**
     * @return the minimum element in the deque as governed by Comparator c; return null if the MaxArrayDeque is empty
     * @param c
     */
    // public T max(Comparator<T> c){ return null; }
}
