package model.guiComponents;

import controller.AudioController;
import model.GameWorld;
import model.entities.movableEntity.LaptopItem;
import model.factories.GuiFactory;
import model.textures.GuiTexture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player's laptop. It can hold 'LaptopItems' (e.g. files and
 * README txt documents).
 *
 * @author Divya
 * @author Ellie
 * @author Marcel
 * @author Reuben
 */
public class Inventory {
    /**
     * The constant MAX_STORAGE_SIZE.
     */
    public static final int MAX_STORAGE_SIZE = 200;

    /**
     * The constant NUM_ACROSS.
     */
// final fields for image display
    public static final int NUM_ACROSS = 2;
    /**
     * The constant NUM_DOWN.
     */
    public static final int NUM_DOWN = 7;
    /**
     * The constant START_X.
     */
    public static final float START_X = -0.6f;
    /**
     * The constant START_Y.
     */
    public static final float START_Y = 0.35f;
    /**
     * The constant ICON_SCALE.
     */
    public static final Vector2f ICON_SCALE = new Vector2f(0.08f, 0.16f);
    /**
     * The constant SELECT_SCALE.
     */
    public static final Vector2f SELECT_SCALE = new Vector2f(0.1f, 0.2f);
    /**
     * The constant CENTER_POS.
     */
    public static final Vector2f CENTER_POS = new Vector2f(0f, 0f);
    /**
     * The constant IMAGE_SCALE.
     */
    public static final Vector2f IMAGE_SCALE = new Vector2f(0.6f, 0.8f);

    private LaptopItem[][] laptopDisplay;   // for display of laptop images
    private ArrayList<LaptopItem> inLaptop;
    private int storageUsed;
    private boolean isVisible;         // true if inventory is visible
    private GuiTexture itemDisplayed;
    private LaptopItem selected;       // currently selected item in laptop
    private GuiFactory guiFactory;
    private List<GuiTexture> textureList;

    /**
     * Instantiates a new Inventory
     *
     * @param guiFactory to create guiTextures
     */
    public Inventory(GuiFactory guiFactory) {
        this.inLaptop = new ArrayList<>();
        this.storageUsed = 0;
        this.isVisible = false;
        this.itemDisplayed = null;
        this.selected = null;
        this.guiFactory = guiFactory;
        this.textureList = new ArrayList<>();
    }

    /**
     * Gets items.
     *
     * @return list of items in inventory
     */
    public ArrayList<LaptopItem> getItems() {
        return inLaptop;
    }

    /**
     * Gets storage used.
     *
     * @return storage used value
     */
    public int getStorageUsed() {
        return this.storageUsed;
    }

    /**
     * Gets texture list.
     *
     * @return current list of images in inventory to be displayed
     */
    public List<GuiTexture> getTextureList() {
        return textureList;
    }

    /**
     * Is visible boolean.
     *
     * @return true if inventory is visible
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Add item to inventory (only allowed to add if required storage space is
     * still available)
     *
     * @param item to add
     * @return true if add was successful
     */
    public boolean addItem(LaptopItem item) {
        if (this.storageUsed + item.getSize() <= MAX_STORAGE_SIZE) {
            inLaptop.add(item);
            increaseStorageUsed(item.getSize());
            return true;
        }
        return false;
    }

    /**
     * Remove item from inventory. Update storage space used.
     *
     * @param game the game
     * @return Item if successfully removed, null if not
     */
    public int deleteItem(GameWorld game) {
        int item = -1;
        if (this.selected != null) {
            item = this.selected.getUID();
            this.storageUsed = this.storageUsed - this.selected.getSize();
            inLaptop.remove(this.selected);
            game.removeFromInventory(this.selected);
            this.selected = null;
            // redraw inventory gui as item has been deleted
            updateLaptopDisplay();
            AudioController.playDeleteSound();
        }
        // Networking
        return item;
    }

    /**
     * Increases storage used by 'size' amount.
     *
     * @param size size
     */
    public void increaseStorageUsed(int size) {
        this.storageUsed = this.storageUsed + size;
    }

    /**
     * Opens the inventory if inventory not already displayed,
     * else closes the inventory.
     */
    public void displayInventory() {
        if (this.isVisible) {
            this.isVisible = false;
            Mouse.setGrabbed(true);
            this.selected = null;

            // change audio
            AudioController.stopEasterEggLoop();
            if (GameWorld.isOutside()) {
                AudioController.playGameWorldLoop();
            } else {
                AudioController.playOfficeLoop();
            }
        } else {
            this.isVisible = true;
            Mouse.setGrabbed(false);
            // if not empty, show first item as selected
            if (!this.inLaptop.isEmpty()) {
                this.selected = this.inLaptop.get(0);
            }
            updateLaptopDisplay();
        }
    }

    /**
     * Add the images in the inLaptop array to the 2d array
     * so laptop display calculations can be carried out
     */
    private void updateLaptopDisplay() {
        this.laptopDisplay = new LaptopItem[NUM_ACROSS][NUM_DOWN];
        int i = 0;
        for (int x = 0; x < NUM_ACROSS; x++) {
            for (int y = 0; y < NUM_DOWN; y++) {
                if (i < this.inLaptop.size()) {
                    this.laptopDisplay[x][y] = this.inLaptop.get(i);
                    i++;
                }
            }
        }
        this.textureList = this.guiFactory.makeInventory(this);
    }

    /**
     * Open up the currently selected item if something is
     * selected. If item is already open, close it.
     */
    public void displayLaptopItem() {
        if (itemDisplayed != null) {
            this.textureList.remove(this.itemDisplayed);
            this.itemDisplayed = null;
            AudioController.stopEasterEggLoop();
        } else {
            if (this.selected != null) {
                // add item image to texture list so that it can be displayed
                this.itemDisplayed = guiFactory.makeItemTexture(this.selected.getImgName(), CENTER_POS, IMAGE_SCALE);
                this.textureList.add(this.itemDisplayed);

                // player easter egg tune for special image
                if (this.selected.getImgName().equals("extImg1Info")) {
                    AudioController.playEasterEggLoop();
                } else {
                    AudioController.playRandomInventorySound();
                }
            }
        }

    }

    /**
     * Get laptop display laptop item [ ] [ ].
     *
     * @return 2D array of items currently in the laptop
     */
    public LaptopItem[][] getLaptopDisplay() {
        return this.laptopDisplay;
    }

    /**
     * Gets selected.
     *
     * @return currently selected item
     */
    public LaptopItem getSelected() {
        return this.selected;
    }

    /**
     * Handle selection of item via key event that has been
     * passed in
     *
     * @param keyEvent up, down, left or right arrow key number
     */
    public void selectItem(int keyEvent) {
        // only can select item if inventory seen, and has items
        // in it
        if (this.isVisible && !this.inLaptop.isEmpty()) {
            if (this.selected == null) {
                // default is selecting first one
                this.selected = this.inLaptop.get(0);
            } else {
                // find currently selected item
                boolean found = false;
                int xPos = 0;
                int yPos = 0;
                for (int x = 0; x < laptopDisplay.length; x++) {
                    for (int y = 0; y < laptopDisplay[0].length; y++) {
                        if (this.selected == laptopDisplay[x][y]) {
                            found = true;
                            xPos = x;
                            yPos = y;
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }

                // move selection based on key press
                if (Keyboard.KEY_UP == keyEvent) {
                    xPos = selectUpOrLeft(xPos);
                }
                if (Keyboard.KEY_DOWN == keyEvent) {
                    xPos = selectDownOrRight(xPos, laptopDisplay.length - 1);
                }
                if (Keyboard.KEY_LEFT == keyEvent) {
                    yPos = selectUpOrLeft(yPos);
                }
                if (Keyboard.KEY_RIGHT == keyEvent) {
                    yPos = selectDownOrRight(yPos, laptopDisplay[0].length - 1);
                }

                // only update if not null
                if (laptopDisplay[xPos][yPos] != null) {
                    this.selected = laptopDisplay[xPos][yPos];
                }
            }

            if (this.selected != null) {
                updateLaptopDisplay();
            }
        }
    }

    /**
     * Calculations for moving towards max row/col number
     * of 2d array
     *
     * @param num current x/y point in array
     * @param max max value of that row/col
     * @return new x/y position
     */
    public int selectDownOrRight(int num, int max) {
        if (num < max) {
            return num + 1;
        }
        return num;
    }

    /**
     * Calculations for moving towards 0 in row/col of 2d array
     *
     * @param num current x/y point in array
     * @return new x/y position
     */
    public int selectUpOrLeft(int num) {
        if (num > 0) {
            return num - 1;
        }
        return num;
    }

    /**
     * Get item from the laptop with the given UID
     *
     * @param uid of item to find
     * @return item with given UID
     */
    public LaptopItem getItem(int uid) {
        for (LaptopItem l : this.inLaptop) {
            if (l.getUID() == uid) {
                return l;
            }
        }
        return null;
    }

    /**
     * Sets storage used.
     *
     * @param used the used
     */
    public void setStorageUsed(int used) {
        this.storageUsed = used;
    }

    /**
     * Method called by server to delete given laptop entity
     * from this laptop
     *
     * @param entity LaptopItem to delete
     */
    public void serverDelete(LaptopItem entity) {
        if (entity != null) {
            this.storageUsed = this.storageUsed - entity.getSize();
            inLaptop.remove(entity);
        }
    }

    /**
     * Sets in laptop.
     *
     * @param inventory to set inLaptop array to
     */
    public void setInLaptop(ArrayList<LaptopItem> inventory) {
        this.inLaptop = inventory;
    }
}
