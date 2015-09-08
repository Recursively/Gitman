package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
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

        Random random = new Random();

        Loader loader = new Loader();
        Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        TexturedModel stallModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
        ModelTexture treeTexture = stallModel.getTexture();
        // TODO This is broken
        treeTexture.setShineDamper(10);
        treeTexture.setReflectivity(1);
        List<Entity> allTrees = new ArrayList<>();

        for (int i = 0; i < 300; i++) {
            float x = random.nextFloat() * 1000;
            float y = 0;
            float z = random.nextFloat() * -1000;
            allTrees.add(new Entity(stallModel, new Vector3f(x, y, z), 0,
                    0, 0f, 10f));
        }

        TexturedModel fernModel = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
                new ModelTexture(loader.loadTexture("fern")));
        ModelTexture fernTexture = fernModel.getTexture();
        fernTexture.setShineDamper(10);
        fernTexture.setReflectivity(1);

        List<Entity> allFerns = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            float x = random.nextFloat() * 1000;
            float y = 0;
            float z = random.nextFloat() * -1000 ;
            allFerns.add(new Entity(fernModel, new Vector3f(x, y, z), 0,
                    0, 0f, 1f));
        }

        //TODO
        Mouse.setGrabbed(true);


        MasterRenderer renderer = new MasterRenderer();
        while (!Display.isCloseRequested()) {
            camera.move();
            renderer.processTerrain(terrain);

            for (Entity tree : allTrees) {
                renderer.processEntity(tree);
            }

            for (Entity fern : allFerns) {
                renderer.processEntity(fern);
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
