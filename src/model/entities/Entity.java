package model.entities;

import model.entities.movableEntity.Player;
import model.models.TexturedModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import view.renderEngine.MasterRenderer;

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

    // used for atlassing
    private int textureIndex = 0;

    /**
     * Instantiates a new Entity.
     *
     * @param model    the model
     * @param position the position
     * @param rotX     the rot x
     * @param rotY     the rot y
     * @param rotZ     the rot z
     * @param scale    the scale
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
     * @param model        the model
     * @param position     the position
     * @param rotX         the rot x
     * @param rotY         the rot y
     * @param rotZ         the rot z
     * @param scale        the scale
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
     * Determine if the given entityPos is in front of the player,
     * and within the player's field of view
     *
     * @param entityPos position of entity
     * @param cam       player to get view of
     * @return true if entity can be seen by player
     */
    public static boolean isInFrontOfPlayer(Vector3f entityPos, Camera cam) {
        // get camera direction vector
        Vector3f camDirection = cam.getDirection();

        // calculate vector from camera to entity
        Vector3f camToEntity = new Vector3f(0, 0, 0);    // need to initialise for .sub method
        Vector3f.sub(entityPos, cam.getPosition(), camToEntity);

        // calculate angle between camera direction and entity
        Vector2f dir = new Vector2f(camDirection.getX(), camDirection.getZ());
        Vector2f ent = new Vector2f(camToEntity.getX(), camToEntity.getZ());
        double angle = Math.toDegrees(Vector2f.angle(dir, ent));

        // check that entity is within player's 1/4 field of view
        double maxAngle = MasterRenderer.getFOV() / 4.0;
        return angle <= maxAngle;
    }

    /**
     * Checks if a given entity is within a render range limit of the players current location
     *
     * @param player players position
     * @return is the entity within range?
     */
    public boolean isWithinRange(Player player) {
        if (Math.abs(position.getX() - player.getPosition().getX()) < MasterRenderer.RENDER_DISTANCE) {
            if (Math.abs(position.getZ() - player.getPosition().getZ()) < MasterRenderer.RENDER_DISTANCE)
                return true;
        }
        return false;
    }
}
