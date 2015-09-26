package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Divya
 *
 */
public class Commit extends Item {
	private static final int COMMIT_SCORE = 10;

	public Commit(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int id) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		// TODO Auto-generated constructor stub
	}
	
	public Commit(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Item pickUp(GameWorld game) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getScore() {
		return COMMIT_SCORE;
	}

	@Override
	public void interact(GameWorld game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String viewInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
