package com.ikosen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.ScreenUtils;
import com.ikosen.geneticAlgorithm.Agent;
import com.ikosen.geneticAlgorithm.BodyPart;

public class Ikosen extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera camera;
	World world;
	Body[] bodies;

	private static float TIME_STEP = 1f/60f;
	private static int VEL_ITER = 6;
	private static int POS_ITER = 3;
	
	@Override
	public void create () {
//        BodyPart b1 = new BodyPart(772, 41);
//        BodyPart b2 = new BodyPart(170, 230);
//        BodyPart b3 = new BodyPart(127, 685);
//        BodyPart b4 = new BodyPart(227, 780);
//        BodyPart b5 = new BodyPart(842, 132);
//        BodyPart b6 = new BodyPart(510, 584);
//
//        b1.setThisAsRoot();
//        b2.setAsRootParent(b1);
//        b3.setAsRootParent(b1);
//        b4.setAsRootParent(b1);
//        b5.setAsRootParent(b1);
//        b6.setAsRootParent(b1);
//
//        b1.insert(b2);
//        b1.insert(b3);
//        b1.insert(b4);
//        b1.insert(b5);
//        b1.insert(b6);

//        visit(b1);
        ///////////////////////
		// Initializing world
		world = new World(new Vector2(0f,-10f), true);
        bodies = new Body[3];
        float[] xs = new float[] {5f, 7.01f, 9.02f};
        float[] ys = new float[] {10f, 12.01f, 14.02f};

		// Initializing boxes
        for(int i=0; i<3; i++) {
            BodyDef boxDef = new BodyDef();
            boxDef.type = BodyDef.BodyType.DynamicBody;
            boxDef.position.set(xs[i], ys[i]);

            Body boxBody = world.createBody(boxDef);
            PolygonShape boxShape = new PolygonShape();
            boxShape.setAsBox(1f, 1f);

            FixtureDef boxFixtureDef = new FixtureDef();
            boxFixtureDef.shape = boxShape;
            boxFixtureDef.density = 1.0f;
            boxFixtureDef.friction = 0.8f;
            boxFixtureDef.restitution = 0.1f;

            boxBody.createFixture(boxFixtureDef);
			bodies[i] = boxBody;

            boxShape.dispose();
        }

        // Initialize joint
        DistanceJointDef joint = new DistanceJointDef();
        joint.length = 0.01f;
		joint.initialize(bodies[0], bodies[1], new Vector2(6f,11f), new Vector2(6f,11f));
		joint.collideConnected = true;
        joint.dampingRatio = 0.3f;
		world.createJoint(joint);

        joint = new DistanceJointDef();
        joint.length = 0.01f;
        joint.initialize(bodies[1], bodies[2], new Vector2(8f,13f), new Vector2(8f,13f));
        joint.collideConnected = true;
        joint.dampingRatio = 0.3f;
        world.createJoint(joint);

		// Initializing ground
        float[] groundPosX = new float[] {5f, 0f, 10f};
        float[] groundPosY = new float[] {0f, 5f, 5f};
        float[] groundWidth = new float[] {5f, 0.1f, 0.1f};
        float[] groundHeight = new float[] {0.1f, 5f, 5f};

        for (int i=0; i<3; i++) {
            BodyDef groundDef = new BodyDef();
            groundDef.type = BodyDef.BodyType.StaticBody;
            groundDef.position.set(groundPosX[i], groundPosY[i]);

            Body groundBody = world.createBody(groundDef);
            PolygonShape groundShape = new PolygonShape();
            groundShape.setAsBox(groundWidth[i], groundHeight[i]);

            FixtureDef groundFixtureDef = new FixtureDef();
            groundFixtureDef.shape = groundShape;

            groundBody.createFixture(groundFixtureDef);

            groundShape.dispose();
        }


		// Setting the debug renderer
		debugRenderer = new Box2DDebugRenderer();
		debugRenderer.setDrawJoints(true);

		// Setting the Sprite Batch
		batch = new SpriteBatch();

		// Setting the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 20f, 20f);

	}

	@Override
	public void render () {
		ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1f);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		debugRenderer.render(world, camera.projection);
		if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			bodies[0].applyAngularImpulse(25.0f, true);
		}
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            bodies[1].applyAngularImpulse(25.0f, true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            camera.translate(-1f,0f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            camera.translate(-1f,0f);
        }
		world.step(TIME_STEP, VEL_ITER, POS_ITER);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

    public static void visit(BodyPart bp) { // debugger
        if (bp == null) return;
        System.out.println(bp.xVal + " " + bp.yVal);
        visit(bp.TL);
        visit(bp.TR);
        visit(bp.BL);
        visit(bp.BR);
    }
}
