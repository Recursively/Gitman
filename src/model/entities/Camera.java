package model.entities;

import org.lwjgl.util.vector.Vector3f;

import model.entities.movableEntity.Player;

public class Camera {

	public static final float CAMERA_OFFSET = 10;

	private Vector3f position;
	private float pitch = 0;
	private float roll;
	private float yaw;

	public Camera(float terrainHeight, Vector3f playerPosition) {
		position = new Vector3f(playerPosition.getX(), terrainHeight + CAMERA_OFFSET, playerPosition.getZ());
	}

	public void update(Vector3f newPosition) {
		position = new Vector3f(newPosition.getX(), newPosition.getY() + CAMERA_OFFSET, newPosition.getZ());

	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}

	public float getYaw() {
		return yaw;
	}

	public void changeYaw(float yawChange) {
		yaw += yawChange;
	}

	public void changePitch(float pitchChange) {
		pitch += pitchChange;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}
}