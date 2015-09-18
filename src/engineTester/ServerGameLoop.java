package engineTester;

import model.Network.Server;
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
import model.toolbox.OBJLoader;
import model.toolbox.objParser.OBJFileLoader;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.sun.org.apache.bcel.internal.generic.NEW;

import controller.NetworkController;
import view.DisplayManager;
import view.renderEngine.GuiRenderer;
import view.renderEngine.MasterRenderer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerGameLoop {

	public static void main(String[] args) throws IOException {
		DisplayManager.createDisplay();

		Random random = new Random();
		Loader loader = new Loader();

		// THIS CODE IS UGLY PLEASE DON'T JUDGE ME!!

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
		// TODO remove this
		Light light = new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f));
		List<Light> lights = new ArrayList<>();
		lights.add(light);
		lights.add(new Light(new Vector3f(0, -20, 0), new Vector3f(15, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370, -20, -300), new Vector3f(0, 15, 15), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(295, -20, -300), new Vector3f(15, 15, 0), new Vector3f(1, 0.01f, 0.002f)));

		ModelData lampData = OBJFileLoader.loadOBJ("models/lamp");
		RawModel lampModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(),
				lampData.getNormals(), lampData.getIndices());

		TexturedModel lampTexturedModel = new TexturedModel(lampModel,
				new ModelTexture(loader.loadTexture("textures/lamp")));
		lampTexturedModel.getTexture().setNumberOfRows(2);
		lampTexturedModel.getTexture().setShineDamper(10);
		lampTexturedModel.getTexture().setReflectivity(1);

		List<Entity> lamps = new ArrayList<>();

		lamps.add(new Entity(lampTexturedModel, new Vector3f(0, -20, 0), 0, 0, 0, 1));
		lamps.add(new Entity(lampTexturedModel, new Vector3f(370, -20, -300), 0, 0, 0, 1));
		lamps.add(new Entity(lampTexturedModel, new Vector3f(295, -20, -300), 0, 0, 0, 1));

		// BEGIN UGLY MODEL LOADING
		// TODO should use factory design pattern fro this

		ModelData data = OBJFileLoader.loadOBJ("models/lowPolyTree");
		RawModel lowPolyTreeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices());

		TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,
				new ModelTexture(loader.loadTexture("textures/lowPolyTree")));
		lowPolyTreeTexturedModel.getTexture().setNumberOfRows(2);
		lowPolyTreeTexturedModel.getTexture().setShineDamper(10);
		lowPolyTreeTexturedModel.getTexture().setReflectivity(1);

		List<Entity> allPolyTrees = new ArrayList<>();

		for (int i = 0; i < 200; i++) {
			float x = random.nextFloat() * 1000;
			float z = random.nextFloat() * -1000;
			float y = terrain.getTerrainHeight(x, z);
			if ((x > 400 && x < 600) && (z > -600 && z < -400)) {
				continue;
			}
			allPolyTrees
					.add(new Entity(lowPolyTreeTexturedModel, new Vector3f(x, y, z), 0, 0, 0f, 1f, random.nextInt(4)));
		}

		ModelData data2 = OBJFileLoader.loadOBJ("models/grassClumps");
		RawModel grassModel = loader.loadToVAO(data2.getVertices(), data2.getTextureCoords(), data2.getNormals(),
				data2.getIndices());

		TexturedModel grassModelTexturedModel = new TexturedModel(grassModel,
				new ModelTexture(loader.loadTexture("textures/grassClumps")));
		grassModelTexturedModel.getTexture().setShineDamper(10);
		grassModelTexturedModel.getTexture().setReflectivity(1);
		grassModelTexturedModel.getTexture().setHasTransparency(true);
		grassModelTexturedModel.getTexture().setUseFakeLighting(true);

		List<Entity> allGrass = new ArrayList<>();

		for (int i = 0; i < 500; i++) {
			float x = random.nextFloat() * 1000;
			float z = random.nextFloat() * -1000;
			float y = terrain.getTerrainHeight(x, z);
			if ((x > 400 && x < 600) && (z > -600 && z < -400)) {
				continue;
			}
			allGrass.add(new Entity(grassModelTexturedModel, new Vector3f(x, y, z), 0, 0, 0f, 1f));
		}

		ModelData data3 = OBJFileLoader.loadOBJ("models/stall");
		RawModel stallModel = loader.loadToVAO(data3.getVertices(), data3.getTextureCoords(), data3.getNormals(),
				data3.getIndices());

		TexturedModel stallTexturedModel = new TexturedModel(stallModel,
				new ModelTexture(loader.loadTexture("textures/stall")));
		stallTexturedModel.getTexture().setShineDamper(10);
		stallTexturedModel.getTexture().setReflectivity(1);

		List<Entity> allStalls = new ArrayList<>();

		for (int i = 0; i < 25; i++) {
			float x = random.nextFloat() * 1000;
			float z = random.nextFloat() * -1000;
			float y = terrain.getTerrainHeight(x, z);
			if ((x > 400 && x < 600) && (z > -600 && z < -400)) {
				continue;
			}
			allStalls.add(new Entity(stallTexturedModel, new Vector3f(x, y, z), 0, 0, 0f, 4f));
		}

		TexturedModel treeModel = new TexturedModel(OBJLoader.loadObjModel("models/tree", loader),
				new ModelTexture(loader.loadTexture("textures/tree")));
		ModelTexture treeTexture = treeModel.getTexture();
		// TODO This is broken
		treeTexture.setShineDamper(10);
		treeTexture.setReflectivity(1);
		treeTexture.setHasTransparency(false);

		List<Entity> allTrees = new ArrayList<>();

		for (int i = 0; i < 200; i++) {
			float x = random.nextFloat() * 1000;
			float z = random.nextFloat() * -1000;
			float y = terrain.getTerrainHeight(x, z);
			if ((x > 400 && x < 600) && (z > -600 && z < -400)) {
				continue;
			}
			allTrees.add(new Entity(treeModel, new Vector3f(x, y, z), 0, 0, 0f, 10f));
		}

		TexturedModel fernModel = new TexturedModel(OBJLoader.loadObjModel("models/fern", loader),
				new ModelTexture(loader.loadTexture("textures/fern")));
		ModelTexture fernTexture = fernModel.getTexture();
		fernTexture.setNumberOfRows(2);
		fernTexture.setShineDamper(10);
		fernTexture.setReflectivity(1);
		fernTexture.setHasTransparency(true);

		List<Entity> allFerns = new ArrayList<>();

		for (int i = 0; i < 300; i++) {
			float x = random.nextFloat() * 1000;
			float z = random.nextFloat() * -1000;
			float y = terrain.getTerrainHeight(x, z);
			allFerns.add(new Entity(fernModel, new Vector3f(x, y, z), 0, 0, 0f, 1f, random.nextInt(4)));
		}

		// create single models
		// TODO again should use factory design pattern here

		TexturedModel dragonModel = new TexturedModel(OBJLoader.loadObjModel("models/dragon", loader),
				new ModelTexture(loader.loadTexture("textures/white")));
		ModelTexture dragonTexture = dragonModel.getTexture();
		dragonTexture.setShineDamper(10);
		dragonTexture.setReflectivity(1);

		TexturedModel bunnyModel = new TexturedModel(OBJLoader.loadObjModel("models/bunny", loader),
				new ModelTexture(loader.loadTexture("textures/white")));
		ModelTexture bunnyTexture = bunnyModel.getTexture();
		bunnyTexture.setShineDamper(10);
		bunnyTexture.setReflectivity(1);

		TexturedModel playerModel = new TexturedModel(OBJLoader.loadObjModel("models/player", loader),
				new ModelTexture(loader.loadTexture("textures/white")));
		ModelTexture playerTexture = playerModel.getTexture();
		playerTexture.setShineDamper(10);
		playerTexture.setReflectivity(1);

		// Create gui elements

		List<GuiTexture> guiImages = new ArrayList<>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("gui/panel_brown"), new Vector2f(-0.75f, 0.75f),
				new Vector2f(0.25f, 0.25f));
		guiImages.add(gui);

		// gui renderer which handles rendering an infinite amount of gui
		// elements
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		// /

		Vector3f playerPosition = new Vector3f(50, 0, -50);
		float initialPlayerY = terrain.getTerrainHeight(playerPosition.getX(), playerPosition.getZ());

		int count = 0;

		// New player and camera to follow the player
		Camera camera = new Camera(initialPlayerY, 10, playerPosition);
		Player player = new Player(playerModel, playerPosition, 0, 180f, 0, 1, camera, count++);

		// TODO do we want the mouse to be captured?
		// It makes sense to be captured if game is first person, not so much
		// for third person
		Mouse.setGrabbed(true);

		// This renders all the goodies
		MasterRenderer renderer = new MasterRenderer(loader);

		/* Server stuff */

		ArrayList<Player> players = new ArrayList<>();

		// TODO create more players

		ServerSocket ss = new ServerSocket(32768);
		// accept more connections per player
		//

		new NetworkController(ss, players).start();

		while (!Display.isCloseRequested()) {
			renderer.processTerrain(terrain);

			// Again ugly and needs work

			player.move(terrain);
			// renderer.processEntity(player);
			for (Player player2 : players) {
				renderer.processEntity(player2);
			}

			renderer.processEntity(new Entity(dragonModel, new Vector3f(500, terrain.getTerrainHeight(500, -500), -500),
					0, 0, 0f, 10f));
			renderer.processEntity(new Entity(bunnyModel, new Vector3f(250, terrain.getTerrainHeight(250, -500), -500),
					0, 0, 0f, 10f));

			for (Entity lamp : lamps) {
				renderer.processEntity(lamp);
			}

			for (Entity tree : allTrees) {
				renderer.processEntity(tree);
			}

			for (Entity fern : allFerns) {
				renderer.processEntity(fern);
			}

			for (Entity polyTree : allPolyTrees) {
				renderer.processEntity(polyTree);
			}

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

			renderer.render(lights, camera);

			guiRenderer.render(guiImages);

			DisplayManager.updateDisplay();

		}

		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
