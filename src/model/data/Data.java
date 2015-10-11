package model.data;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import model.entities.Camera;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.entities.movableEntity.SwipeCard;

public class Data {
	
	private Player player;
	private ArrayList<LaptopItem> inventory;
	private ArrayList<MovableEntity> movableEntities;
	private ArrayList<SwipeCard> swipeCards;
	
	public Data (Player player, ArrayList<LaptopItem> inventory, ArrayList<MovableEntity> movableEntities, ArrayList<SwipeCard> swipeCards){
		this.player = player;
		this.inventory = inventory;
		this.movableEntities = movableEntities;
		this.swipeCards = swipeCards;
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<LaptopItem> getInventory() {
		return inventory;
	}

	public ArrayList<MovableEntity> getMovableEntities() {
		return movableEntities;
	}

	public ArrayList<SwipeCard> getSwipeCards() {
		return swipeCards;
	}
}
