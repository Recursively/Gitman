package model;

import controller.AudioController;
import controller.GameController;
import model.data.Data;
import model.data.Load;
import model.entities.Camera;
import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.*;
import model.factories.*;
import model.guiComponents.GuiMessages;
import model.guiComponents.Inventory;
import model.terrains.Terrain;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import view.renderEngine.MasterRenderer;

import java.util.*;

/**
 * Delegate class used to represent all the current components of the game
 * world.
 *
 * @author Marcel van Workum
 * @author Divya
 */
public class GameWorld {
	public static final int GAME_WIN = 1;
	public static final int CODE_VALUE = 20;
	public static final int MAX_PROGRESS = 100;

	private static final int START_PATCH = 10; // starting patch progress value												
	private static final double PATCH_DECREASE = 0.1; 
	private static final double PATCH_TIMER = 100000;  // time before decrease 
	private static final int AVG_COMMIT_COLLECT = 5; // by each player  
	
	private static final float Y_OFFSET = 2; // y offset to place deleted items
	public static final Vector3f SPAWN_POSITION = new Vector3f(30, 100, -20);
	public static final Vector3f OFFICE_SPAWN_POSITION = new Vector3f(128060, 100, -127930);

	// need to update y position when initialised
	private static final Vector3f OUTSIDE_PORTAL_POSITION = new Vector3f(6, 19, -35);
	public static final int PORTAL_LOWER_BOUND_OUTSIDE_Z = -30;
	public static final int PORTAL_UPPER_BOUND_OUTSIDE_Z = -40;
	public static final int PORTAL_EDGE_BOUND_OUTSIDE_X = 12;

	private static final Vector3f OFFICE_PORTAL_POSITION = new Vector3f(128011f, 0, -127930);
	public static final int PORTAL_LOWER_BOUND_OFFICE_Z = -127920;
	public static final int PORTAL_UPPER_BOUND_OFFICE_Z = -127940;
	public static final int PORTAL_EDGE_BOUND_OFFICE_X = 128012;


	private static float WORLD_TIME = 0;
	private static boolean isProgramCompiled = false;
	private static boolean isOutside = false;

	// Object creation factories
	private EntityFactory entityFactory;
	private TerrainFactory terrainFactory;
	private LightFactory lightFactory;
	private GuiFactory guiFactory;
	private PlayerFactory playerFactory;

	// Collection of guiImages to render to the screen
	private List<GuiTexture> guiImages;
	private static GuiMessages guiMessages;

	// collection of entities in the game
	private ArrayList<Entity> staticEntities;
	private Map<Integer, MovableEntity> movableEntities;
	private ArrayList<SwipeCard> cards;

	// Terrain the world is on

	private static Terrain currentTerrain;
	private static Terrain otherTerrain;

	// The actual player
	private static Player player;

	// Collection of other players stored separately
	private Map<Integer, Player> allPlayers;

	// Constant sun light-source
	private static Light sun;
	private static Light blackHoleSun;
	private Light officeLight;

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
	private int commitIndex;
	private long timer;
	private int interactDistance;
	
	// game state
	private int gameState; // -1 is playing. 0 is lost. 1 is won
	private boolean helpVisible;

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
	 * Initialises the game by setting up the lighting, factories and currentTerrain
	 *
	 * @param isHost
	 */
	public void initGame(boolean isHost, boolean load) {
		// initialise factories and data structures
		initFactories();
		initDataStructures();

		// creates the gui to be displayed on the display
		initGui();

		// initialises the currentTerrain
		// currentTerrain at some point.
		initTerrain();

		entityFactory = new EntityFactory(loader, otherTerrain, currentTerrain);

		// Adds lighting to game world
		setupLighting();

		initPlayerModel();

		staticEntities = entityFactory.getEntities();
		
		if(!load){
			movableEntities = entityFactory.getMovableEntities();

			// game state
			inventory = new Inventory(guiFactory);
			this.patchProgress = START_PATCH;
			this.codeProgress = 0;
			this.cards = new ArrayList<SwipeCard>();
			this.inProgram = false;
			this.canApplyPatch = false;			
		}
		else {
			initLoadGame(Load.loadGame());
		}

		this.helpVisible = false;
		this.gameState = -1;
		this.interactDistance = 15;
		this.commitIndex = 10;   // start with 10 commits
		staticEntities.add(entityFactory.makePortal(OUTSIDE_PORTAL_POSITION, currentTerrain));
		
		// create commits
		initCommits();
	}
	
	public void initLoadGame(Data load) {
		movableEntities = new HashMap<Integer, MovableEntity>();
		for(MovableEntity e : load.getMovableEntities()){
			this.movableEntities.put(e.getUID(), e);
		} 

		// game state
		inventory = new Inventory(guiFactory);
		this.patchProgress = load.getPatchProgress();
		this.codeProgress = load.getCodeProgress(); 
		this.cards = load.getSwipeCards();
		this.inProgram = load.isInProgram();  
		this.canApplyPatch = load.isCanApplyPatch();
		inventory.setStorageUsed(load.getStorageUsed());
	}

	private void initCommits() {
		int count = 0;
		for (Vector3f position : entityFactory.getCommitPositions()) {
			if (count == 10) break;
			Commit newCommit = EntityFactory.createCommit(position);
			this.movableEntities.put(newCommit.getUID(), newCommit);
			count++;
		}
	}

	/**
	 * Adds the light sources to the game worlds list of lights to be rendered
	 */
	private void setupLighting() {
		sun = lightFactory.createSun();
		blackHoleSun = lightFactory.createSun();
		officeLight = lightFactory.createOfficeLight();
		lights.add(officeLight);

		lights.addAll(LightFactory.getStaticEntityLights());
	}

	/**
	 * initialises the Gui to be rendered to the display
	 */
	private void initGui() {
		guiImages = new ArrayList<GuiTexture>();
		guiImages = guiFactory.getInfoPanel();
		guiMessages = new GuiMessages(guiFactory);

	}

	/**
	 * Initialises all the terrains of the gameworld
	 */
	private void initTerrain() {
		otherTerrain = terrainFactory.makeOutsideTerrain(0, -1);
		currentTerrain = terrainFactory.makeOfficeTerrain(1000, -1000);
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
		playerFactory = new PlayerFactory(this);
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
		ArrayList<Light> collectionOfLights = new ArrayList<>();
		if (isOutside) {
			collectionOfLights.add(sun);
		} else {
			collectionOfLights.add(blackHoleSun);
		}


		ArrayList<Light> possibleLights = new ArrayList<>();
		possibleLights.add(officeLight);
		possibleLights.addAll(lights);

		Collections.sort(possibleLights);

		for (int i = 0; i < 4; i++) {
			collectionOfLights.add(possibleLights.get(i));
		}

		return collectionOfLights;
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
	public List<GuiTexture> getGuiImages() {
		updateGui();
		return guiImages;
	}

	/**
	 * Gets currentTerrain.
	 *
	 * @return the currentTerrain
	 */
	public Terrain getTerrain() {
		return currentTerrain;
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
		GameWorld.player = player;
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

	public ArrayList<SwipeCard> getSwipeCards() {
		return this.cards;
	}

	public int getGameState() {
		return this.gameState;
	}

	public boolean canApplyPatch() {
		return this.canApplyPatch;
	}

	public boolean isHelpVisible() {
		return this.helpVisible;
	}

	public void updateGui() {
		int progress = this.inProgram ? this.patchProgress : this.codeProgress;
		this.guiImages = this.guiFactory.getInfoPanel();
		this.guiImages.addAll(this.guiFactory.getProgress(progress));
		this.guiImages.addAll(this.guiFactory.getScore(this.score));
		this.guiImages.addAll(this.guiFactory.getSwipeCards(this.cards));
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
	}

	private void sendInteraction(int type, MovableEntity entity) {
		gameController.setNetworkUpdate(type, entity);
	}

	/**
	 * Go through all movable entities and find the movable entity that is the
	 * closest to the player, and also within the players field of view.
	 *
	 * @return closest movable entity found
	 */
	public MovableEntity findMovEntity(Camera camera) {
		MovableEntity closest = null;
		double closestDiff = interactDistance * interactDistance;

		for(Map.Entry<Double, MovableEntity> e : this.withinDistance().entrySet()){
			if(e.getKey() <= closestDiff){
				closestDiff = e.getKey();
				closest = e.getValue();
			}
		}
		return closest;
	}

	public Map<Double, MovableEntity> withinDistance(){
		HashMap<Double, MovableEntity> interactable = new HashMap<Double, MovableEntity>();

		// get position of player
		Camera camera = player.getCamera();
		float px = camera.getPosition().getX();
		float pz = camera.getPosition().getZ();

		for (MovableEntity e : this.movableEntities.values()) {
			// check that entity is 'intractable'
			if (!e.canInteract()) {
				continue;
			}

			float ex = e.getPosition().getX();
			float ez = e.getPosition().getZ();
			double diff = (ex - px) * (ex - px) + (ez - pz) * (ez - pz);

			// if within interactable distance, add to map
			if (diff <= (interactDistance*interactDistance)
					&& Entity.isInFrontOfPlayer(e.getPosition(), camera)) {
				interactable.put(diff, e);
			}
		}
		return interactable;
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
		ArrayList<Vector3f> commitPos = entityFactory.getCommitPositions();
		Commit newCommit = EntityFactory.createCommit(commitPos.get(commitIndex));
		this.movableEntities.put(newCommit.getUID(), newCommit);
		commitIndex++;
		if(commitIndex >= commitPos.size()){
			commitIndex = 0;
		}
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
		this.setGuiMessage("laptopMemoryFull", 3000);
		return false;
	}

	/**
	 * Remove the given item from the inventory, and drop the item at the player
	 * position
	 *
	 * @param item
	 *            to remove
	 * @return true if remove was successful
	 */
	public void removeFromInventory(LaptopItem item) {
		if (item != null) {
			Vector3f playerPos = player.getPosition();
			float y = currentTerrain.getTerrainHeight(playerPos.getX(), playerPos.getZ());
			float scale = item.getScale();
			item.setScale(scale);
			item.setPosition(new Vector3f(playerPos.getX(), y + Y_OFFSET, playerPos.getZ()));
			this.movableEntities.put(item.getUID(), item);
		}
	}

	/**
	 * Remove the given item from the inventory, and drop the item at the player
	 * position
	 *
	 * @param item
	 *            to remove
	 * @return true if remove was successful
	 */
	public void removeFromInventory(LaptopItem item, int playerID) {
		if (item != null) {
			Vector3f playerPos = gameController.getPlayerWithID(playerID).getPosition();
			float y = currentTerrain.getTerrainHeight(playerPos.getX(), playerPos.getZ());
			float scale = item.getScale();
			item.setScale(scale);
			item.setPosition(new Vector3f(playerPos.getX(), y + Y_OFFSET, playerPos.getZ()));
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
				gameState = 0;
				AudioController.playGameOverSound();
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
			this.interactDistance = 40;
			this.setGuiMessage("patchComplete", 3000);
			AudioController.playGameWonLoop();
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

	public void updateCodeProgress() {
		this.codeProgress += CODE_VALUE;
		inventory.increaseStorageUsed(CODE_VALUE);

		// player has cloned all bits of code
		if (this.codeProgress >= MAX_PROGRESS) {
			compileProgram();
		}
	}

	/**
	 * Code progress reached 100 means all bits of code collected. Player is
	 * given the option of multiplayer or single player, and the environment
	 * they are displayed in changes in
	 */
	public void compileProgram() {
		this.inProgram = true;  
		this.timer = System.currentTimeMillis(); // start timer
		this.interactDistance = 20;
		this.setGuiMessage("codeCompiledMessage", 5000);

		// adds the portal to the game
		officeLight.setColour(new Vector3f(6, 1, 1));
		staticEntities.add(entityFactory.makePortal(OFFICE_PORTAL_POSITION, currentTerrain));
		GameWorld.isProgramCompiled = true;

		AudioController.playPortalHum();
	}

	public List<GuiTexture> getEndStateScreen() {
		if(this.gameState == GAME_WIN){
			return guiFactory.getWinScreen();
		}
		else{
			return guiFactory.getLostScreen();
		}
	}

	public void addNewPlayer(Vector3f position, int uid) {
		Player player = playerFactory.makeNewPlayer(position, EntityFactory.getPlayerTexturedModel(), uid);
		allPlayers.put(uid, player);

		System.out.println("ADDED NEW PLAYER, ID: " + uid);
	}

	public void addPlayer(Vector3f position, int uid) {
		player = playerFactory.makeNewPlayer(position, EntityFactory.getPlayerTexturedModel(), uid);
		allPlayers.put(uid, player);

		System.out.println("ADDED THIS PLAYER, ID: " + uid);
	}

	private void initPlayerModel() {
		EntityFactory.initPayerModel(loader);
	}

	/**
	 * Swaps out the terrains for the players game world
	 */
	public static void teleportToOutside() {
		Terrain temp = currentTerrain;
		currentTerrain = otherTerrain;
		otherTerrain = temp;
		player.setCurrentTerrain(currentTerrain);
		player.getPosition().x = SPAWN_POSITION.getX();
		player.getPosition().z = SPAWN_POSITION.getZ();
		player.getCamera().changeYaw(160f);
		MasterRenderer.setRenderSkybox(true);
		isOutside = true;
		AudioController.stopPortalHum();
		AudioController.playPortalSound();
	}

	public static void telportToOffice() {
		Terrain temp = currentTerrain;
		currentTerrain = otherTerrain;
		otherTerrain = temp;
		player.setCurrentTerrain(currentTerrain);
		player.getPosition().x = OFFICE_SPAWN_POSITION.getX();
		player.getPosition().z = OFFICE_SPAWN_POSITION.getZ();
		player.getCamera().changeYaw(180f);
		MasterRenderer.setRenderSkybox(false);
		isOutside = false;
		AudioController.playPortalHum();
		AudioController.playPortalSound();
	}



	public void displayHelp() {
		if(this.helpVisible){
			this.helpVisible = false;
			Mouse.setGrabbed(true);
		}
		else {
			this.helpVisible = true;
			Mouse.setGrabbed(false);
		}
	}
	public List<GuiTexture> eInteractMessage(MovableEntity e) {
		return guiFactory.getPopUpInteract(e.getPosition());
	}

	public static boolean isProgramCompiled() {
		return isProgramCompiled;
	}

	public static void setIsProgramCompiled(boolean isProgramCompiled) {
		GameWorld.isProgramCompiled = isProgramCompiled;
	}


	public List<GuiTexture> displayMessages() {
		return guiMessages.getMessages();
	}

	public static void setGuiMessage(String msg, long time) {
		guiMessages.setMessage(msg, time);
	}

	public void setGameState(int state) {
		this.gameState = state;
	}

	public List<GuiTexture> helpMessage() {
		return guiFactory.getHelpScreen();
	}
	
	public int getCodeProgress() {
		return codeProgress;
	}

	public void setCodeProgress(int codeProgress) {
		this.codeProgress = codeProgress;
	}

	public int getPatchProgress() {
		return patchProgress;
	}

	public void setPatchProgress(int patchProgress) {
		this.patchProgress = patchProgress;
	}

	public int getScore() {
		return score;
	}

	public boolean isInProgram() {
		return inProgram;
	}

	public boolean isCanApplyPatch() {
		return canApplyPatch;
	}

	public int getCommitIndex() {
		return commitIndex;
	}

	public long getTimer() {
		return timer;
	}

	public static Vector3f getPlayerPosition() {
		return player.getPosition();
	}

	public static float getWorldTime() {
		return WORLD_TIME;
	}

	public static void increaseTime(float worldTime) {
		WORLD_TIME += worldTime;
		WORLD_TIME %= 24000;
	}

	public static boolean isOutside() {
		return isOutside;
	}

	public static void updateSun() {
		if (GameWorld.getWorldTime() < 5000) {
			sun.setColour(new Vector3f(0, 0, 0));
		} else if (GameWorld.getWorldTime() < 8000) {
			sun.increaseColour(0.0001f, 0.0001f, 0.0001f);
		} else if (GameWorld.getWorldTime() > 21000) {
			sun.decreaseColour(0.0001f, 0.0001f, 0.0001f);
		}

	}
}

