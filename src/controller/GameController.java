package controller;

import model.entities.Camera;
import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.Player;
import model.models.ModelData;
import model.models.RawModel;
import model.models.TexturedModel;
import model.terrains.Terrain;
import model.textures.GuiTexture;
import model.textures.ModelTexture;
import model.textures.TerrainTexture;
import model.textures.TerrainTexturePack;
import model.toolbox.Loader;
import model.toolbox.objParser.OBJFileLoader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import view.DisplayManager;
import view.renderEngine.GuiRenderer;
import view.renderEngine.MasterRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controller class to handle the delegations between the Model and View package.
 *
 * Deals with Game logic
 *
 * @author Marcel van Workum
 */
public class GameController {

    // Model
    private final Loader loader;

    // View
    private final MasterRenderer renderer;
    private final GuiRenderer guiRenderer;

    //Controller
    private final NetworkController networkController;

    /**
     * Delegates the creation of the MVC and then starts the game
     */
    public GameController() {

        // initialise model
        loader = new Loader();

        // initialise view
        DisplayManager.createDisplay();
        renderer = new MasterRenderer(loader);
        guiRenderer = new GuiRenderer(loader);

        //initialise controller
        networkController = new NetworkController();

        //start the game
        doGame();
    }

    /**
     * Ga
     */
    private void doGame() {

        // Terrain creation
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("textures/mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("textures/grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("textures/path"));

        // Bundle terrains into pack
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        // Blend map for mixing terrains
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("terrains/blendMap"));

        // Create the new terrain object, using pack blendermap and heightmap
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "terrains/heightMap");

        // Multiple light sources
        // This is a test and makes shit look weird
        //TODO remove this
        Light light = new Light(new Vector3f(250, 400, -250), new Vector3f(0.4f, 0.4f, 0.4f));
        List<Light> lights = new ArrayList<>();
        lights.add(light);
        lights.add(new Light(new Vector3f(50, 27, -50), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(100, 27, -50), new Vector3f(0, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(150, 27, -50), new Vector3f(3, 0, 20), new Vector3f(1, 0.01f, 0.002f)));

        ModelData lampData = OBJFileLoader.loadOBJ("models/lamp");
        RawModel lampModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(),
                lampData.getNormals(), lampData.getIndices());

        TexturedModel lampTexturedModel = new TexturedModel(lampModel,
                new ModelTexture(loader.loadTexture("textures/lamp")));
        lampTexturedModel.getTexture().setNumberOfRows(2);
        lampTexturedModel.getTexture().setShineDamper(10);
        lampTexturedModel.getTexture().setReflectivity(1);

        List<Entity> lamps = new ArrayList<>();

        lamps.add(new Entity(lampTexturedModel, new Vector3f(50, 20, -50), 0, 0, 0, 1));
        lamps.add(new Entity(lampTexturedModel, new Vector3f(100, 20, -50), 0, 0, 0, 1));
        lamps.add(new Entity(lampTexturedModel, new Vector3f(150, 20, -50), 0, 0, 0, 1));

        ModelData data3 = OBJFileLoader.loadOBJ("models/stall");
        RawModel stallModel = loader.loadToVAO(data3.getVertices(), data3.getTextureCoords(), data3.getNormals(),
                data3.getIndices());

        TexturedModel stallTexturedModel = new TexturedModel(stallModel,
                new ModelTexture(loader.loadTexture("textures/stall")));
        stallTexturedModel.getTexture().setShineDamper(10);
        stallTexturedModel.getTexture().setReflectivity(1);

        List<Entity> allStalls = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 25; i++) {
            float x = random.nextFloat() * 1000;
            float z = random.nextFloat() * -1000;
            float y = terrain.getTerrainHeight(x, z);
            if ((x > 400 && x < 600) && (z > -600 && z < -400)) {
                continue;
            }
            allStalls.add(new Entity(stallTexturedModel, new Vector3f(x, y, z), 0,
                    0, 0f, 4f));
        }

        // Create gui elements

        List<GuiTexture> guiImages = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("gui/panel_brown"), new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f));
        guiImages.add(gui);

        // gui renderer which handles rendering an infinite amount of gui elements
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        ///

        Vector3f playerPosition = new Vector3f(50, 0, -50);
        float initialPlayerY = terrain.getTerrainHeight(playerPosition.getX(), playerPosition.getZ());

        // New player and camera to follow the player
        Camera camera = new Camera(initialPlayerY, 10, playerPosition);
        Player player = new Player(null, playerPosition, 0, 180f, 0, 1, camera);


        //TODO do we want the mouse to be captured?
        // It makes sense to be captured if game is first person, not so much for third person
        Mouse.setGrabbed(true);

        while (!Display.isCloseRequested()) {


            renderer.processTerrain(terrain);

            // Again ugly and needs work

            player.move(terrain);


            for (Entity lamp : lamps) {
                renderer.processEntity(lamp);
            }

            renderer.render(lights, camera);

            guiRenderer.render(guiImages);
            DisplayManager.updateDisplay();

        }

        cleanUp();
    }

    private void cleanUp() {
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
