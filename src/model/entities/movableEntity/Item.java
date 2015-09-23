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
	
	public Item(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public Item(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex);
	}
	
	
	public abstract Item pickUp();
	
	/**
	 * @return score of item
	 */
	public abstract int getScore();
	
	/**
	 * Interact with the item. Updates game state and score
	 * accordingly. 
	 */
	public abstract void interact(GameWorld game);
	
	/**
	 * @return String information about the item and what 
	 * interacting with it will do
	 */
	public abstract String viewInfo(); 
}
