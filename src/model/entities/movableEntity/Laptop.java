package model.entities.movableEntity;

import java.util.Set;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.input.Keyboard;
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
	public static final int LAPTOP_SCORE = 50;
	
	private boolean hasCode;
	private boolean locked;
	private int cardID;

	public Laptop(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int id, boolean code, int cardID) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		this.hasCode = code;  // to make it so that not all laptops have clonable clode
		this.locked = true;
		this.cardID = cardID;
	}
	
	public Laptop(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
            int textureIndex, int id, boolean code, int cardID) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		this.hasCode = code;
		this.locked = true;
		this.cardID = cardID;
	}

	@Override
	public int interact(GameWorld game) {
		// useful interaction requires locked laptop that has code on it	
		if(locked && hasCode){
			Set<SwipeCard> cards = game.getSwipeCards();
			for(SwipeCard s: cards){
				if(s.matchID(cardID)){
					this.locked = false;
					System.out.println("Collecting code");
					game.updateCodeProgress();
					game.updateScore(LAPTOP_SCORE);
					this.hasCode = false;
					return 18;
				}
			}
			// no card found that can unlock door. display message
			unsuccessfulUnlockMessage();
		}
		return -1;
	}
	
	private void unsuccessfulUnlockMessage() {
		// TODO display message about unsuccesful unlock.
		// maybe have enter key to make message disappear
		// call to update display here
		
	}
	
	@Override
	public String viewInfo() {
		return "Clone code from laptops to help complete the program";
	}
	
	@Override
	public String getType(){
		return "Laptop";
	}

}
