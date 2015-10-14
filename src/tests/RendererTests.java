package tests;

import controller.TimeController;
import model.GameWorld;
import model.entities.Entity;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.terrains.Terrain;
import org.junit.Before;
import org.junit.Test;
import view.renderEngine.MasterRenderer;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Collection of tests to check the performance of the render engine
 *
 * @author Marcel van Workum - 300313949
 */
public class RendererTests {

    private static TestSuite suite = new TestSuite();
    private GameWorld gameWorld = null;
    private MasterRenderer renderer = null;

    @Before
    public void initRenderer() {
        if (gameWorld == null) {
            gameWorld = TestSuite.getGameWorld();
        }
        if (renderer == null) {
            renderer = new MasterRenderer();
        }
    }

    @Test
    public void testTerrainGrid() {
        Terrain t = gameWorld.getTerrain();

        assertTrue("Grid X equals 1000", 128000 == t.getGridX());
        assertTrue("Grid Z equals -1000", -128000 == t.getGridZ());
    }

    @Test
    public void testTerrainProcessing() {

        // tests rendering process time

        long startTime = System.nanoTime();
        renderer.processTerrain(gameWorld.getTerrain());
        long endTime = System.nanoTime() - startTime;
        assertTrue("Terrain Processing should be fast", endTime <= 216703000000l);
    }

    @Test
    public void testMasterRenderer() {

        /* Tests the render loop time */

        long startTime = System.nanoTime();

        // Stores the objects so that you don't call the getter twice
        ArrayList<Entity> statics = gameWorld.getStaticEntities();
        Player player = gameWorld.getPlayer();

        // First rotate the commits
        gameWorld.rotateCommits();

        // PROCESS ENTITIES
        for (Entity e : statics) {
            if (e.isWithinRange(player)) {
                renderer.processEntity(e);
            }
        }

        // PROCESS Movable entities
        for (MovableEntity e : gameWorld.getMoveableEntities().values()) {
            if (e.isWithinRange(player)) {
                renderer.processEntity(e);
            }
        }

        // Process the walls
        for (Entity e : gameWorld.getWallEntities()) {
            renderer.processEntity(e);
        }
        
        // test patch progress decreasing
        gameWorld.decreasePatch();

        // Increment the time
        TimeController.tickTock();

        long difference = System.nanoTime() - startTime;
        assertTrue("Render time should be quick", difference <= 1435000000);
    }

}