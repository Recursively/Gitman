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
	private int codeProgress;
	private boolean isCodeCompiled;
	private boolean isOutside;
	private int patchProgress;
	private int score;
	private boolean canApplyPatch;
	private int commitIndex;
	private long timer;
	private int storageUsed;

	public Data(Vector3f playerPos, float pitch, float roll, float yaw,
			int uid, ArrayList<LaptopItem> inventory,
			ArrayList<MovableEntity> movableEntities,
			ArrayList<SwipeCard> swipeCards, boolean isCodeCompiled,
			boolean isOutside, int codeProgress, int patchProgress, int score,
			boolean canApplyPatch, int commitIndex,
			long timer, int storageUsed) {

		this.playerPos = playerPos;
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = yaw;
		this.uid = uid;
		this.inventory = inventory;
		this.movableEntities = movableEntities;
		this.swipeCards = swipeCards;
		this.isCodeCompiled = isCodeCompiled;
		this.isOutside = isOutside;
		this.codeProgress = codeProgress;
		this.patchProgress = patchProgress;
		this.score = score;
		this.canApplyPatch = canApplyPatch;
		this.commitIndex = commitIndex;
		this.timer = timer;
		this.storageUsed = storageUsed;

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
	
	public boolean isIsCodeCompiled() {
		return isCodeCompiled;
	}

	public boolean isIsOutside() {
		return isOutside;
	}

	public int getCodeProgress() {
		return codeProgress;
	}

	public int getPatchProgress() {
		return patchProgress;
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
}
