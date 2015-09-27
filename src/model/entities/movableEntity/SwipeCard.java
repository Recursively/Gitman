package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents swipe card in the game
 * @author Divya
 *
 */
public class SwipeCard extends Item {
	private static final int SWIPE_CARD_SCORE = 15;
	
	private final int cardID;

	public SwipeCard(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int id, int cardNum) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		this.cardID = cardNum;
	}
	
	public SwipeCard(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id, int cardNum) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		this.cardID = cardNum;
	}
	
	@Override
	public Item pickUp(GameWorld game) {
		return this;
	}

	@Override
	public int getScore() {
		return SWIPE_CARD_SCORE;
	}

	@Override
	public void interact(GameWorld game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String viewInfo() {
		return "Swipe Cards help you unlock doors";
	}
	
	/**
	 * Check if
	 * @param id number of door to match
	 * @return true if the given id number matches the card's id number
	 */
	public boolean matchID(int id){
		return this.cardID == id;
	}
}
