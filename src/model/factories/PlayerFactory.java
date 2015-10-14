package model.factories;

import model.GameWorld;
import model.data.Data;
import model.entities.Camera;
import model.entities.movableEntity.Player;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Factory class to handle the creation of Players in the game.
 * <p/>
 * Handles both the main player and other multilayer players
 *
 * @author Marcel van Workum
 * @author Reuben Puketapu
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
     * @param position    position to create the player at
     * @param playerModel the player model
     * @param uid         the uid
     * @param load        the load
     * @return Player with Camera
     */
    public Player makeNewPlayer(Vector3f position, TexturedModel playerModel, int uid, Data load) {
        // where on the ground should the player be
        float initialPlayerY = gameWorld.getTerrain().getTerrainHeight(position.getX(), position.getZ());
        position.y = initialPlayerY;

        // New player and camera to follow the player
        Player player = new Player(playerModel, position, 0, 180f, 0, 2f, uid, new Camera(initialPlayerY, position));

        if (load != null) {
            player.getCamera().setPitch((int) load.getPitch());
            player.getCamera().setYaw((int) load.getYaw());
        }
        return player;
    }
}
