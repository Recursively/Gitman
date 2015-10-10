package model.entities.movableEntity;

import org.lwjgl.util.vector.Vector3f;

import model.GameWorld;
import model.entities.Entity;
import model.models.TexturedModel;

/**
 * This class represents a movable entity in the game. This is an 
 * entity that's postion can change throughout the course of the game, 
 * and that can be interacted with by the player.
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
	
	/**
	 * All movable entities may be able to be interacted 
	 * with. This method will carry out the relevant 
	 * interactions with the player, and update
	 * the game state (score and progress) accordingly.
	 * @param game
	 * @return 
	 */
	public abstract int interact(GameWorld game);
	
	/**
	 * @return true if entity can be interacted with
	 */
	public abstract boolean canInteract();
	
	public abstract String getType();
}
