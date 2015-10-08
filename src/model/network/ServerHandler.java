package model.network;

import org.lwjgl.util.vector.Vector3f;

import model.GameWorld;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.SwipeCard;

public class ServerHandler {

	private GameWorld gameWorld;

	public ServerHandler(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
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
		
		// remove from movables
		gameWorld.removeMovableEntity(entity);
		// add to swipe cards array
		gameWorld.getSwipeCards().add(entity);

	}

	public void dropLaptopItem(int id, float x, float y, float z) {
		System.out.println("DROPPED ITEM");
	
		LaptopItem entity = null;
		
		for(LaptopItem laptopItem : gameWorld.getInventory().getInventory()){
			if(laptopItem.getUID() == id){
				entity = laptopItem;
			}
		}
		
		// remove uid from inventory laptop
		gameWorld.removeFromInventory(entity);
		// item.setPosition(x,y,z)
		entity.setPosition(new Vector3f(x,y,z));
		// add to movable maps...
		gameWorld.getMoveableEntities().put(id, entity);
	}

}
