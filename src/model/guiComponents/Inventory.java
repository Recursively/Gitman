package model.guiComponents;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import model.GameWorld;
import model.entities.Entity;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.factories.GuiFactory;
import model.textures.GuiTexture;

/**
 * Represents the player's laptop. It can hold 'LaptopItems' (e.g.
 * files and README txt documents).
 * 
 * @author Divya 
 * @author Ellie
 *
 */
public class Inventory {	
	private static final int MAX_STORAGE_SIZE = 200;   // FIXME laptop has 512MB available for storage
	
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
	private ArrayList<GuiTexture> textureList;

	public Inventory (GuiFactory guiFactory){
		this.inLaptop = new ArrayList<LaptopItem>();
		this.storageUsed = 0;
		this.isVisible = false;
		this.itemDisplayed = null;
		this.selected = null;
		this.guiFactory = guiFactory;

	}
	
	/**
	 * @return list of items in inventory
	 */
	public ArrayList<LaptopItem> getItems(){
		return inLaptop;
	}
	
	/**
	 * @return storage used value
	 */
	public int getStorageUsed(){
		return this.storageUsed;
	}
	
	public ArrayList<GuiTexture> getTextureList() {
		return textureList;
	}

	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Add item to inventory (only allowed to add if required 
	 * storage space is still available)
	 * 
	 * @param item to add
	 * @return true if add was successful
	 */
	public boolean addItem(LaptopItem item){
		if(this.storageUsed + item.getSize() <= MAX_STORAGE_SIZE){
			inLaptop.add(item);
			increaseStorageUsed(item.getSize());
			return true;
		}
		return false;
	}
	
	/**
	 * Remove item from inventory. Update storage space
	 * used. 
	 * 
	 * @param item to remove
	 * @return Item if successfully removed, null if not
	 */
	public LaptopItem deleteItem(GameWorld game){
		LaptopItem item = this.selected;
		if(this.selected != null){
			this.storageUsed = this.storageUsed - this.selected.getSize();
			inLaptop.remove(this.selected);
			game.removeFromInventory(this.selected);
			this.selected = null;
			// redraw inventory gui as item has been deleted 
			updateLaptopDisplay();
		}
		
		//Networking
		return item;
	}
	
	/**
	 * Increases storage used by 'size' amount. 
	 * @param size
	 */
	public void increaseStorageUsed(int size){
		this.storageUsed = this.storageUsed + size;
		System.out.println("Inventory Storage:" + storageUsed + "/" + MAX_STORAGE_SIZE);
	}

	public void displayInventory() {
		if(this.isVisible){
			closeInventory();
		}
		else {
			openInventory();
		}
	}
	
	private void openInventory(){
		this.isVisible = true;
		Mouse.setGrabbed(false);
		updateLaptopDisplay();
	}
	
	private void updateLaptopDisplay() {
		this.laptopDisplay = new LaptopItem[NUM_ACROSS][NUM_DOWN];
		int i = 0;
		for(int x = 0; x < NUM_ACROSS; x++){
			for(int y = 0; y < NUM_DOWN; y++){
				if(i < this.inLaptop.size()){
					this.laptopDisplay[x][y] = this.inLaptop.get(i);
					i++;
				}
			}
		}

		this.textureList = this.guiFactory.makeInventory(this);
	}

	private void closeInventory(){
		this.isVisible = false;
		Mouse.setGrabbed(true);
		this.selected = null;
	}
	
	public void displayLaptopItem() {
		if(itemDisplayed != null){
			this.textureList.remove(this.itemDisplayed);
			this.itemDisplayed = null;
		}
		else {
			if(this.selected != null){
				this.itemDisplayed = guiFactory.makeItemTexture(this.selected.getImgName(), CENTER_POS, IMAGE_SCALE);
				this.textureList.add(this.itemDisplayed);
			}
		}
		
	}

	public LaptopItem[][] getLaptopDisplay() {
		return this.laptopDisplay;
	}
	
	public LaptopItem getSelected(){
		return this.selected;
	}

	public void selectItem(int keyEvent) {
		if(this.isVisible && !this.inLaptop.isEmpty()){
			if(this.selected == null){
				this.selected = this.inLaptop.get(0);
			}
			else{
				// find currently selected item
				boolean found = false;
				int xPos = 0;
				int yPos = 0;
				for(int x = 0; x < laptopDisplay.length; x++){
					for(int y = 0; y < laptopDisplay[0].length; y++){
						if(this.selected == laptopDisplay[x][y]){
							found = true;
							xPos = x;
							yPos = y;
							break;
						}
					}
					if(found){break;}
				}
				
				// move selection based on key press
				if(Keyboard.KEY_UP == keyEvent){
					xPos = selectUpOrLeft(xPos);
				}
				if(Keyboard.KEY_DOWN == keyEvent){
					xPos = selectDownOrRight(xPos, laptopDisplay.length-1);
				}
				if(Keyboard.KEY_LEFT == keyEvent){
					yPos = selectUpOrLeft(yPos);
				}
				if(Keyboard.KEY_RIGHT == keyEvent){
					yPos = selectDownOrRight(yPos, laptopDisplay[0].length-1);
				}
				
				this.selected = laptopDisplay[xPos][yPos];
			}
			
			if(this.selected != null){
				updateLaptopDisplay();
			}
		}
	}

	public int selectDownOrRight(int num, int max) {
		if(num < max){
			return num + 1;
		}
		return num;
	}

	public int selectUpOrLeft(int num) {
		if(num > 0){
			return num - 1;
		}
		return num;
	}

}
