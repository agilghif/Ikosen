package com.ikosen.geneticAlgorithm;

public class RootBodyPart extends BodyPart {
    public RootBodyPart(float width, float height, int xVal, int yVal) {
        super(width, height, xVal, yVal);
        locationList = new LocationList();
    }

    public void insert(BodyPart other) {
        System.out.println("\nStart inserting...");
        other.locationList = locationList;
        insertRec(other);
    }
}