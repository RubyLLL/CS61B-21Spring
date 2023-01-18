package deque;

import static org.junit.Assert.*;

public class ArrayDeque<T> {
    private T[] items;
    private int front;
    private int rear;
    private int size;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        front = 3;
        rear = 4;
        size = 0;
    }

    public void addFirst(T item){
        if (front == -1) resize(2 * items.length);
        items[front] = item;
        front --;
        size ++;
    }

    public void addLast(T item) {
        if(rear == items.length) resize(2 * items.length);
        items[rear] = item;
        rear ++;
        size ++;
    }

    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        int frontCount = items.length/2-1 - front;
        int middle = capacity / 2;
        System.arraycopy(items, front + 1, temp, middle - frontCount, size);
        front = capacity/2 - (items.length / 2 - front);
        rear = capacity/2 + (rear - items.length / 2);
        items = temp;
    }


    public int size() {
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public T removeFirst() {
        if(size == 0) return null;
        T target = items[front + 1];
        items[front + 1] = null;
        front ++;
        size --;
        return target;
    }
    public T removeLast() {
        if(size == 0) return null;
        T target = items[rear - 1];
        items[rear - 1] = null;
        rear --;
        size --;
        return target;
    }

    public T get(int index){
        if(index >= size || index < -1) return null;
        int target = front + 1 + index;
        return items[target];
    }

    public void printDeque(){
        int curr = 0;
        String res = "";
        while(curr < size){
            res += get(curr) + " ";
            curr ++;
        }
        System.out.println(res);
    }

    public static void main(String[] args){}
}
