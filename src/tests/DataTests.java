package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import controller.GameController;
import model.GameWorld;
import model.data.Load;
import model.data.Save;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.SwipeCard;
import model.toolbox.Loader;

public class DataTests {

	private boolean isHost = true;
	private String ipAddress = "0";
	private boolean load = false;
	private boolean fullscreen = true;
	private GameWorld gameWorld = new GameWorld(new Loader(), new GameController(isHost, ipAddress, load, fullscreen));
	private ArrayList<LaptopItem> laptopItems;
	private ArrayList<MovableEntity> movableEntities;
	private ArrayList<SwipeCard> swipeCards;

	private void initGameWorld() {
		gameWorld.initGame(isHost, load);
		laptopItems = gameWorld.getInventory().getItems();
		movableEntities = (ArrayList<MovableEntity>) gameWorld.getMoveableEntities().values();
		swipeCards = gameWorld.getSwipeCards();
		Save.saveGame(gameWorld);
	}
	
	@Test
	public void testCompareLaptopItems(){
		assertTrue("LaptopItems saved and loaded correctly", laptopItems.equals(Load.loadGame().getInventory()));
	}
	
	@Test
	public void testCompareMovableEntities(){
		assertTrue("MovableEntities saved and loaded correctly", laptopItems.equals(Load.loadGame().getInventory()));
	}
	
	@Test
	public void testCompareSwipeCards(){
		assertTrue("MovableEntities saved and loaded correctly", laptopItems.equals(Load.loadGame().getInventory()));
	}
}
