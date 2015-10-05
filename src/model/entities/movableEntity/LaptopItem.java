package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import javax.swing.filechooser.FileNameExtensionFilter;

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
	
	public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		this.name = name;
	}
	
	public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		this.name = name;
	}

	@Override
	public int interact(GameWorld game) {
		// if add to inventory is successful, update score
		if(game.addToInventory(this)){
			game.updateScore(this.getScore());
			return 13;
		}
		return -1;
	}
	
	public abstract int getSize();
	
	public String getName(){
		return name;
	}


}
