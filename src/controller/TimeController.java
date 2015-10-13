package controller;

import model.GameWorld;
import view.DisplayManager;
import view.renderEngine.MasterRenderer;

/**
 * Class to handle the lighting and shading timing the game
 *
 * Really thin controller
 *
 * @author Marcel van Workum
 */
public final class TimeController {

    /**
     * Tick the clock, and update the world
     */
    public static void tickTock() {
        GameWorld.increaseTime(DisplayManager.getFrameTimeSeconds() * 100);
        update();
    }

    /**
     * Updates the sun and fog colours
     */
    private static void update() {
        GameWorld.updateSun();
        MasterRenderer.updateFog();
    }
}
