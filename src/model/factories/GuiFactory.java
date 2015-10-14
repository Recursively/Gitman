package model.factories;

import model.GameWorld;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.SwipeCard;
import model.guiComponents.Inventory;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating Gui Components
 *
 * @author Marcel van Workum
 * @author Ellie Coyle
 * @author Divya
 */
public class GuiFactory {

    //paths
    private static final String GUI_PATH = "gui/";
    private static final String ITEM_PATH = "itemImages/";

    // basic positions
    private static final Vector2f CENTER_POS = new Vector2f(0f, 0f);
    private static final Vector2f FULL_SCALE = new Vector2f(1f, 1f);

    // progress block positions and scale
    private static final float PROGRESS_START_X = -0.965f;
    private static final Vector2f PROGRESS_SCALE = new Vector2f(0.00355f, 0.027f);
    private static final float PROGRESS_YPOS = 0.941f;

    // score scale and positions
    private static final float SCORE_START_X = -0.56f;
    private static final Vector2f SCORE_SCALE = new Vector2f(0.05f, 0.05f);
    private static final float SCORE_YPOS = 0.95f;

    // textures
    private GuiTexture inventoryScreen;
    private GuiTexture interactMessage;
    private GuiTexture infoPanel;
    private GuiTexture lostScreen;
    private GuiTexture winScreen;
    private GuiTexture progressBlock;
    private GuiTexture disconnectedServer;

    // gui panel
    private int oldCardsSize;
    private List<GuiTexture> cards;

    private int oldProgress;
    private List<GuiTexture> progressBar;

    private int oldScore;
    private List<GuiTexture> scoreNum;

    /**
     * Create the Gui factory passing in the object loader
     */
    public GuiFactory() {
        loadImages();
    }

    /**
     * Load in commonly used gui images
     */
    private void loadImages() {
        inventoryScreen = makeGuiTexture("blankInventoryScreen", CENTER_POS, new Vector2f(0.8f, 1f));
        interactMessage = makeGuiTexture("pressEToInteract", new Vector2f(0f, -0.3f), new Vector2f(0.5f, 0.5f));
        infoPanel = makeGuiTexture("topLeftCornerGUI", new Vector2f(-0.6875f, 0.8f), new Vector2f(0.4f, 0.4f));
        lostScreen = makeGuiTexture("youLostScreen", CENTER_POS, FULL_SCALE);
        winScreen = makeGuiTexture("youWonScreen", CENTER_POS, FULL_SCALE);
        progressBlock = makeGuiTexture("progressBlock", CENTER_POS, PROGRESS_SCALE);
        disconnectedServer = makeGuiTexture("youHaveBeenDisconnected", CENTER_POS, FULL_SCALE);

        // info panel
        this.cards = new ArrayList<>();
        this.oldCardsSize = 0;
        this.progressBar = new ArrayList<>();
        this.oldProgress = 0;
        this.scoreNum = new ArrayList<>();
        this.oldScore = 0;
    }


    /**
     * Creates a {@link GuiTexture} with the specified texture, position and
     * scale.
     *
     * @param textureName Texture
     * @param position    Position on the display x(0-1) y(0-1)
     * @param scale       scale of the texture on display x(0-1) y(0-1)
     * @return The GuiTexture created
     */

    public GuiTexture makeGuiTexture(String textureName, Vector2f position, Vector2f scale) {
        return new GuiTexture(Loader.loadTexture(GUI_PATH + textureName), position, scale);
    }

    /**
     * Constructs new gui textures for Items
     *
     * @param textureName name of texture
     * @param position    Vector2f of position
     * @param scale       Vector2f of scale
     * @return the GuiTexture
     */
    public GuiTexture makeItemTexture(String textureName, Vector2f position, Vector2f scale) {
        return new GuiTexture(Loader.loadTexture(ITEM_PATH + textureName), position, scale);
    }

    /**
     * Creates list of GuiTextures to be displayed
     *
     * @param inventory The shared inventory to be displayed
     * @return list of GuiTextures to be displayed
     */
    public List<GuiTexture> makeInventory(Inventory inventory) {
        List<GuiTexture> inventoryImages = new ArrayList<>();
        inventoryImages.add(inventoryScreen);
        if (inventory.getLaptopDisplay() != null) {
            LaptopItem[][] items = inventory.getLaptopDisplay();
            for (int x = 0; x < items.length; x++) {
                for (int y = 0; y < items[0].length; y++) {
                    if (items[x][y] != null) {
                        float xPos = Inventory.START_X + y * Inventory.ICON_SCALE.getX() * 3f;
                        float yPos = Inventory.START_Y - x * Inventory.ICON_SCALE.getY() * 1.5f;
                        Vector2f pos = new Vector2f(xPos, yPos);
                        GuiTexture img = makeItemTexture(items[x][y].getName(), pos, Inventory.ICON_SCALE);
                        inventoryImages.add(img);

                        // highlight selected image
                        if (items[x][y] == inventory.getSelected()) {
                            GuiTexture select = makeItemTexture("selectBox", pos, Inventory.SELECT_SCALE);
                            inventoryImages.add(select);
                        }
                    }
                }
            }
        }

        return inventoryImages;

    }

    /**
     * returns the list containing the lostscreen to be rendered
     *
     * @return List of guitextures to be rendered
     */
    public List<GuiTexture> getLostScreen() {
        List<GuiTexture> lostScreens = new ArrayList<>();
        lostScreens.add(lostScreen);
        return lostScreens;

    }

    /**
     * returns the list containing the winscreen to be rendered
     *
     * @return List of guitextures to be rendered
     */

    public List<GuiTexture> getWinScreen() {
        List<GuiTexture> winScreens = new ArrayList<>();
        winScreens.add(winScreen);
        return winScreens;
    }

    /**
     * returns the list containing the disconnectscreen to be rendered
     *
     * @return List of guitextures to be rendered
     */
    public List<GuiTexture> getDisconnectedScreen() {
        List<GuiTexture> disconnected = new ArrayList<>();
        disconnected.add(disconnectedServer);
        return disconnected;
    }

    /**
     * returns the list containing the number of progress bars to render
     *
     * @param progress progress > 0 && <=100 amount of progress to be displayed
     * @return List of gui textures to be rendered
     */
    public List<GuiTexture> getProgress(int progress) {
        if (oldProgress != progress) {
            // if progress has decreased, remove how many blocks it has decreased by
            if (progress < this.oldProgress) {
                for (int i = this.progressBar.size() - 1; i > progress; i--) {
                    this.progressBar.remove(i);
                }
            }
            // else add how many blocks it has increased by
            else {
                for (int i = this.oldProgress; i < progress; i++) {
                    if (i <= GameWorld.MAX_PROGRESS) {
                        float xPos = PROGRESS_START_X + i * PROGRESS_SCALE.getX();
                        Vector2f pos = new Vector2f(xPos, PROGRESS_YPOS);
                        GuiTexture progBlock = progressBlock.copy();
                        progBlock.setPosition(pos);
                        this.progressBar.add(progBlock);
                    }
                }
            }
            this.oldProgress = progress;
        }
        return progressBar;
    }

    /**
     * Returns the list containing the score to be rendered
     *
     * @param score Score to be displayed
     * @return List of gui textures to be rendered
     */
    public List<GuiTexture> getScore(int score) {
        if (this.oldScore != score) {
            this.oldScore = score;
            this.scoreNum = new ArrayList<>();
            String num = this.oldScore + "";
            for (int i = 0; i < num.length(); i++) {
                String charNum = num.substring(i, i + 1);
                float xPos = SCORE_START_X + i * SCORE_SCALE.getX() * 0.8f;
                Vector2f pos = new Vector2f(xPos, SCORE_YPOS);
                GuiTexture img = makeGuiTexture(charNum, pos, SCORE_SCALE);
                this.scoreNum.add(img);
            }
        }
        return this.scoreNum;
    }

    /**
     * makes a list of swipe card gui textures to be rendered
     *
     * @param collected The swipe cards collected
     * @return List of gui textures to render
     */
    public List<GuiTexture> getSwipeCards(ArrayList<SwipeCard> collected) {
        if (this.oldCardsSize != collected.size()) {
            for (int i = this.oldCardsSize; i < collected.size(); i++) {
                String name = collected.get(i).getImgName();
                float xPos = SwipeCard.START_X + i * SwipeCard.CARD_SCALE.getX() * 2f;
                Vector2f pos = new Vector2f(xPos, SwipeCard.CARD_YPOS);
                GuiTexture img = makeItemTexture(name, pos, SwipeCard.CARD_SCALE);
                this.cards.add(img);
            }
            this.oldCardsSize = collected.size();
        }
        return this.cards;
    }

    /**
     * makes a list of press E to interact messages
     *
     * @return List of gui textures to render
     */
    public List<GuiTexture> getPopUpInteract() {
        List<GuiTexture> message = new ArrayList<>();
        message.add(interactMessage);
        return message;
    }

    /**
     * returns the list containing the infoPanel to be rendered
     *
     * @return List of gui textures to render
     */
    public List<GuiTexture> getInfoPanel() {
        List<GuiTexture> infoPanels = new ArrayList<>();
        infoPanels.add(infoPanel);
        return infoPanels;
    }

    /**
     * returns the list containing the helpscreen to be rendered
     *
     * @return List of gui textures to render
     */
    public List<GuiTexture> getHelpScreen() {
        List<GuiTexture> help = new ArrayList<>();
        help.add(makeGuiTexture("helpScreen", new Vector2f(0f, 0f), new Vector2f(0.8f, 1f)));
        return help;
    }
}
