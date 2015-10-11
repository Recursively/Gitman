package model.entities.movableEntity;

import java.util.ArrayList;
import java.util.Set;

import model.GameWorld;
import model.guiComponents.Inventory;
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
	public static final int LAPTOP_SCORE = 10;
	
	private boolean hasCode;
	private boolean locked;
	private int cardID;

	public Laptop(TexturedModel model, Vector3f position, float rotX,
				  float rotY, float rotZ, float scale, int id, int cardID) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		this.hasCode = true;  
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
			ArrayList<SwipeCard> cards = game.getSwipeCards();
			for(SwipeCard s: cards){
				if(s.matchID(cardID)){
					this.locked = false;
					game.updateCodeProgress();
					game.updateScore(LAPTOP_SCORE);
					this.hasCode = false;
					return 18;
				}
			}
			
			// only show one message. If laptop full, prioritise that message
			if(game.getInventory().getStorageUsed() + GameWorld.CODE_VALUE > Inventory.MAX_STORAGE_SIZE){
				game.setGuiMessage("laptopMemoryFull", 2500);
			}
			else {
				// no card found that can unlock door. display message
				game.setGuiMessage("unsuccessfulUnlock", 2500);
			}
		}
		return -1;
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
