package model.entities;

import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position;
    private float pitch = 0;
    private float roll;
    private float yaw;

    private float cameraHeightOffset;

    public Camera(float terrainHeight, float cameraHeightOffset, Vector3f playerPosition) {
        this.cameraHeightOffset = cameraHeightOffset;
        position = new Vector3f(playerPosition.getX(), terrainHeight + cameraHeightOffset, playerPosition.getZ());
    }

    public void update(Vector3f newPosition) {
        position = new Vector3f(newPosition.getX(), newPosition.getY() + cameraHeightOffset, newPosition.getZ());
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float getYaw() {
        return yaw;
    }

    public void changeYaw(float yawChange) {
        yaw += yawChange;
    }

    public void changePitch(float pitchChange) {
        pitch += pitchChange;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }
}