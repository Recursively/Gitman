package model.data;

import org.lwjgl.util.vector.Vector3f;

import model.entities.Camera;

public class Data {
	
	private Vector3f playerVec;
	private Camera camera;
	private int uid;
	
	public Data(Vector3f playerVec, String pitch, String roll, String yaw, Vector3f cameraVec, String uid){
		this.playerVec = playerVec;
		camera = new Camera(10, cameraVec);
		this.uid = Integer.parseInt(uid);
	}
}
