package model.entities.movableEntity;

import java.util.ArrayList;

import model.GameWorld;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Divya
 *
 */
public class FlashDrive extends LaptopItem {
	private static final int FLASH_DRIVE_SCORE = 20;
	

	public FlashDrive(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, String name, int size) {
		super(model, position, rotX, rotY, rotZ, scale, name, size);
	}
	
	public FlashDrive(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, String name, ArrayList<ReadMe> files, int size) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, name, size);
	}

	@Override
	public int getScore() {
		return FLASH_DRIVE_SCORE;
	}

	@Override
	public void interact(GameWorld game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String viewInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public Item pickUp() {
		// TODO Auto-generated method stub
		return null;
	}
}
