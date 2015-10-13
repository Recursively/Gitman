package tests;

import model.GameWorld;
import model.data.Load;
import model.data.Save;
import model.entities.movableEntity.MovableEntity;
import org.junit.Test;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class DataTests {


	private static TestSuite suite = new TestSuite();
	private GameWorld gameWorld;

	private void initTestGame() {
		
		gameWorld = TestSuite.getGameWorld();
		Vector3f position = new Vector3f(10, 10, 10);
		gameWorld.addPlayer(position, 0);
		Save.saveGame(gameWorld);
		
	}
	
	@Test
	public void testCompareGamestateFields() {
		
		initTestGame();
		
		assertTrue("isProgramCompiled comparison", GameWorld.isProgramCompiled() == Load.loadGame().isIsOutside());
		assertTrue("isOutside comparison", GameWorld.isOutside() == Load.loadGame().isIsOutside());
		assertTrue("progress comparison", gameWorld.getProgress() == Load.loadGame().getProgress());
		assertTrue("score comparison", gameWorld.getScore() == Load.loadGame().getScore());
		assertTrue("canApplyPatch comparison", gameWorld.isCanApplyPatch() == Load.loadGame().isCanApplyPatch());
		assertTrue("commitIndex comparison", gameWorld.getCommitIndex() == Load.loadGame().getCommitIndex());
		assertTrue("timer comparison", gameWorld.getTimer() == Load.loadGame().getTimer());
		
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
