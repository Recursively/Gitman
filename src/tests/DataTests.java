package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import view.DisplayManager;
import controller.AudioController;
import controller.GameController;
import model.GameWorld;
import model.data.Load;
import model.data.Save;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.entities.movableEntity.SwipeCard;
import model.toolbox.Loader;

public class DataTests {

	private GameWorld gameWorld;

	private void initTestGame() {
		
		this.gameWorld = TestSuite.gameWorld;
	}

	@Test
	public void testCompareGamestateFields() {
		
		initTestGame();
		assertTrue("Progress comparison", gameWorld.getProgress() == Load
				.loadGame().getProgress());
	}

	@Test
	public void testCompareCollections() {
		
		initTestGame();
		
		assertTrue(
				"LaptopItems saved and loaded correctly",
				gameWorld.getInventory().getItems()
						.equals(Load.loadGame().getInventory()));
		assertTrue("MovableEntities saved and loaded correctly",
				new ArrayList<MovableEntity>(gameWorld.getMoveableEntities()
						.values()).equals(Load.loadGame().getInventory()));
		assertTrue("MovableEntities saved and loaded correctly", gameWorld
				.getSwipeCards().equals(Load.loadGame().getInventory()));
	}

}
