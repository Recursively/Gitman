package model;

import model.entities.Camera;
import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.Player;
import model.factories.EntityFactory;
import model.factories.TerrainFactory;
import model.terrains.Terrain;
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

    public GameWorld(Loader loader) {

    }

}
