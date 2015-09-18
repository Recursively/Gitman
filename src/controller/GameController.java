package controller;

import model.GameWorld;
import model.entities.Light;
import model.entities.movableEntity.Player;
import model.factories.GuiFactory;
import model.factories.LightFactory;
import model.factories.PlayerFactory;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import view.DisplayManager;
import view.renderEngine.GuiRenderer;
import view.renderEngine.MasterRenderer;

import java.util.ArrayList;

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

    // View
    private final MasterRenderer renderer;
    private final GuiRenderer guiRenderer;

    //Controller
    private final NetworkController networkController;

    private final GameWorld gameWorld;

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

        intiGame();

        //start the game
        doGame();
    }

    private void intiGame() {
        LightFactory lightFactory = new LightFactory();
        lights = lightFactory.getLights();

        GuiFactory guiFactory = new GuiFactory(loader);
        guiImages = guiFactory.getGuiImages();

        PlayerFactory playerFactory = new PlayerFactory(gameWorld);
        player = playerFactory.getPlayer();
    }

    private ArrayList<Light> lights;
    private ArrayList<GuiTexture> guiImages;
    private Player player;

    /**
     *
     */
    private void doGame() {

        Mouse.setGrabbed(true);

        while (!Display.isCloseRequested()) {


            renderer.processTerrain(gameWorld.getTerrain());

            // Again ugly and needs work

            player.move(gameWorld.getTerrain());

            renderer.render(lights, player.getCamera());

            guiRenderer.render(guiImages);
            DisplayManager.updateDisplay();

        }

        cleanUp();
    }

    private void cleanUp() {
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
