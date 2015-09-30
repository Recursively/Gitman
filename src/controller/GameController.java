package controller;

import model.GameWorld;
import model.entities.Entity;
import model.entities.movableEntity.Player;
import model.toolbox.Loader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import view.DisplayManager;
import view.renderEngine.GuiRenderer;
import view.renderEngine.MasterRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Controller class to handle the delegations between the Model and View
 * package.
 *
 * Deals with Game logic
 *
 * @author Marcel van Workum
 */
public class GameController {

	public boolean ready;
	public boolean networkRunning;

	// Model
	private final Loader loader;
	private final GameWorld gameWorld;

	// View
	private final MasterRenderer renderer;
	private final GuiRenderer guiRenderer;

	// Controller
	private ClientController clientController;
	private ServerController serverController;

	private final boolean isHost;
	private int playerCount;


	/**
	 * Delegates the creation of the MVC and then starts the game
	 * 
	 * @throws IOException
	 */
	public GameController(boolean isHost, String ipAddress) {

		// initialise model
		loader = new Loader();

		// initialise view
		DisplayManager.createDisplay();
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);

		// initialise the game world
		gameWorld = new GameWorld(loader);
		gameWorld.initGame(isHost);

		// setup client
		this.isHost = isHost;
		if (isHost) {
			serverController = new ServerController(this);
			serverController.start();
		} else {
			clientController = new ClientController(this, ipAddress);
			clientController.start();
		}
		this.networkRunning = true;

		// hook the mouse
		//Mouse.setGrabbed(true);

		try {
			while (!ready) {
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		// start the game
		doGame();
	}

	/**
	 * Main game loop where all the goodness will happen
	 */
	private void doGame() {

		while (!Display.isCloseRequested() && networkRunning) {

			// process the terrains

			renderer.processTerrain(gameWorld.getTerrain());

			// PROCESS PLAYER

			for (Player player : gameWorld.getAllPlayers().values()) {
				if (player.getUid() != gameWorld.getPlayer().getUid()) {
					renderer.processEntity(player);
				}
			}

			// TODO Should only get static entities
			ArrayList<Entity> statics = gameWorld.getTestEntity();

			// PROCESS ENTITIES// PROCESS ENTITIES
			for (Entity e : statics) {
				renderer.processEntity(e);
			}

			// update the players position in the world
			//gameWorld.getPlayer().move(gameWorld.getTerrain());
			gameWorld.getPlayer().move(gameWorld.getTerrain(), statics);

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
	public void cleanUp() {
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		if(isHost){
			serverController.terminate();
		}
		else{
			clientController.terminate();
		}
	}

	public boolean isHost() {
		return isHost;
	}

	public void createPlayer(int uid) {
		gameWorld.addNewPlayer(new Vector3f(50, 100, -50), uid);
		playerCount++;
	}

	public void createPlayer(int uid, boolean b) {
		gameWorld.addPlayer(new Vector3f(50, 100, -50), uid);
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

	public void removePlayer(int uid) {
		gameWorld.getAllPlayers().remove(uid);
	}

	public int gameSize() {
		return playerCount;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

}
