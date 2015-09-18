package model.entities.movableEntity;

import org.lwjgl.util.vector.Vector3f;

import model.models.TexturedModel;

public class RandomPlayer extends Player {

	public RandomPlayer(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	//public void move(Terrain terrain) {
		//jump();
	//}

}
