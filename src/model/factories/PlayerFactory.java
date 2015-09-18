package model.factories;

import model.GameWorld;
import model.entities.Camera;
import model.entities.movableEntity.Player;
import org.lwjgl.util.vector.Vector3f;

public class PlayerFactory {

    private Player player;

    public PlayerFactory(GameWorld gameWorld) {
        Vector3f playerPosition = new Vector3f(50, 0, -50);
        float initialPlayerY = gameWorld.getTerrain().getTerrainHeight(playerPosition.getX(), playerPosition.getZ());

        // New player and camera to follow the player
        Camera camera = new Camera(initialPlayerY, 10, playerPosition);
        player = new Player(null, playerPosition, 0, 180f, 0, 1, camera);
    }

    public Player getPlayer() {
        return player;
    }
}
