package org.example.dnc.select;

import org.example.dnc.metrics.Metrics;
import org.example.dnc.util.ArrayOps;

public final class Select {
    private Select() {}
    public static int select(int[] a, int k, int cutoff, Metrics m) {
        if (a == null || a.length == 0) throw new IllegalArgumentException("empty array");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        return selectRange(a, 0, a.length, k, cutoff, m);
    }

    private static int selectRange(int[] a, int lo, int hi, int k, int cutoff, Metrics m) {
        while (true) {
            int n = hi - lo;
            if (n <= cutoff) {
                ArrayOps.insertionSort(a, lo, hi, m);
                return a[lo + k];
            }


            int numGroups = 0;
            for (int start = lo; start < hi; start += 5) {
                int end = Math.min(start + 5, hi);

                ArrayOps.insertionSort(a, start, end, m);
                int medianIndex = start + (end - start - 1) / 2;

                int target = lo + numGroups;
                if (medianIndex != target) ArrayOps.swap(a, medianIndex, target, m);
                numGroups++;
            }


            int medOfMedsIdx = (numGroups - 1) / 2;
            int mom = selectRange(a, lo, lo + numGroups, medOfMedsIdx, cutoff, m);


            int lt = lo, i = lo, gt = hi - 1;
            while (i <= gt) {
                int cmp = ArrayOps.cmp(a[i], mom, m);
                if (cmp < 0) { ArrayOps.swap(a, lt++, i++, m); }
                else if (cmp > 0) { ArrayOps.swap(a, i, gt--, m); }
                else { i++; }
            }


            int leftSize = lt - lo;
            int midSize = i - lt;


            if (k < leftSize) {
                hi = lt;
            } else if (k < leftSize + midSize) {
                return mom;
            } else {
                k = k - (leftSize + midSize);
                lo = i;
            }
        }
    }
}
