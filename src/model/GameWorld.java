package model;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.Item;
import model.entities.movableEntity.Player;
import model.factories.EntityFactory;
import model.factories.GuiFactory;
import model.factories.LightFactory;
import model.factories.PlayerFactory;
import model.factories.TerrainFactory;
import model.guiComponents.Inventory;
import model.models.TexturedModel;
import model.terrains.Terrain;
import model.textures.GuiTexture;
import model.textures.ModelTexture;
import model.toolbox.Loader;
import model.toolbox.OBJLoader;

public class GameWorld {

	private static final int MAX_PROGRESS = 100;
	private static final int START_PATCH = 10;   // starting progress value for patch
	private static final double PATCH_DECREASE = 0.1; // percent to decrease patch progress
	private static final int CODE_VALUE = 20;    // value to increment code progress by (5 clones required)
	
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

    // Collection of multiply players stored separately
    private ArrayList<Player> otherPlayers;

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
    private Item pickedUp;
    private int score;               // overall score

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
     */
    public void initGame() {
        // initialise factories and data structures
        initFactories();
        initDataStructures();

        // Adds lighting to game world
        setupLighting();

        // creates the gui to be displayed on the display
        initGui();

        // initialises the terrain //TODO this will need to support multi terrain at some point.
        initTerrain();
        

		TexturedModel playerModel = new TexturedModel(OBJLoader.loadObjModel("models/player", loader),
				new ModelTexture(loader.loadTexture("textures/white")));
		ModelTexture playerTexture = playerModel.getTexture();
		playerTexture.setShineDamper(10);
		playerTexture.setReflectivity(1);

        

        // finally create the player.
        player = playerFactory.makeNewMainPlayer(new Vector3f(50, 100, -50), playerModel);
    }

    /**
     * Adds the light sources to the game worlds list of lights to be rendered
     */
    private void setupLighting() {
        sun = lightFactory.createSun();
        lights.add(sun);

        //TODO remove
        lightFactory.createTestAttenuatingLights();
        for (Light l : lightFactory.getLights()) {
            lights.add(l);
        }
    }

    /**
     * initialises the Gui to be rendered to the display
     */
    private void initGui() {
        guiImages.add(guiFactory.makeGuiTexture("panel_brown", new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f)));
    }

    /**
     * Initialises all the terrains of the gameworld
     */
    private void initTerrain() {
        terrain = terrainFactory.makeTerrain();
        
        // game state
        inventory = new Inventory();
        this.patchProgress = START_PATCH;
    }

    /**
     * initialises the data structures which hold all of the world data
     */
    private void initDataStructures() {
        guiImages = new ArrayList<>();
        staticEntities = new ArrayList<>();
        movableEntities = new ArrayList<>();
        otherPlayers = new ArrayList<>();
        lights = new ArrayList<>();
    }

    /**
     * initialises the factories
     */
    private void initFactories() {
        entityFactory = new EntityFactory();
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

	public void incrementPatch(int commitScore){
    	this.patchProgress = this.patchProgress + commitScore;
    	
    	// 100% reached, game won
    	if(this.patchProgress >= MAX_PROGRESS){
    		winGame();
    	}
    }

	public void updateCodeProgress(){
    	this.codeProgress = this.codeProgress + CODE_VALUE;
    	
    	// player has cloned all bits of code
    	if(this.codeProgress >= MAX_PROGRESS){
    		compileProgram();
    	}
    }
    
    /**
     * Updates game score (players get points
     * for interacting with items)
     * @param n
     */
    public void updateScore(int n){
    	this.score = this.score + n;
    }
    
    public void pickUpItem(Item i){
    	this.pickedUp = i;
    }
    
    public void dropItem(){
    	this.pickedUp = null;
    }

    // TODO method called when player should be given
    // options to compile and run program
	private void compileProgram() {
		
	}
	
	private void loseGame() {
		// TODO Auto-generated method stub
		
	}
	
	private void winGame() {
		// TODO Auto-generated method stub

	}
	
	public void addNewPlayer(Vector3f position, int uid, TexturedModel playerModel) {
		otherPlayers.add(playerFactory.makeNewPlayer(playerModel, position, uid));
	}
	
	/**
	 * @return the otherPlayers
	 */
	public ArrayList<Player> getOtherPlayers() {
		return otherPlayers;
	}

	/**
	 * @param otherPlayers
	 *            the otherPlayers to set
	 */
	public void setOtherPlayers(ArrayList<Player> otherPlayers) {
		this.otherPlayers = otherPlayers;
	}
}
