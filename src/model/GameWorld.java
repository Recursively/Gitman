package model;

import model.entities.Camera;
import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.Player;
import model.factories.*;
import model.terrains.Terrain;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Delegate class used to represent all the current components of the game
 * world.
 *
 * @author Marcel van Workum
 */
public class GameWorld {

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


	public PlayerFactory getPlayerFactory() {
		return playerFactory;
	}
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

        // finally create the player.
        player = playerFactory.makeNewMainPlayer(new Vector3f(50, 100, -50));
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
}
