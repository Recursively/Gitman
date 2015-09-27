package model.factories;

import model.entities.Entity;
import model.models.ModelData;
import model.models.RawModel;
import model.models.TexturedModel;
import model.terrains.Terrain;
import model.textures.ModelTexture;
import model.toolbox.Loader;
import model.toolbox.objParser.OBJFileLoader;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Entity factory which abstracts the creation of an entity. It loads the entity map for a given terrain or
 * randomly generates entities for testing.
 *
 * @author Marcel van Workum
 */
public class EntityFactory {

    // Paths to the object and textures files
    private static final String MODEL_PATH = "models/";
    private static final String TEXTURES_PATH = "textures/";

    // Collection of object models in the game
    private ArrayList<String> objectModels;

    // parameters
    private float reflectivity = 1f;
    private float shineDamper = 10f;
    private float scale = 1f;
    private float maxTerrainOffset = 200f;

    private Vector3f rotation = new Vector3f(0, 0, 0);
    private Random random = new Random();

    /**
     * Construct a new Entity factor with no models preloaded
     */
    public EntityFactory() {

    }

    /**
     * Creates the object models collections and loads the list of modesl to each
     *
     * @param models Basic models
     */
    public EntityFactory(List<String> models) {
        objectModels = new ArrayList<>();

        objectModels.addAll(models);
    }

    /**
     * Adds a object model name to the hashset of available objects
     *
     * @param objectModel Name of object model to add
     */
    public void addObjectToSet(String objectModel) {
        objectModels.add(objectModel);
    }

    /**
     * Create random entity entity.
     *
     * @param loader  the loader
     * @param terrain the terrain
     * @return the entity
     */
    public Entity createRandomEntity(Loader loader, Terrain terrain) {
        return makeRandomEntity(random.nextInt(objectModels.size()), loader, terrain);
    }

    // HELPER METHODS

    private Entity makeRandomEntity(int i, Loader loader, Terrain terrain) {
        return makeFernEntity(loader, terrain, objectModels.get(i));
    }

    private Entity makeFernEntity(Loader loader, Terrain terrain, String name) {
        TexturedModel texturedModel = getTexturedModel(loader, name);

        //setupTexture(texturedModel, name);

        return constructEntity(terrain, texturedModel);
    }

    private Entity constructEntity(Terrain terrain, TexturedModel texturedModel) {
        float x = random.nextFloat() * maxTerrainOffset;
        float z = random.nextFloat() * -maxTerrainOffset;
        float y = terrain.getTerrainHeight(x, z);

        return new Entity(texturedModel, new Vector3f(x, y, z), rotation.x, rotation.y, rotation.z, scale);
    }

    private void setupTexture(TexturedModel texturedModel, String name) {
        ModelTexture modelTexture = texturedModel.getTexture();

        modelTexture.setShineDamper(shineDamper);
        modelTexture.setReflectivity(reflectivity);

        // Have to deal with cases where extra parameters must be added for transparency or atlassing

        // gotta have at least one switch right?

        switch (name) {
            case "fern":
                modelTexture.setNumberOfRows(2);
                modelTexture.setHasTransparency(true);
                break;
            case "grassClumps":
                modelTexture.setHasTransparency(true);
                modelTexture.setUseFakeLighting(true);
                break;
            case "lowPolyTree":
                modelTexture.setNumberOfRows(2);
                break;
        }
    }

    private TexturedModel getTexturedModel(Loader loader, String name) {
        ModelData data = OBJFileLoader.loadOBJ(MODEL_PATH + name);
        RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
                data.getIndices());

        return new TexturedModel(model,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + name)));
    }


    // TESTING METHOD
    // TODO REMOVE #generateRandomMap

    public ArrayList<Entity> generateRandomMap(Loader loader, Terrain terrain) {
        ModelData data = OBJFileLoader.loadOBJ("models/lowPolyTree");
        RawModel lowPolyTreeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
                data.getIndices());

        TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,
                new ModelTexture(loader.loadTexture("textures/lowPolyTree")));
        lowPolyTreeTexturedModel.getTexture().setNumberOfRows(2);
        lowPolyTreeTexturedModel.getTexture().setShineDamper(10);
        lowPolyTreeTexturedModel.getTexture().setReflectivity(1);

        ArrayList<Entity> allPolyTrees = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            float x = random.nextFloat() * 256;
            float z = random.nextFloat() * -256;
            float y = terrain.getTerrainHeight(x, z);
            allPolyTrees.add(new Entity(lowPolyTreeTexturedModel, new Vector3f(x, y, z), 0,
                    0, 0f, 1f, random.nextInt(4)));
        }

        return allPolyTrees;
    }
}
