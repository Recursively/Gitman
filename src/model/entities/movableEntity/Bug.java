package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents the bug in the game. Bug can only be interacted with. 
 * Otherwise, it does nothing else currently. 
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
		// only can 'apply patch' to bug if enough commits collected
		if(game.canApplyPatch()){
			game.setGameState(GameWorld.GAME_WIN);
			return 10;
		}
		return -1;  // unsuccessful interaction
	}
	
	@Override
	public String getType(){
		return "Bug";
	}

}
