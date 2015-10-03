package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Bug extends NonPlayerCharacters {

	public Bug(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int id) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		// TODO Auto-generated constructor stub
	}
	
	public Bug(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
            int textureIndex, int id) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact(GameWorld game) {
		// TODO Auto-generated method stub
		
	}

}
