package model.guiComponents;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

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
	private static final int MAX_STORAGE_SIZE = 512;   // laptop has 512MB available for storage
	
	private ArrayList<LaptopItem> inLaptop;
	private int storageUsed;
	private boolean isVisible;
	private boolean itemDisplayed;
	private LaptopItem selected;
	private GuiFactory guiFactory;
	private ArrayList<GuiTexture> textureList;

	
	
	public Inventory (GuiFactory guiFactory){
		this.inLaptop = new ArrayList<LaptopItem>();
		this.storageUsed = 0;
		this.isVisible = false;
		this.itemDisplayed = false;
		this.selected = null;
		this.guiFactory = guiFactory;

	}
	
	/**
	 * @return list of items in inventory
	 */
	public ArrayList<LaptopItem> getInventory(){
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
	public void deleteItem(GameWorld game){
		// TODO for reuben! :)
		// at end of this method there are changes to:
		// storageUsed in Inventory
		// movableEnities map in GameWorld
		// inLaptop list in Inventory
		
		if(this.selected != null){
			this.storageUsed = this.storageUsed - this.selected.getSize();
			inLaptop.remove(this.selected);
			game.removeFromInventory(this.selected);
			this.selected = null;
		}
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
		if(isVisible){
			closeInventory();
		}
		else {
			openInventory();
		}
	}
	
	private void openInventory(){
		isVisible = true;
		Mouse.setGrabbed(false);
		textureList = guiFactory.makeInventory(this);
		
		//List<GuiTexture> guiList = new ArrayList<>();
		//
		//DisplayManager.updateDisplay();
		//		for(LaptopItem item : inLaptop){
//			guiFactory.makeGuiTexture(item.getFileName(), 0f, 1);
//		}
	}
	
	private void closeInventory(){
		isVisible = false;
		Mouse.setGrabbed(true);
		this.selected = null;
		//TODO
	}
	
	public void displayLaptopItem(int x, int y) {
		this.selected = null;  // make sure no item is shown as selected with left click
		if(itemDisplayed){
			closeLaptopItem();
		}
		else {
			openLaptopItem(x, y);
		}
		
	}

	private void openLaptopItem(int x, int y) {
		LaptopItem item = findItem(x, y);
		if(item != null){
			itemDisplayed = true;
		}
		// TODO open displays in front of laptop screen showing 
		// full image of item
		// just add image to texture list
		
	}
	
	public void closeLaptopItem(){
		if(itemDisplayed){
			itemDisplayed = false;
			// TODO close the display of the item 
		}
	}
	
	private LaptopItem findItem(int x, int y) {
		// TODO find item that has been clicked on
		// maybe implement 2d array storage to make finding 
		// items easier???
		return null;
	}

	public void showSelected(int x, int y) {
		LaptopItem item = findItem(x, y);
		if(item != null){
			this.selected = item;
			// update textures to show the clicked on item's 'selected' image, not normal one
		}		
	}

}
