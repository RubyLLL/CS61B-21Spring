package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> comparator;
    /**
     * creates a MaxArrayDeque with the given Comparator
     * @param
     */
    public MaxArrayDeque(Comparator<T> c){
        comparator = c;
    }



    /**
     * @return the maximum element in the deque as governed by the previously given Comparator.
     * @return null if the MaxArrayDeque is empty
     */
    public T max(){
        if(size() == 0){
            return null;
        }
        T maximum = get(0);
        for(int i = 0; i < size(); i++){
            if(comparator.compare(maximum, get(i)) < 0){
                maximum = get(i);
            }
        }
        return maximum;
    }

    /**
     * @return the minimum element in the deque as governed by Comparator c;
     * @return null if the MaxArrayDeque is empty
     * @param c
     */
    public T max(Comparator<T> c){
        if(size() == 0){
            return null;
        }
        T maximum = get(0);
        for(int i = 0; i < size(); i++){
            if(c.compare(maximum, get(i)) < 0){
                maximum = get(i);
            }
        }
        return maximum;
    }
}
