package model.data;

import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.SwipeCard;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Class used to store all data loaded from the xml save file. Data is stored in
 * appropriate objects and collections.
 *
 * @author Finn Kinnear
 */

public class Data {

	// player element
	private Vector3f playerPos;
	private float pitch;
	private float roll;
	private float yaw;
	private int uid;

	// entity elements
	private ArrayList<LaptopItem> inventory;
	private ArrayList<MovableEntity> movableEntities;
	private ArrayList<SwipeCard> swipeCards;

	// gamestate elements
	private boolean isProgramCompiled;
	private boolean isOutside;
	private int progress;
	private int score;
	private boolean canApplyPatch;
	private int commitIndex;
	private long timer;
	private int storageUsed;
	private int gameState;

	/**
	 * Instantiates a new Data.
	 *
	 * @param playerPos       the player pos
	 * @param pitch           the pitch
	 * @param roll            the roll
	 * @param yaw             the yaw
	 * @param uid             the uid
	 * @param inventory       the inventory
	 * @param movableEntities the movable entities
	 * @param swipeCards      the swipe cards
	 * @param isProgramCompiled  the is code compiled
	 * @param isOutside       the is outside
	 * @param progress        the progress
	 * @param score           the score
	 * @param canApplyPatch   the can apply patch
	 * @param commitIndex     the commit index
	 * @param timer           the timer
	 * @param storageUsed     the storage used
	 * @param gameState       the game state
	 */
	public Data(Vector3f playerPos, float pitch, float roll, float yaw, int uid, ArrayList<LaptopItem> inventory,
			ArrayList<MovableEntity> movableEntities, ArrayList<SwipeCard> swipeCards, boolean isProgramCompiled,
			boolean isOutside, int progress, int score, boolean canApplyPatch, int commitIndex, long timer,
				int storageUsed, int gameState) {
		this.playerPos = playerPos;
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = yaw;
		this.uid = uid;
		this.inventory = inventory;
		this.movableEntities = movableEntities;
		this.swipeCards = swipeCards;
		this.isProgramCompiled = isProgramCompiled;
		this.isOutside = isOutside;
		this.progress = progress;
		this.score = score;
		this.canApplyPatch = canApplyPatch;
		this.commitIndex = commitIndex;
		this.timer = timer;
		this.storageUsed = storageUsed;
		this.gameState = gameState;
	}

	/**
	 * Gets player pos.
	 *
	 * @return the player pos
	 */
	public Vector3f getPlayerPos() {
		return playerPos;
	}

	/**
	 * Gets pitch.
	 *
	 * @return the pitch
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * Gets roll.
	 *
	 * @return the roll
	 */
	public float getRoll() {
		return roll;
	}

	/**
	 * Gets yaw.
	 *
	 * @return the yaw
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * Gets uid.
	 *
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * Is is program compiled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isIsProgramCompiled() {
		return isProgramCompiled;
	}

	/**
	 * Is is outside boolean.
	 *
	 * @return the boolean
	 */
	public boolean isIsOutside() {
		return isOutside;
	}

	/**
	 * Gets progress.
	 *
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * Gets score.
	 *
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Is can apply patch boolean.
	 *
	 * @return the boolean
	 */
	public boolean isCanApplyPatch() {
		return canApplyPatch;
	}

	/**
	 * Gets commit index.
	 *
	 * @return the commit index
	 */
	public int getCommitIndex() {
		return commitIndex;
	}

	/**
	 * Gets timer.
	 *
	 * @return the timer
	 */
	public long getTimer() {
		return timer;
	}

	/**
	 * Gets inventory.
	 *
	 * @return the inventory
	 */
	public ArrayList<LaptopItem> getInventory() {
		return inventory;
	}

	/**
	 * Gets movable entities.
	 *
	 * @return the movable entities
	 */
	public ArrayList<MovableEntity> getMovableEntities() {
		return movableEntities;
	}

	/**
	 * Gets swipe cards.
	 *
	 * @return the swipe cards
	 */
	public ArrayList<SwipeCard> getSwipeCards() {
		return swipeCards;
	}

	/**
	 * Gets storage used.
	 *
	 * @return the storage used
	 */
	public int getStorageUsed() {
		return storageUsed;
	}

	/**
	 * Gets game state.
	 *
	 * @return the game state
	 */
	public int getGameState() {
		return this.gameState;
	}
}
