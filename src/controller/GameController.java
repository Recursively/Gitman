package controller;

import model.GameWorld;
import model.entities.movableEntity.Player;
import model.models.TexturedModel;
import model.toolbox.Loader;
import model.toolbox.OBJLoader;
import model.toolbox.objParser.OBJFileLoader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.omg.CORBA.PRIVATE_MEMBER;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
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

	// Network stuff
	private ArrayList<Player> players;

	private Player currentPlayer;

	private TexturedModel playerModel;

	/**
	 * Delegates the creation of the MVC and then starts the game
	 * 
	 * @throws IOException
	 */
	public GameController() throws IOException {

		// initialise model
		loader = new Loader();
		players = new ArrayList<>();

		// initialise view
		DisplayManager.createDisplay();
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);

		// initialise the game world
		gameWorld = new GameWorld(loader);
		gameWorld.initGame();

		// hook the mouse
		Mouse.setGrabbed(true);

		// setup client
		//clientController = new ClientController(this);
		//clientController.start();

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
			// for (Player player : players) {
			// if (player.getUid() != currentPlayer.getUid()) {
			// renderer.processEntity(player);
			// }
			// }

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

	public void addPlayerServer() {

		Vector3f playerPosition = new Vector3f(50, 0, -50);
		Player player = new Player(playerModel, playerPosition, 0, 180f, 0, 1, null, players.size());
		players.add(player);
	}

	public void addPlayerClient(int playerID, float[] packet) {

		Vector3f playerPosition = new Vector3f(packet[0], packet[1], packet[2]);
		Player player = new Player(playerModel, playerPosition, 0, 180f, 0, 1, null, playerID);
		players.add(player);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Player getPlayer() {
		return currentPlayer;
	}

}
