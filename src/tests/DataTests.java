package tests;

import model.GameWorld;
import model.data.Data;
import model.data.Load;
import model.data.Save;
import model.entities.movableEntity.MovableEntity;
import org.junit.Test;
import org.lwjgl.util.vector.Vector3f;

import static org.junit.Assert.assertTrue;

public class DataTests {

    private static TestSuite suite = new TestSuite();
    private GameWorld gameWorld;
    private Data data;

    private void initTestGame() {
        gameWorld = TestSuite.getGameWorld();
        Vector3f position = new Vector3f(10, 10, 10);
        gameWorld.addPlayer(position, 0);
        Save.saveGame(gameWorld);
        data = Load.loadGame();

    }

    @Test
    public void testCompareGamestateFields() {
        initTestGame();

        assertTrue("isProgramCompiled comparison",
                GameWorld.isProgramCompiled() == data.isIsProgramCompiled());
        assertTrue("isOutside comparison",
                GameWorld.isOutside() == data.isIsOutside());
        assertTrue("progress comparison",
                gameWorld.getProgress() == data.getProgress());
        assertTrue("score comparison", gameWorld.getScore() == data.getScore());
        assertTrue("canApplyPatch comparison",
                gameWorld.isCanApplyPatch() == data.isCanApplyPatch());
        assertTrue("commitIndex comparison",
                gameWorld.getCommitIndex() == data.getCommitIndex());
        assertTrue("timer comparison", gameWorld.getTimer() == Load.loadGame().getTimer());

    }

    @Test
    public void testCompareLaptopItems() {
        initTestGame();

        assertTrue("LaptopItems comparison", gameWorld.getInventory()
                .getItems().equals(data.getInventory()));
    }

    @Test
    public void testCompareMovableEntities() {
        initTestGame();

        for (MovableEntity e : gameWorld.getMoveableEntities().values()) {
            assertTrue("MovableEntity comparison of " + e.getType(), data.getMovableEntities().contains(e));
        }
    }

    @Test
    public void testCompareSwipeCards() {
        initTestGame();

        assertTrue(
                "SwipeCards comparison",
                gameWorld.getSwipeCards().equals(
                        data.getSwipeCards()));
    }
}
