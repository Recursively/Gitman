package model.entities;

import model.GameWorld;
import org.lwjgl.util.vector.Vector3f;

import java.util.Comparator;

/**
 * There are two different types of light sources in the world. Those that understand the pointlessness of
 * these java doc comments, and those that don't.
 *
 * But seriously, there is two types of light. A sun which is a constant source of light and an attenuated light
 * source, which fades over a distance.
 *
 * @author Marcel van Workum
 */
public class Light implements Comparator<Light>, Comparable<Light> {

    private Vector3f position;
    private Vector3f colour;
    private Vector3f attenuation = new Vector3f(1, 0, 0);

    /**
     * Instantiates a new sun
     *
     * @param position position of the sun
     * @param colour colour of the sun
     */
    public Light(Vector3f position, Vector3f colour) {
        this.position = position;
        this.colour = colour;
    }

    /**
     * Instantiates a new attenuated light source
     *
     * @param position position of light source
     * @param colour colour of light source
     * @param attenuation attenuation factor
     */
    public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
        this.position = position;
        this.colour = colour;
        this.attenuation = attenuation;
    }

    /**
     * Gets attenuation.
     *
     * @return the attenuation
     */
    public Vector3f getAttenuation() {
        return attenuation;
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
     * Gets colour.
     *
     * @return the colour
     */
    public Vector3f getColour() {
        return colour;
    }

    /**
     * Sets colour.
     *
     * @param colour the colour
     */
    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    /**
     * Returns the squared hypotenuse side of the distance to the light from a given position
     *
     * @param position position
     * @return hypotenuse
     */
    public float getDistanceTo(Vector3f position) {
        float diffX = (float) Math.pow(Math.abs(position.getX() - this.position.getX()), 2);
        float diffZ = (float) Math.pow(Math.abs(position.getZ() - this.position.getZ()), 2);

        return diffX + diffZ;
    }

    @Override
    public int compareTo(Light l) {
        Vector3f playerPos = GameWorld.getPlayerPosition();
        float distance = this.getDistanceTo(playerPos);
        float distance2 = l.getDistanceTo(playerPos);
        if (distance < distance2) return -1;
        else if (distance == distance2) return 0;
        else return 1;
    }

    @Override
    public int compare(Light light1, Light light2) {
        Vector3f playerPos = GameWorld.getPlayerPosition();
        float distance = light1.getDistanceTo(playerPos);
        float distance2 = light2.getDistanceTo(playerPos);
        if (distance < distance2) return -1;
        else if (distance == distance2) return 0;
        else return 1;
    }
}
