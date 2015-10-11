package model.network;

import org.lwjgl.util.vector.Vector3f;

import model.GameWorld;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.SwipeCard;

public class NetworkHandler {

	private GameWorld gameWorld;

	private int update = -1;
	private int completedUpdateProgress;
	
	public boolean serverUpdate;

	private MovableEntity mostRecentEntity;

	public NetworkHandler(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	// when an update is sent to the server about an entity update process it
	// here
	public void dealWithUpdate(int type, int id, int playerID) {

		System.out.println("DECIDING WHETHER TO UPDATE");


		if (gameWorld.getMoveableEntities().get(id) == null && type != 8)
			return;

		System.out.println("UPDATING NOW");

		switch (type) {
		case 8:
			dropLaptopItem(id, playerID);
			break;
		case 10:
			interactBug();
			break;
		case 11:
			interactCommit();
			break;
		case 13:
			interactLaptopItem(id);
			break;
		case 16:
			interactSwipeCard(id);
			break;
		case 17:

			break;
		default:
			break;
		}

	}

	public void interactBug() {
		System.out.println("INTERACTED WITH BUG");

		// win games???
	}

	public void interactCommit() {
		System.out.println("INTERACTED WITH COMMIT");

		// update score
		gameWorld.incrementPatch();

	}

	public void interactLaptopItem(int id) {
		System.out.println("INTERACTED WITH LAPTOP ITEM");

		LaptopItem entity = (LaptopItem) gameWorld.getMoveableEntities()
				.get(id);

		// removes uid from movables map
		gameWorld.removeMovableEntity(entity);
		// adds that item to inlaptop array in inventory
		gameWorld.addToInventory(entity);
		// updates score by .getScore()
		gameWorld.updateScore(entity.getScore());

	}

	public void interactSwipeCard(int id) {
		System.out.println("INTERACTED WITH SWIPE CARD");

		SwipeCard entity = (SwipeCard) gameWorld.getMoveableEntities().get(id);

		entity.interact(gameWorld);

		// remove from movables
		//gameWorld.removeMovableEntity(entity);
		// add to swipe cards array
		//gameWorld.getSwipeCards().add(entity);

	}

	public void dropLaptopItem(int id, int playerID) {
		System.out.println("DROPPED ITEM");

		LaptopItem entity = gameWorld.getInventory().getItem(id);
		
		gameWorld.getInventory().serverDelete(entity);
		// remove uid from inventory laptop
		gameWorld.removeFromInventory(entity, playerID);

	}

	/**
	 * @return the update
	 */
	public int getUpdate() {
		return update;
	}

	/**
	 * @param update
	 *            the update to set
	 */
	public void setUpdate(int update) {
		if (update != -1) {
			this.update = update;
		}
	}

	public void sentDone(int uid) {
		System.out.println("SENT DONE ID: " + uid);
		completedUpdateProgress++;
		if(completedUpdateProgress == gameWorld.getAllPlayers().size()){
			this.update = -1;
		}
	}

	public MovableEntity getMostRecentEntity() {
		return mostRecentEntity;
	}

	public void setMostRecentEntity(MovableEntity mostRecentEntity) {
		this.mostRecentEntity = mostRecentEntity;
	}
}
