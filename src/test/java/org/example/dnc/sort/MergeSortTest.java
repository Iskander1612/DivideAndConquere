package org.example.dnc.sort;

import org.example.dnc.metrics.Metrics;
import org.example.dnc.util.ArrayOps;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;


public class MergeSortTest {


    @Test
    void randomCorrectness() {
        for (int n : new int[]{0,1,2,10,1000,10_000}) {
            int[] a = ArrayOps.randomArray(n, 1234);
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            Metrics m = new Metrics();
            MergeSort.sort(a, 32, m);
            assertArrayEquals(b, a);

            int bound = (int)Math.ceil(2 * (Math.log(Math.max(1,n)) / Math.log(2.0))) + 4;
            assertTrue(m.maxDepth <= bound, "depth too large: " + m.maxDepth + ", n=" + n);
        }
    }

    @Test
    void adversarialReversed() {
        for (int n : new int[]{1,2,3,50,1_000}) {
            int[] a = ArrayOps.reversed(n);
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            Metrics m = new Metrics();
            MergeSort.sort(a, 32, m);
            assertArrayEquals(b, a);
        }
    }
}