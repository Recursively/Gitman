package model.entities;

import model.entities.movableEntity.Player;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0,0, 0);
    private float pitch = 40;
    private float roll;
    private float yaw;

    private float playerZoom = 50;
    private float playerAngle = 0;

    private Player player;

    private float speed = 1f;
    private boolean jumping = false;

    public Camera(Player player) {
        this.player = player;
    }

    public void move(){
        calculateZoom();
        calculatePitch();
        calculatePlayerAngle();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + playerAngle);
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

    public void updateCameraPosition(Vector3f position) {
        this.position = new Vector3f(position.x, position.y + 50, position.z + 50);
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        float theta = player.getRotY() + playerAngle;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.05f;
        playerZoom -= zoomLevel;
        if (playerZoom < 30) playerZoom = 30;
        if (playerZoom > 300) playerZoom = 300;
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(0)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
        }
    }

    private void calculatePlayerAngle() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            playerAngle -= angleChange;
        }
    }

    private float calculateHorizontalDistance() {
        return (float) (playerZoom * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (playerZoom * Math.sin(Math.toRadians(pitch)));
    }

    private void firstPersonMove() {

        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            moveFromLook(0,0, -1 * speed );
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            moveFromLook(0, 0, 1 * speed);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            moveFromLook(1 * speed, 0, 0);

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            moveFromLook(-1 * speed, 0, 0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
            position.y += 3;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            position.y += 0.981f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_R)){
            roll += speed;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_1)){
            position = new Vector3f(5, .5f, 0);
        }
        /* Prevents the camera from turning over 360 or under -360 */
        yaw += Mouse.getDX()/2;
        pitch -= Mouse.getDY()/2;
        if(yaw > 360 || yaw < -360){
            yaw = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if (position.y == 10) {
                jumping = true;
            }
        }

        if (position.y > 30) {
            jumping = false;
        }
        if (jumping) {
            position.y += 2;
        }

        gravityPull();

        checkBounds();
    }

    private void gravityPull() {
        position.y -= 0.981f;
    }

    private void checkBounds() {
        if (position.x < 0) position.x = 0;
        if (position.x > 1000) position.x = 1000;
        if (position.y < 10) position.y = 10;
        if (position.z > 0) position.z = 0;
        if (position.z < -1000) position.z = -1000;
    }

    public void moveFromLook(float dx, float dy, float dz) {
        /* Basic Trig, see oscars video for more information :D */
        position.z += dx * (float) Math.cos(Math.toRadians(yaw - 90)) + dz * Math.cos(Math.toRadians(yaw));
        position.x -= dx * (float) Math.sin(Math.toRadians(yaw - 90)) + dz * Math.sin(Math.toRadians(yaw));
        //position.y += dy * (float) Math.sin(Math.toRadians(pitch - 90)) + dz * Math.sin(Math.toRadians(pitch));
    }
}