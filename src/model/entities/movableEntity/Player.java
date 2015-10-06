package model.entities.movableEntity;

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
 * @author Marcel van Workum
 *
 *
 */
public class Player extends MovableEntity {

    private static final float RUN_SPEED = 1f;
    private static final float JUMP_POWER = 30;

    private static float terrainHeight = 0;
    private Camera camera;

    private float verticalVelocity = 0;

    private float edgeBound = 12;

    private Terrain currentTerrain;

    // bad?
    private Vector3f oldPosition;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int uid, Camera camera) {
        super(model, position, rotX, rotY, rotZ, scale, uid);
        this.camera = camera;
    }

    // TODO does this still need to be here?
    public void move(Terrain terrain) {
        updateTerrainHeight(terrain);
        gravityPull();
        firstPersonMove();
        camera.update(super.getPosition());
    }

    public void move(Terrain terrain, ArrayList<Entity> statics) {
        updateTerrainHeight(terrain);
        gravityPull();
        if(firstPersonMove(statics, terrain)) {
            camera.update(oldPosition);
        } else {
            camera.update(super.getPosition());
        }
    }

    //TODO this is ugly and needs love
    private boolean firstPersonMove(ArrayList<Entity> statics, Terrain terrain) {

        boolean collision = false;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            if(moveFromLook(0, 0, -1 * RUN_SPEED, statics)) {
                collision = true;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            if (moveFromLook(0, 0, 1 * RUN_SPEED, statics)) {
                collision = true;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            if (moveFromLook(1 * RUN_SPEED, 0, 0, statics)) {
                collision = true;
            }

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            if (moveFromLook(-1 * RUN_SPEED, 0, 0, statics)) {
                collision = true;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if (super.getPosition().y == terrainHeight) {
                jump();
            }
        }

        /* Prevents the camera from turning over 360 or under -360 */
        camera.changeYaw(Mouse.getDX() / 2);
        camera.changePitch(-(Mouse.getDY() / 2));
        if (camera.getPitch() > 60) {
            camera.setPitch(60);
        } else if (camera.getPitch() < -30) {
            camera.setPitch(-30);
        }

        checkBounds(terrain);

        return collision;
    }

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
    }

    private boolean moveFromLook(float dx, float dy, float dz, ArrayList<Entity> statics) {
        Vector3f position = super.getPosition();

        oldPosition = super.getPosition();

        float z = position.getZ();
        float x = position.getX();

        z += dx * (float) Math.cos(Math.toRadians(camera.getYaw() - 90)) + dz * Math.cos(Math.toRadians(camera.getYaw()));
        x -= dx * (float) Math.sin(Math.toRadians(camera.getYaw() - 90)) + dz * Math.sin(Math.toRadians(camera.getYaw()));

        Vector3f move = new Vector3f(x, position.y, z);

        boolean collision = false;

        for (Entity e : statics) {
            StaticEntity staticEntity = (StaticEntity) e;
            if (staticEntity.checkCollision(move)) {
                collision = true;
            }
        }

        if (!collision) {
            super.setPosition(move);
            return false;
        } else {
            super.setPosition(oldPosition);
            return true;
        }
    }

    private void gravityPull() {
        verticalVelocity += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, verticalVelocity * DisplayManager.getFrameTimeSeconds(), 0);
        if (super.getPosition().y < terrainHeight) {
            verticalVelocity = 0;
            super.getPosition().y = terrainHeight;
        }
    }

    private void updateTerrainHeight(Terrain terrain) {
        terrainHeight = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);
    }

    private void jump() {
        verticalVelocity += JUMP_POWER;
    }

    // TODO does this still need to be here
    private void firstPersonMove() {

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            moveFromLook(0, 0, -1 * RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            moveFromLook(0, 0, 1 * RUN_SPEED);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            moveFromLook(1 * RUN_SPEED, 0, 0);

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            moveFromLook(-1 * RUN_SPEED, 0, 0);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if (super.getPosition().y == terrainHeight) {
                jump();
            }
        }

        /* Prevents the camera from turning over 360 or under -360 */
        camera.changeYaw(Mouse.getDX() / 2);
        camera.changePitch(-(Mouse.getDY() / 2));
        if (camera.getPitch() > 60) {
            camera.setPitch(60);
        } else if (camera.getPitch() < -30) {
            camera.setPitch(-30);
        }
    }

	public void moveFromLook(float dx, float dy, float dz) {

        Vector3f position = super.getPosition();

        position.z += dx * (float) Math.cos(Math.toRadians(camera.getYaw() - 90)) + dz * Math.cos(Math.toRadians(camera.getYaw()));
        position.x -= dx * (float) Math.sin(Math.toRadians(camera.getYaw() - 90)) + dz * Math.sin(Math.toRadians(camera.getYaw()));

        super.setPosition(position);
    }

    public Camera getCamera() {
        return camera;
    }

    //TODO implement terrain specification
    public Terrain getCurrentTerrain() {
        return currentTerrain;
    }

    public void setCurrentTerrain(Terrain currentTerrain) {
        this.currentTerrain = currentTerrain;
    }

	@Override
	public void interact(GameWorld game) {
		return;
	}

	@Override
	public boolean canInteract() {
		return false;
	}
}
