package model.factories;

import model.entities.Light;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Created by Marcel on 19/09/15.
 */
public class LightFactory {

    private ArrayList<Light> lights;

    public LightFactory() {
        Light light = new Light(new Vector3f(250, 400, -250), new Vector3f(0.4f, 0.4f, 0.4f));
        lights = new ArrayList<>();
        lights.add(light);
        lights.add(new Light(new Vector3f(50, 27, -50), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(100, 27, -50), new Vector3f(0, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(150, 27, -50), new Vector3f(3, 0, 20), new Vector3f(1, 0.01f, 0.002f)));
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

}
