package model.entities.movableEntity;

import controller.AudioController;
import model.GameWorld;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents an item in the game that can be "cloned" (stored) in a laptop
 * (the player's inventory). This class stores all the logic that deals with
 * what happens when the players interact with laptop items.
 *
 * @author Divya
 */
public abstract class LaptopItem extends Item {

    private final String name;
    private boolean pickedUpAlready;   // ensures that score of item only added once

    /**
     * Instantiates a new LaptopItem
     *
     * @param model    the model
     * @param position of entity
     * @param rotX     x rotation of entity
     * @param rotY     y rotation of entity
     * @param rotZ     z rotation of entity
     * @param scale    size of entity
     * @param id       unique id for networking
     * @param name     name of image file icon to show in inventory
     */
    public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
                      float rotZ, float scale, int id, String name) {
        super(model, position, rotX, rotY, rotZ, scale, id);
        this.name = name;
        this.pickedUpAlready = false;
    }

    /**
     * Instantiates a new LaptopItem
     *
     * @param model        the model
     * @param position     of entity
     * @param rotX         x rotation of entity
     * @param rotY         y rotation of entity
     * @param rotZ         z rotation of entity
     * @param scale        size of entity
     * @param textureIndex texture index for atlassing
     * @param id           unique id for networking
     * @param name         name of image file icon to show in inventory
     */
    public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
                      float rotZ, float scale, int textureIndex, int id, String name) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
        this.name = name;
        this.pickedUpAlready = false;
    }

    @Override
    public int interact(GameWorld game) {
        // if add to inventory is successful, update score
        if (game.addToInventory(this)) {
            game.updateScore(this.getScore());
            AudioController.playPickupSound();
            this.pickedUpAlready = true;
            return 13;
        }
        return -1;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @return size of the laptop item
     */
    public abstract int getSize();

    /**
     * @return item score
     */
    public abstract int getScore();

    /**
     * @return name of image file that has the image
     * this item needs to display when opened in the
     * inventory
     */
    public String getImgName() {
        return this.name + "Info";
    }

    /**
     * @return true if it the item has already been picked
     * up before
     */
    public boolean getPickedUpAlready() {
        return this.pickedUpAlready;
    }
}
