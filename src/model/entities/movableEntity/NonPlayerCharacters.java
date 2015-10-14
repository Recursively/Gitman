package model.entities.movableEntity;

import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represent a non player character in the game (characters that
 * are not controlled by players but can be interacted with by
 * the players in a set way).
 *
 * @author Divya Patel - 300304450
 */
public abstract class NonPlayerCharacters extends MovableEntity {

    /**
     * Instantiates a NonPlayerCharacter.
     *
     * @param model    the model
     * @param position of entity
     * @param rotX     x rotation of entity
     * @param rotY     y rotation of entity
     * @param rotZ     z rotation of entity
     * @param scale    size of entity
     * @param id       unique id for networking
     */
    public NonPlayerCharacters(TexturedModel model, Vector3f position,
                               float rotX, float rotY, float rotZ, float scale, int id) {
        super(model, position, rotX, rotY, rotZ, scale, id);
    }

    /**
     * Instantiates a NonPlayerCharacter
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
    public NonPlayerCharacters(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
                               int textureIndex, int id) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
    }

    @Override
    public boolean canInteract() {
        return true;
    }
}
