package model.entities.movableEntity;

import controller.AudioController;
import model.GameWorld;
import model.entities.Camera;
import model.entities.Entity;
import model.entities.staticEntity.StaticEntity;
import model.models.TexturedModel;
import model.terrains.Terrain;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import view.DisplayManager;

import java.util.ArrayList;

/**
 * Represents a player in the game. The player has a camera associated with them.
 *
 * @author Marcel van Workum
 */
public class Player extends MovableEntity {

    private static final float RUN_SPEED = 1f;
    private static final float JUMP_POWER = 30;
    private static final float edgeBound = 12;

    private static float terrainHeight = 0;
    private Camera camera;
    private float verticalVelocity = 0;
    private Vector3f oldPosition;
    private boolean firstTimeOutside = true;

    /**
     * Instantiates a new Player.
     *
     * @param model    the model
     * @param position the position
     * @param rotX     the rot x
     * @param rotY     the rot y
     * @param rotZ     the rot z
     * @param scale    the scale
     * @param uid      the uid
     * @param camera   the camera
     */
    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int uid, Camera camera) {
        super(model, position, rotX, rotY, rotZ, scale, uid);
        this.camera = camera;
    }

    /**
     * Moves the player
     *
     * @param terrain The current terrain the player is on
     * @param statics A list of static entities to check for collision
     */
    public void move(Terrain terrain, ArrayList<Entity> statics) {

        // First update the players current terrain height
        updateTerrainHeight(terrain);

        // Pull the player back to the terrain
        gravityPull();

        // And move
        if (parsePlayerMove(statics, terrain)) {
            camera.update(oldPosition);
        } else {
            camera.update(super.getPosition());
        }
    }

    /**
     * Checks the player's input and moves the player accordingly
     *
     * @param statics List of collidable entities
     * @param terrain Terrain the player is on
     * @return If the player has collided
     */
    private boolean parsePlayerMove(ArrayList<Entity> statics, Terrain terrain) {

        // Parse the inputs
        boolean collision = checkInputs(statics, false);

        /* Prevents the camera from turning over 360 or under -360 */
        lockCamera();

        // Check bounds of the terrain
        checkBounds(terrain);

        return collision;
    }

    /**
     * Prevents the camera from being able to spin full 360
     */
    private void lockCamera() {
        camera.changeYaw(Mouse.getDX() / 2);
        camera.changePitch(-(Mouse.getDY() / 2));
        if (camera.getPitch() > 60) {
            camera.setPitch(60);
        } else if (camera.getPitch() < -30) {
            camera.setPitch(-30);
        }
    }

    /**
     * Checks for movement inputs
     *
     * @param statics   collidable entities
     * @param collision is collided
     * @return if collided or not
     */
    private boolean checkInputs(ArrayList<Entity> statics, boolean collision) {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            if (moveFromLook(0, -1 * RUN_SPEED, statics)) {
                collision = true;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            if (moveFromLook(0, 1 * RUN_SPEED, statics)) {
                collision = true;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            if (moveFromLook(1 * RUN_SPEED, 0, statics)) {
                collision = true;
            }

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            if (moveFromLook(-1 * RUN_SPEED, 0, statics)) {
                collision = true;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if (super.getPosition().y == terrainHeight) {
                jump();
            }
        }
        return collision;
    }

    /**
     * Checks that the player is within the bounds of the terrain
     *
     * @param terrain current terrain
     */
    private void checkBounds(Terrain terrain) {
        Vector3f position = super.getPosition();
        float terrainSize = terrain.getSIZE();

        float terrainOriginX = terrain.getGridX();
        float terrainOriginZ = terrain.getGridZ();

        float terrainBoundX = terrainOriginX + terrainSize;
        float terrainBoundZ = terrainOriginZ + terrainSize;

        float xPos = position.getX();
        float zPos = position.getZ();

        if (xPos < terrainOriginX + edgeBound) {
            position.x = terrainOriginX + edgeBound;
        } else if (xPos > terrainBoundX - edgeBound) {
            position.x = terrainBoundX - edgeBound;
        }

        if (zPos < terrainOriginZ + edgeBound) {
            position.z = terrainOriginZ + edgeBound;
        } else if (zPos > terrainBoundZ - edgeBound) {
            position.z = terrainBoundZ - edgeBound;
        }

        // Now check for portal collision

        if (xPos <= GameWorld.PORTAL_EDGE_BOUND_OUTSIDE_X && zPos <= GameWorld.PORTAL_LOWER_BOUND_OUTSIDE_Z
                && zPos >= GameWorld.PORTAL_UPPER_BOUND_OUTSIDE_Z) {
            // swap terrain
            GameWorld.teleportToOffice();
        }

        if (GameWorld.isProgramCompiled()) {
            if (xPos <= GameWorld.PORTAL_EDGE_BOUND_OFFICE_X && zPos <= GameWorld.PORTAL_LOWER_BOUND_OFFICE_Z
                    && zPos >= GameWorld.PORTAL_UPPER_BOUND_OFFICE_Z) {
                // swap terrain
                GameWorld.teleportToOutside();

                if (firstTimeOutside) {
                    firstTimeOutside = false;
                    GameWorld.setGuiMessage("inGameMessage", 5000);

                    // Play the cool sound
                    AudioController.playCoolStuffSound();
                }
            }
        }
    }

    /**
     * Moves the player based on the camera angle
     *
     * @param dx      dx
     * @param dz      dz
     * @param statics collidable entities
     * @return if we collided or not
     */
    private boolean moveFromLook(float dx, float dz, ArrayList<Entity> statics) {
        Vector3f position = super.getPosition();

        oldPosition = super.getPosition();

        float z = position.getZ();
        float x = position.getX();

        // Math
        z += dx * (float) Math.cos(Math.toRadians(camera.getYaw() - 90)) + dz * Math.cos(Math.toRadians(camera.getYaw()));
        x -= dx * (float) Math.sin(Math.toRadians(camera.getYaw() - 90)) + dz * Math.sin(Math.toRadians(camera.getYaw()));

        Vector3f move = new Vector3f(x, position.y, z);

        boolean collision = false;

        // Check for collision
        for (Entity e : statics) {
            StaticEntity staticEntity = (StaticEntity) e;
            if (staticEntity.checkCollision(move)) {
                collision = true;
            }
        }

        // Finally update position of camera
        if (!collision) {
            super.setPosition(move);
            return false;
        } else {
            super.setPosition(oldPosition);
            return true;
        }
    }

    /**
     * Gravity pull. Somewhat simulated to be real
     */
    private void gravityPull() {
        verticalVelocity += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, verticalVelocity * DisplayManager.getFrameTimeSeconds(), 0);
        if (super.getPosition().y < terrainHeight) {
            verticalVelocity = 0;
            super.getPosition().y = terrainHeight;
        }
    }

    /**
     * Updates the terrain height
     *
     * @param terrain terrain to check with
     */
    private void updateTerrainHeight(Terrain terrain) {
        terrainHeight = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);
    }

    /**
     * Jumps and plays the sound
     */
    private void jump() {
        verticalVelocity += JUMP_POWER;
        AudioController.playJumpSound();
    }

    /**
     * Gets camera.
     *
     * @return the camera
     */
    public Camera getCamera() {
        return camera;
    }

    @Override
    public int interact(GameWorld game) {
        return 15;
    }

    @Override
    public boolean canInteract() {
        return true;
    }

    @Override
    public String getType() {
        return "Player";
    }
}
