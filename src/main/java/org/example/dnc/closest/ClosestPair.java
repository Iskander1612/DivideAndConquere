package org.example.dnc.closest;

import org.example.dnc.metrics.Metrics;
import org.example.dnc.geom.Point2D;
import java.util.*;


public final class ClosestPair {
    private ClosestPair() {}

    public static double closestDist2(Point2D[] pts, Metrics m) {
        if (pts == null || pts.length < 2) return Double.POSITIVE_INFINITY;
        Point2D[] px = pts.clone();
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x()));
        Point2D[] py = px.clone();
        Arrays.sort(py, Comparator.comparingDouble(p -> p.y()));
        return solve(px, py, 0, px.length, m);
    }


    private static double solve(Point2D[] px, Point2D[] py, int lo, int hi, Metrics m) {
        int n = hi - lo;
        if (n <= 3) {
            double best = Double.POSITIVE_INFINITY;
            for (int i = lo; i < hi; i++)
                for (int j = i + 1; j < hi; j++) {
                    if (m != null) m.incCmp();
                    best = Math.min(best, Point2D.dist2(px[i], px[j]));
                }
            Arrays.sort(px, lo, hi, Comparator.comparingDouble(Point2D::y));
            return best;
        }


        int mid = lo + n / 2;
        double midX = px[mid].x();
        if (m != null) m.pushDepth();

        List<Point2D> leftY = new ArrayList<>(mid - lo);
        List<Point2D> rightY = new ArrayList<>(hi - mid);
        for (Point2D p : py) {
            double x = p.x();
            if (x < midX || (x == midX && indexOf(px, lo, mid, p) >= 0)) leftY.add(p); else rightY.add(p);
        }

        double dl = solve(px, leftY.toArray(Point2D[]::new), lo, mid, m);
        double dr = solve(px, rightY.toArray(Point2D[]::new), mid, hi, m);
        if (m != null) m.popDepth();
        double d = Math.min(dl, dr);

        List<Point2D> strip = new ArrayList<>();
        for (Point2D p : py) {
            double dx = p.x() - midX;
            if (dx*dx < d) strip.add(p);
        }


        int s = strip.size();
        for (int i = 0; i < s; i++) {
            Point2D a = strip.get(i);
            for (int j = i + 1; j < s && (strip.get(j).y() - a.y())*(strip.get(j).y() - a.y()) < d; j++) {
                if (m != null) m.incCmp();
                double dij = Point2D.dist2(a, strip.get(j));
                if (dij < d) d = dij;
            }
        }
        return d;
    }

    private static int indexOf(Point2D[] px, int lo, int hi, Point2D p) {
        for (int i = lo; i < hi; i++) if (px[i] == p) return i;
        return -1;
    }
}