package deque;


import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;

public class MaxArrayDequeTest {

    @Test
    public void testIntegerMax(){
        Comparator<Integer> byValueASC =
                (Integer o1, Integer o2)->Integer.valueOf(o1) - Integer.valueOf(o2);
        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(byValueASC);
        for(int i = 0; i < 1000; i++){
            int operationNumber = StdRandom.uniform(0, 2);
            if(operationNumber == 0){
                deque.addFirst(i);
            }
            else if(operationNumber == 1){
                deque.addLast(i);
            }
        }
        assertEquals(Integer.valueOf(999), deque.max());

        Comparator<Integer> byValueDESC =
                (Integer o1, Integer o2)->Integer.valueOf(o2).compareTo(Integer.valueOf(o1));
        assertEquals(Integer.valueOf(0), deque.max(byValueDESC));
    }

    @Test
    public void testStringMax(){
        Comparator<String> byValueASE =
                (String o1, String o2)->o1.compareTo(o2);
        Comparator<String> byValueDESC =
                (String o1, String o2)->o2.compareTo(o1);
        MaxArrayDeque<String> deque = new MaxArrayDeque<>(byValueASE);
        deque.addFirst("a");
        deque.addFirst("b");
        deque.addFirst("c");
        deque.addFirst("d");
        deque.addFirst("e");
        deque.addLast("f");
        deque.addLast("g");
        deque.addLast("h");
        deque.addLast("i");
        deque.addLast("j");
        deque.addLast("k");

        assertEquals("k", deque.max());
        assertEquals("a", deque.max(byValueDESC));
    }
}
