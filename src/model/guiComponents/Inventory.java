package model.guiComponents;

import java.util.ArrayList;

import model.entities.movableEntity.Item;

/**
 * 
 * @author Divya
 *
 */
public class Inventory {
	private ArrayList<Item> inLaptop;
	
	public Inventory (){
		inLaptop = new ArrayList<Item>();
	}
	
	public void addItem(Item i){
		inLaptop.add(i);
	}
	
	public void deleteItem(Item i){
		inLaptop.remove(i);
	}
	
	public ArrayList<Item> getInventory(){
		return inLaptop;
	}

}
