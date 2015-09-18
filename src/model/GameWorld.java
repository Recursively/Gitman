package model;

import model.entities.Camera;
import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.Player;
import model.factories.*;
import model.terrains.Terrain;
import model.textures.GuiTexture;
import model.toolbox.Loader;

import java.util.ArrayList;

/**
 * Delegate class used to represent all the current components of the game world.
 *
 * @author Marcel van Workum
 */
public class GameWorld {

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
}
