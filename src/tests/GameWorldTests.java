package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

import model.GameWorld;
import model.entities.movableEntity.Bug;
import model.entities.movableEntity.Commit;
import model.entities.movableEntity.FlashDrive;
import model.entities.movableEntity.Laptop;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.entities.movableEntity.ReadMe;
import model.entities.movableEntity.SwipeCard;

import org.junit.Test;
import org.lwjgl.util.vector.Vector3f;

import controller.AudioController;

public class GameWorldTests {
	
	private static TestSuite suite = new TestSuite();
	private static GameWorld gameWorld = TestSuite.getGameWorld();

	public GameWorldTests() {
		gameWorld.addPlayer(new Vector3f(10,10,10),0);		
	}
	
	@Test
	public void testSwipeCardInteract(){
		SwipeCard e = (SwipeCard) getEntity("SwipeCard");
		assertEquals(16, e.interact(gameWorld));
	}
	
	@Test
	public void testReadMeInteract(){
		ReadMe e = (ReadMe) getEntity("ReadMe");
		assertEquals(13, e.interact(gameWorld));
	}
	
	@Test
	public void testFlashDriveInteract(){
		FlashDrive e = (FlashDrive) getEntity("FlashDrive");
		assertEquals(13, e.interact(gameWorld));
	}
	
	@Test
	public void testBugInteract(){
		assertFalse(gameWorld.canApplyPatch());
		Bug e = (Bug) getEntity("Bug");
		assertEquals(-1, e.interact(gameWorld));
	}
	
	@Test
	public void testLaptopInteract(){
		Laptop e = (Laptop) getEntity("Laptop");
		// if have card, ensure successful interaction
		if(checkSwipeCard(e.getCardID())){
			assertEquals(17, e.interact(gameWorld));
		}
		else {
			assertEquals(-1, e.interact(gameWorld));
		}
	}
	
	@Test
	public void testCommitInteract(){
		Commit e = (Commit) getEntity("Commit");
		assertEquals(11, e.interact(gameWorld));
	}
	
	@Test
	public void testPlayerCanInteract(){
		Player p = gameWorld.getPlayer();
		assertFalse(p.canInteract());
	}
	
	@Test
	public void testSwipeCardCanInteract(){
		SwipeCard e = (SwipeCard) getEntity("SwipeCard");
		assertTrue(e.canInteract());
	}
	
	@Test
	public void testReadMeCanInteract(){
		ReadMe e = (ReadMe) getEntity("ReadMe");
		assertTrue(e.canInteract());
	}
	
	@Test
	public void testFlashDriveCanInteract(){
		FlashDrive e = (FlashDrive) getEntity("FlashDrive");
		assertTrue(e.canInteract());
	}
	
	@Test
	public void testBugCanInteract(){
		Bug e = (Bug) getEntity("Bug");
		assertTrue(e.canInteract());
	}
	
	@Test
	public void testLaptopCanInteract(){
		Laptop e = (Laptop) getEntity("Laptop");
		assertTrue(e.canInteract());
	}
	
	@Test
	public void testCommitCanInteract(){
		Commit e = (Commit) getEntity("Commit");
		assertTrue(e.canInteract());
	}
	
	
	
	
	// ------------------------------------------------------
	// HELPER METHODS
	// ------------------------------------------------------
	
	private MovableEntity getEntity(String type){
		Map<Integer, MovableEntity> movableEntities = gameWorld.getMoveableEntities();
		for(MovableEntity e : movableEntities.values()){
			if(e.getType().equals(type)){
				return e;
			}
		}
		return null;
	}
	
	private boolean checkSwipeCard(int cardID){
		ArrayList<SwipeCard> cards = gameWorld.getSwipeCards();
		for(SwipeCard s: cards){
			if(s.matchID(cardID)){
				return true;
			}
		}
		return false;
	}

}
