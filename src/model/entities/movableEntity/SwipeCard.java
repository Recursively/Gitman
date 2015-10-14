package model.entities.movableEntity;

import controller.AudioController;
import model.GameWorld;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents swipe card in the game and handles the game logic that is
 * related to the player interacting with the swipe card (to pick it up
 * or use it to unlock doors)
 *
 * @author Divya Patel - 300304450
 */
public class SwipeCard extends Item {
    /**
     * The constant START_X.
     */
// final positioning values for swipe card images drawn on info panel
    public static final float START_X = -0.915f;
    /**
     * The constant CARD_SCALE.
     */
    public static final Vector2f CARD_SCALE = new Vector2f(0.06f, 0.06f);
    /**
     * The constant CARD_YPOS.
     */
    public static final float CARD_YPOS = 0.7f;

    private static final int SWIPE_CARD_SCORE = 5;
    private final int cardNum;

    /**
     * Instantiates a new SwipeCard
     *
     * @param model    the model
     * @param position of entity
     * @param rotX     x rotation of entity
     * @param rotY     y rotation of entity
     * @param rotZ     z rotation of entity
     * @param scale    size of entity
     * @param id       unique id for networking
     * @param cardNum  number of card that matches cardID of single laptop
     */
    public SwipeCard(TexturedModel model, Vector3f position, float rotX,
                     float rotY, float rotZ, float scale, int id, int cardNum) {
        super(model, position, rotX, rotY, rotZ, scale, id);
        this.cardNum = cardNum;
    }

    /**
     * Instantiates a new SwipeCard
     *
     * @param model        the model
     * @param position     of entity
     * @param rotX         x rotation of entity
     * @param rotY         y rotation of entity
     * @param rotZ         z rotation of entity
     * @param scale        size of entity
     * @param textureIndex index for atlassing
     * @param id           unique id for networking
     * @param cardNum      number of card that matches cardID of single laptop
     */
    public SwipeCard(TexturedModel model, Vector3f position, float rotX, float rotY,
                     float rotZ, float scale, int textureIndex, int id, int cardNum) {
        super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
        this.cardNum = cardNum;
    }

    @Override
    public int interact(GameWorld game) {
        game.updateScore(SWIPE_CARD_SCORE);
        // swipe cards are stored separately when picked up
        game.removeMovableEntity(this);
        game.addCard(this);
        AudioController.playPickupSound();
        return 16;
    }

    @Override
    public String getType() {
        return "SwipeCard";
    }

    @Override
    public int getCardNum() {
        return cardNum;
    }

    /**
     * Check if this swipe card can unlock the laptop the player
     * wants to unlock
     *
     * @param id number of laptop to match
     * @return true if the given id number matches the card's id number
     */
    public boolean matchID(int id) {
        return this.cardNum == id;
    }

    /**
     * Gets score.
     *
     * @return score of swipe cards
     */
    public int getScore() {
        return SWIPE_CARD_SCORE;
    }

    /**
     * Gets img name.
     *
     * @return name of image of this swipe card (each swipe card will have different colours)
     */
    public String getImgName() {
        return "swipeCard" + this.cardNum;
    }
}
