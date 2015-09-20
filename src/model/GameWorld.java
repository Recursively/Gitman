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

	public GameWorld(Loader loader) {
		this.loader = loader;

		guiImages = new ArrayList<>();
	}

	public void initGame() {
		// initialise all the factories
		initFactories();

		lights = lightFactory.getLights();
		guiImages
				.add(guiFactory.makeGuiTexture("panel_brown", new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f)));
		terrain = terrainFactory.makeTerrain();
		player = playerFactory.makeNewMainPlayer(new Vector3f(50, 100, -50));
	}

	private void initFactories() {
		entityFactory = new EntityFactory();
		playerFactory = new PlayerFactory(this, loader);
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

	public PlayerFactory getPlayerFactory() {
		return playerFactory;
	}
}
