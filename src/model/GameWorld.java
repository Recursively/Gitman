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

import java.util.ArrayList;

/**
 * Delegate class used to represent all the current components of the game world.
 *
 * @author Marcel van Workum, Divya
 */
public class GameWorld {
	private static final int START_PATCH = 10;   // starting progress value for patch
	private static final int CODE_VALUE = 10;    // value to increment code progress by

    // collection of entities in the game
    private ArrayList<Entity> staticEntities;
    private ArrayList<Entity> movableEntities;

    // Collection of players stored separately
    private ArrayList<Player> players;

    // Camera bound to a player
    private Camera playerCamera;

    // Collection of terrains
    private ArrayList<Terrain> terrains;

    // Constant sun light-source
    private Light sun;

    // Collection of attenuating light-sources
    private ArrayList<Light> lights;

    // Object creation factories
    private EntityFactory entityFactory;
    private TerrainFactory terrainFactory;

    private Loader loader;
    private Terrain terrain;

    private ArrayList<GuiTexture> guiImages;

    private Player player;
    
    // game state elements
    private Inventory inventory;
    private int codeProgress;        // code collection progress
    private int patchProgress;       // commit collection progress
    private Item pickedUp;
    
    // overall score
    private int score;

    public GameWorld(Loader loader) {
        this.loader = loader;
        TerrainFactory terrainFactory = new TerrainFactory(loader);
        terrain = terrainFactory.makeTerrain();
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void initGame() {
        LightFactory lightFactory = new LightFactory();
        lights = lightFactory.getLights();

        GuiFactory guiFactory = new GuiFactory(loader);
        guiImages = guiFactory.getGuiImages();

        PlayerFactory playerFactory = new PlayerFactory(this);
        player = playerFactory.getPlayer();
        
        // game state
        inventory = new Inventory();
        this.patchProgress = START_PATCH;
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
    
    public void decreasePatch(int percent){
    	double value = percent/100;
    	this.patchProgress = (int) (this.patchProgress*(1-value));
    }
    
    public void incrementPatch(int percent){
    	double value = percent/100;
    	this.patchProgress = (int) (this.patchProgress*(1+value));
    }
    
    public void updateCodeProgress(){
    	this.codeProgress = this.codeProgress + CODE_VALUE;
    	
    	// player has cloned all bits of code
    	if(CODE_VALUE >= 100){
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
}
