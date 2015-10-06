package model.factories;

import model.entities.Entity;
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
    private static final String ENTITY_MAP = "terrains/entityMap";
    private static final String OFFICE_ENTITY_MAP = "terrains/officeEntityMap";


    private Loader loader;

    private ArrayList<Vector3f> commitPositions = new ArrayList<>();

    private Random random = new Random();

    private ModelData pineData;
    private TexturedModel pineTexturedModel;
    private ModelData lampData;
    private TexturedModel lampTexturedModel;
    private ModelData wallData;
    private TexturedModel wallTexturedModel;

    /**
     * Construct a new Entity factor with no models preloaded
     */
    public EntityFactory(Loader loader, Terrain terrain, Terrain office) {
        this.loader = loader;
        initModels();

        BufferedImage image = getBufferedImage(ENTITY_MAP);
        parseEntityMap(terrain, image);

        image = getBufferedImage(OFFICE_ENTITY_MAP);
        parseEntityMap(office, image);
    }

    private void initModels() {
        pineData = OBJFileLoader.loadOBJ(MODEL_PATH + "pine");
        RawModel pineRawModel = loader.loadToVAO(pineData.getVertices(), pineData.getTextureCoords(),
                pineData.getNormals(), pineData.getIndices());
        pineTexturedModel = new TexturedModel(pineRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "pine")));


        lampData = OBJFileLoader.loadOBJ(MODEL_PATH + "lamp");
        RawModel lampRawModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(),
                lampData.getNormals(), lampData.getIndices());
        lampTexturedModel = new TexturedModel(lampRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "lamp")));

        wallData = OBJFileLoader.loadOBJ(MODEL_PATH + "wall");
        RawModel wallRawModel = loader.loadToVAO(wallData.getVertices(), wallData.getTextureCoords(),
                wallData.getNormals(), wallData.getIndices());
        wallTexturedModel = new TexturedModel(wallRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "wall")));
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

    private ArrayList<Entity> entities = new ArrayList<>();

    private void parseEntityMap(Terrain terrain, BufferedImage image) {

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                if (color == -65536) {
                	makeEntity(terrain, i, j, "pine", false);
                } else if (color == -16776961){
                    makeEntity(terrain, i, j, "commit", false);
                } else if (color == -16711936) {
                    makeEntity(terrain, i, j, "lamp", false);
                } else if (color == -16777216) {
                    makeEntity(terrain, i, j, "wall", false);
                } else if (color == -6908266) {
                    makeEntity(terrain, i, j, "wall", true);
                }
            }
        }
    }

    // TODO only load model data once?!?!

    private void makeEntity(Terrain terrain, int i, int j, String entityName, boolean rotate) {

        float x = i + terrain.getGridX();
        float z = j + terrain.getGridZ();
        float y = terrain.getTerrainHeight(x, z);
        float scale = random.nextFloat() + 1;

        if (entityName.equals("commit")) {
            commitPositions.add(new Vector3f(x, y, z));
        } else if (entityName.equals("lamp")) {
            entities.add(new CollidableEntity(lampTexturedModel, new Vector3f(x, y, z), 0,
                    random.nextFloat() * 256f, 0, 1f, 0, lampData));
        } else if (entityName.equals("pine")) {
            y -= 2;
            entities.add(new CollidableEntity(pineTexturedModel, new Vector3f(x, y, z), 0,
                    random.nextFloat() * 256f, 0, scale, 0, pineData));
        } else if (entityName.equals("wall")) {
            if (rotate) {
                entities.add(new CollidableEntity(wallTexturedModel, new Vector3f(x, y, z), 0,
                        90f, 0, 10f, 0, wallData));
            } else {
                entities.add(new CollidableEntity(wallTexturedModel, new Vector3f(x, y, z), 0,
                        0, 0, 10f, 0, wallData));
            }
        }
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Vector3f> getCommitPositions() {
        return commitPositions;
    }
}
