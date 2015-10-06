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
public class NPCCharacter extends NonPlayerCharacters {
	private static final int CHARACTER_INTERACT_SCORE = 10;
	private boolean hasCode;

	public NPCCharacter(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int id, boolean code) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		this.hasCode = code;  // to make it so that not all NPCCharacters have clonable clode
	}
	
	public NPCCharacter(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
            int textureIndex, int id, boolean code) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		this.hasCode = code;
	}

	@Override
	public int interact(GameWorld game) {
		// can only clone code from characters once
		if(hasCode){
			System.out.println("Collecting code");
			game.updateCodeProgress();
			game.updateScore(CHARACTER_INTERACT_SCORE);
			game.removeMovableEntity(this);
			this.hasCode = false;
			return 14;
		}
		return -1;
	}

}
