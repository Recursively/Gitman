package model.entities.movableEntity;

import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a README file in the game ( a tablet that can
 * be found laying around the office). This class stores the specific 
 * attributes of the item that make the item different from other 
 * laptop items. 
 * 
 * @author Divya
 *
 */
public class ReadMe extends LaptopItem{
	private static final int README_SIZE = 60;
	private static int readMeScore = 8;

	/**
	 * Instantiates a new ReadMe
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
	public ReadMe(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, id, name);
	}
	
	/**
	 * Instantiates a new ReadMe
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
	public ReadMe(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, int id, String name) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex, id, name);
	}
	
	@Override
	public int getScore() {
		if(super.getPickedUpAlready()){
			readMeScore = 0;
		}
		return readMeScore;
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
