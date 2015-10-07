package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a "Commit" item in the game. This, when picked
 * up by players add to the patch progress, and aids in defeating
 * the 'bug' in the game. 
 * 
 * @author Divya
 *
 */
public class Commit extends Item {
	public static final int COMMIT_SCORE = 10;

	public Commit(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int id) {
		super(model, position, rotX, rotY, rotZ, scale, id);
	}
	
	public Commit(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
	}

	@Override
	public int interact(GameWorld game) {
		game.updateScore(COMMIT_SCORE);
		// commits disappear when picked up (added to the patch progress)
		game.removeMovableEntity(this); 
		game.incrementPatch();
		// add new commit in random position in game
		game.addCommit();
		
		return 11;
	}

	@Override
	public String viewInfo() {
		return "Commits add to the patch progress";
	}
}
