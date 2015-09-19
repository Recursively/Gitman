package model.factories;

import model.GameWorld;
import model.entities.Camera;
import model.entities.movableEntity.Player;
import org.lwjgl.util.vector.Vector3f;

public class PlayerFactory {

    private final GameWorld gameWorld;

    public PlayerFactory(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

    }

    public Player makeNewMainPlayer(Vector3f position) {
        float initialPlayerY = gameWorld.getTerrain().getTerrainHeight(position.getX(), position.getZ());

        // New player and camera to follow the player
        return new Player(null, position, 0, 180f, 0, 1, new Camera(initialPlayerY, position));
    }
}
