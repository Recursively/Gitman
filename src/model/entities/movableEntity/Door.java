package model.entities.movableEntity;

import java.util.Set;

import model.GameWorld;
import model.entities.staticEntity.CollidableEntity;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Door extends Item{
	private static final int UNLOCK_DOOR_SCORE = 5;
	
	private boolean locked;
	private int matchingCard;

	public Door(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int id, int cardID) {
		super(model, position, rotX, rotY, rotZ, scale, id);
		this.locked = true;
		this.matchingCard = cardID;
	}
	
	public Door(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id, int cardID) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id);
		this.locked = true;
		this.matchingCard = cardID;
	}

	@Override
	public int interact(GameWorld game) {
		// can only unlock door if it is locked
		if(locked){
			Set<SwipeCard> cards = game.getSwipeCards();
			for(SwipeCard s: cards){
				if(s.matchID(matchingCard)){
					locked = false;
					game.updateScore(UNLOCK_DOOR_SCORE);
					// TODO send message to gameworld that the door at this doors position is no longer locked
					return 3;
				}
			}
			
			// no card found that can unlock door. display message
			unsuccessfulUnlockMessage();
		}
		return -1;
	}

	private void unsuccessfulUnlockMessage() {
		// TODO display message about unsuccesful unlock.
		// maybe have enter key to make message disappear
		
	}

	@Override
	public int getScore() {
		return UNLOCK_DOOR_SCORE;
	}

	@Override
	public String viewInfo() {
		return "The right swipe card can unlock this door";
	}

}
