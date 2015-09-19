package controller;

import model.GameWorld;
import model.toolbox.Loader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import view.DisplayManager;
import view.renderEngine.GuiRenderer;
import view.renderEngine.MasterRenderer;

/**
 * Controller class to handle the delegations between the Model and View package.
 *
 * Deals with Game logic
 *
 * @author Marcel van Workum
 */
public class GameController {

    // Model
    private final Loader loader;
    private final GameWorld gameWorld;

    // View
    private final MasterRenderer renderer;
    private final GuiRenderer guiRenderer;

    //Controller
    private final NetworkController networkController;

    /**
     * Delegates the creation of the MVC and then starts the game
     */
    public GameController() {

        // initialise model
        loader = new Loader();

        // initialise view
        DisplayManager.createDisplay();
        renderer = new MasterRenderer(loader);
        guiRenderer = new GuiRenderer(loader);

        //initialise controller
        networkController = new NetworkController();

        // initialise the game world
        gameWorld = new GameWorld(loader);
        gameWorld.initGame();

        // hook the mouse
        Mouse.setGrabbed(true);

        //start the game
        doGame();
    }

    /**
     * Main game loop where all the goodness will happen
     */
    private void doGame() {

        while (!Display.isCloseRequested()) {

            // process the terrain
            renderer.processTerrain(gameWorld.getTerrain());

            // PROCESS PLAYER

            // PROCESS ENTITIES


            // update the players position in the world
            gameWorld.getPlayer().move(gameWorld.getTerrain());

            // Render the player's view
            renderer.render(gameWorld.getLights(), gameWorld.getPlayer().getCamera());

            // render the gui
            guiRenderer.render(gameWorld.getGuiImages());

            // update the Display window
            DisplayManager.updateDisplay();
        }

        // Finally clean up resources
        cleanUp();
    }

    /**
     * Cleans up the game when it is closed
     */
    private void cleanUp() {
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
