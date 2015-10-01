package model.factories;

import model.GameWorld;
import model.entities.Camera;
import model.entities.movableEntity.Player;
import model.models.TexturedModel;
import model.textures.ModelTexture;
import model.toolbox.Loader;
import model.toolbox.OBJLoader;

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
	private final Loader loader;

	/**
	 * Construct the factory and set the game world
	 *
	 * @param gameWorld
	 *            Game world
	 */
	public PlayerFactory(GameWorld gameWorld, Loader loader) {
		this.gameWorld = gameWorld;
		this.loader = loader;

	}

	/**
	 * Creates a new Main player at a given position
	 *
	 * @param position
	 *            position to create the player at
	 *
	 * @return Player with Camera
	 */
	public Player makeNewPlayer(Vector3f position, TexturedModel playerModel, int uid) {
		// where on the ground should the player be
		float initialPlayerY = gameWorld.getTerrain().getTerrainHeight(position.getX(), position.getZ());
		position.y = initialPlayerY;

		System.out.println("CREATED PLAYER WITH ID: " + uid);

		// New player and camera to follow the player
		return new Player(playerModel, position, 0, 180f, 0, 2f, uid, new Camera(initialPlayerY, position));
	}
}
