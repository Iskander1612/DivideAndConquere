package org.example.dnc.geom;


public record Point2D(double x, double y) {
    public static double dist2(Point2D a, Point2D b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return dx*dx + dy*dy;
    }
}