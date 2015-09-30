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

public class Player extends MovableEntity {

    private static final float RUN_SPEED = 1f;
    private static final float JUMP_POWER = 30;

    private static float terrainHeight = 0;
    private Camera camera;
    
    private int uid;

    private float verticalVelocity = 0;
    
    private GameWorld gameWorld;
    private Item holding;

    // bad?
    private Vector3f oldPosition;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Camera camera, GameWorld game, int uid) {

        super(model, position, rotX, rotY, rotZ, scale);
        this.uid = uid;
        this.camera = camera;
        this.gameWorld = game;
    }

    public void move(Terrain terrain) {
        updateTerrainHeight(terrain);
        gravityPull();
        firstPersonMove();
        camera.update(super.getPosition());
    }

    public void move(Terrain terrain, ArrayList<Entity> statics) {
        updateTerrainHeight(terrain);
        gravityPull();
        if(firstPersonMove(statics)) {
            camera.update(oldPosition);
        } else {
            camera.update(super.getPosition());
        }
    }

    //TODO this is ugly and needs love
    private boolean firstPersonMove(ArrayList<Entity> statics) {

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

        // ensures single reaction to a key press event when dealing with items
        while(Keyboard.next()){
            // carry out methods when key is pressed (not released)
            if(Keyboard.getEventKeyState()){
                if(Keyboard.getEventKey() == Keyboard.KEY_E){
                    pickUpOrInteract();
                }
                if(Keyboard.getEventKey() == Keyboard.KEY_R){
                    dropItem();
                }
                // TODO have 'C' or something to interact/copy code from npc?
                // TODO have 'U' or something for unlock door method
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

        return collision;
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
        
        // ensures single reaction to a key press event when dealing with items
        while(Keyboard.next()){
        	// carry out methods when key is pressed (not released)
        	if(Keyboard.getEventKeyState()){
        		if(Keyboard.getEventKey() == Keyboard.KEY_E){
        			pickUpOrInteract();
        		}
        		if(Keyboard.getEventKey() == Keyboard.KEY_R){
        			dropItem();
        		}
        		// TODO have 'C' or something to interact/copy code from npc?
                // TODO have 'U' or something for unlock door method
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
    
	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
    
    /**
     * Determine whether player is picking up an item
     * or trying to interact with it
     */
    private void pickUpOrInteract() {
    	// if player is not holding anything, they can pick up the item
    	if(this.holding == null){
    		pickUpItem();
    	}
    	else {
    		// otherwise, E means interact
    		this.holding.interact(this.gameWorld);
    	}
	}

    /**
     * Find item that player is trying to pick up 
     * and and allow item to be picked up by player
     */
	private void pickUpItem(){
		// find item that is directly in front of player
    	Item item = gameWorld.findItem(this.getPosition()); 
    	if(item != null){
    		// picking up items affect game differently depending on item
    		// so allow the item to deal with changing game state accordingly
    		this.holding = item.pickUp(this.gameWorld); 
    	}
    }
    
	/**
	 * Drop the item the player is currently holding
	 */
    private void dropItem(){
    	// can only drop item if holding something
    	if(this.holding != null){
    		Vector3f itemNewPos = getPosition();
    		// TODO does item need to be dropped in front of player???
    		this.holding.setPosition(itemNewPos);
    		this.holding = null;
    	}
    }
}
