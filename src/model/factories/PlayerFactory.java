package model.factories;

import model.GameWorld;
import model.entities.Camera;
import model.entities.movableEntity.Player;
import org.lwjgl.util.vector.Vector3f;

/**
 * Factory class to handle the creation of Players in the game.
 *
 * Handles both the main player and other multilayer players
 *
 * @author Marcel van Workum
 */
public class PlayerFactory {

    // Reference to parent
    private final GameWorld gameWorld;

    /**
     * Construct the factory and set the game world
     *
     * @param gameWorld Game world
     */
    public PlayerFactory(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

    }

    /**
     * Creates a new Main player at a given position
     *
     * @param position position to create the player at
     *
     * @return Player with Camera
     */
    public Player makeNewMainPlayer(Vector3f position) {
        // where on the ground should the player be
        float initialPlayerY = gameWorld.getTerrain().getTerrainHeight(position.getX(), position.getZ());
        position.y = initialPlayerY;

        // New player and camera to follow the player
        return new Player(null, position, 0, 180f, 0, 1, new Camera(initialPlayerY, position));
    }
}
