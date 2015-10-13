package model.entities.movableEntity;

import controller.AudioController;
import model.GameWorld;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a "Commit" item in the game. This, when picked
 * up by players add to the patch progress, and aids in defeating
 * the 'bug' in the game.
 *
 * @author Divya
 */
public class Commit extends Item {
    private static final int COMMIT_SCORE = 2;

    /**
     * Instantiates a new Commit.
     *
     * @param model    the model
     * @param position of entity
     * @param rotX     x rotation of entity
     * @param rotY     y rotation of entity
     * @param rotZ     z rotation of entity
     * @param scale    size of entity
     * @param id       unique id for networking
     */
    public Commit(TexturedModel model, Vector3f position, float rotX,
                  float rotY, float rotZ, float scale, int id) {
        super(model, position, rotX, rotY, rotZ, scale, id);
    }

    /**
     * Instantiates a new Commit
     *
     * @param model        the model
     * @param position     of entity
     * @param rotX         x rotation of entity
     * @param rotY         y rotation of entity
     * @param rotZ         z rotation of entity
     * @param scale        size of entity
     * @param textureIndex index for atlassing
     * @param id           unique id for networking
     */
    public Commit(TexturedModel model, Vector3f position, float rotX, float rotY,
                  float rotZ, float scale, int textureIndex, int id) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
    }

    @Override
    public int interact(GameWorld game) {
        game.updateScore(COMMIT_SCORE);
        game.setCommitCollected(1);
        // commits disappear when picked up (added to the progress)
        game.removeMovableEntity(this);
        game.incrementPatch();
        // add new commit in random position in game
        game.addCommit();
        AudioController.playRandomCommitSound();
        return 11;
    }

    @Override
    public String getType() {
        return "Commit";
    }
}
