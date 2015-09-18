package model.entities.movableEntity;

import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Divya
 *
 */
public class Commit extends Item {

	public Commit(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}
	
	public Commit(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void pickUp() {
		// update patch progress bar
	}

	@Override
	public void viewOptions() {
		// TODO Auto-generated method stub
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
		
	}

}
