package com.ikosen.geneticAlgorithm;

public class BodyPart {
    public int xVal, yVal;
    public Location location;  // Lokasi terkini dari body part
    public Location locationList;  // Location dari root body part node [0,0]
    public BodyPart TR, TL, BR, BL; // Children dari body part

    public BodyPart(float width, float height, int xVal, int yVal) {
        location = new Location(width, height);
        this.xVal = xVal;
        this.yVal = yVal;
    }

    public void insert(BodyPart other) {
        locationList = location;
        other.locationList = locationList;
        insert(other, 0f, 0f);
    }

    private void insert(BodyPart other, float offsetX, float offsetY) {
        if (other.xVal < xVal) { // LEFT
            if (other.yVal < yVal) { // BOTTOM
                other.moveBL(other.location.width, other.location.height);
                if (BL != null) {
                    BL.insert(other);
                }
                else {
                    if (!locationList.contains(other.location)) {
                        System.out.println("Masuk BL");
                        BL = other;
                        locationList.insert(other.location);
                    }
                }
            }
            else { // TOP
                other.moveTL(other.location.width, location.height);
                if (TL != null) {
                    TL.insert(other);
                }
                else {
                    if (!locationList.contains(other.location)) {
                        System.out.println("Masuk TL");
                        TL = other;
                        locationList.insert(other.location);
                    }
                }
            }
        }
        else { // RIGHT
            if (other.yVal < yVal) { // BOTTOM
                other.moveBR(location.width, other.location.height);
                if (BR != null) {
                    BR.insert(other);
                }
                else {
                    if (!locationList.contains(other.location)) {
                        System.out.println("Masuk BR");
                        BR = other;
                        locationList.insert(other.location);
                    }
                }
            }
            else { // TOP
                other.moveTR(location.width, location.height);
                if (TR != null) {
                    TR.insert(other);
                }
                else {
                    if (!locationList.contains(other.location)) {
                        System.out.println("Masuk TR");
                        TR = other;
                        locationList.insert(other.location);
                    }
                }
            }
        }
    }

    public void disassemble() {
        TR = null;
        TL = null;
        BR = null;
        BL = null;
        location.resetLocation();
        location.setChild(null, null);
    }

    public void moveTR(float dx, float dy) {
        location.moveTR(dx, dy);
    }

    public void moveTL(float dx, float dy) {
        location.moveTL(dx, dy);
    }

    public void moveBR(float dx, float dy) {
        location.moveBR(dx, dy);
    }

    public void moveBL(float dx, float dy) {
        location.moveBL(dx, dy);
    }

    public void printLocation() {
        System.out.println("::" + location.x1 + " " + location.y1);
    }
}
