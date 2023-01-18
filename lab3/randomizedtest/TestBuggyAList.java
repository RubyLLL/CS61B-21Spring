package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {


    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> correctList = new AListNoResizing<>();
        BuggyAList<Integer> suspiciousList = new BuggyAList<>();
        for (int i = 0; i < 3; i++) {
            correctList.addLast(i);
            suspiciousList.addLast(i);
        }

        Assert.assertEquals(correctList.removeLast(), suspiciousList.removeLast());
        Assert.assertEquals(correctList.removeLast(), suspiciousList.removeLast());
        Assert.assertEquals(correctList.removeLast(), suspiciousList.removeLast());

    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> R = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                R.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int sizeR = R.size();
            } else if (operationNumber == 2 && L.size() > 0 && R.size() > 0) {
                L.getLast();
                R.getLast();

            } else if (operationNumber == 3 && L.size() > 0 && R.size() > 0) {
                L.removeLast();
                R.removeLast();
            }
        }
    }
}
