package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents an item in the game that can be "cloned" (stored) in a laptop
 * (the player's inventory). This class stores all the logic that deals with 
 * what happens when the players interact with laptop items.
 *  
 * @author Divya
 *
 */
public abstract class LaptopItem extends Item {
	private final String name;
	private final int size;
	
	public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int id, String name, int size) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		this.name = name;
		this.size = size;
	}
	
	public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id, String name, int size) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		this.name = name;
		this.size = size;
	}
	
	@Override
	public Item pickUp(GameWorld game) {
		return this;
	}

	@Override
	public void interact(GameWorld game) {
		if(game.addToInventory(this)){
			game.updateScore(this.getScore());
		}
	}
	
	public int getSize(){
		return this.size;
	}
}
