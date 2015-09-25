package controller;

import model.GameWorld;
import model.entities.Camera;
import model.entities.movableEntity.Player;
import model.models.TexturedModel;
import model.textures.ModelTexture;
import model.toolbox.Loader;
import model.toolbox.OBJLoader;
import model.toolbox.objParser.OBJFileLoader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.omg.CORBA.PRIVATE_MEMBER;

import view.DisplayManager;
import view.renderEngine.GuiRenderer;
import view.renderEngine.MasterRenderer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controller class to handle the delegations between the Model and View
 * package.
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

	// Controller
	private ClientController clientController;
	private ServerController serverController;
	private final boolean isHost;

	/**
	 * Delegates the creation of the MVC and then starts the game
	 * 
	 * @throws IOException
	 */
	public GameController(boolean isHost) {

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
			addHostPlayer();
			serverController = new ServerController(this);
			serverController.start();
		} else {
			clientController = new ClientController(this);
			clientController.start();
		}

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// hook the mouse
		Mouse.setGrabbed(true);

		// start the game
		doGame();
	}

	/**
	 * Main game loop where all the goodness will happen
	 */
	private void doGame() {

		while (!Display.isCloseRequested()) {

			// process the terrains

			renderer.processTerrain(gameWorld.getTerrain());

			// PROCESS PLAYER
			for (Player player : gameWorld.getAllPlayers()) {
				if (player.getUid() != gameWorld.getPlayer().getUid()) {
					renderer.processEntity(player);
				}
			}

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

	public boolean isHost() {
		return isHost;
	}

	public void addHostPlayer() {
		gameWorld.addNewPlayer(new Vector3f(50, 100, -50), 0);
		System.out.println("ADDED PLAYER WITH ID: " + 0);
	}

	public void addClientPlayer(int uid) {
		gameWorld.addNewPlayer(new Vector3f(50, 100, -50), uid);
		System.out.println("ADDED PLAYER WITH ID: " + uid);
	}

	public void addPlayer(int playerID, float[] packet) {
		gameWorld.addNewPlayer(new Vector3f(packet[0], packet[1], packet[2]), playerID);
		System.out.println("ADDED PLAYER WITH ID: " + playerID);
	}

	public void addPlayer() {
		gameWorld.addNewPlayer(new Vector3f(50, 100, -50), gameWorld.getAllPlayers().size());
		System.out.println("ADDED PLAYER WITH ID: " + gameWorld.getAllPlayers().size());
	}

	public ArrayList<Player> getPlayers() {
		return gameWorld.getAllPlayers();
	}

	public Player getPlayerWithID(int uid) {
		for (Player player : gameWorld.getAllPlayers()) {
			if (player.getUid() == uid) {
				return player;
			}
		}
		return null;
	}

	public Player getPlayer() {
		return gameWorld.getPlayer();
	}
}
