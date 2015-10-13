package model.network;

import java.util.ArrayList;

import model.GameWorld;
import model.entities.movableEntity.Laptop;
import model.entities.movableEntity.LaptopItem;

/**
 * 
 * Class that looks after what kind of Updates have occured in another Player's
 * game world and delegates to each of the types of interactions then calls
 * interact on them on this Client/Server
 * 
 * @author Reuben Puketapu
 *
 */
public class NetworkHandler {

	private GameWorld gameWorld;
	private Update lastClientUpdate;
	private ArrayList<Laptop> interactedLaptops;

	/**
	 * Constructor for NetworkHandler
	 * 
	 * @param gameWorld
	 *            gameWorld
	 */
	public NetworkHandler(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.interactedLaptops = new ArrayList<>();
	}

	/**
	 * When there is an update sent, this deals with what type of update it was
	 * then executes it correctly
	 * 
	 * @param type
	 *            type of interaction
	 * @param id
	 *            the id of the entity
	 * @param playerID
	 *            the id of the player who sent the interaction
	 */
	public void dealWithUpdate(int type, int id, int playerID) {

		// if this entity has already been interacted with, don't interact with
		// it again
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

	/**
	 * Interacts with the laptop with the given ID and adds the laptop to the
	 * interacted Laptops
	 * 
	 * @param id
	 *            LaptopID
	 */
	private void interactLaptop(int id) {
		Laptop laptop = (Laptop) gameWorld.getMoveableEntities().get(id);
		laptop.interact(gameWorld);
		interactedLaptops.add(laptop);

	}

	/**
	 * Interacts with a Bug
	 * 
	 * @param id
	 *            BugID
	 */
	public void interactBug(int id) {

		gameWorld.getMoveableEntities().get(id).interact(gameWorld);

	}

	/**
	 * Interacts with a Commit
	 * 
	 * @param id
	 *            CommitID
	 */
	public void interactCommit(int id) {

		// interact with commit
		gameWorld.getMoveableEntities().get(id).interact(gameWorld);
	}

	/**
	 * Interacts with a Laptop
	 * 
	 * @param id
	 *            LaptopID
	 */
	public void interactLaptopItem(int id) {

		LaptopItem entity = (LaptopItem) gameWorld.getMoveableEntities().get(id);

		// removes uid from movables map
		gameWorld.removeMovableEntity(entity);
		// adds that item to inlaptop array in inventory
		gameWorld.addToInventory(entity);
		// updates score by .getScore()
		gameWorld.updateScore(entity.getScore());

	}

	/**
	 * Interacts with a SwipeCard
	 * 
	 * @param id
	 *            SwipeCardID
	 */
	public void interactSwipeCard(int id) {

		// interact with swipe card
		gameWorld.getMoveableEntities().get(id).interact(gameWorld);

	}

	/**
	 * Drops a Laptop Item
	 * 
	 * @param id
	 *            the ID of the Laptop item
	 * @param playerID
	 *            the ID of the player who dropped the item
	 */
	public void dropLaptopItem(int id, int playerID) {

		LaptopItem entity = gameWorld.getInventory().getItem(id);

		gameWorld.getInventory().serverDelete(entity);
		// remove uid from inventory laptop
		gameWorld.removeFromInventory(entity, playerID);

	}

	/**
	 * Gets the Client's update
	 * 
	 * @return the update
	 */
	public Update getClientUpdate() {
		return lastClientUpdate;
	}

	/**
	 * Sets the Client's update
	 * 
	 * @param update
	 */
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
