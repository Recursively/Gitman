package model.entities.movableEntity;

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
		// TODO Auto-generated constructor stub
	}
	
	public Item(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex);
		// TODO Auto-generated constructor stub
	}
	
	//TODO add item methods
	public void pickUp(){
		
	}
	
	public void drop(){
		
	}
	
	// method to display interactions with object
	public abstract void interact();
	
	// method to view what can be done with the object
	public abstract void viewOptions();
	
}
