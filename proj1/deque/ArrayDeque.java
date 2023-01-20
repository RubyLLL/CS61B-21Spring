package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T>{
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

    @Override
    public void addFirst(T item){
        if (front == -1) resize(2 * items.length);
        items[front] = item;
        front --;
        size ++;
    }

    @Override
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

    // TODO: implement size down
    @Override
    public T removeFirst() {
        if(size == 0) return null;
        T target = items[front + 1];
        items[front + 1] = null;
        front ++;
        size --;
        return target;
    }

    // TODO: implement size down
    @Override
    public T removeLast() {
        if(size == 0) return null;
        T target = items[rear - 1];
        items[rear - 1] = null;
        rear --;
        size --;
        return target;
    }

    @Override
    public T get(int index){
        if(index >= size || index < -1) return null;
        int target = front + 1 + index;
        return items[target];
    }

    @Override
    public void printDeque(){
        int curr = 0;
        String res = "";
        while(curr < size){
            res += get(curr) + " ";
            curr ++;
        }
        System.out.println(res);
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof ArrayDeque)){
            return false;
        }

        ArrayDeque<?> arrayDeque = (ArrayDeque<?>) o;
        if (size!= arrayDeque.size) return false;

        for(int i = 0; i < size; i++){
            if(!get(i).equals(arrayDeque.get(i))) {
                return false;
            }
        }
        return true;
    }

    private class MyArrayIterator implements Iterator<T> {
        int curr;

        public MyArrayIterator(){
            curr = front + 1;
        }

        @Override
        public boolean hasNext() {
            return curr < rear;
        }

        @Override
        public T next() {
            if(!hasNext()) {
                return null;
            }
            T result =  items[curr];
            if(curr == 0){ // front done, moves to rear
                curr = items.length / 2;
            } else if(curr <= items.length / 2 - 1){
                curr ++;
            } else if(curr <= items.length){
                curr ++;
            }
            return result;
        }
    }

    public Iterator<T> iterator(){
        return new MyArrayIterator();
    }

    public static void main(String[] args){

    }
}
