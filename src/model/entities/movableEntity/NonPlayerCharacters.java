package model.entities.movableEntity;

import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public abstract class NonPlayerCharacters extends MovableEntity {

	public NonPlayerCharacters(TexturedModel model, Vector3f position,
			float rotX, float rotY, float rotZ, float scale, int id) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		// TODO Auto-generated constructor stub
	}
	
	public NonPlayerCharacters(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
            int textureIndex, int id) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canInteract() {
		return true;
	}
}
