package org.example.dnc.metrics;

public class Metrics {
    public long comparisons;
    public long swaps;
    public long allocations;
    public int maxDepth;
    private int depth;


    public void reset() {
        comparisons = swaps = allocations = 0L;
        maxDepth = depth = 0;
    }


    public void addAlloc(long c) {
        allocations += Math.max(0, c);
    }

    public void incCmp() {
        comparisons++;
    }

    public void incSwap() {
        swaps++;
    }


    public void pushDepth() {
        depth++;
        if (depth > maxDepth) maxDepth = depth;
    }


    public void popDepth() {
        depth--;
    }
}