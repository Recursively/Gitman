package model;

import model.entities.Camera;
import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.*;
import model.factories.*;
import model.guiComponents.Inventory;
import model.models.TexturedModel;
import model.terrains.Terrain;
import model.textures.GuiTexture;
import model.textures.ModelTexture;
import model.toolbox.Loader;
import model.toolbox.OBJLoader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import controller.GameController;

import java.util.*;

/**
 * Delegate class used to represent all the current components of the game
 * world.
 *
 * @author Marcel van Workum
 * @author Divya
 */
public class GameWorld {
	private static final int MAX_PROGRESS = 100;
	private static final int START_PATCH = 10; // starting progress value for
												// patch
	private static final double PATCH_DECREASE = 0.1; // percent to decrease
														// patch progress
	private static final double PATCH_TIMER = 5000; // FIXME currently is 5
													// seconds
	private static final int AVG_COMMIT_COLLECT = 5; // number of commits each
														// player should collect
														// on average...
	private static final int CODE_VALUE = 20; // value to increment code
												// progress by (5 clones
												// required)
	private static final int INTERACT_DISTANCE = 30; // max distance player can
														// be from entity and
														// still interact with
														// it

	// Object creation factories
	private EntityFactory entityFactory;
	private TerrainFactory terrainFactory;
	private LightFactory lightFactory;
	private GuiFactory guiFactory;
	private PlayerFactory playerFactory;

	// Collection of guiImages to render to the screen
	private ArrayList<GuiTexture> guiImages;

	// collection of entities in the game
	private ArrayList<Entity> staticEntities;
	private Map<Integer, MovableEntity> movableEntities;

	// Terrain the world is on
	// TODO this will need to become a list once we have multiple terrains
	private Terrain terrain;
	private Terrain otherTerrain;

	// The actual player
	private Player player;

	// Collection of other players stored separately
	private Map<Integer, Player> allPlayers;

	// Constant sun light-source
	private Light sun;

	// Collection of attenuating light-sources
	private ArrayList<Light> lights;

	// object file loader
	private Loader loader;

	// reference to the gameController
	private GameController gameController;

	// game state elements
	private Inventory inventory;
	private int codeProgress; // code collection progress
	private int patchProgress; // commit collection progress
	private int score; // overall score
	private boolean inProgram;
	private boolean canApplyPatch;
	private long timer;
	private Set<SwipeCard> cards;
	private TexturedModel playerModel;

	/**
	 * Creates the game world and passes in the loader
	 *
	 * @param loader
	 *            loader
	 */
	public GameWorld(Loader loader, GameController gameController) {
		this.loader = loader;
		this.gameController = gameController;
	}

	/**
	 * Initialises the game by setting up the lighting, factories and terrain
	 * 
	 * @param isHost
	 */
	public void initGame(boolean isHost) {
		// initialise factories and data structures
		initFactories();
		initDataStructures();

		// creates the gui to be displayed on the display
		initGui();

		// initialises the terrain //TODO this will need to support multi
		// terrain at some point.
		initTerrain();

		entityFactory = new EntityFactory(loader, terrain);

		// Adds lighting to game world
		setupLighting();

		initPlayerModel();

		staticEntities = entityFactory.getTestEntities();

		// game state
		inventory = new Inventory(guiFactory);
		this.patchProgress = START_PATCH;
		this.cards = new HashSet<SwipeCard>();
		this.inProgram = false;
		this.canApplyPatch = false;
	}

	/**
	 * Adds the light sources to the game worlds list of lights to be rendered
	 */
	private void setupLighting() {
		sun = lightFactory.createSun();
		lights.add(sun);

		// TODO remove
		for (Light l : lightFactory.getLights()) {
			lights.add(l);
		}

		lights.addAll(LightFactory.getStaticEntityLights());
	}

	/**
	 * initialises the Gui to be rendered to the display
	 */
	private void initGui() {
		// TODO should init some gui with score, progress and cards collected.
		// TODO fix this...maybe make it smaller...different design?
		// this shoudl create the basic background, if there is one.
		guiImages
				.add(guiFactory.makeGuiTexture("panel_brown", new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f)));
	}

	/**
	 * Initialises all the terrains of the gameworld
	 */
	private void initTerrain() {
		terrain = terrainFactory.makeTerrain(0, -1);
		otherTerrain = terrainFactory.makeTerrain(2, 2);
	}

	/**
	 * initialises the data structures which hold all of the world data
	 */
	private void initDataStructures() {
		guiImages = new ArrayList<>();
		staticEntities = new ArrayList<>();
		movableEntities = new HashMap<>();
		allPlayers = new HashMap<>();
		lights = new ArrayList<>();

	}

	/**
	 * initialises the factories
	 */
	private void initFactories() {
		playerFactory = new PlayerFactory(this, loader);
		lightFactory = new LightFactory();
		terrainFactory = new TerrainFactory(loader);
		guiFactory = new GuiFactory(loader);
	}

	/**
	 * Gets lights.
	 *
	 * @return the lights
	 */
	public ArrayList<Light> getLights() {
		return lights;
	}

	/**
	 * Gets player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets gui images.
	 *
	 * @return the gui images
	 */
	public ArrayList<GuiTexture> getGuiImages() {
		updateGui();
		return guiImages;
	}

	/**
	 * Gets terrain.
	 *
	 * @return the terrain
	 */
	public Terrain getTerrain() {
		return terrain;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	public ArrayList<Entity> getStaticEntities() {
		return staticEntities;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the otherPlayers
	 */
	public Map<Integer, Player> getAllPlayers() {
		return allPlayers;
	}

	public Map<Integer, MovableEntity> getMoveableEntities() {
		return movableEntities;
	}

	public Set<SwipeCard> getSwipeCards() {
		return this.cards;
	}

	public boolean canApplyPatch() {
		return this.canApplyPatch;
	}

	private void updateGui() {
		// TODO like init gui, but with current score, progress and cards
		// collected
		int progress = this.inProgram ? this.patchProgress : this.codeProgress;
	}

	/**
	 * Find item that player is trying to interact with and then carry out
	 * interaction
	 */
	public void interactWithMovEntity() {
		if (inventory.isVisible())
			return;
		MovableEntity entity = findMovEntity(player.getCamera());
		if (entity != null) {
			int type = entity.interact(this);
			sendInteraction(type, entity);
		}
		// TODO for reuben! :)
		// at end of this method there are changes to:
		// storageUsed in Inventory
		// movableEnities map in GameWorld
		// inLaptop list in Inventory
		// swipeCards list in GameWorld
		// codeProgress in GameWorld
		// patchProgress in GameWOrld
		// score in GameWorld
	}

	private void sendInteraction(int type, MovableEntity entity) {
		gameController.setNetworkUpdate(type, entity);
	}

	/**
	 * Go through all movable entities and find the movable entity that is the
	 * closest to the player, and also within the players field of view.
	 * 
	 * @param playerPos
	 *            position of player
	 * @return closest movable entity found
	 */
	public MovableEntity findMovEntity(Camera camera) {
		MovableEntity closest = null;
		double closestDiff = INTERACT_DISTANCE * INTERACT_DISTANCE;

		// get position of player
		float px = camera.getPosition().getX();
		float pz = camera.getPosition().getZ();

		for (MovableEntity e : this.allPlayers.values()) {
			// check that entity is 'interactable'
			if (!e.canInteract()) {
				continue;
			}

			float ex = e.getPosition().getX();
			float ez = e.getPosition().getZ();
			double diff = (ex - px) * (ex - px) + (ez - pz) * (ez - pz);

			// update closest entity if e is within max interacting distance
			// and in front of the player (within view of player)
			if (diff <= closestDiff && Entity.isInFrontOfPlayer(e.getPosition(), camera)) {
				closest = e;
				closestDiff = diff;
			}
		}
		return closest;
	}

	/**
	 * Remove a movable entity from the game
	 * 
	 * @param entity
	 *            to remove
	 */
	public void removeMovableEntity(MovableEntity entity) {
		movableEntities.remove(entity.getUID());
	}

	public void addCommit() {
		// TODO creates and adds a new commit to the array list of movable
		// entities

	}

	/**
	 * Add the given item to the inventory
	 * 
	 * @param item
	 *            to add
	 * @return true if add is successful
	 */
	public boolean addToInventory(LaptopItem item) {
		if (this.inventory.addItem(item)) {
			this.removeMovableEntity(item);
			return true;
		}
		// TODO display message that inventory is too full and player must
		// delete an item first
		// TODO (Message on how to delete: right click to select and X to
		// delete)
		return false;
	}

	/**
	 * Remove the given item from the inventory, and drop the item at the player
	 * position
	 * 
	 * @param item
	 *            to remove
	 * @param playerPosition
	 *            position to drop item at
	 * @return true if remove was successful
	 */
	public void removeFromInventory(LaptopItem item) {
		if (item != null) {
			item.setPosition(player.getPosition());
			this.movableEntities.put(item.getUID(), item);
		}
	}

	/**
	 * Add card to list of swipe cards
	 * 
	 * @param swipeCard
	 */
	public void addCard(SwipeCard swipeCard) {
		this.cards.add(swipeCard);
	}

	/**
	 * Decreases patch progress bar steadily by 10% of current progress
	 * 
	 */
	public void decreasePatch() {
		// if not in outside area, do nothing
		if (!inProgram)
			return;

		// decrease patch in relation to how much time has passed since last
		// decrease
		long currentTime = System.currentTimeMillis();
		if (currentTime - this.timer > PATCH_TIMER) {

			if (this.patchProgress >= MAX_PROGRESS) {
				return; // do nothing if reached 100%
			}
			double value = this.patchProgress * PATCH_DECREASE;
			this.patchProgress = (int) (this.patchProgress - value);

			// if patch progress reaches zero, players lose
			if (this.patchProgress <= 0) {
				loseGame();
			}

			// update new time
			this.timer = System.currentTimeMillis();
		}
	}

	/**
	 * Updates patch progress by "commitScore" ( a score that takes into account
	 * how many commits are expected to be collected by each player depending on
	 * the number of players trying to 'fix' the bug)
	 */
	public void incrementPatch() {
		int commitScore = MAX_PROGRESS / ((allPlayers.size() + 1) * AVG_COMMIT_COLLECT);

		this.patchProgress += commitScore;
		// 100% reached, game almost won...display message with last task
		if (this.patchProgress >= MAX_PROGRESS) {
			this.canApplyPatch = true;
			findBugMessage();
		}
	}

	/**
	 * As player collects code into inventory, code progress level increases
	 */
	public void updateCodeProgress() {
		this.codeProgress += CODE_VALUE;
		inventory.increaseStorageUsed(CODE_VALUE);

		// player has cloned all bits of code
		if (this.codeProgress >= MAX_PROGRESS) {
			compileProgram();
		}
	}

	/**
	 * Updates game score (players get points for interacting with items)
	 * 
	 * @param score
	 *            is score of item in game
	 */
	public void updateScore(int score) {
		this.score += score;
	}

	/**
	 * Code progess reached 100 means all bits of code collected. Player is
	 * given the option of multiplayer or single player, and the environment
	 * they are displayed in changes in
	 */
	private void compileProgram() {
		this.inProgram = true;
		this.timer = System.currentTimeMillis(); // start timer

		// TODO display message to show that player has collected all bits of
		// code
		// and what they have to do now (e.g. press enter to continue)
		// move player into different terrian
		// show single vs multiplayer option
		// should create method that deals with decreasing patch progress over
		// time (look at title screen as example)
	}

	/**
	 * Display message to player when they have lost the game
	 */
	private void loseGame() {
		// TODO display lose game message
		// ungrab mouse and message is end of game.
		// can you make it so that pressing enter takes you back to the
		// play/options screen

	}

	/**
	 * Display message to player when they have won the game
	 */
	public void winGame() {
		// TODO display win game message
		// ungrab mouse and message is end of game.
		// can you make it so that pressing enter takes you back to the
		// play/options screen

	}

	private void findBugMessage() {
		// TODO display message to inform user that they
		// now have to find bug and apply patch
		// maybe press enter to remove message

	}

	public void addNewPlayer(Vector3f position, int uid) {
		Player player = playerFactory.makeNewPlayer(position, playerModel, uid);
		allPlayers.put(uid, player);

		System.out.println("ADDED NEW PLAYER, ID: " + uid);
	}

	public void addPlayer(Vector3f position, int uid) {
		player = playerFactory.makeNewPlayer(position, playerModel, uid);
		allPlayers.put(uid, player);

		System.out.println("ADDED THIS PLAYER, ID: " + uid);
	}

	private void initPlayerModel() {
		this.playerModel = new TexturedModel(OBJLoader.loadObjModel("models/orb", loader),
				new ModelTexture(loader.loadTexture("textures/orb")));
		ModelTexture playerTexture = playerModel.getTexture();
		playerTexture.setShineDamper(10);
		playerTexture.setReflectivity(1);
	}

	public ArrayList<Entity> getTestEntity() {
		return entityFactory.getTestEntities();
	}

	public void swapTerrains() {
		Terrain temp = terrain;
		terrain = otherTerrain;
		otherTerrain = temp;
	}

	public void interactBug() {
		// TODO Auto-generated method stub

	}

	public void interactCommit() {
		// TODO Auto-generated method stub

	}

	public void interactDoor() {
		// TODO Auto-generated method stub

	}

	public void interactLaptopItem() {
		// TODO Auto-generated method stub

	}

	public void interactNPCCharacter() {
		// TODO Auto-generated method stub

	}

	public void interactPlayer() {
		// TODO Auto-generated method stub

	}

	public void interactSwipeCard() {
		// TODO Auto-generated method stub

	}

	public void dropMovableEntity() {
		System.out.println("DROPPED");
	}
}
