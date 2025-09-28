package org.example.dnc.sort;


import org.example.dnc.metrics.Metrics;
import org.example.dnc.util.ArrayOps;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;


public class QuickSortTest {


    @Test
    void randomCorrectnessAndDepthBound() {
        long seed = 12345L;
        for (int n : new int[]{1, 2, 5, 50, 5_000, 50_000}) {
            int[] a = ArrayOps.randomArray(n, seed);
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            Metrics m = new Metrics();
            QuickSort.sort(a, 24, seed, m);
            assertArrayEquals(b, a);
            int bound = (int) Math.ceil(2 * (Math.log(Math.max(1, n)) / Math.log(2.0))) + 8;
            assertTrue(m.maxDepth <= bound,
                    () -> "depth too large: " + m.maxDepth + " for n=" + n);
        }
    }


    @Test
    void manyEqualElements() {
        int n = 20_000;
        int[] a = new int[n];
        Arrays.fill(a, 7);
        int[] b = Arrays.copyOf(a, a.length);
        Metrics m = new Metrics();
        QuickSort.sort(a, 24, 1L, m);
        Arrays.sort(b);
        assertArrayEquals(b, a);
    }


    @Test
    void adversarialSortedAscending() {
        for (int n : new int[]{10, 1_000, 20_000}) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = i;
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            Metrics m = new Metrics();
            QuickSort.sort(a, 24, 2024L, m);
            assertArrayEquals(b, a);
        }
    }
}