package model.data;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.SwipeCard;

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

	public Data(Vector3f playerPos, float pitch, float roll, float yaw,
			int uid, ArrayList<LaptopItem> inventory,
			ArrayList<MovableEntity> movableEntities,
			ArrayList<SwipeCard> swipeCards, boolean isProgramCompiled,
			boolean isOutside, int progress, int score,
			boolean canApplyPatch, int commitIndex,
			long timer, int storageUsed, int gameState) {

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

	public Vector3f getPlayerPos() {
		return playerPos;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}

	public float getYaw() {
		return yaw;
	}

	public int getUid() {
		return uid;
	}
	
	public boolean isIsProgramCompiled() {
		return isProgramCompiled;
	}

	public boolean isIsOutside() {
		return isOutside;
	}

	public int getProgress() {
		return progress;
	}

	public int getScore() {
		return score;
	}

	public boolean isCanApplyPatch() {
		return canApplyPatch;
	}

	public int getCommitIndex() {
		return commitIndex;
	}

	public long getTimer() {
		return timer;
	}

	public ArrayList<LaptopItem> getInventory() {
		return inventory;
	}

	public ArrayList<MovableEntity> getMovableEntities() {
		return movableEntities;
	}

	public ArrayList<SwipeCard> getSwipeCards() {
		return swipeCards;
	}

	public int getStorageUsed() {
		return storageUsed;
	}

	public int getGameState() {
		return this.gameState;
	}
}
