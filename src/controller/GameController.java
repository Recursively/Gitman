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
    public static boolean RUNNING;
    public static boolean READY;
    public static boolean NETWORK_DISCONNECTED;

    // Model
    private final Loader loader;
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
    private final String ipAddress;
    private int playerCount;
    
    private boolean compiled = false;

    /**
     * Delegates the creation of the MVC and then starts the game
     *
     * @throws IOException
     */
    public GameController(boolean isHost, String ipAddress, boolean load, boolean fullscreen) {
    	GameController.NETWORK_DISCONNECTED = false;
        GameController.RUNNING = true;

        // initialise model
        loader = new Loader();

        // initialise view
        renderer = new MasterRenderer(loader);
        guiRenderer = new GuiRenderer(loader);

        // initialise the game world
        gameWorld = new GameWorld(loader, this);
        gameWorld.initGame(isHost, load);

        // initialise controller for actions
        actionController = new ActionController(loader, gameWorld, this);

        // setup client
        this.isHost = isHost;
        this.ipAddress = ipAddress;
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
            e.printStackTrace();

        }

        // start the game
        doGame();
    }

    /**
     * Main game loop where all the goodness happens. Handles 
     * calling rendering methods and processing user actions
     */
    private void doGame() {
    	// set up audio for game 
        AudioController.stopMenuLoop();
        if(GameWorld.isOutside()){
        	AudioController.playGameWorldLoop();
        }
        else {
        	AudioController.playOfficeLoop();
        }
     		
        // main game loop 
        while (!Display.isCloseRequested() && RUNNING) {
        	// handle disconnected connections
        	if(NETWORK_DISCONNECTED){
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

            // TODO Should only get static entities
            ArrayList<Entity> statics = gameWorld.getStaticEntities();
            ArrayList<Entity> walls = gameWorld.getWallEntities();
            Map<Integer, MovableEntity> movables = gameWorld.getMoveableEntities();
            Player player = gameWorld.getPlayer();

            gameWorld.rotateCommits();

            // PROCESS ENTITIES
            for (Entity e : statics) {
                if (e.isWithinRange(player)) {
                    renderer.processEntity(e);
                }
            }

            for (MovableEntity e : movables.values()) {
                if (e.isWithinRange(player)) {
                    renderer.processEntity(e);
                }
            }

            for (Entity e : walls) {
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

            if (gameWorld.getInventory().isVisible()) {
                guiRenderer.render(gameWorld.getInventory().getTextureList());

            } else {
                // only show e to interact message if inventory is not open
                for (MovableEntity e : gameWorld.withinDistance().values()) {
                    guiRenderer.render(gameWorld.eInteractMessage(e));
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
        loader.cleanUp();
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

    public void createPlayer(int uid) {
        gameWorld.addNewPlayer(GameWorld.OFFICE_SPAWN_POSITION, uid);
        playerCount++;
    }

    public void createPlayer(int uid, boolean b) {
        gameWorld.addPlayer(GameWorld.OFFICE_SPAWN_POSITION, uid);
        playerCount++;
    }

    public Map<Integer, Player> getPlayers() {
        return gameWorld.getAllPlayers();
    }

    public Player getPlayerWithID(int uid) {
        return gameWorld.getAllPlayers().get(uid);
    }

    public Player getPlayer() {
        return gameWorld.getPlayer();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void removePlayer(int uid) {
        gameWorld.getAllPlayers().remove(uid);
		GameWorld.setGuiMessage("aPlayerHasLeftTheGame", 2000);
        playerCount--;
    }

    public void setNetworkUpdate(int status, MovableEntity entity) {

        if (!isHost()) {
            clientController.setNetworkUpdate(status, entity);
        } else {
            serverController.setNetworkUpdate(status, entity);
        }

    }

    public int gameSize() {
        return playerCount;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public boolean isCompiled() {
        return compiled;
    }

    public void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }
    
    /**
     * Shows "You have Been Disconnected Screen". (Handles 
     * disconnections in a graceful way by informing client 
     * that serve has disconnected first)
     */
    private void handleDisconnection() {
    	while(true){
    		guiRenderer.render(gameWorld.getDisconnectedScreen());
    		DisplayManager.updateDisplay();
    		
    		// wait for client to acknowledge failed connection
    		if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
    			break;
    		}
    	}
	}

}
