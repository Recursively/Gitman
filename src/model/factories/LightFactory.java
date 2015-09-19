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

    private static final Vector3f DEFAULT_ATTENUATION_FACTOR = new Vector3f(1, 0.01f, 0.002f);

    private Light sun;


    private ArrayList<Light> lights;

    public LightFactory() {
        lights = new ArrayList<>();
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public Light createSun() {
        sun = new Light(INITIAL_SUN_POSITION, INITIAL_SUN_COLOUR);
        return sun;
    }
}
