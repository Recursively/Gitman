package model.network;


import model.GameWorld;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.SwipeCard;

public class NetworkHandler {

	private GameWorld gameWorld;

	private Update lastServerUpdate;
	private Update lastClientUpdate;

	public NetworkHandler(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
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
			interactLaptop(id);
			break;
		default:
			break;
		}

	}

	private void interactLaptop(int id) {
		gameWorld.getMoveableEntities().get(id).interact(gameWorld);

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

		LaptopItem entity = (LaptopItem) gameWorld.getMoveableEntities().get(id);

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
		// gameWorld.removeMovableEntity(entity);
		// add to swipe cards array
		// gameWorld.getSwipeCards().add(entity);

	}

	public void dropLaptopItem(int id, int playerID) {

		LaptopItem entity = gameWorld.getInventory().getItem(id);

		gameWorld.getInventory().serverDelete(entity);
		// remove uid from inventory laptop
		gameWorld.removeFromInventory(entity, playerID);

	}

	public Update getServerUpdate(){
		return lastServerUpdate;
	}
	
	public void setServerUpdate(Update update){
		this.lastServerUpdate = update;
	}
	
	public Update getClientUpdate(){
		return lastClientUpdate;
	}
	
	public void setClientUpdate(Update update){
		this.lastClientUpdate = update;
	}

}
