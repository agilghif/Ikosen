package com.ikosen.geneticAlgorithm;

public class AgentBodyPart extends BodyPart {
    public boolean isMotor;
    public float freq;
    public float phase;
    public float strength;
    public float color;

    public AgentBodyPart(float width, float height, int xVal, int yVal, boolean isMotor, float freq, float phase, float strength, float color) {
        super(width, height, xVal, yVal);
        this.isMotor = isMotor;
        this.freq = freq;
        this.phase = phase;
        this.strength = strength;
        this.color = color;
    }

    public void copyValue(AgentBodyPart other) {
        location.width = other.location.width;
        location.height = other.location.height;
        xVal = other.xVal;
        yVal = other.yVal;
        isMotor = other.isMotor;
        freq = other.freq;
        phase = other.phase;
        strength = other.strength;
        color = other.color;
    }
}
