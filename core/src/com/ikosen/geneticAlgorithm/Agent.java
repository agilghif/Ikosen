package com.ikosen.geneticAlgorithm;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.Random;

public class Agent {
    // Genome
    public AgentBodyPart[] bodyParts;

    // Fenotype


    // Static attributes
    private static Random random;
    private static int genomeLength;
    private static int maxFrequency;
    private static float maxStrength;

    // Simulator attributes
    public World world;
    private Body[] bodies;
    private AgentRootBodyPart bodyRoot;
    public float fitness; // COM's distance traveled calculated from origin
    private boolean isCalculated;

    // constants
    private static final int SIMULATION_DURATION = 6000; // number of steps in the simulation, only 10 seconds
    private static final float TIME_STEP = 1f/60f; // number of steps in the simulation, only 10 seconds
    private static final int VELOCITY_ITERATIONS = 6; // number of steps in the simulation, only 10 seconds
    private static final int POSITION_ITERATIONS = 3; // number of steps in the simulation, only 10 seconds
    private static boolean initialized = false;

    public static void init() {
        init(6, 16, 100f);
    }

    public static void init(int genomeLength, int maxFrequency, float maxStrength) {
        random = new Random();
        Agent.genomeLength = genomeLength;
        Agent.maxFrequency = maxFrequency;
        Agent.maxStrength = maxStrength;
        initialized = true;
    }

    public Agent() {
        if (!initialized)
            init();

        // Create world
        world = new World(new Vector2(0, -0.5f), true);
        isCalculated = false;

        // Generate Genome
        bodyParts = new AgentBodyPart[genomeLength];

        // Root
        bodyParts[0] = new AgentRootBodyPart(
                random.nextFloat(), // Width
                random.nextFloat(), // Height
                random.nextInt(),  // xVal
                random.nextInt(),  // yVal
                random.nextBoolean(),  // isMotor
                random.nextFloat() * maxFrequency,  // freq
                random.nextFloat() * (float) Math.PI,  // phase
                random.nextFloat() * maxStrength,  // strength
                random.nextFloat()  // color
        );
        bodyRoot = (AgentRootBodyPart) bodyParts[0];

        // Non-Root
        for (int i=1; i<genomeLength; i++) {
            bodyParts[i] = new AgentBodyPart(
                    random.nextFloat(), // Width
                    random.nextFloat(), // Height
                    random.nextInt(),  // xVal
                    random.nextInt(),  // yVal
                    random.nextBoolean(),  // isMotor
                    random.nextFloat() * maxFrequency,  // freq
                    random.nextFloat() * (float) Math.PI,  // phase
                    random.nextFloat() * maxStrength,  // strength
                    random.nextFloat()  // color
                    );
        }

        // Create ground for the world
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(0, -4);

        Body groundBody = world.createBody(groundDef);
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(20f, 0.1f);

        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;

        groundBody.createFixture(groundFixtureDef);

        groundShape.dispose();

        // Construct body
        constructBody();
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
        for (Body body : bodies) {
            COMSum += body.getWorldCenter().x;
            totalMass += body.getMass();
        }
        fitness = COMSum / totalMass;
    }

    public static void crossover(Agent agentA, Agent agentB, Agent targetMemory) {
        int index = random.nextInt(genomeLength);
        for (int i=0; i<index; i++) {
            targetMemory.bodyParts[i].copyValue(agentA.bodyParts[i]);
        }
        for (int i=index; i<genomeLength; i++) {
            targetMemory.bodyParts[i].copyValue(agentB.bodyParts[i]);
        }
    }

    public void mutate() {
        int index = random.nextInt(genomeLength);

        switch(random.nextInt(8)) {
            case 0:
                bodyParts[index].location.width = random.nextInt();
                break;
            case 1:
                bodyParts[index].location.height = random.nextInt();
                break;
            case 2:
                bodyParts[index].xVal = random.nextInt();
                break;
            case 3:
                bodyParts[index].yVal = random.nextInt();
                break;
            case 4:
                bodyParts[index].isMotor = !bodyParts[index].isMotor;
                break;
            case 5:
                bodyParts[index].freq = random.nextFloat() * maxFrequency;
                break;
            case 6:
                bodyParts[index].phase = random.nextInt() * (float) Math.PI;
                break;
            case 7:
                bodyParts[index].strength = random.nextFloat() * maxStrength;
                break;
            case 8:
                bodyParts[index].color = random.nextFloat();
                break;

        }
    }

    // Agent-world integration initialization
    public void constructBody() {
        for (int i=1; i<genomeLength; i++) {
            bodyRoot.insert(bodyParts[i]);
        }
        traverseAndConstruct(bodyRoot, null, null, -1);
    }

    private void traverseAndConstruct(AgentBodyPart bodyPart, AgentBodyPart prev, Body prevBody, int case_) { // Auxiliary recursive method to construct world
        if (bodyPart == null)
            return;

        bodyPart.printLocation();

        // frequently used references
        Location location = bodyPart.location;

        // visit
        BodyDef boxDef = new BodyDef();
        boxDef.type = BodyDef.BodyType.DynamicBody;
        boxDef.position.set(location.x1, location.y1);

        Body boxBody = world.createBody(boxDef);
        PolygonShape boxShape = new PolygonShape();
        // to prevent bodies colliding due to precision problemm
        float skinX = location.width * 0.001f, skinY = location.height * 0.001f;
        boxShape.set(new Vector2[] {
                new Vector2(0f + skinX, 0f + skinY),
                new Vector2(location.width - skinX, 0f + skinY),
                new Vector2(location.width - skinX, location.height - skinY),
                new Vector2(0f + skinX, location.height - skinY),
        });

        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = boxShape;
        boxFixtureDef.density = 1.0f;
        boxFixtureDef.friction = 0.8f;
        boxFixtureDef.restitution = 0.1f;

        boxBody.createFixture(boxFixtureDef);
        boxShape.dispose();

        // Create joint with prev
        if (prev != null) {
            // TODO : implementasikan ini
            RevoluteJointDef joint = new RevoluteJointDef();
            Vector2 localAnchorA = null, localAnchorB = null;

            switch(case_) {
                case 0: // TL
                    localAnchorA = new Vector2(0f, prev.location.height);
                    localAnchorB = new Vector2(bodyPart.location.width, 0f);
                    break;
                case 1: // TR
                    localAnchorA = new Vector2(prev.location.width, prev.location.height);
                    localAnchorB = new Vector2(0f, 0f);
                    break;
                case 2: // BR
                    localAnchorA = new Vector2(prev.location.width, 0f);
                    localAnchorB = new Vector2(0f, bodyPart.location.height);
                    break;
                case 3: // BL
                    localAnchorA = new Vector2(0f, 0f);
                    localAnchorB = new Vector2(bodyPart.location.width, bodyPart.location.height);
                    break;
            }

            joint.bodyA = prevBody;
            joint.bodyB = boxBody;
            joint.localAnchorA.set(localAnchorA);
            joint.localAnchorB.set(localAnchorB);
            joint.collideConnected = true;
            world.createJoint(joint);
        }

        // visit children
        traverseAndConstruct((AgentBodyPart) bodyPart.TL, bodyPart, boxBody, 0);
        traverseAndConstruct((AgentBodyPart) bodyPart.TR, bodyPart, boxBody, 1);
        traverseAndConstruct((AgentBodyPart) bodyPart.BR, bodyPart, boxBody, 2);
        traverseAndConstruct((AgentBodyPart) bodyPart.BL, bodyPart, boxBody, 3);
    }

}
