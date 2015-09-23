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
public class File extends LaptopItem {
	private static final int FILE_SCORE = 20;
	
	private ArrayList<ReadMe> txtFiles;

	public File(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, String name, ArrayList<ReadMe> files, int size) {
		super(model, position, rotX, rotY, rotZ, scale, name, size);
		this.txtFiles = files;
	}
	
	public File(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, String name, ArrayList<ReadMe> files, int size) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, name, size);
		this.txtFiles = files;
	}

	@Override
	public int getScore() {
		return FILE_SCORE;
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
}
