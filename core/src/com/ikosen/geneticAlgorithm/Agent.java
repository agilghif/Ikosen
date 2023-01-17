package com.ikosen.geneticAlgorithm;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

public class Agent {
    // Genome
    public int[] sGen1, sGen2; // Regulator of Agent's body structure
    public byte[] tGen;
    public boolean[] isMotor; // Decide whether the i-th part could exert internal torque or not
    public byte[] frequencies; // Frequency of angular impulse (in 256 cycles unit)
    public byte[] phases; // Body part initial phases (ranges from 0 to 256-1)
    public float[] strength; // Menentukan kekuatan impuls (jika isMotor true)

    // Fenotype


    // Static attributes
    private static Random random;
    private static int genomeLength;
    private static int maxFrequency;
    private static float maxStrength;

    // Simulator attributes
    private World world;
    private Body[] bodyParts;
    private BodyPart bodyRoot;
    public float fitness; // COM's distance traveled calculated from origin
    private boolean isCalculated;

    // constants
    private static final int SIMULATION_DURATION = 6000; // number of steps in the simulation, only 10 seconds
    private static final float TIME_STEP = 1f/60f; // number of steps in the simulation, only 10 seconds
    private static final int VELOCITY_ITERATIONS = 6; // number of steps in the simulation, only 10 seconds
    private static final int POSITION_ITERATIONS = 3; // number of steps in the simulation, only 10 seconds

    public static void init() {
        init(6, 16, 100f);
    }

    public static void init(int genomeLength, int maxFrequency, float maxStrength) {
        random = new Random();
        Agent.genomeLength = genomeLength;
        Agent.maxFrequency = maxFrequency;
        Agent.maxStrength = maxStrength;
    }

    public Agent() {
        // Create world
        world = new World(new Vector2(0, -10), true);
        isCalculated = false;

        // Generate Genome
        sGen1 = new int[genomeLength];
        sGen2 = new int[genomeLength];
        tGen = new byte[genomeLength];
        isMotor = new boolean[genomeLength];
        frequencies = new byte[genomeLength];
        phases = new byte[genomeLength];
        strength = new float[genomeLength];
        float temp = 0; // temporary variable for generating scaleGens

        for (int i=0; i<genomeLength; i++) {
            sGen1[i] = random.nextInt();
            sGen2[i] = random.nextInt();
            tGen[i] = (byte) random.nextInt(4);
            isMotor[i] = random.nextBoolean();
            frequencies[i] = (byte) random.nextInt(maxFrequency);
            phases[i] = (byte) random.nextInt(maxFrequency);
            strength[i] = random.nextFloat() * maxStrength;
        }
    }

    // Genetic algorithm methods
    public void calculateFitness() {
        // No need to recalculate if Agent's fitness is already calculated
        if (isCalculated)
            return;

        // do a simulation
        for (int time=0; time<SIMULATION_DURATION; time++) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }

        float COMSum = 0f; // Center of mass
        float totalMass = 0f;
        for (Body body : bodyParts) {
            COMSum += body.getPosition().x;
            totalMass += body.getMass();
        }
        fitness = COMSum / totalMass;
    }

    public static void crossover(Agent agentA, Agent agentB, Agent targetMemory) {
        int index = random.nextInt(genomeLength);

        for (int i=0; i<index; i++) {
            targetMemory.sGen1[i] = agentA.sGen1[i];
            targetMemory.sGen2[i] = agentA.sGen2[i];
            targetMemory.tGen[i] = agentA.tGen[i];
            targetMemory.isMotor[i] = agentA.isMotor[i];
            targetMemory.frequencies[i] = agentA.frequencies[i];
            targetMemory.phases[i] = agentA.phases[i];
            targetMemory.strength[i] = agentA.strength[i];
        }
        for (int i=index; i<genomeLength; i++) {
            targetMemory.sGen1[i] = agentB.sGen1[i];
            targetMemory.sGen2[i] = agentB.sGen2[i];
            targetMemory.tGen[i] = agentB.tGen[i];
            targetMemory.isMotor[i] = agentB.isMotor[i];
            targetMemory.frequencies[i] = agentB.frequencies[i];
            targetMemory.phases[i] = agentB.phases[i];
            targetMemory.strength[i] = agentB.strength[i];
        }
    }

    public void mutate() {
        int index = random.nextInt(genomeLength);
        float temp;

        switch(random.nextInt(8)) {
            case 0:
                sGen1[index] = random.nextInt();
                break;
            case 1:
                sGen2[index] = random.nextInt();
                break;
            case 2:
                tGen[index] =(byte) random.nextInt(4);
                break;
            case 3:
                isMotor[index] = random.nextBoolean();
                break;
            case 4:
                frequencies[index] = (byte) random.nextInt(maxFrequency);
                break;
            case 5:
                phases[index] = (byte) random.nextInt(maxFrequency);
                break;
            case 6:
                strength[index] = random.nextFloat() * maxStrength;
                break;

        }
    }

    // Agent-world integration initialization
    public void constructBody(World world) {
        // TODO : IMPLEMENT THIS
    }

    // Auxillary methods
    private static int gcd(int a, int b)
    {
        return b == 0? a: gcd(b, a % b);
    }

    private static int lcm(int[] arr, int idx) {
        // lcm(a,b) = (a*b/gcd(a,b))
        if (idx == arr.length - 1){
            return arr[idx];
        }
        int a = arr[idx];
        int b = lcm(arr, idx+1);
        return (a*b/ gcd(a,b));
    }

}
