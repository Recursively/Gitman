package model.guiComponents;

import java.util.ArrayList;

import model.entities.Entity;
import model.entities.movableEntity.Item;
import model.entities.movableEntity.LaptopItem;
import model.textures.GuiTexture;

/**
 * Represents the player's laptop. It can hold 'LaptopItems' (e.g.
 * files and README txt documents).
 * 
 * @author Divya
 *
 */
public class Inventory {
	private static final int MAX_STORAGE_SIZE = 512;   // laptop has 512MB available for storage
	
	private ArrayList<LaptopItem> inLaptop;
	private int storageUsed;
	private boolean isVisible;
	
	
	//private ArrayList<GuiTexture> inventoryTexture //populate in GUI fac
	
	public Inventory (){
		this.inLaptop = new ArrayList<LaptopItem>();
		this.storageUsed = 0;
		this.isVisible = false;
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
			this.storageUsed = this.storageUsed + item.getSize();
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
	public Entity deleteItem(LaptopItem item){
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
		System.out.println("Open");
		isVisible = true;
		//TODO
	}
	
	private void closeInventory(){
		System.out.println("Close");
		isVisible = false;
		//TODO
	}

}
