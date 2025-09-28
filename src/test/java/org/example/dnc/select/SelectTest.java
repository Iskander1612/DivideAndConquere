package org.example.dnc.select;


import org.example.dnc.metrics.Metrics;
import org.example.dnc.util.ArrayOps;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.Random;


import static org.junit.jupiter.api.Assertions.*;


public class SelectTest {


    @Test
    void compareWithArraysSortAcrossManyK() {
        Random rnd = new Random(42);
        for (int trial = 0; trial < 100; trial++) {
            int n = 100 + rnd.nextInt(900); // 100..999
            int[] a = ArrayOps.randomArray(n, rnd.nextLong());
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);


            for (int k : new int[]{0, n/4, n/2, 3*n/4, n-1}) {
                Metrics m = new Metrics();
                int got = Select.select(a.clone(), k, 24, m);
                assertEquals(b[k], got, "trial=" + trial + ", k=" + k);
                assertTrue(m.maxDepth <= Math.ceil(2 * Math.log(Math.max(2,n)) / Math.log(2.0)) + 16);
            }
        }
    }


    @Test
    void allEqualElements() {
        int n = 10_000;
        int[] a = new int[n];
        Arrays.fill(a, 5);
        Metrics m = new Metrics();
        int val = Select.select(a, n/2, 24, m);
        assertEquals(5, val);
    }


    @Test
    void smallEdgeCases() {
        int[] a = {9};
        assertEquals(9, Select.select(a, 0, 5, new Metrics()));


        int[] b = {2,1};
        assertEquals(1, Select.select(b, 0, 5, new Metrics()));
        assertEquals(2, Select.select(b, 1, 5, new Metrics()));
    }
}