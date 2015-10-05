package model.data;

import org.lwjgl.util.vector.Vector3f;

import model.entities.Camera;

public class Data {
	
	private Vector3f playerVec;
	private Camera camera;
	private int uid;
	
	public Data(Vector3f playerVec, String pitch, String roll, String yaw, String uid){
		this.playerVec = playerVec;
		//camera = new Camera();
		this.uid = Integer.parseInt(uid);
	}
}
