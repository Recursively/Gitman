package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represent the characters in the game that will be found in 
 * the office section, and will have laptops with them that
 * the player can clone code off. 
 * 
 * @author Divya
 *
 */
public class Laptop extends Item {
	private static final int LAPTOP_SCORE = 10;
	private boolean hasCode;

	public Laptop(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int id, boolean code) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		this.hasCode = code;  // to make it so that not all laptops have clonable clode
	}
	
	public Laptop(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
            int textureIndex, int id, boolean code) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		this.hasCode = code;
	}

	@Override
	public void interact(GameWorld game) {
		// can only clone code from characters once
		if(hasCode){
			System.out.println("Collecting code");
			game.updateCodeProgress();
			game.updateScore(LAPTOP_SCORE);
			this.hasCode = false;
		}
	}

	@Override
	public int getScore() {
		return LAPTOP_SCORE;
	}

	@Override
	public String viewInfo() {
		return "Clone code from laptops to help complete the program";
	}

}
