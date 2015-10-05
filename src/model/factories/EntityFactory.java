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
    private static final String ENTITY_MAP = "terrains/entityMap2";

    // parameters
    private float reflectivity = 1f;
    private float shineDamper = 10f;
    private float scale = 1f;

    private Random random = new Random();

    /**
     * Construct a new Entity factor with no models preloaded
     */
    public EntityFactory(Loader loader, Terrain terrain) {
        parseEntityMap(loader, terrain);
    }

    // HELPER METHOD

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

    // ENTITY MAP DEBUGGING

    private ArrayList<Entity> testEntities = new ArrayList<>();

    private void parseEntityMap(Loader loader, Terrain terrain) {
        BufferedImage image = getBufferedImage(ENTITY_MAP);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                if (color == -65536) {
                	makeEntity(loader, terrain, i, j, "pine");
                }
            }
        }
    }

    private void makeEntity(Loader loader, Terrain terrain, int i, int j, String entityName) {
        ModelData data = OBJFileLoader.loadOBJ(MODEL_PATH + entityName);
        RawModel lowPolyTreeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
                data.getIndices());

        TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + entityName)));
        lowPolyTreeTexturedModel.getTexture().setShineDamper(shineDamper);
        lowPolyTreeTexturedModel.getTexture().setReflectivity(reflectivity);


        float x = i;
        float z = j - Terrain.getSIZE();
        float y = terrain.getTerrainHeight(x, z) - 2;

        StaticEntity e = new CollidableEntity(lowPolyTreeTexturedModel, new Vector3f(x, y, z), 0, random.nextFloat() * 256f, 0, 1f, 0, data);
        testEntities.add(e);
    }

    public ArrayList<Entity> getTestEntities() {
        return testEntities;
    }
}
