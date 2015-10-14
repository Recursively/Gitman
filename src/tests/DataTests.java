package tests;

import model.GameWorld;
import model.data.Data;
import model.data.Load;
import model.data.Save;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.ReadMe;
import model.entities.movableEntity.SwipeCard;
import model.guiComponents.Inventory;
import org.junit.Test;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for data tests
 *
 * @author Finn Kinnear - 300310504
 */
public class DataTests {

    private static TestSuite suite = new TestSuite();
    private GameWorld gameWorld;
    private Data data;

    private void initTestGame() {
        gameWorld = TestSuite.getGameWorld();

        Vector3f position = new Vector3f(10, 10, 10);
        gameWorld.addPlayer(position, 0);

        Inventory i = gameWorld.getInventory();

        ReadMe e = (ReadMe) getEntity("ReadMe");
        // if the item can't be found, create a copy
        if (e == null) {
            e = new ReadMe(null, null, 0, 0, 0, 0, 0, 0, "readme10");
        }
        i.addItem(e);

        SwipeCard s = (SwipeCard) getEntity("SwipeCard");
        // if the item can't be found, create a copy
        if (s == null) {
            s = new SwipeCard(null, null, 0, 0, 0, 0, 0, 0, 0);
        }
        gameWorld.addCard(s);

        Save.saveGame(gameWorld);
        data = Load.loadGame();

    }

    @Test
    public void testCompareIsProgramCompiled() {
        initTestGame();

        assertTrue("IsProgramCompiled comparison",
                GameWorld.isProgramCompiled() == data.isIsProgramCompiled());
    }

    @Test
    public void testIsOutside() {
        initTestGame();

        assertTrue("IsOutside comparison",
                GameWorld.isOutside() == data.isIsOutside());
    }

    @Test
    public void testProgress() {
        initTestGame();

        assertTrue("Progress comparison",
                gameWorld.getProgress() == data.getProgress());
    }

    @Test
    public void testGameState() {
        initTestGame();

        assertTrue("GameState comparison",
                gameWorld.getGameState() == data.getGameState());
    }

    @Test
    public void testCommitCollected() {
        initTestGame();

        assertTrue("CommitCollected comparison",
                gameWorld.getCommitCollected() == data.getCommitCollected());
    }

    @Test
    public void testScore() {
        initTestGame();

        assertTrue("Score comparison", gameWorld.getScore() == data.getScore());
    }

    @Test
    public void testCanApplyPatch() {
        initTestGame();

        assertTrue("CanApplyPatch comparison",
                gameWorld.isCanApplyPatch() == data.isCanApplyPatch());
    }

    @Test
    public void testCommitIndex() {
        initTestGame();

        assertTrue("CommitIndex comparison",
                gameWorld.getCommitIndex() == data.getCommitIndex());
    }

    @Test
    public void testTimer() {
        initTestGame();

        assertTrue("Timer comparison", gameWorld.getTimer() == Load.loadGame()
                .getTimer());
    }

    @Test
    public void testPlayerPosition() {
        initTestGame();

        assertTrue("Player position comparison", gameWorld.getPlayer()
                .getPosition().equals(data.getPlayerPos()));
    }

    @Test
    public void testPlayerCameraPitch() {
        initTestGame();

        assertTrue("Camera pitch comparison", gameWorld.getPlayer().getCamera()
                .getPitch() == data.getPitch());
    }

    @Test
    public void testPlayerCameraRoll() {
        initTestGame();

        assertTrue("Camera roll comparison", gameWorld.getPlayer().getCamera()
                .getRoll() == data.getRoll());
    }

    @Test
    public void testPlayerCameraYaw() {
        initTestGame();

        assertTrue("Camera yaw comparison", gameWorld.getPlayer().getCamera()
                .getYaw() == data.getYaw());
    }

    @Test
    public void testPlayerUID() {
        initTestGame();

        assertTrue("Player uid comparison",
                gameWorld.getPlayer().getUID() == data.getUid());
    }

    @Test
    public void testInventory() {
        initTestGame();

        HashSet<Integer> uids = new HashSet<>();

        for (MovableEntity e : gameWorld.getInventory().getItems()) {
            uids.add(e.getUID());
        }

        for (MovableEntity m : data.getInventory()) {
            assertTrue("Not contained" + m.getUID(), uids.contains(m.getUID()));
        }
    }

    @Test
    public void testStorageUsed() {
        initTestGame();

        assertTrue("Inventory storage used comparison", gameWorld
                .getInventory().getStorageUsed() == data.getStorageUsed());
    }

    @Test
    public void testMovableEntities() {
        initTestGame();

        initTestGame();

        HashSet<Integer> uids = new HashSet<>();

        for (MovableEntity e : gameWorld.getMoveableEntities().values()) {
            uids.add(e.getUID());
        }

        for (MovableEntity m : data.getMovableEntities()) {
            assertTrue("Not contained" + m.getUID(), uids.contains(m.getUID()));
        }
    }

    @Test
    public void testSwipeCards() {
        initTestGame();

        HashSet<Integer> uids = new HashSet<>();

        for (MovableEntity e : gameWorld.getSwipeCards()) {
            uids.add(e.getUID());
        }

        for (MovableEntity m : data.getSwipeCards()) {
            assertTrue("Not contained" + m.getUID(), uids.contains(m.getUID()));
        }
    }

    private MovableEntity getEntity(String type) {
        Map<Integer, MovableEntity> movableEntities = gameWorld.getMoveableEntities();
        for (MovableEntity e : movableEntities.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
