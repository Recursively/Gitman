package controller;

import model.GameWorld;
import model.entities.Camera;
import model.entities.movableEntity.Player;
import model.models.TexturedModel;
import model.textures.ModelTexture;
import model.toolbox.Loader;
import model.toolbox.OBJLoader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import view.DisplayManager;
import view.renderEngine.GuiRenderer;
import view.renderEngine.MasterRenderer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller class to handle the delegations between the Model and View
 * package.
 *
 * Deals with network logic
 *
 * @author Marcel van Workum
 */
public class NetworkController {

	// Model
	private final Loader loader;
	private final GameWorld gameWorld;

	// View
	private final MasterRenderer renderer;
	private final GuiRenderer guiRenderer;

	// Controller
	private ServerController serverController;

	// Network stuff
	private ArrayList<Player> players;

	private TexturedModel playerModel;

	/**
	 * Delegates the creation of the MVC and then starts the game
	 * 
	 * @throws IOException
	 */
	public NetworkController() {

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

		// setup server
		serverController = new ServerController(this);
		serverController.start();
		players.add(gameWorld.getPlayer());
		System.out.println("SERVER");

		playerModel = new TexturedModel(OBJLoader.loadObjModel("models/player", loader),
				new ModelTexture(loader.loadTexture("textures/white")));
		ModelTexture playerTexture = playerModel.getTexture();
		playerTexture.setShineDamper(10);
		playerTexture.setReflectivity(1);

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
			for (Player player : players) {
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

	public void addPlayerServer() {

		Vector3f position = new Vector3f(50, 100, -50);

		float initialPlayerY = gameWorld.getTerrain().getTerrainHeight(position.getX(), position.getZ());
		position.y = initialPlayerY;

		Player player = new Player(playerModel, position, 0, 180f, 0, 1, new Camera(initialPlayerY, position),
				players.size());
		players.add(player);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Player getPlayer() {
		return gameWorld.getPlayer();
	}

}
