package model.network;

import java.util.ArrayList;

import model.GameWorld;
import model.entities.movableEntity.Laptop;
import model.entities.movableEntity.LaptopItem;

public class NetworkHandler {

	private GameWorld gameWorld;

	private Update lastClientUpdate;

	private ArrayList<Laptop> interactedLaptops;

	public NetworkHandler(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.interactedLaptops = new ArrayList<>();
	}

	// when an update is sent to the server about an entity update process it
	// here
	public void dealWithUpdate(int type, int id, int playerID) {

		if (gameWorld.getMoveableEntities().get(id) == null && type != 8)
			return;

		switch (type) {
		case 8:
			dropLaptopItem(id, playerID);
			break;
		case 10:
			interactBug(id);
			break;
		case 11:
			interactCommit(id);
			break;
		case 13:
			interactLaptopItem(id);
			break;
		case 16:
			interactSwipeCard(id);
			break;
		case 17:
			interactLaptop(id);
			break;
		default:
			break;
		}

	}

	private void interactLaptop(int id) {
		Laptop laptop = (Laptop) gameWorld.getMoveableEntities().get(id);
		laptop.interact(gameWorld);
		interactedLaptops.add(laptop);

	}

	public void interactBug(int id) {

		// win games???
		gameWorld.getMoveableEntities().get(id).interact(gameWorld);

	}

	public void interactCommit(int id) {

		// interact with commit
		gameWorld.getMoveableEntities().get(id).interact(gameWorld);
	}

	public void interactLaptopItem(int id) {

		LaptopItem entity = (LaptopItem) gameWorld.getMoveableEntities().get(id);

		// removes uid from movables map
		gameWorld.removeMovableEntity(entity);
		// adds that item to inlaptop array in inventory
		gameWorld.addToInventory(entity);
		// updates score by .getScore()
		gameWorld.updateScore(entity.getScore());

	}

	public void interactSwipeCard(int id) {

		// interact with swipe card
		gameWorld.getMoveableEntities().get(id).interact(gameWorld);

	}

	public void dropLaptopItem(int id, int playerID) {

		LaptopItem entity = gameWorld.getInventory().getItem(id);

		gameWorld.getInventory().serverDelete(entity);
		// remove uid from inventory laptop
		gameWorld.removeFromInventory(entity, playerID);

	}

	public Update getClientUpdate() {
		return lastClientUpdate;
	}

	public void setClientUpdate(Update update) {
		this.lastClientUpdate = update;
	}
	
	/**
	 * @return the interactedLaptops
	 */
	public ArrayList<Laptop> getInteractedLaptops() {
		return interactedLaptops;
	}

}
