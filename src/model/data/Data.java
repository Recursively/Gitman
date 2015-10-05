package model.data;

import org.lwjgl.util.vector.Vector3f;

import model.entities.Camera;

public class Data {
	
	private Vector3f playerVec;
	private float pitch;
	private float roll;
	private float yaw;
	private int uid;
	
	public Data(Vector3f playerVec, String pitch, String roll, String yaw, String uid){
		this.playerVec = playerVec;
		this.pitch = Float.parseFloat(pitch);
		this.roll = Float.parseFloat(roll);
		this.yaw = Float.parseFloat(yaw);
		this.uid = Integer.parseInt(uid);
	}

	public Vector3f getPlayerVec() {
		return playerVec;
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

	public int getUid() {
		return uid;
	}
}
