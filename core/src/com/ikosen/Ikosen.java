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
        // Initializing Agent;
		Agent.init(6, 10, 10);
		Agent agent = new Agent();
		world = agent.world;

		// Setting the debug renderer
		debugRenderer = new Box2DDebugRenderer();

		// Setting the Sprite Batch
		batch = new SpriteBatch();

		// Setting the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 10f, 10f);
		camera.translate(-5f, -5f);

	}

	@Override
	public void render () {
		camera.update();
		ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1f);
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0f, 0.1f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-0.1f, 0f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0f, -0.1f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(0.1f, 0f);
		}
		world.step(TIME_STEP, VEL_ITER, POS_ITER);
		debugRenderer.render(world, camera.combined);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
