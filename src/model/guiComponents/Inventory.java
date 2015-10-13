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
 *
 */
public class Inventory {
	public static final int MAX_STORAGE_SIZE = 200;

	// final fields for image display
	public static final int NUM_ACROSS = 2;
	public static final int NUM_DOWN = 7;
	public static final float START_X = -0.6f;
	public static final float START_Y = 0.35f;
	public static final Vector2f ICON_SCALE = new Vector2f(0.08f, 0.16f);
	public static final Vector2f SELECT_SCALE = new Vector2f(0.1f, 0.2f);
	public static final Vector2f CENTER_POS = new Vector2f(0f, 0f);
	public static final Vector2f IMAGE_SCALE = new Vector2f(0.6f, 0.8f);

	private LaptopItem[][] laptopDisplay;
	private ArrayList<LaptopItem> inLaptop;
	private int storageUsed;
	private boolean isVisible;
	private GuiTexture itemDisplayed;
	private LaptopItem selected;
	private GuiFactory guiFactory;
	private List<GuiTexture> textureList;

	public Inventory(GuiFactory guiFactory) {
		this.inLaptop = new ArrayList<LaptopItem>();
		this.storageUsed = 0;
		this.isVisible = false;
		this.itemDisplayed = null;
		this.selected = null;
		this.guiFactory = guiFactory;
		this.textureList = new ArrayList<GuiTexture>();

	}

	/**
	 * @return list of items in inventory
	 */
	public ArrayList<LaptopItem> getItems() {
		return inLaptop;
	}

	/**
	 * @return storage used value
	 */
	public int getStorageUsed() {
		return this.storageUsed;
	}

	public List<GuiTexture> getTextureList() {
		return textureList;
	}

	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Add item to inventory (only allowed to add if required storage space is
	 * still available)
	 * 
	 * @param item
	 *            to add
	 * @return true if add was successful
	 */
	public boolean addItem(LaptopItem item) {
		if (this.storageUsed + item.getSize() <= MAX_STORAGE_SIZE) {
			inLaptop.add(item);
			increaseStorageUsed(item.getSize());
			// updateLaptopDisplay(); //TODO
			return true;
		}
		return false;
	}

	/**
	 * Remove item from inventory. Update storage space used.
	 * 
	 * @param item
	 *            to remove
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
	 * @param size
	 */
	public void increaseStorageUsed(int size) {
		this.storageUsed = this.storageUsed + size;
	}

	public void displayInventory() {
		if (this.isVisible) {
			this.isVisible = false;
			Mouse.setGrabbed(true);
			this.selected = null;
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

	public void displayLaptopItem() {
		if (itemDisplayed != null) {
			this.textureList.remove(this.itemDisplayed);
			this.itemDisplayed = null;
			AudioController.stopEasterEggLoop();
		}
		else {
			if(this.selected != null){
				this.itemDisplayed = guiFactory.makeItemTexture(this.selected.getImgName(), CENTER_POS, IMAGE_SCALE);
				this.textureList.add(this.itemDisplayed);
				if (this.selected.getImgName().equals("extImg1Info")) {
					AudioController.playEasterEggLoop();
				} else {
					AudioController.playRandomInventorySound();
				}
			}
		}

	}

	public LaptopItem[][] getLaptopDisplay() {
		return this.laptopDisplay;
	}

	public LaptopItem getSelected() {
		return this.selected;
	}

	public void selectItem(int keyEvent) {
		if (this.isVisible && !this.inLaptop.isEmpty()) {
			if (this.selected == null) {
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

				if (laptopDisplay[xPos][yPos] != null) {
					this.selected = laptopDisplay[xPos][yPos];
				}
			}

			if (this.selected != null) {
				updateLaptopDisplay();
			}
		}
	}

	public int selectDownOrRight(int num, int max) {
		if (num < max) {
			return num + 1;
		}
		return num;
	}

	public int selectUpOrLeft(int num) {
		if (num > 0) {
			return num - 1;
		}
		return num;
	}

	public LaptopItem getItem(int uid) {
		for (LaptopItem l : this.inLaptop) {
			if (l.getUID() == uid) {
				return l;
			}
		}
		return null;
	}

	public void setStorageUsed(int used) {
		this.storageUsed = used;
	}

	public void serverDelete(LaptopItem entity) {
		if (entity != null) {
			this.storageUsed = this.storageUsed - entity.getSize();
			inLaptop.remove(entity);
			//updateLaptopDisplay();
		}
	}

	public void setInLaptop(ArrayList<LaptopItem> inventory) {
		this.inLaptop = inventory;
	}
}
