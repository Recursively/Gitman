package model.entities.movableEntity;

import org.lwjgl.util.vector.Vector3f;

import model.entities.Entity;
import model.models.TexturedModel;

/**
 * 
 * @author Divya
 *
 */
public abstract class MovableEntity extends Entity {
	 protected static final float GRAVITY = -50;

	public MovableEntity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public MovableEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
            int textureIndex) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex);
	}
}
