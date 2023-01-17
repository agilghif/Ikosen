package com.ikosen.geneticAlgorithm;

public class BodyPart {
    public int xVal, yVal;
    public Location location;  // Lokasi terkini dari body part
    public LocationList locationList;  // Location dari root body part node [0,0]
    public BodyPart TR, TL, BR, BL; // Children dari body part

    public BodyPart(float width, float height, int xVal, int yVal) {
        location = new Location(width, height);
        this.xVal = xVal;
        this.yVal = yVal;
    }

    protected void insertRec(BodyPart other) {
        other.location.snapTo(location);

        if (other.xVal < xVal) { // LEFT
            if (other.yVal < yVal) { // BOTTOM
                // ----------------------- [BL]
                if (BL == null) {
                    other.move(-other.location.width, -other.location.height);
                    if (!locationList.contains(other.location)) {
                        BL = other;
                        locationList.add(other.location);
                    }
                }
                else {
                    BL.insertRec(other);
                }
            }
            else { // TOP
                // ----------------------- [TL]
                if (TL == null) {
                    other.move(-other.location.width, location.height);
                    if (!locationList.contains(other.location)) {
                        TL = other;
                        locationList.add(other.location);
                    }
                }
                else {
                    TL.insertRec(other);
                }
            }
        }
        else { // RIGHT
            if (other.yVal < yVal) { // BOTTOM
                // ----------------------- [BR]
                if (BR == null) {
                    other.move(location.width, -other.location.height);
                    if (!locationList.contains(other.location)) {
                        BR = other;
                        locationList.add(other.location);
                    }
                }
                else {
                    BR.insertRec(other);
                }
            }
            else { // TOP
                // ----------------------- [TR]
                if (TR == null) {
                    other.move(location.width, location.height);
                    if (!locationList.contains(other.location)) {
                        TR = other;
                        locationList.add(other.location);
                    }
                }
                else {
                    TR.insertRec(other);
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
    }

    public void move(float dx, float dy) {
        location.move(dx, dy);
    }

    public void printLocation() {
        System.out.println("::" + location.x1 + " " + location.y1);
    }
}
