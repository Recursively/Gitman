package model.entities.movableEntity;

import model.entities.Camera;
import model.entities.Entity;
import model.models.TexturedModel;
import model.terrains.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import view.DisplayManager;

public class Player extends Entity {

	private static final float RUN_SPEED = 1;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;

	private static float terrainHeight = 0;
	private Camera camera;

	private int uid;

	private float verticalVelocity = 0;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
			Camera camera, int uid) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.uid = uid;
		this.camera = camera;
	}

	public void move(Terrain terrain) {
		updateTerrainHeight(terrain);
		gravityPull();
		firstPersonMove();
		camera.update(super.getPosition());
	}

	private void gravityPull() {
		verticalVelocity += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, verticalVelocity * DisplayManager.getFrameTimeSeconds(), 0);
		if (super.getPosition().y < terrainHeight) {
			verticalVelocity = 0;
			super.getPosition().y = terrainHeight;
		}
	}

	private void updateTerrainHeight(Terrain terrain) {
		terrainHeight = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);
	}

	private void jump() {
		verticalVelocity += JUMP_POWER;
	}

	private void firstPersonMove() {

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveFromLook(0, 0, -1 * RUN_SPEED);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveFromLook(0, 0, 1 * RUN_SPEED);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			moveFromLook(1 * RUN_SPEED, 0, 0);

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			moveFromLook(-1 * RUN_SPEED, 0, 0);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			if (super.getPosition().y == terrainHeight) {
				jump();
			}
		}

		/* Prevents the camera from turning over 360 or under -360 */
		camera.changeYaw(Mouse.getDX() / 2);
		camera.changePitch(-(Mouse.getDY() / 2));
		if (camera.getPitch() > 60) {
			camera.setPitch(60);
		} else if (camera.getPitch() < -30) {
			camera.setPitch(-30);
		}
	}

	public void moveFromLook(float dx, float dy, float dz) {

		Vector3f position = super.getPosition();

		position.z += dx * (float) Math.cos(Math.toRadians(camera.getYaw() - 90))
				+ dz * Math.cos(Math.toRadians(camera.getYaw()));
		position.x -= dx * (float) Math.sin(Math.toRadians(camera.getYaw() - 90))
				+ dz * Math.sin(Math.toRadians(camera.getYaw()));

		super.setPosition(position);
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
}
