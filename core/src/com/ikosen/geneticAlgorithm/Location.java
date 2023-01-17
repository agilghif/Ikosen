package com.ikosen.geneticAlgorithm;

public class Location {
    public float x1, x2, y1, y2;
    public float width, height;
    Location left, right;

    public Location(float width, float height) {
        this.x1 = 0;
        this.x2 = Math.abs(width);
        this.y1 = 0;
        this.y2 = Math.abs(height);

        this.width = width;
        this.height = height;
    }

    public void insert(Location other) { // assumption : there are no two equal location
        if (this.lessThan(other)) {
            if (left == null)
                left = other;
            else
                left.insert(other);
        }
        else {
            if (right == null)
                right = other;
            else
                right.insert(other);
        }
    }

    public void moveTR(float dx, float dy) {
        this.x1+=dx;
        this.x2+=dx;
        this.y1+=dy;
        this.y2+=dy;
    }

    public void moveTL(float dx, float dy) {
        this.x1-=dx;
        this.x2-=dx;
        this.y1+=dy;
        this.y2+=dy;
    }

    public void moveBR(float dx, float dy) {
        this.x1+=dx;
        this.x2+=dx;
        this.y1-=dy;
        this.y2-=dy;
    }

    public void moveBL(float dx, float dy) {
        this.x1-=dx;
        this.x2-=dx;
        this.y1-=dy;
        this.y2-=dy;
    }

    public boolean equals(Location other) {
        return (x1 < other.x2 && other.x1 < x2) && (y1 < other.y2 && other.y1 < y2);
    }

    public boolean lessThan(Location other) {
        if (x2 < other.x1) {
            return true;
        }
        else if (x1 < other.x2 && other.x1 < x2) {
            if (y2 < other.y1)
                return true;
        }
        return false;
    }

    public boolean greaterThan(Location other) {
        return y2 < x1;
    }

    public boolean contains(Location location) {
        if (this.equals(location))
            return true;
        boolean ans = false;
        if (left != null)
            ans |= left.contains(location);
        if (right != null)
            ans |= right.contains(location);
        return ans;
    }

    public void resetLocation() {
        x1 = 0;
        x2 = width;
        y1 = 0;
        y2 = height;
    }

    public void setChild(Location left, Location right) {
        this.left = left;
        this.right = right;
    }
}
