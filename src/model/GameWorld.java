package model;

import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.Item;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.Player;
import model.entities.movableEntity.SwipeCard;
import model.factories.*;
import model.guiComponents.Inventory;
import model.models.TexturedModel;
import model.terrains.Terrain;
import model.textures.GuiTexture;
import model.textures.ModelTexture;
import model.toolbox.Loader;
import model.toolbox.OBJLoader;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

/**
 * Delegate class used to represent all the current components of the game world.
 *
 * @author Marcel van Workum
 * @author Divya
 */
public class GameWorld {
	private static final int MAX_PROGRESS = 100;
	private static final int START_PATCH = 10;   // starting progress value for patch
	private static final double PATCH_DECREASE = 0.1; // percent to decrease patch progress
	private static final int AVG_COMMIT_COLLECT = 5;  // number of commits each player should collect on average...
	private static final int CODE_VALUE = 20;    // value to increment code progress by (5 clones required)
	private static final int ITEM_DISTANCE = 30; //TODO furtherest distance a player can be from an item and still be allowed to interact with it
	
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
    private ArrayList<Entity> movableEntities;

    // Terrain the world is on
    // TODO this will need to become a list once we have multiple terrains
    private Terrain terrain;

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
    
    
    // game state elements
    private Inventory inventory;
    private int codeProgress;        // code collection progress
    private int patchProgress;       // commit collection progress
    private int score;               // overall score
    private Set<SwipeCard> cards;
	private TexturedModel playerModel;

    /**
     * Creates the game world and passes in the loader
     *
     * @param loader loader
     */
    public GameWorld(Loader loader) {
        this.loader = loader;
    }

    /**
     * Initialises the game by setting up the lighting, factories and terrain
     * @param isHost 
     */
    public void initGame(boolean isHost) {
        // initialise factories and data structures
        initFactories();
        initDataStructures();

        // creates the gui to be displayed on the display
        initGui();

		// initialises the terrain //TODO this will need to support multi terrain at some point.
		initTerrain();

		entityFactory = new EntityFactory(loader, terrain);

		// Adds lighting to game world
		setupLighting();

        initPlayerModel();

        staticEntities = entityFactory.getTestEntities();
        
        // game state
        inventory = new Inventory();
        this.patchProgress = START_PATCH;
        this.cards = new HashSet<SwipeCard>();
    }

    /**
     * Adds the light sources to the game worlds list of lights to be rendered
     */
    private void setupLighting() {
        sun = lightFactory.createSun();
        lights.add(sun);

        //TODO remove
        for (Light l : lightFactory.getLights()) {
			lights.add(l);
		}

		lights.addAll(LightFactory.getStaticEntityLights());
    }

    /**
     * initialises the Gui to be rendered to the display
     */
    private void initGui() {
		//TODO should init some gui here maybe?
        //guiImages.add(guiFactory.makeGuiTexture("panel_brown", new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f)));
    }

    /**
     * Initialises all the terrains of the gameworld
     */
    private void initTerrain() {
        terrain = terrainFactory.makeTerrain();
    }

    /**
     * initialises the data structures which hold all of the world data
     */
    private void initDataStructures() {
        guiImages = new ArrayList<>();
        staticEntities = new ArrayList<>();
        movableEntities = new ArrayList<>();
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
     * Find the item that is within ITEM_DISTANCE 
     * of the given position
     * 
     * @param position of player
     * @return closest item to given position, within certain radius
     */
    public Item findItem(Vector3f position) {
		Item item = null;
		Vector3f itemPos = null;
		for(Entity e: this.movableEntities){
			// only check entity if it is an item (i.e. ignore players)
			if(e instanceof Item){
				if(Entity.isCloserThan(e.getPosition(), itemPos, position, ITEM_DISTANCE)){ 
					item = (Item) e;
					itemPos = e.getPosition();
				}
			}
		}
		return item;
	}

    /**
     * Remove a movable entity from the game
     * 
     * @param entity to remove
     */
	public void removeMovableEntity(Entity entity) {
		movableEntities.remove(entity);
	}

	
	public void addCommit() {
		// TODO creates and adds a new commit to the array list of movable entities
		
	}

	/**
	 * Add the given item to the inventory
	 * 
	 * @param item to add
	 * @return true if add is successful
	 */
	public boolean addToInventory(LaptopItem item) {
		if(this.inventory.addItem(item)){
			this.removeMovableEntity(item);
			return true;
		}
		// TODO display message that inventory is too full and player must delete an item first
		return false;
	}
	
	/**
	 * Remove the given item from the inventory, and
	 * drop the item at the player position
	 * 
	 * @param item to remove
	 * @param playerPosition position to drop item at
	 * @return true if remove was successful
	 */
	public boolean removeFromInventory(LaptopItem item, Vector3f playerPosition) {
		//TODO does set position need to be slightly in front of player?
		Entity entity = this.inventory.deleteItem(item);
		if(entity != null){
			entity.setPosition(playerPosition);
			this.movableEntities.add(entity);
			return true;
		}
		return false;
	}

	/**
	 * Add card to list of swipe cards
	 * @param swipeCard
	 */
	public void addCard(SwipeCard swipeCard) {
		this.cards.add(swipeCard);		
	}
    
    /**
     * Decreases patch progress bar steadily by 10% of current
     * progress
     *  
     */
    public void decreasePatch(){
    	if(this.patchProgress >= MAX_PROGRESS){
    		return;  // do nothing if reached 100%
    	}
    	double value = this.patchProgress*PATCH_DECREASE;
    	this.patchProgress = (int) (this.patchProgress - value);
    	
    	// if patch progress reaches zero, players lose
    	if(this.patchProgress <= 0) {
    		loseGame();
    	}
    }

   /**
    * Updates patch progress by "commitScore" ( a score that 
    * takes into account how many commits are expected to be collected
    * by each player depending on the number of players trying to 
    * 'fix' the bug)
    */
	public void incrementPatch(){
		int commitScore = MAX_PROGRESS / ((allPlayers.size() + 1) * AVG_COMMIT_COLLECT);
		
    	this.patchProgress = this.patchProgress + commitScore;
    	// 100% reached, game won
    	if(this.patchProgress >= MAX_PROGRESS){
    		winGame();
    	}
    }

	/**
	 * As player collects code into inventory, code progress 
	 * level increases
	 */
	public void updateCodeProgress(){
    	this.codeProgress = this.codeProgress + CODE_VALUE;
    	
    	// player has cloned all bits of code
    	if(this.codeProgress >= MAX_PROGRESS){
    		compileProgram();
    	}
    }
    
    /**
     * Updates game score (players get points for interacting with items)
     * @param score is score of item in game
     */
    public void updateScore(int score){
    	this.score = this.score + score;
    }
    
	private void compileProgram() {
		// TODO method called when player should be given
	    // options to compile and run program
		// should start PatchTime thread here with delay added in thread before decrease patch  method is called
	}
	
	private void loseGame() {
		// TODO Auto-generated method stub
		
	}
	
	private void winGame() {
		// TODO Auto-generated method stub

	}

    public ArrayList<Entity> getStaticEntities() {
        return staticEntities;
    }
    

	public void setPlayer(Player player) {
		this.player = player;
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

	/**
	 * @return the otherPlayers
	 */
	public Map<Integer, Player> getAllPlayers() {
		return allPlayers;
	}
	
	private void initPlayerModel() {
		this.playerModel = new TexturedModel(OBJLoader.loadObjModel("models/player", loader),
				new ModelTexture(loader.loadTexture("textures/white")));
		ModelTexture playerTexture = playerModel.getTexture();
		playerTexture.setShineDamper(10);
		playerTexture.setReflectivity(1);
	}

	public ArrayList<Entity> getMoveableEntities() {
		return movableEntities;
	}

	public ArrayList<Entity> getTestEntity() {
		return entityFactory.getTestEntities();
	}
	
}
