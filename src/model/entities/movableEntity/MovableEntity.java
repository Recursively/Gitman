package model.entities.movableEntity;

import org.lwjgl.util.vector.Vector3f;

import model.GameWorld;
import model.entities.Entity;
import model.models.TexturedModel;

/**
 * 
 * @author Divya
 *
 */
public abstract class MovableEntity extends Entity {
	 protected static final float GRAVITY = -50;
	 private final int UID;

	public MovableEntity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int id) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.UID = id;
	}
	
	public MovableEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
            int textureIndex, int id) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex);
		this.UID = id;
	}
	
	
	/**
	 * @return ID number of the item
	 */
	public int getUID(){
		return this.UID;
	}
	
	public abstract void interact(GameWorld game);
	
	public abstract boolean canInteract();
	
	// TODO add NPC (which include bugs and characters)...
	// TODO add door as item (it's invisible)
}
