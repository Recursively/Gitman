package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        RawModel model = OBJLoader.loadObjModel("stall", loader);
        TexturedModel stallModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));
        ModelTexture texture = stallModel.getTexture();

        // TODO This is broken
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("lana")));

        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        List<Entity> allStalls = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 1000; i++) {
            float x = random.nextFloat() * 1000 - 50;
            float y = 0;
            float z = random.nextFloat() * -1000 - 50;
            allStalls.add(new Entity(stallModel, new Vector3f(x, y, z), 0,
                    0, 0f, 1f));
        }

        //TODO
        Mouse.setGrabbed(true);


        MasterRenderer renderer = new MasterRenderer();
        while (!Display.isCloseRequested()) {
            camera.move();
            renderer.processTerrain(terrain);

            for (Entity stall : allStalls) {
                renderer.processEntity(stall);
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
