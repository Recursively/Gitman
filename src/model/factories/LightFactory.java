package model.factories;

import model.GameWorld;
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

    private static final Vector3f INITIAL_SUN_POSITION = new Vector3f(GameWorld.SPAWN_POSITION.getX(), 1000, GameWorld.SPAWN_POSITION.getZ());
    private static final Vector3f INITIAL_SUN_COLOUR = new Vector3f(0, 0, 0);

    private static final Vector3f OFFICE_LIGHT_COLOUR = new Vector3f(5, 4, 2.5f);

    // This can be override, but this gives a nice gradual fadeout
    private static final Vector3f DEFAULT_ATTENUATION_FACTOR = new Vector3f(1, 0.01f, 0.002f);
    private static final Vector3f DEFAULT_LIGHT_COLOUR = new Vector3f(1f, 1f, 1f);

    private static ArrayList<Light> staticEntityLights = new ArrayList<>();

    private static ArrayList<Light> entityLights = new ArrayList<>();

    /**
     * Constructs the light factory and initialises the data structure to hold the collection of lights
     */
    public LightFactory() {}

    /**
     * Creates a new Sun... Wait, I thought we were computer scientists, not astrophysicists...
     *
     * @return Ra : God of the Sun and Radiance
     */
    public Light createSun() {
        return new Light(INITIAL_SUN_POSITION, INITIAL_SUN_COLOUR);
    }

    public static ArrayList<Light> getStaticEntityLights() {
        return staticEntityLights;
    }

    public Light createOfficeLight() {
        return new Light(new Vector3f(128060, 70, -127930), OFFICE_LIGHT_COLOUR, new Vector3f(1f, 0.005f, 0.001f));
    }

    public static void createEntityLight(Vector3f position) {
        entityLights.add(new Light(position, DEFAULT_LIGHT_COLOUR, DEFAULT_ATTENUATION_FACTOR));
    }

    public static void createEntityLight(Vector3f position, Vector3f colour) {
        entityLights.add(new Light(position, colour, DEFAULT_ATTENUATION_FACTOR));
    }

    public static void removeEntityLight(Vector3f position) {
        for (Light light : entityLights) {
            if (light.getPosition().equals(position)) {
                entityLights.remove(light);
                break;
            }
        }
    }
}
