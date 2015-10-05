package model.entities.movableEntity;

import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represent a non player character in the game (characters that
 * are not controlled by players but can be interacted with by
 * the players in a set way).
 * 
 * @author Divya
 *
 */
public abstract class NonPlayerCharacters extends MovableEntity {

	public NonPlayerCharacters(TexturedModel model, Vector3f position,
			float rotX, float rotY, float rotZ, float scale, int id) {
		super(model, position, rotX, rotY, rotZ, scale, id);
	}
	
	public NonPlayerCharacters(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
            int textureIndex, int id) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
	}
	
	@Override
	public boolean canInteract() {
		return true;
	}
}
