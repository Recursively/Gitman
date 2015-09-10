package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0,10,0);
    private float pitch;
    private float roll;
    private float yaw;

    private boolean jumping = false;


    private float speed = 1f;

    public Camera() {
    }

    public void move(){
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

    public void moveOld() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.y -= 1;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.y += 1;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += 1;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 1;
        }
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
}
