package model.guiComponents;

import java.util.ArrayList;

import model.entities.Entity;
import model.entities.movableEntity.Item;
import model.entities.movableEntity.LaptopItem;

/**
 * Represents the player's laptop. It can hold 'LaptopItems' (e.g.
 * files and README txt documents).
 * 
 * @author Divya
 *
 */
public class Inventory {
	private static final int MAX_STORAGE_SIZE = 256;
	
	private ArrayList<LaptopItem> inLaptop;
	private int storageUsed;
	
	public Inventory (){
		this.inLaptop = new ArrayList<LaptopItem>();
		this.storageUsed = 0;
	}
	
	public boolean addItem(LaptopItem item){
		if(this.storageUsed + item.getSize() <= MAX_STORAGE_SIZE){
			inLaptop.add(item);
			this.storageUsed = this.storageUsed + item.getSize();
			return true;
		}
		else {
			return false;
		}
	}
	
	public Entity deleteItem(LaptopItem item){
		this.storageUsed = this.storageUsed - item.getSize();
		inLaptop.remove(item);
		//TODO
		return null; //FIXME
	}
	

	public ArrayList<LaptopItem> getInventory(){
		return inLaptop;
	}
	
	public int getStorageUsed(){
		return this.storageUsed;
	}
	
	/**
	 * Increases storage used by 'size' amount. 
	 * @param size
	 */
	public void increaseStorageUsed(int size){
		this.storageUsed = this.storageUsed + size;
	}

}
