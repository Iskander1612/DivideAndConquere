package org.example.dnc.sort;

import org.example.dnc.metrics.Metrics;
import org.example.dnc.util.ArrayOps;
import java.util.Random;


public final class QuickSort {
    private QuickSort() {}

    public static void sort(int[] a, int cutoff, long seed, Metrics m) {
        if (a == null || a.length <= 1) return;
        Random rnd = new Random(seed);
        quicksort(a, 0, a.length, cutoff, rnd, m);
    }

    private static void quicksort(int[] a, int lo, int hi, int cutoff, Random rnd, Metrics m) {
        while (hi - lo > 1) {
            int n = hi - lo;
            if (n <= cutoff) {
                ArrayOps.insertionSort(a, lo, hi, m);
                return;
            }

            int p = lo + rnd.nextInt(n);
            int pivot = a[p];
            ArrayOps.swap(a, p, hi - 1, m);

            int i = lo;
            for (int k = lo; k < hi - 1; k++) {
                if (ArrayOps.cmp(a[k], pivot, m) < 0) {
                    ArrayOps.swap(a, k, i, m);
                    i++;
                }
            }
            ArrayOps.swap(a, i, hi - 1, m);
            int mid = i;

            int leftSize  = mid - lo;
            int rightSize = hi - (mid + 1);
            if (m != null) m.pushDepth();
            if (leftSize < rightSize) {
                if (leftSize > 1) quicksort(a, lo, mid, cutoff, rnd, m);
                if (m != null) m.popDepth();
                lo = mid + 1;
            } else {
                if (rightSize > 1) quicksort(a, mid + 1, hi, cutoff, rnd, m);
                if (m != null) m.popDepth();
                hi = mid;
            }
        }
    }
}