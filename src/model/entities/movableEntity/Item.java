package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Divya
 *
 */
public abstract class Item extends MovableEntity {
	private static final int ITEM_SCORE = 5;

	public Item(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public Item(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex);
	}
	
	// TODO changing view when model is picked up,
	// and registering that the item is the one currently being held
	public void pickUp(GameWorld game){
		
		 // update game state
		game.pickUpItem(this);
		game.updateScore(ITEM_SCORE);
	}
	
	// TODO 
	public void drop(GameWorld game){
		
		 // update game state
		game.dropItem();   
	}
	
	
	public String viewOptions(){
		return null;
	}
	
	public void interact(){
		
	}
}
