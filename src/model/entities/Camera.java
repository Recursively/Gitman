package model.entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Camera represents a players current view of the world
 *
 * @author Marcel van Workum
 */
public class Camera {

    /**
     * I guess the player can be 10 tall, so offset the camera by 10
     */
    public static final float CAMERA_OFFSET = 10;

    // camera parameters
    private Vector3f position;
    private float pitch = 0;
    private float roll;
    private float yaw;

    /**
     * Instantiates a new Camera.
     *
     * @param terrainHeight the terrain height
     * @param playerPosition the player position
     */
    public Camera(float terrainHeight, Vector3f playerPosition) {
        position = new Vector3f(playerPosition.getX(), terrainHeight + CAMERA_OFFSET, playerPosition.getZ());
    }

    /**
     * Updates the position of the camera
     *
     * @param newPosition the new position
     */
    public void update(Vector3f newPosition) {
        position = new Vector3f(newPosition.getX(), newPosition.getY() + CAMERA_OFFSET, newPosition.getZ());
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
     * Gets pitch.
     *
     * @return the pitch
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Gets roll.
     *
     * @return the roll
     */
    public float getRoll() {
        return roll;
    }

    /**
     * Gets yaw.
     *
     * @return the yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Change yaw.
     *
     * @param yawChange the yaw change
     */
    public void changeYaw(float yawChange) {
        yaw += yawChange;
    }

    /**
     * Change pitch.
     *
     * @param pitchChange the pitch change
     */
    public void changePitch(float pitchChange) {
        pitch += pitchChange;
    }

    /**
     * Sets pitch.
     *
     * @param pitch the pitch
     */
    public void setPitch(int pitch) {
        this.pitch = pitch;
    }
    
    //TODO fix this? are sin and cos right way around
    public Vector3f getDirection(){
    	double pitchRadians = Math.toRadians(pitch);
    	double yawRadians = Math.toRadians(yaw);

    	float sinPitch = (float) Math.sin(pitchRadians);
    	float cosPitch = (float) Math.cos(pitchRadians);
    	float sinYaw = (float) Math.sin(yawRadians);
    	float cosYaw = (float) Math.cos(yawRadians);

    	return new Vector3f(sinYaw, -(sinPitch*cosYaw), -(cosPitch*cosYaw));
    }
}