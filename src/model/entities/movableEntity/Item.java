package model.entities.movableEntity;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents the items and stores the logic of how the players
 * can pick up/interact with the items in the game
 *
 * @author Divya
 */
public abstract class Item extends MovableEntity {

    /**
     * Instantiates a new Item.
     *
     * @param model    the model
     * @param position of entity
     * @param rotX     x rotation of entity
     * @param rotY     y rotation of entity
     * @param rotZ     z rotation of entity
     * @param scale    size of entity
     * @param id       unique id for networking
     */
    public Item(TexturedModel model, Vector3f position, float rotX, float rotY,
                float rotZ, float scale, int id) {
        super(model, position, rotX, rotY, rotZ, scale, id);
    }

    /**
     * Instantiates a new Item
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
    public Item(TexturedModel model, Vector3f position, float rotX, float rotY,
                float rotZ, float scale, int textureIndex, int id) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
    }

    /**
     * Interact with the item. Updates game state and score
     * accordingly.
     */
    public abstract int interact(GameWorld game);

    @Override
    public boolean canInteract() {
        return true;
    }
}
