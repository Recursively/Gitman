package controller;

import model.GameWorld;
import model.entities.Entity;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.toolbox.Loader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import view.DisplayManager;
import view.renderEngine.GuiRenderer;
import view.renderEngine.MasterRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Controller class to handle the delegations between the Model and View
 * package.
 * <p/>
 * Deals with Game Loop game logic
 *
 * @author Marcel van Workum
 * @author Reuben
 * @author Divya
 * @author Ellie
 */
public class GameController {
    // network state
    protected static boolean RUNNING;
    protected static boolean READY;
    public static boolean NETWORK_DISCONNECTED;

    // Model
    private final GameWorld gameWorld;

    // View
    private final MasterRenderer renderer;
    private final GuiRenderer guiRenderer;

    // Controller
    private ClientController clientController;
    private ServerController serverController;
    private ActionController actionController;

    // networking fields
    private final boolean isHost;
    private int playerCount;

    /**
     * Delegates the creation of the MVC and then starts the game
     *
     * @throws IOException
     */
    public GameController(boolean isHost, String ipAddress, boolean load) {
        GameController.NETWORK_DISCONNECTED = false;
        GameController.RUNNING = true;

        // initialise view
        renderer = new MasterRenderer();
        guiRenderer = new GuiRenderer();

        // initialise the game world
        gameWorld = new GameWorld(this);
        gameWorld.initGame(load);

        // initialise controller for actions
        actionController = new ActionController(gameWorld, this);

        // setup client
        this.isHost = isHost;
        if (isHost) {
            serverController = new ServerController(this);
            serverController.start();
        } else {
            clientController = new ClientController(this, ipAddress);
            clientController.start();
        }

        // hook the mouse
        Mouse.setGrabbed(true);

        try {
            while (!READY) {
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.err.println("Failed to sleep Game thread");
        }

        // start the game
        doGame();
    }

    /**
     * Main game loop where all the goodness happens. Handles calling rendering
     * methods and processing user actions
     */
    private void doGame() {
        // set up audio for game
        AudioController.stopMenuLoop();

        // checks which audio should play
        if (GameWorld.isOutside()) {
            AudioController.playGameWorldLoop();
        } else {
            AudioController.playOfficeLoop();
        }

        // main game loop
        while (!Display.isCloseRequested() && RUNNING) {
            // handle disconnected connections
            if (NETWORK_DISCONNECTED) {
                handleDisconnection();
                break;
            }

            // process the terrains
            renderer.processTerrain(gameWorld.getTerrain());

            // PROCESS PLAYER
            for (Player player : gameWorld.getAllPlayers().values()) {
                if (player.getUID() != gameWorld.getPlayer().getUID()) {
                    renderer.processEntity(player);
                }
            }

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

            // handle user actions
            actionController.processActions();

            // update the players position in the world
            if (!gameWorld.getInventory().isVisible() && !gameWorld.isHelpVisible()) {
                gameWorld.getPlayer().move(gameWorld.getTerrain(), statics);
            }

            // decrease patch progress as time passes
            gameWorld.decreasePatch();

            // Render the player's view
            renderer.render(gameWorld.getLights(), gameWorld.getPlayer().getCamera());

            // render the gui
            guiRenderer.render(gameWorld.getGuiImages());

            // Render the inventory
            if (gameWorld.getInventory().isVisible()) {
                guiRenderer.render(gameWorld.getInventory().getTextureList());
            } else {
                // only show e to interact message if inventory is not open
                for (MovableEntity e : gameWorld.withinDistance().values()) {
                    guiRenderer.render(gameWorld.eInteractMessage());
                }
            }

            // display any helper messages that have been triggered
            guiRenderer.render(gameWorld.displayMessages());

            // render help menu, if it has been requested
            if (gameWorld.isHelpVisible()) {
                guiRenderer.render(gameWorld.helpMessage());
            }

            // check if game has ended
            if (gameWorld.getGameState() > -1) {
                guiRenderer.render(gameWorld.getEndStateScreen());
            }

            // Increment the time
            TimeController.tickTock();

            // update the Display window
            DisplayManager.updateDisplay();
        }

        // Finally clean up resources
        cleanUp();
    }

    /**
     * Cleans up the game when it is closed
     */
    public void cleanUp() {
        guiRenderer.cleanUp();
        renderer.cleanUp();
        Loader.cleanUp();

        // Destroys the display
        DisplayManager.closeDisplay();

        // Cleans Audio resources
        AL.destroy();

        if (isHost) {
            serverController.terminate();
        } else {
            clientController.terminate();
        }
    }

    /**
     * @return if this controller is the host
     */
    public boolean isHost() {
        return isHost;
    }

    /**
     * Create player.
     *
     * @param uid the uid
     */
    public void createPlayer(int uid) {
        gameWorld.addNewPlayer(GameWorld.OFFICE_SPAWN_POSITION, uid);
        playerCount++;
    }

    /**
     * Creates a ClientPlayer
     *
     * @param uid userID
     * @param b   isHost
     */
    public void createPlayer(int uid, boolean b) {
        gameWorld.addPlayer(GameWorld.OFFICE_SPAWN_POSITION, uid);
        playerCount++;
    }

    /**
     * Remove player.
     *
     * @param uid the uid
     */
    public void removePlayer(int uid) {
        gameWorld.getAllPlayers().remove(uid);
        GameWorld.setGuiMessage("aPlayerHasLeftTheGame", 2000);
        playerCount--;
    }

    /**
     * Sends a network update to the corresponding controller
     *
     * @param status type of update
     * @param entity entity that was updated
     */
    public void setNetworkUpdate(int status, MovableEntity entity) {
        if (!isHost()) {
            clientController.setNetworkUpdate(status, entity);
        } else {
            serverController.setNetworkUpdate(status, entity);
        }

    }

    /**
     * Shows "You have Been Disconnected Screen". (Handles disconnections in a
     * graceful way by informing client that serve has disconnected first)
     */
    private void handleDisconnection() {
        while (true) {
            guiRenderer.render(gameWorld.getDisconnectedScreen());
            DisplayManager.updateDisplay();

            // wait for client to acknowledge failed connection
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                break;
            }
        }
    }

    /**
     * Game size int.
     *
     * @return the int
     */
    public int gameSize() {
        return playerCount;
    }

    /**
     * Gets game world.
     *
     * @return the game world
     */
    public GameWorld getGameWorld() {
        return gameWorld;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public Map<Integer, Player> getPlayers() {
        return gameWorld.getAllPlayers();
    }

    /**
     * Gets player with id.
     *
     * @param uid the uid
     * @return the player with id
     */
    public Player getPlayerWithID(int uid) {
        return gameWorld.getAllPlayers().get(uid);
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return gameWorld.getPlayer();
    }

}
