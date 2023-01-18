import static org.junit.Assert.*;
import org.junit.Test;


public class Sort {


    @Test
    public void testFindSmallestInex1(){
        double[] input = {2, 1, 5, 6, 3, 4};
        int idx = SelectionSort.findSmallestIndex(input, 0);

        assertEquals(1, idx);
    }

    @Test
    public void testFindSmallestInex2(){
        double[] input = {2, 1, 5, 6, 3, 4};
        int idx = SelectionSort.findSmallestIndex(input, 1);

        assertEquals(1, idx);
    }

    @Test
    public void testFindSmallestInex3(){
        double[] input = {2, 1, 5, 6, 3, 4};
        int idx = SelectionSort.findSmallestIndex(input, 2);

        assertEquals(4, idx);
    }

    @Test
    public void testSwap(){
        double[] input = {2, 1, 5, 6, 3, 4};
        double[] expected = {1, 2, 5, 6, 3, 4};
        SelectionSort.swap(0 , 1, input);

        assertArrayEquals(expected, input, 0);
    }

    @Test
    public void testRecursiveSort(){
        double[] input = {2, 1, 5, 6, 3, 4};
        double[] expected = {1, 2, 3, 4, 5, 6};
        SelectionSort.recursiveSort(input, 0);

        assertArrayEquals(expected, input, 0);
    }

    @Test
    public void testIterativeSort(){
        double[] input = {2, 1, 5, 6, 3, 4};
        double[] expected = {1, 2, 3, 4, 5, 6};
        SelectionSort.iterativeSort(input);

        assertArrayEquals(expected, input, 0);
    }
}
