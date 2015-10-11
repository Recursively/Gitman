package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents the bug in the game.
 * 
 * @author Divya
 *
 */
public class Bug extends NonPlayerCharacters {

	public Bug(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int id) {
		super(model, position, rotX, rotY, rotZ, scale, id);
	}
	
	public Bug(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
            int textureIndex, int id) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
	}

	@Override
	public int interact(GameWorld game) {
		// only can 'apply patch' to bug if all commits collected
		if(game.canApplyPatch()){
			game.winGame();
			return 10;
		}
		
		return -1;
	}

}
