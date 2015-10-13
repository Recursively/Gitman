package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import model.GameWorld;
import model.data.Save;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.SwipeCard;

import org.junit.Test;
import org.lwjgl.util.vector.Vector3f;

public class GameWorldTests {
	
	private static TestSuite suite = new TestSuite();
	private static GameWorld gameWorld = TestSuite.getGameWorld();

	public GameWorldTests() {
		gameWorld.addPlayer(new Vector3f(10,10,10),0);		
	}
	
	@Test
	public void testSwipeCardInteract(){
		SwipeCard s = (SwipeCard) getEntity("SwipeCard");
		assertEquals(16, s.interact(gameWorld));
	}
	
	private MovableEntity getEntity(String type){
		Map<Integer, MovableEntity> movableEntities = gameWorld.getMoveableEntities();
		for(MovableEntity e : movableEntities.values()){
			if(e.getType().equals(type)){
				return e;
			}
		}
		return null;
	}

}
