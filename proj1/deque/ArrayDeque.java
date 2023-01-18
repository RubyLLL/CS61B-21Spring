package deque;

import static org.junit.Assert.*;

public class ArrayDeque<T> {
    private T[] items;
    private int front;
    private int rear;
    private int size;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        front = -1;
        rear = 0;
        size = 0;
    }

    public void addFirst(T item){
        if (size == items.length){
            resize(2 * items.length);
        }
        if(front == -1) front = items.length;
        front--;
        items[front] = item;
        size++;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(2 * items.length);
        }
        items[rear++] = item;
        size++;
    }

    private void resize(int capacity) {
        T[] res = (T[])new Object[capacity];
        System.arraycopy(items, 0, res, 0, rear);
        if(front != -1) {
            System.arraycopy(items, front, res, capacity - (size - front), (size - front));
            front = capacity-(size-front);
        }
        items = res;
    }


    public int size() {
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public T removeFirst() {
        if(size == 0) return null;
        if ((size < items.length / 4) && (size > 4)) {
            resize(items.length / 4);
        }
        T first;
        if(front == -1){
            int index = 0;
            while(items[index] == null) index ++;
            first = items[index];
            items[index] = null;
        }
        else{
            first = items[front];
            items[front] = null;
            front++;
        }
        size--;
        return first;
    }
    public T removeLast() {
        if(size == 0) return null;
        if ((size < items.length / 4) && (size > 4)) {
            resize(items.length / 4);
        }
        return null;
    }

    public T get(int index){
        if(index >= size) return null;
        if(front == -1) return items[index];
        for(int i = front; i < items.length; i++){
            if(index == 0) return items[i];
            index--;
        }
        return items[index];
    }

    public void printDeque(){
        String res = "";
        int curr = 0;
        while(curr < size){
            res += get(curr++) + " ";
        }
        System.out.println(res);
    }

    public static void main(String[] args){
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for(int i = 0; i < 100; i++){
            deque.addLast(i);
        }
        int curr = 0;
        while(curr < 100){
            deque.removeFirst();
            curr ++;
        }

    }
}
