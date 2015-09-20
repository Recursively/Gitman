package model;

import model.entities.Camera;
import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.Item;
import model.entities.movableEntity.Player;
import model.factories.*;
import model.guiComponents.Inventory;
import model.terrains.Terrain;
import model.textures.GuiTexture;
import model.toolbox.Loader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Delegate class used to represent all the current components of the game world.
 *
 * @author Marcel van Workum, Divya
 */
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

    // Camera bound to a player
    private Camera playerCamera;

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

    public GameWorld(Loader loader) {
        this.loader = loader;

        guiImages = new ArrayList<>();
    }

    public void initGame() {
        // initialise all the factories
        initFactories();

        lights = lightFactory.getLights();
        guiImages.add(guiFactory.makeGuiTexture("panel_brown", new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f)));
        terrain = terrainFactory.makeTerrain();
        player = playerFactory.makeNewMainPlayer(new Vector3f(50, 100, -50));
        
        // game state
        inventory = new Inventory();
        this.patchProgress = START_PATCH;
    }

    private void initFactories() {
        entityFactory = new EntityFactory();
        playerFactory = new PlayerFactory(this);
        lightFactory = new LightFactory();
        terrainFactory = new TerrainFactory(loader);
        guiFactory = new GuiFactory(loader);
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<GuiTexture> getGuiImages() {
        return guiImages;
    }

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
}
