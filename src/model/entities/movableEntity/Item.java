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
	private int itemID;   // TODO ID for networking component? FIXME does it need to be final
	
	public Item(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int id) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.itemID = id;
	}
	
	public Item(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex);
		this.itemID = id;
	}
	
	/**
	 * @return the item if it can be picked up, otherwise null
	 */
	public abstract Item pickUp(GameWorld game);
	
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
	
	/**
	 * @return ID number of the item
	 */
	public int getID(){
		return this.itemID;
	}
}
