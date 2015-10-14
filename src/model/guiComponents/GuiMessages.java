package model.guiComponents;

import model.factories.GuiFactory;
import model.textures.GuiTexture;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Stores a collection of helper messages that are displayed during
 * the game and handles how long the messages are display for
 * (Class handles timed helper messages)
 *
 * @author Divya
 * @author Ellie
 */
public class GuiMessages {
    /**
     * The constant MESSAGE_POS.
     */
// fixed positionin of messages
    public static final Vector2f MESSAGE_POS = new Vector2f(0f, 0.5f);
    /**
     * The constant MESSAGE_SCALE.
     */
    public static final Vector2f MESSAGE_SCALE = new Vector2f(1f, 1f);
    /**
     * The constant MINOR_MESSAGE_POS.
     */
    public static final Vector2f MINOR_MESSAGE_POS = new Vector2f(0.6f, -0.8f);
    /**
     * The constant MINOR_MESSAGE_SCALE.
     */
    public static final Vector2f MINOR_MESSAGE_SCALE = new Vector2f(0.4f, 0.4f);

    private GuiFactory guiFactory;   // needed to make gui textures
    private List<GuiTexture> messages;  // list of messages that should currently be displayed
    private long timer;
    private double messageTime;  //length of time messages in messages list need to be displayed
    private HashMap<String, GuiTexture> messageMap;

    /**
     * Instantiates a GuiMessages class
     *
     * @param gui guiFactory that will make the guiTextures
     */
    public GuiMessages(GuiFactory gui) {
        this.guiFactory = gui;
        loadImages();
        this.messages = new ArrayList<>();
    }

    /**
     * Load in common messages that will be displayed
     * in the game
     */
    private void loadImages() {
        messageMap = new HashMap<>();
        messageMap.put("codeCompiledMessage", guiFactory.makeGuiTexture("codeCompiledMessage", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("codeCopied", guiFactory.makeGuiTexture("codeCopied", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("inGameMessage", guiFactory.makeGuiTexture("inGameMessage", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("laptopEmpty", guiFactory.makeGuiTexture("laptopEmpty", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("laptopMemoryFull", guiFactory.makeGuiTexture("laptopMemoryFull", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("patchComplete", guiFactory.makeGuiTexture("patchComplete", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("pressEToInteract", guiFactory.makeGuiTexture("pressEToInteract", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("unsuccessfulUnlock", guiFactory.makeGuiTexture("unsuccessfulUnlock", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("gameSaved", guiFactory.makeGuiTexture("gameSaved", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("failedToLoad", guiFactory.makeGuiTexture("failedToLoad", MESSAGE_POS, MESSAGE_SCALE));
        messageMap.put("aPlayerHasLeftTheGame", guiFactory.makeGuiTexture("aPlayerHasLeftTheGame", MINOR_MESSAGE_POS, MINOR_MESSAGE_SCALE));
    }

    /**
     * Add the given message into the messages list
     * so that it will be displayed.
     *
     * @param msg  to display
     * @param time to display msg for
     */
    public void setMessage(String msg, long time) {
        this.messages.clear();  // only show one message at a time
        this.timer = System.currentTimeMillis();
        this.messages.add(messageMap.get(msg));
        this.messageTime = time;
    }

    /**
     * Check if current messages that are displayed have
     * passed their time displayed limit
     */
    private void updateMessages() {
        if (!this.messages.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            // if message time reached, clear messages list
            if (currentTime - this.timer >= this.messageTime) {
                this.messages.clear();
            }
        }
    }

    /**
     * Gets messages.
     *
     * @return current messages to be displayed
     */
    public List<GuiTexture> getMessages() {
        updateMessages();
        return this.messages;
    }
}
