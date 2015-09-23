package model.entities.movableEntity;

import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public abstract class LaptopItem extends Item {
	private final String name;
	private final int size;
	
	public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, String name, int size) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.name = name;
		this.size = size;
	}
	
	public LaptopItem(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, int textureIndex, String name, int size) {
		super(model, position, rotX, rotY, rotZ, scale, textureIndex);
		this.name = name;
		this.size = size;
	}
	
	//TODO pick up
	//drop methods
	
	public int getSize(){
		return this.size;
	}
}
