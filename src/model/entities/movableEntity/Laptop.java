package model.entities.movableEntity;

import controller.AudioController;
import model.GameWorld;
import model.guiComponents.Inventory;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Represent the laptops in the Game that the player can clone code off.
 *
 * @author Divya
 */
public class Laptop extends Item {

    private static final int LAPTOP_SCORE = 10;

    private boolean hasCode;  // store if laptop still has code on it
    private boolean locked;   // true if laptop is locked
    private int cardID;       // cardId matches cardId of a swipe card

    /**
     * Instantiates a new Laptop.
     *
     * @param model    the model
     * @param position of entity
     * @param rotX     x rotation of entity
     * @param rotY     y rotation of entity
     * @param rotZ     z rotation of entity
     * @param scale    size of entity
     * @param id       unique id for networking
     * @param cardID   matching id of card that unlocks the this laptop
     * @param hasCode  true if laptop has code on it
     */
    public Laptop(TexturedModel model, Vector3f position, float rotX,
                  float rotY, float rotZ, float scale, int id, int cardID, boolean hasCode) {
        super(model, position, rotX, rotY, rotZ, scale, id);
        this.hasCode = hasCode;
        this.locked = true;
        this.cardID = cardID;
    }

    /**
     * Instantiates a new Laptop.
     *
     * @param model        the model
     * @param position     of entity
     * @param rotX         x rotation of entity
     * @param rotY         y rotation of entity
     * @param rotZ         z rotation of entity
     * @param scale        size of entity
     * @param textureIndex index for atlassing
     * @param id           unique id for networking
     * @param cardID       matching id of card that unlocks the this laptop
     * @param hasCode      true if laptop has code on it
     */
    public Laptop(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
                  int textureIndex, int id, int cardID, boolean hasCode) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
        this.hasCode = hasCode;
        this.locked = true;
        this.cardID = cardID;
    }

    @Override
    public int interact(GameWorld game) {
        // useful interaction requires locked laptop that has code on it
        if (locked && hasCode) {
            ArrayList<SwipeCard> cards = game.getSwipeCards();
            for (SwipeCard s : cards) {
                if (s.matchID(cardID)) {
                    this.locked = false;
                    this.hasCode = false;
                    game.updateScore(LAPTOP_SCORE);
                    GameWorld.setGuiMessage("codeCopied", 1500);
                    AudioController.playSuccessfulUnlockSound();
                    game.updateCodeProgress();
                    return 17;
                }
            }
        }
        // only show one message.
        if (!hasCode) {
            GameWorld.setGuiMessage("laptopEmpty", 1500);
        }
        // If laptop full, prioritize that message over unsuccessful unlock
        else if (game.getInventory().getStorageUsed() + GameWorld.CODE_VALUE > Inventory.MAX_STORAGE_SIZE) {
            GameWorld.setGuiMessage("laptopMemoryFull", 2000);
        } else {
            // no card found that can unlock door. display message
            GameWorld.setGuiMessage("unsuccessfulUnlock", 1500);
            AudioController.playUnsuccessfulUnlockSound();
        }

        return -1;
    }

    @Override
    public String getType() {
        return "Laptop";
    }

    @Override
    public int getCardID() {
        return cardID;
    }

    @Override
    public boolean getHasCode() {
        return hasCode;
    }

}
