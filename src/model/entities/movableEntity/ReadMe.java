package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Divya
 *
 */
public class ReadMe extends LaptopItem{
	private static final int README_SCORE = 10;

	private final String message;

	public ReadMe(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, String name, String msg, int size) {
		super(model, position, rotX, rotY, rotZ, scale, name, size);
		this.message = msg;
	}
	
	public ReadMe(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, String name, String msg, int size) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, name, size);
		this.message = msg;
	}

	@Override
	public int getScore() {
		return README_SCORE;
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
