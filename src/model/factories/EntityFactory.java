package model.factories;

import model.entities.Entity;
import model.entities.staticEntity.StaticEntity;
import model.entities.staticEntity.CollidableEntity;
import model.models.ModelData;
import model.models.RawModel;
import model.models.TexturedModel;
import model.terrains.Terrain;
import model.textures.ModelTexture;
import model.toolbox.Loader;
import model.toolbox.objParser.OBJFileLoader;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private static final String ENTITY_MAP = "terrains/entityMap";

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
    public EntityFactory(Loader loader, Terrain terrain) {
        parseEntityMap(loader, terrain);
        //getTestEntity(loader, terrain);
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


    // ENTITY MAP DEBUGGING

    private ArrayList<Entity> testEntities = new ArrayList<>();

    /**
     * Attempts to parse the height map
     *
     * @param entityMap Height map
     * @return Buffered image
     */
    private BufferedImage getBufferedImage(String entityMap) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + entityMap + ".png"));
        } catch (IOException e) {
            System.err.println("Failed to load entity map");
            e.printStackTrace();
        }
        return image;
    }

    private void parseEntityMap(Loader loader, Terrain terrain) {
        BufferedImage image = getBufferedImage(ENTITY_MAP);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                if (color == -65536) {
                	makePineTree(loader, terrain, i, j); 
                } else if (color == -11731200) {
                    makeRandomLamp(loader, terrain, i, j);
                } else if (color == -10240){
                    makeRandomOrb(loader, terrain, i, j);
                }
            }
        }
    }

    private void makeRandomOrb(Loader loader, Terrain terrain, int i, int j) {
        ModelData data = OBJFileLoader.loadOBJ("models/orb");
        RawModel lowPolyTreeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
                data.getIndices());

        TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,
                new ModelTexture(loader.loadTexture("textures/orb")));
        lowPolyTreeTexturedModel.getTexture().setShineDamper(10);
        lowPolyTreeTexturedModel.getTexture().setReflectivity(1);


        float x = i;
        float z = j - 256;
        float y = terrain.getTerrainHeight(x, z) + 10;

        LightFactory.createPlayerOrbLight(x, y + 10, z);

        StaticEntity e = new CollidableEntity(lowPolyTreeTexturedModel, new Vector3f(x, y, z), 0, random.nextFloat() * 256f, 0, 2f, 0, data);

        testEntities.add(e);
    }

    private void makeRandomLamp(Loader loader, Terrain terrain, int i, int j) {
        ModelData data = OBJFileLoader.loadOBJ("models/lamp");
        RawModel lowPolyTreeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
                data.getIndices());

        TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,
                new ModelTexture(loader.loadTexture("textures/lamp")));
        lowPolyTreeTexturedModel.getTexture().setShineDamper(10);
        lowPolyTreeTexturedModel.getTexture().setReflectivity(1);


        float x = i;
        float z = j - 256;
        float y = terrain.getTerrainHeight(x, z);

        LightFactory.createRandomEntityLight(x, y + 3.5f, z);

        StaticEntity e = new CollidableEntity(lowPolyTreeTexturedModel, new Vector3f(x, y, z), 0, random.nextFloat() * 256f, 0, 1f, 0, data);

        testEntities.add(e);
    }

    private void makePineTree(Loader loader, Terrain terrain, int i, int j) {
        ModelData data = OBJFileLoader.loadOBJ("models/pine");
        RawModel lowPolyTreeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
                data.getIndices());

        TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,
                new ModelTexture(loader.loadTexture("textures/pine")));
        lowPolyTreeTexturedModel.getTexture().setShineDamper(10);
        lowPolyTreeTexturedModel.getTexture().setReflectivity(1);


        float x = i;
        float z = j - 256;
        float y = terrain.getTerrainHeight(x, z) - 2;

        StaticEntity e = new CollidableEntity(lowPolyTreeTexturedModel, new Vector3f(x, y, z), 0, random.nextFloat() * 256f, 0, 1f, 0, data);

        testEntities.add(e);
    }

    public void getTestEntity(Loader loader, Terrain terrain) {
        for (int i = 0; i < 100; i++) {
            ModelData data = OBJFileLoader.loadOBJ("models/lowPolyTree");
            RawModel lowPolyTreeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
                    data.getIndices());

            TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,
                    new ModelTexture(loader.loadTexture("textures/lowPolyTree")));
            lowPolyTreeTexturedModel.getTexture().setNumberOfRows(2);
            lowPolyTreeTexturedModel.getTexture().setShineDamper(10);
            lowPolyTreeTexturedModel.getTexture().setReflectivity(1);

            float x = random.nextInt(256);
            float z = random.nextInt(256) - 256;
            float y = terrain.getTerrainHeight(x, z) + 10;

            StaticEntity e = new CollidableEntity(lowPolyTreeTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 1f, random.nextInt(4), data);

            testEntities.add(e);
        }
    }

    public ArrayList<Entity> getTestEntities() {
        return testEntities;
    }
}
