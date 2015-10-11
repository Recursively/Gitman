package model.entities.movableEntity;

import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a README file in the game ( a txt document that can
 * be found laying around the office). This class stores the specific 
 * attributes of the item that make the item different from other 
 * laptop items. 
 * 
 * @author Divya
 *
 */
public class ReadMe extends LaptopItem{
	public static final int README_SCORE = 1;
	private static final int README_SIZE = 60;

	public ReadMe(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, id, name);
	}
	
	public ReadMe(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id, name);
	}
	
	@Override
	public int getScore() {
		return README_SCORE;
	}

	@Override
	public String viewInfo() {
		return "README documents can contain useful hints about the game...";
	}

	@Override
	public int getSize() {
		return README_SIZE;
	}
	
	@Override
	public String getType(){
		return "ReadMe";
	}
}
