package controller;

import model.GameWorld;
import model.entities.Entity;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.factories.GuiFactory;
import model.toolbox.Loader;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.TrueTypeFont;

import view.DisplayManager;
import view.renderEngine.GuiRenderer;
import view.renderEngine.MasterRenderer;

import java.awt.Font;
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

	private boolean compiled = false;

	public static boolean READY;
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
	private ActionController actionController;

	private final boolean isHost;
	private final String ipAddress;
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
		gameWorld = new GameWorld(loader, this, guiRenderer);
		gameWorld.initGame(isHost);

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
			clientController.run();
		}

		this.networkRunning = true;

		// hook the mouse
		Mouse.setGrabbed(true);

		try {
			while (!READY) {
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
				if (player.getUID() != gameWorld.getPlayer().getUID()) {
					renderer.processEntity(player);
				}
			}

			// TODO Should only get static entities
			ArrayList<Entity> statics = gameWorld.getStaticEntities();
			Map<Integer, MovableEntity> movables = gameWorld.getMoveableEntities();
			Player player = gameWorld.getPlayer();

			// PROCESS ENTITIES// PROCESS ENTITIES
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

			// checks to see if inventory needs to be displayed
			actionController.processActions();

			// update the players position in the world
			// gameWorld.getPlayer().move(gameWorld.getTerrain());
			if (!gameWorld.getInventory().isVisible()) {
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
			}

			// TODO remove this !!
			if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
				if (!compiled) {
					gameWorld.compileProgram();
					compiled = true;
				}
			}

			if (gameWorld.isGameLost()) {
				guiRenderer.render(gameWorld.loseGame());
			}

			// TODO pick up e to interact
			for (MovableEntity e : gameWorld.withinDistance().values()) {
				guiRenderer.render(gameWorld.eInteractMessage(e));
			}

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
		if (isHost) {
			serverController.terminate();
		} else {
			clientController.terminate();
		}
	}

	public boolean isHost() {
		return isHost;
	}

	public void createPlayer(int uid) {
		gameWorld.addNewPlayer(GameWorld.OFFICE_SPAWN_POSITON, uid);
		playerCount++;
	}

	public void createPlayer(int uid, boolean b) {
		gameWorld.addPlayer(GameWorld.OFFICE_SPAWN_POSITON, uid);
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

}
