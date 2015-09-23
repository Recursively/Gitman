package model.entities.movableEntity;

import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Divya
 *
 */
public class SwipeCard extends Item {
	private static final int SWIPE_CARD_SCORE = 15;

	public SwipeCard(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}
	
	public SwipeCard(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getScore() {
		return SWIPE_CARD_SCORE;
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String viewInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
