package model.factories;

import model.entities.Light;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Factory class to handle the creation of {@link Light} sources in the game.
 *
 * A Light source is either the sun, a constant source of light, regardless of distance,
 * or an attenuating light source, one which fades with distance.
 *
 * @author Marcel van Workum
 */
public class LightFactory {

    private static final Vector3f INITIAL_SUN_POSITION = new Vector3f(500, 500, -500);
    private static final Vector3f INITIAL_SUN_COLOUR = new Vector3f(0.4f, 0.4f, 0.4f);

    // This can be override, but this gives a nice gradual fadeout
    private static final Vector3f DEFAULT_ATTENUATION_FACTOR = new Vector3f(1, 0.01f, 0.002f);

    // should the sun be included in the lights?
    private ArrayList<Light> lights;
    private Light sun;

    /**
     * Constructs the light factory and initialises the data structure to hold the collection of lights
     */
    public LightFactory() {
        lights = new ArrayList<>();
    }

    /**
     * Creates a new Sun... Wait, I thought we were computer scientists, not astrophysicists...
     *
     * @return Ra : God of the Sun and Radiance
     */
    public Light createSun() {
        sun = new Light(INITIAL_SUN_POSITION, INITIAL_SUN_COLOUR);
        return sun;
    }
}
