package model.factories;

import model.entities.Light;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Factory class to handle the creation of {@link Light} sources in the game.
 * <p/>
 * A Light source is either the sun, a constant source of light, regardless of distance,
 * or an attenuating light source, one which fades with distance.
 *
 * @author Marcel van Workum
 */
public class LightFactory {

    private static final Vector3f INITIAL_SUN_POSITION = new Vector3f(256, 1000, 256);
    private static final Vector3f INITIAL_SUN_COLOUR = new Vector3f(0, 0, 0);

    private static final Vector3f OFFICE_LIGHT_COLOUR = new Vector3f(5, 4, 2.5f);

    // This can be override, but this gives a nice gradual fadeout
    private static final Vector3f DEFAULT_ATTENUATION_FACTOR = new Vector3f(1f, 0.005f, 0.001f);
    private static final Vector3f DEFAULT_LIGHT_COLOUR = new Vector3f(2.2f, 2f, 1.5f);

    private static ArrayList<Light> staticEntityLights = new ArrayList<>();

    /**
     * Constructs the light factory and initialises the data structure to hold the collection of lights
     */
    public LightFactory() {
    }

    /**
     * Creates a new Sun... Wait, I thought we were computer scientists, not astrophysicists...
     *
     * @return Ra : God of the Sun and Radiance
     */
    public Light createSun() {
        return new Light(INITIAL_SUN_POSITION, INITIAL_SUN_COLOUR);
    }

    /**
     * Create office light light.
     *
     * @return the light
     */
    public Light createOfficeLight() {
        return new Light(new Vector3f(128060, 70, -127930), OFFICE_LIGHT_COLOUR, new Vector3f(1f, 0.005f, 0.001f));
    }

    /**
     * Create entity light.
     *
     * @param position the position
     */
    public static void createEntityLight(Vector3f position) {
        staticEntityLights.add(new Light(position, DEFAULT_LIGHT_COLOUR, DEFAULT_ATTENUATION_FACTOR));
    }

    /**
     * Gets static entity lights.
     *
     * @return the static entity lights
     */
    public static ArrayList<Light> getStaticEntityLights() {
        return staticEntityLights;
    }

}
