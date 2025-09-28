package org.example.dnc.sort;


import org.example.dnc.metrics.Metrics;
import org.example.dnc.util.ArrayOps;


public final class MergeSort {
    private MergeSort() {}


    public static void sort(int[] a, int cutoff, Metrics m) {
        if (a == null || a.length <= 1) return;
        int[] buf = new int[a.length];
        if (m != null) m.addAlloc(a.length);
        mergesort(a, buf, 0, a.length, cutoff, m);
    }


    private static void mergesort(int[] a, int[] buf, int lo, int hi, int cutoff, Metrics m) {
        int n = hi - lo;
        if (n <= 1) return;
        if (n <= cutoff) {
            ArrayOps.insertionSort(a, lo, hi, m);
            return;
        }
        int mid = lo + (n >>> 1);
        if (m != null) m.pushDepth();
        mergesort(a, buf, lo, mid, cutoff, m);
        mergesort(a, buf, mid, hi, cutoff, m);
        if (m != null) m.popDepth();
        merge(a, buf, lo, mid, hi, m);
    }



    private static void merge(int[] a, int[] buf, int lo, int mid, int hi, Metrics m) {
        int len = hi - lo;
        System.arraycopy(a, lo, buf, lo, len);
        if (m != null) m.addAlloc(len);
        int i = lo, j = mid, k = lo;
        while (i < mid && j < hi) {
            if (ArrayOps.cmp(buf[i], buf[j], m) <= 0) a[k++] = buf[i++];
            else a[k++] = buf[j++];
        }
        while (i < mid) a[k++] = buf[i++];
        while (j < hi) a[k++] = buf[j++];
    }
}