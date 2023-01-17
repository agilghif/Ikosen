package com.ikosen.geneticAlgorithm;

public class Location {
    public float x1, x2, y1, y2;
    public float width, height;

    public Location(float width, float height) {
        this.x1 = 0;
        this.x2 = Math.abs(width);
        this.y1 = 0;
        this.y2 = Math.abs(height);

        this.width = width;
        this.height = height;
    }

    public void move(float dx, float dy) {
        this.x1+=dx;
        this.x2+=dx;
        this.y1+=dy;
        this.y2+=dy;
    }

    public void snapTo(Location other) {
        x1 = other.x1;
        x2 = x1 + width;
        y1 = other.y1;
        y2 = y1 + height;
    }

    public boolean intersect(Location other) {
        return (x1 < other.x2 && other.x1 < x2) && (y1 < other.y2 && other.y1 < y2);
    }

    public void resetLocation() {
        x1 = 0;
        x2 = width;
        y1 = 0;
        y2 = height;
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f] - [%.2f, %.2f]", x1, x2, y1, y2);
    }
}
