package model.data;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import model.entities.Camera;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.entities.movableEntity.SwipeCard;

public class Data {

	private Player player;
	private ArrayList<LaptopItem> inventory;
	private ArrayList<MovableEntity> movableEntities;
	private ArrayList<SwipeCard> swipeCards;

	// gamestate elements
	private static int codeProgress;
	private static int patchProgress;
	private static int score;
	private static boolean inProgram;
	private static boolean canApplyPatch;
	private static int commitIndex;
	private static long timer;

	public Data(Player player, ArrayList<LaptopItem> inventory,
			ArrayList<MovableEntity> movableEntities,
			ArrayList<SwipeCard> swipeCards, int codeProgress,
			int patchProgress, int score, boolean inProgram,
			boolean canApplyPatch, int commitIndex, long timer) {
		this.player = player;
		this.inventory = inventory;
		this.movableEntities = movableEntities;
		this.swipeCards = swipeCards;
		this.codeProgress = codeProgress;
		this.patchProgress = patchProgress;
		this.score = score;
		this.inProgram = inProgram;
		this.canApplyPatch = canApplyPatch;
		this.commitIndex = commitIndex;
		this.timer = timer;
	}

	public static int getCodeProgress() {
		return codeProgress;
	}

	public static int getPatchProgress() {
		return patchProgress;
	}

	public static int getScore() {
		return score;
	}

	public static boolean isInProgram() {
		return inProgram;
	}

	public static boolean isCanApplyPatch() {
		return canApplyPatch;
	}

	public static int getCommitIndex() {
		return commitIndex;
	}

	public static long getTimer() {
		return timer;
	}

	public Player getPlayer() {
		return player;
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
}
