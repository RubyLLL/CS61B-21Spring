package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testIsPrime() {
        //2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
        assertTrue(Primes.isPrime(2));
        assertTrue(Primes.isPrime(3));
        assertTrue(Primes.isPrime(5));
        assertTrue(Primes.isPrime(7));

        assertFalse(Primes.isPrime(0));
        assertFalse(Primes.isPrime(20));
        assertFalse(Primes.isPrime(50));
    }

    @Test
    public void testSquarePrimes() {
        IntList lst = IntList.of(0, 20, 30, 40, 50);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0 -> 20 -> 30 -> 40 -> 50", lst.toString());
        assertFalse(changed);

        lst = IntList.of(5, 7, 11, 13, 17, 19);
        changed = IntListExercises.squarePrimes(lst);
        assertEquals("25 -> 49 -> 121 -> 169 -> 289 -> 361", lst.toString());
        assertTrue(changed);
    }
}
