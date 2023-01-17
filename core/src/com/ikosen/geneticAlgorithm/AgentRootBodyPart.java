package com.ikosen.geneticAlgorithm;

public class AgentRootBodyPart extends AgentBodyPart {
    public AgentRootBodyPart(float width, float height, int xVal, int yVal, boolean isMotor, float freq, float phase, float strength, float color) {
        super(width, height, xVal, yVal, isMotor, freq, phase, strength, color);
        locationList = new LocationList();
    }

    public void insert(AgentBodyPart other) {
        other.locationList = locationList;
        insertRec(other);
    }
}
