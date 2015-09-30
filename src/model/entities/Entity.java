package model.entities;

import model.entities.movableEntity.Player;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * An entity is any object in the game world. They are represented as a RawModel and TexturedModel
 * which gives the model a shape and texture.
 *
 * @author Marcel van Workum
 * @author Divya
 */
public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rotX = 0, rotY = 0, rotZ = 0;
    private float scale;

    // TODO remove as debug
    private boolean collidable = true;

    // used for atlassing
    private int textureIndex = 0;

    /**
     * Instantiates a new Entity.
     *
     * @param model the model
     * @param position the position
     * @param rotX the rot x
     * @param rotY the rot y
     * @param rotZ the rot z
     * @param scale the scale
     */
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    /**
     * Instantiates a new Entity.
     *
     * @param model the model
     * @param position the position
     * @param rotX the rot x
     * @param rotY the rot y
     * @param rotZ the rot z
     * @param scale the scale
     * @param textureIndex the texture index
     */
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
                  int textureIndex) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
        this.textureIndex = textureIndex;
    }

    public Entity() {
    }

    /**
     * Gets texture x offset.
     *
     * @return the texture x offset
     */
    public float getTextureXOffset() {
        int column = textureIndex % model.getTexture().getNumberOfRows();
        return (float) column / (float) model.getTexture().getNumberOfRows();
    }

    /**
     * Gets texture y offset.
     *
     * @return the texture y offset
     */
    public float getTextureYOffset() {
        int row = textureIndex / model.getTexture().getNumberOfRows();
        return (float) row / (float) model.getTexture().getNumberOfRows();
    }

    /**
     * Increase position.
     *
     * @param dx the dx
     * @param dy the dy
     * @param dz the dz
     */
    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    /**
     * Increase rotation.
     *
     * @param dx the dx
     * @param dy the dy
     * @param dz the dz
     */
    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public TexturedModel getModel() {
        return model;
    }

    /**
     * Sets model.
     *
     * @param model the model
     */
    public void setModel(TexturedModel model) {
        this.model = model;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * Gets rot x.
     *
     * @return the rot x
     */
    public float getRotX() {
        return rotX;
    }

    /**
     * Gets rot y.
     *
     * @return the rot y
     */
    public float getRotY() {
        return rotY;
    }

    /**
     * Gets rot z.
     *
     * @return the rot z
     */
    public float getRotZ() {
        return rotZ;
    }

    /**
     * Gets scale.
     *
     * @return the scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Sets scale.
     *
     * @param scale the scale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

	/**
	 * Determine which position (new or old) is closer to the
	 * origin position
	 * 
	 * @param newPos
	 * @param oldPos
	 * @param player that is interacting with the item
	 * @return true if the new position is closer to the player
	 * than the oldPos
	 */
	public static boolean isCloserThan(Vector3f newPos, Vector3f oldPos,
			Player player, int radius) {
		Vector3f origin = player.getPosition();
		if(Entity.isWithinBounds(newPos, origin, radius)){
			if(oldPos == null){
				return true;
			}
			// TODO check what is closer
			// player.getcamera yaw...
			// x and z
			// ask ellie to make graphics...
		}
		// TODO Auto-generated method stub
		return false;
	}
	
	 /**
     * Determine if the point is within the given radius bounds from 
     * the origin position
     * 
     * @param point
     * @param origin
     * @param radius
     * @return true if point is within the bounds, false otherwise
     */
	public static boolean isWithinBounds(Vector3f point, Vector3f origin, int radius) {
		// TODO Auto-generated method stub
		return false;
	}


}
