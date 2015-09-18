package model.entities.movableEntity;

import model.entities.Camera;
import model.entities.Entity;
import model.models.TexturedModel;
import model.terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;
import view.DisplayManager;

public class Player extends Entity {

    private static final float RUN_SPEED = 1;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;

    private static float terrainHeight = 0;
    private Camera camera;

    private float upwardSpeed = 0;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(Terrain terrain) {
        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        terrainHeight = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);
        if (super.getPosition().y < terrainHeight) {
            upwardSpeed = 0;
            super.getPosition().y = terrainHeight;
        }
        camera.update();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setCameraSpeed() {
        this.camera.setSpeed(RUN_SPEED);
    }
}
