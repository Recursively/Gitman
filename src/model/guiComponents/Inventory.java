package model.guiComponents;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import model.entities.Entity;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.factories.GuiFactory;
import model.textures.GuiTexture;

/**
 * Represents the player's laptop. It can hold 'LaptopItems' (e.g.
 * files and README txt documents).
 * 
 * @author Divya and Ellie
 *
 */
public class Inventory {
	private static final int MAX_STORAGE_SIZE = 512;   // laptop has 512MB available for storage
	
	private ArrayList<LaptopItem> inLaptop;
	private int storageUsed;
	private boolean isVisible;
	private boolean itemDisplayed;
	private GuiFactory guiFactory;
	private ArrayList<GuiTexture> textureList;

	
	
	public Inventory (GuiFactory guiFactory){
		this.inLaptop = new ArrayList<LaptopItem>();
		this.storageUsed = 0;
		this.isVisible = false;
		this.itemDisplayed = false;
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
	public MovableEntity deleteItem(LaptopItem item){
		if(inLaptop.contains(item)){
			this.storageUsed = this.storageUsed - item.getSize();
			inLaptop.remove(item);
			return item;
		}
		return null; 
	}
	
	/**
	 * Increases storage used by 'size' amount. 
	 * @param size
	 */
	public void increaseStorageUsed(int size){
		this.storageUsed = this.storageUsed + size;
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
		//TODO
	}
	
	public void displayLaptopItem(int x, int y) {
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

	public void showDeleteOption(int x, int y) {
		LaptopItem item = findItem(x, y);
		if(item != null){
			// TODO how should deletes be carried out?
			// IDEAS: maybe show message: do you want to delete this item: Y = Yes, N = No???
		}		
	}

}
