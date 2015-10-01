package model.guiComponents;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;

import model.entities.Entity;
import model.entities.movableEntity.Item;
import model.entities.movableEntity.LaptopItem;
import model.factories.GuiFactory;
import model.textures.GuiTexture;
import view.DisplayManager;
import view.renderEngine.GuiRenderer;

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
	private ArrayList<GuiTexture> inventoryTexture;
	private GuiFactory guiFactory;
	private GuiRenderer guiRenderer;
	
	
	public Inventory (GuiFactory guiFactory, model.toolbox.Loader loader){
		this.inLaptop = new ArrayList<LaptopItem>();
		this.storageUsed = 0;
		this.isVisible = false;
		this.guiFactory = guiFactory;
		
		this.guiRenderer = new GuiRenderer(loader);
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
		
		List<GuiTexture> guiList = new ArrayList<>();
		guiList.add(guiFactory.makeGuiTexture("blankInventoryScreen", new Vector2f(0f, 0f), new Vector2f(1f,1f)));
		guiRenderer.render(guiList);
		DisplayManager.updateDisplay();
		//		for(LaptopItem item : inLaptop){
//			guiFactory.makeGuiTexture(item.getFileName(), 0f, 1);
//		}
	}
	
	private void closeInventory(){
		System.out.println("Close");
		isVisible = false;
		//TODO
	}

}
