package model.entities.movableEntity;

import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a flash drive item in the game. Stores the specific attributes of 
 * the item that make the item different from other laptop items. 
 * 
 * @author Divya
 *
 */
public class FlashDrive extends LaptopItem {   
	private static final int FLASH_DRIVE_SIZE = 30;
	private static int flashDriveScore = 5; 
	
	/**
	 * Instantiates a new FlashDrive
	 * 
	 * @param model the model
	 * @param position of entity
	 * @param rotX x rotation of entity
	 * @param rotY y rotation of entity
	 * @param rotZ z rotation of entity
	 * @param scale size of entity
	 * @param id unique id for networking
	 * @param name name of image file icon to show in inventory
	 */
	public FlashDrive(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, id, name);
	}
	
	/**
	 * Instantiates a new FlashDrive
	 * 
	 * @param model the model
	 * @param position of entity
	 * @param rotX x rotation of entity
	 * @param rotY y rotation of entity
	 * @param rotZ z rotation of entity
	 * @param scale size of entity
	 * @param textureIndex texture index for atlassing
	 * @param id unique id for networking
	 * @param name name of image file icon to show in inventory
	 */
	public FlashDrive(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id, name);
	}
	
	@Override
	public int getScore() {
		if(super.getPickedUpAlready()){
			flashDriveScore = 0;
		}
		return flashDriveScore;
	}

	@Override
	public String viewInfo() {
		return "Flash Drives store special items. Interact with them and see what happens.";
	}

	@Override
	public int getSize() {
		return FLASH_DRIVE_SIZE;
	}
	
	@Override
	public String getType(){
		return "FlashDrive";
	}
}
