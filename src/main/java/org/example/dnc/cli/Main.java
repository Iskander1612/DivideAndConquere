package org.example.dnc.cli;


import org.example.dnc.io.CsvWriter;
import org.example.dnc.metrics.Metrics;
import org.example.dnc.sort.MergeSort;
import org.example.dnc.sort.QuickSort;
import org.example.dnc.select.Select;
import org.example.dnc.closest.ClosestPair;
import org.example.dnc.geom.Point2D;

import org.example.dnc.util.ArrayOps;



public final class Main {
    public static void main(String[] args) throws Exception {
        String algo = val(args, "--algo", "mergesort");
        String dist = val(args, "--dist", "uniform");
        int n = Integer.parseInt(val(args, "--n", "100000"));
        int k = Integer.parseInt(val(args, "--k", String.valueOf(n / 2))); // по умолчанию медиана
        int trials = Integer.parseInt(val(args, "--trials", "3"));
        int cutoff = Integer.parseInt(val(args, "--cutoff", "32"));
        long seed = Long.parseLong(val(args, "--seed", "42"));
        String csv = val(args, "--csv", "metrics.csv");

        CsvWriter out = new CsvWriter(csv);
        out.headerIfNew();

        Metrics m = new Metrics();

        for (int t = 1; t <= trials; t++) {
            int[] a = ArrayOps.randomArray(n, seed + t);
            m.reset();

            long t0 = System.nanoTime();

            String resultForCsv = "";

            if ("closest".equalsIgnoreCase(algo)) {
                Point2D[] pts = new Point2D[n];
                var rr = new java.util.Random(seed + t);
                for (int i = 0; i < n; i++) pts[i] = new Point2D(rr.nextDouble(), rr.nextDouble());
                double d2 = ClosestPair.closestDist2(pts, m);
                resultForCsv = Double.toString(d2);
            }
            else if ("select".equalsIgnoreCase(algo)) {
                int kk = Math.min(Math.max(0, k), n - 1);
                int ans = Select.select(a, kk, cutoff, m);
                resultForCsv = Integer.toString(ans);
            }
            else if ("quicksort".equalsIgnoreCase(algo)) {
                QuickSort.sort(a, cutoff, seed + t, m);
            }
            else if ("mergesort".equalsIgnoreCase(algo)) {
                MergeSort.sort(a, cutoff, m);
            }
            else {
                throw new IllegalArgumentException("Unsupported algo: " + algo);
            }


            long t1 = System.nanoTime();

            if (!"select".equalsIgnoreCase(algo) && !"closest".equalsIgnoreCase(algo)) {
                if (!ArrayOps.isSorted(a)) throw new AssertionError("Array not sorted");
            }


            String row = String.join(",",
                    algo,
                    String.valueOf(n),
                    String.valueOf(t),
                    String.valueOf(t1 - t0),
                    String.valueOf(m.maxDepth),
                    String.valueOf(m.comparisons),
                    String.valueOf(m.swaps),
                    String.valueOf(m.allocations),
                    resultForCsv
            );
            out.appendRow(row);
        }
        System.out.println("Done. Wrote: " + csv);
    }


    private static String val(String[] args, String key, String def) {
        for (int i = 0; i < args.length - 1; i++) if (key.equals(args[i])) return args[i + 1];
        return def;
    }
}