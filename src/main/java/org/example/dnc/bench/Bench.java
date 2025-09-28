package org.example.dnc.bench;

import org.example.dnc.select.Select;
import org.openjdk.jmh.annotations.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class Bench {

    @Param({"10000","20000","40000"})
    int n;

    @Param({"0.50"})
    double frac;

    int[] data;
    int k;

    @Setup(Level.Iteration)
    public void setup() {
        Random r = new Random(42);
        data = new int[n];
        for (int i = 0; i < n; i++) data[i] = r.nextInt();
        k = (int) Math.floor(frac * (n - 1));
    }

    @Benchmark
    public int mom5_select() {
        int[] a = data.clone();            // не портим исходный массив
        return Select.select(a, k, 24, null);
    }

    @Benchmark
    public int sort_then_pick() {
        int[] a = data.clone();
        Arrays.sort(a);
        return a[k];
    }
}
