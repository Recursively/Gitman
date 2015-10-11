package controller;

import model.GameWorld;
import view.DisplayManager;

/**
 * Class to handle the lighting and shading timing the game
 *
 * @author Marcel van Workum
 */
public class TimeController {

    public static void tickTock() {
        GameWorld.increaseTime(DisplayManager.getFrameTimeSeconds() * 100);
        update();
    }

    private static void update() {

    }
}
