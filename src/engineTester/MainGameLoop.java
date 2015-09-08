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
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();
//        StaticShader shader = new StaticShader();
//        Renderer renderer = new Renderer(shader);

        RawModel model = OBJLoader.loadObjModel("cube_01", loader);
        TexturedModel stallModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("test2")));
        ModelTexture texture = stallModel.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        List<Entity> allStalls = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;
            allStalls.add(new Entity(stallModel, new Vector3f(x, y, z), random.nextFloat() * 180f,
                    random.nextFloat() * 180f, 0f, 1f));
        }

        //TODO
        Mouse.setGrabbed(true);


        MasterRenderer renderer = new MasterRenderer();
        while (!Display.isCloseRequested()) {
            camera.move();
//            renderer.prepare();
//            shader.start();
//            shader.loadLight(light);
//            shader.loadViewMatrix(camera);
            for (Entity stall : allStalls) {
//                renderer.render(dragon, shader);
                renderer.processEntity(stall);
            }
            renderer.render(light, camera);
//            shader.stop();
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
//        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
