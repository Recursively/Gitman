package model.entities;

import model.entities.movableEntity.Player;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position;
    private float pitch = 40;
    private float roll;
    private float yaw;

    private float speed = 20;

    private Player player;

    public Camera(Player player) {
        this.player = player;
        position = new Vector3f(player.getPosition().getX(), player.getPosition().y, player.getPosition().z);
    }

    public void update() {
        firstPersonMove();
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

    private void firstPersonMove() {

        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            moveFromLook(0,0, -1 *  speed);
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

        /* Prevents the camera from turning over 360 or under -360 */
        yaw += Mouse.getDX()/2;
        pitch -= Mouse.getDY()/2;
        if (pitch > 60) {
            pitch = 60;
        } else if (pitch < -30) {
            pitch = -30;
        }
    }

    public void moveFromLook(float dx, float dy, float dz) {
        /* Basic Trig, see oscars video for more information :D */
        position.z += dx * (float) Math.cos(Math.toRadians(yaw - 90)) + dz * Math.cos(Math.toRadians(yaw));
        position.x -= dx * (float) Math.sin(Math.toRadians(yaw - 90)) + dz * Math.sin(Math.toRadians(yaw));

        player.setPosition(new Vector3f(position.x, player.getPosition().getY(), position.z));

        position.y = player.getPosition().getY() + 10;

        //position.y += dy * (float) Math.sin(Math.toRadians(pitch - 90)) + dz * Math.sin(Math.toRadians(pitch));
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}