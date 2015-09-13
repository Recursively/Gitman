package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objParser.ModelData;
import objParser.OBJFileLoader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Random random = new Random();
        Loader loader = new Loader();

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mudTexture"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowersTexture"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("pathTexture"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");

        Light light = new Light(new Vector3f(-3000, 2000, -3000), new Vector3f(1, 1, 1));

        ModelData data = OBJFileLoader.loadOBJ("lowPolyTree");
        RawModel lowPolyTreeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
                data.getIndices());

        TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,
                new ModelTexture(loader.loadTexture("lowPolyTree")));
        lowPolyTreeTexturedModel.getTexture().setShineDamper(10);
        lowPolyTreeTexturedModel.getTexture().setReflectivity(1);

        List<Entity> allPolyTrees = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 1000;
            float y = 0;
            float z = random.nextFloat() * -1000;
            if ((x > 400 && x < 600) && (z > -600 && z < -400)) {
                continue;
            }
            allPolyTrees.add(new Entity(lowPolyTreeTexturedModel, new Vector3f(x, y, z), 0,
                    0, 0f, 1f));
        }

        ModelData data2 = OBJFileLoader.loadOBJ("grassModel");
        RawModel grassModel = loader.loadToVAO(data2.getVertices(), data2.getTextureCoords(), data2.getNormals(),
                data2.getIndices());

        TexturedModel grassModelTexturedModel = new TexturedModel(grassModel,
                new ModelTexture(loader.loadTexture("grassTexture")));
        grassModelTexturedModel.getTexture().setShineDamper(10);
        grassModelTexturedModel.getTexture().setReflectivity(1);
        grassModelTexturedModel.getTexture().setHasTransparency(true);
        grassModelTexturedModel.getTexture().setUseFakeLighting(true);

        List<Entity> allGrass = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            float x = random.nextFloat() * 1000;
            float y = 0;
            float z = random.nextFloat() * -1000;
            if ((x > 400 && x < 600) && (z > -600 && z < -400)) {
                continue;
            }
            allGrass.add(new Entity(grassModelTexturedModel, new Vector3f(x, y, z), 0,
                    0, 0f, 1f));
        }

        ModelData data3 = OBJFileLoader.loadOBJ("stall");
        RawModel stallModel = loader.loadToVAO(data3.getVertices(), data3.getTextureCoords(), data3.getNormals(),
                data3.getIndices());

        TexturedModel stallTexturedModel = new TexturedModel(stallModel,
                new ModelTexture(loader.loadTexture("stallTexture")));
        stallTexturedModel.getTexture().setShineDamper(10);
        stallTexturedModel.getTexture().setReflectivity(1);

        List<Entity> allStalls = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            float x = random.nextFloat() * 1000;
            float y = 0;
            float z = random.nextFloat() * -1000;
            if ((x > 400 && x < 600) && (z > -600 && z < -400)) {
                continue;
            }
            allStalls.add(new Entity(stallTexturedModel, new Vector3f(x, y, z), 0,
                    0, 0f, 4f));
        }

        TexturedModel treeModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
        ModelTexture treeTexture = treeModel.getTexture();
        // TODO This is broken
        treeTexture.setShineDamper(10);
        treeTexture.setReflectivity(1);
        treeTexture.setHasTransparency(false);

        List<Entity> allTrees = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 1000;
            float y = 0;
            float z = random.nextFloat() * -1000;
            if ((x > 400 && x < 600) && (z > -600 && z < -400)) {
                continue;
            }
            allTrees.add(new Entity(treeModel, new Vector3f(x, y, z), 0,
                    0, 0f, 10f));
        }

        TexturedModel fernModel = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
                new ModelTexture(loader.loadTexture("fern")));
        ModelTexture fernTexture = fernModel.getTexture();
        fernTexture.setShineDamper(10);
        fernTexture.setReflectivity(1);
        fernTexture.setHasTransparency(true);

        List<Entity> allFerns = new ArrayList<>();

        for (int i = 0; i < 300; i++) {
            float x = random.nextFloat() * 1000;
            float y = 0;
            float z = random.nextFloat() * -1000 ;
            allFerns.add(new Entity(fernModel, new Vector3f(x, y, z), 0,
                    0, 0f, 1f));
        }

        ///

        TexturedModel dragonModel = new TexturedModel(OBJLoader.loadObjModel("dragon", loader),
                new ModelTexture(loader.loadTexture("white")));
        ModelTexture dragonTexture = dragonModel.getTexture();
        dragonTexture.setShineDamper(10);
        dragonTexture.setReflectivity(1);

        TexturedModel bunnyModel = new TexturedModel(OBJLoader.loadObjModel("bunny", loader),
                new ModelTexture(loader.loadTexture("white")));
        ModelTexture bunnyTexture = bunnyModel.getTexture();
        bunnyTexture.setShineDamper(10);
        bunnyTexture.setReflectivity(1);

        TexturedModel exampleModel = new TexturedModel(OBJLoader.loadObjModel("exampleOBJ", loader),
                new ModelTexture(loader.loadTexture("white")));
        ModelTexture exmapleTexture = exampleModel.getTexture();
        exmapleTexture.setShineDamper(10);
        exmapleTexture.setReflectivity(1);

        TexturedModel playerModel = new TexturedModel(OBJLoader.loadObjModel("player", loader),
                new ModelTexture(loader.loadTexture("white")));
        ModelTexture playerTexture = playerModel.getTexture();
        bunnyTexture.setShineDamper(10);
        bunnyTexture.setReflectivity(1);

        ///

        Player player = new Player(playerModel, new Vector3f(50, 0, -50), 0, 180f, 0, 1);
        Camera camera = new Camera(player);

        //TODO
        Mouse.setGrabbed(true);

        MasterRenderer renderer = new MasterRenderer();
        while (!Display.isCloseRequested()) {
            camera.move();
            renderer.processTerrain(terrain);

            player.move(terrain);
            renderer.processEntity(player);

            renderer.processEntity(new Entity(dragonModel, new Vector3f(500, 0, -500), 0, 0, 0f, 10f));
            renderer.processEntity(new Entity(bunnyModel, new Vector3f(250, 0, -500), 0, 0, 0f, 10f));
            renderer.processEntity(new Entity(exampleModel, new Vector3f(0, 0, -500), 0, 0, 0f, 10f));


            for (Entity tree : allTrees) {
                renderer.processEntity(tree);
            }

            for (Entity fern : allFerns) {
                renderer.processEntity(fern);
            }

            for (Entity polyTree : allPolyTrees) {
                renderer.processEntity(polyTree);
            }

            for (Entity stall : allStalls) {
                renderer.processEntity(stall);
            }

            for (Entity grass : allGrass) {
                renderer.processEntity(grass);
            }

            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
