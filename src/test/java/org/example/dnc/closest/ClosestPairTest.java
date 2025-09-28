package org.example.dnc.closest;


import org.example.dnc.geom.Point2D;
import org.example.dnc.metrics.Metrics;
import org.junit.jupiter.api.Test;


import java.util.Random;


import static org.junit.jupiter.api.Assertions.*;


public class ClosestPairTest {


    @Test
    void bruteForceCheckSmallN() {
        Random r = new Random(1);
        for (int n : new int[]{2, 3, 5, 50, 200, 1000, 2000}) {
            Point2D[] pts = new Point2D[n];
            for (int i = 0; i < n; i++) pts[i] = new Point2D(r.nextDouble(), r.nextDouble());
            Metrics m = new Metrics();
            double dfast = ClosestPair.closestDist2(pts, m);
            double dslow = brute(pts);
            assertEquals(dslow, dfast, 1e-12, "n=" + n);
            assertTrue(m.maxDepth <= Math.ceil(2 * (Math.log(Math.max(2,n)) / Math.log(2.0))) + 8);
        }
    }


    private static double brute(Point2D[] a) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                best = Math.min(best, Point2D.dist2(a[i], a[j]));
        return best;
    }
}