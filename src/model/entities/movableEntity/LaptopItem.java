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
	private static final String SELECTED_ENDING = "selected";
	
	private final String name;
	private String imgName;
	
	public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		this.name = name;
		this.imgName = name;
	}
	
	public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		this.name = name;
		this.imgName = name;
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
	
	public abstract int getScore();
	
	public String getName(){
		return this.name;
	}
	
	public String getImgName(){
		return this.imgName;
	}
	
	public void setSelected(boolean isSelected){
		if(!isSelected){
			this.imgName = this.name;
		}
		else {
			this.imgName = this.name + SELECTED_ENDING;
		}
	}


}
