package tests;

import model.GameWorld;
import model.data.Load;
import model.entities.movableEntity.MovableEntity;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class DataTests {


	private GameWorld gameWorld;

	private void initTestGame() {
		
		this.gameWorld = TestSuite.gameWorld;
	}
	
	@Test
	public void testCompareGamestateFields() {
		
		initTestGame();
		assertTrue("Progress comparison", gameWorld.getProgress() == Load.loadGame().getProgress());
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
