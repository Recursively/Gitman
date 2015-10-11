package model.factories;

import model.entities.Entity;
import model.entities.movableEntity.*;
import model.entities.movableEntity.Laptop;
import model.entities.movableEntity.MovableEntity;
import model.entities.staticEntity.CollidableEntity;
import model.entities.staticEntity.StaticEntity;
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
import java.util.HashMap;
import java.util.Map;
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
    private ModelData whiteboardData;
    private TexturedModel whiteboardTexturedModel;
    private ModelData tableData;
    private TexturedModel tableTexturedModel;
    private ModelData laptopData;
    private TexturedModel laptopTexturedModel;
    private ModelData bugData;
    private TexturedModel bugTexturedModel;
    private ModelData tabletData;
    private static TexturedModel tabletTexturedModel;

    private ModelData swipecardData;
    private TexturedModel[] swipecardTexturedModel = new TexturedModel[5];
    private ModelData commitData;
    private static TexturedModel commitTexturedModel;
    private ModelData flashdriveData;
    private TexturedModel flashdriveTexturedModel;

    private ArrayList<Entity> entities = new ArrayList<>();
    private Map<Integer, MovableEntity> movableEntities = new HashMap<>();
    private static int movableItemID = 0;
    private static int laptopItemID = 0;
    private static int swipecardItemID = 0;
    private static int readmeItemID = 0;
    private static int flashdriveItemID = 0;
    private ModelData portalData;
    private TexturedModel portalTexturedModel;

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
        pineTexturedModel.getTexture().setReflectivity(0);


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
        wallTexturedModel.getTexture().setReflectivity(0);

        whiteboardData = OBJFileLoader.loadOBJ(MODEL_PATH + "free_standing_whiteboard");
        RawModel whiteboardRawModel = loader.loadToVAO(whiteboardData.getVertices(), whiteboardData.getTextureCoords(),
                whiteboardData.getNormals(), whiteboardData.getIndices());
        whiteboardTexturedModel = new TexturedModel(whiteboardRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "free_standing_whiteboard")));
        whiteboardTexturedModel.getTexture().setReflectivity(0);

        tableData = OBJFileLoader.loadOBJ(MODEL_PATH + "table_with_drawer");
        RawModel tableRawModel = loader.loadToVAO(tableData.getVertices(), tableData.getTextureCoords(),
                tableData.getNormals(), tableData.getIndices());
        tableTexturedModel = new TexturedModel(tableRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "table_with_drawer")));

        laptopData = OBJFileLoader.loadOBJ(MODEL_PATH + "laptop");
        RawModel laptopRawModel = loader.loadToVAO(laptopData.getVertices(), laptopData.getTextureCoords(),
                laptopData.getNormals(), laptopData.getIndices());
        laptopTexturedModel = new TexturedModel(laptopRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "laptop")));

        bugData = OBJFileLoader.loadOBJ(MODEL_PATH + "bug");
        RawModel bugRawModel = loader.loadToVAO(bugData.getVertices(), bugData.getTextureCoords(),
                bugData.getNormals(), bugData.getIndices());
        bugTexturedModel = new TexturedModel(bugRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "bug")));

        tabletData = OBJFileLoader.loadOBJ(MODEL_PATH + "tablet");
        RawModel tabletRawModel = loader.loadToVAO(tabletData.getVertices(), tabletData.getTextureCoords(),
                tabletData.getNormals(), tabletData.getIndices());
        EntityFactory.tabletTexturedModel = new TexturedModel(tabletRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "tablet")));

        commitData = OBJFileLoader.loadOBJ(MODEL_PATH + "commit_cube");
        RawModel commitRawModel = loader.loadToVAO(commitData.getVertices(), commitData.getTextureCoords(),
                commitData.getNormals(), commitData.getIndices());
        commitTexturedModel = new TexturedModel(commitRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "commit_cube")));

        flashdriveData = OBJFileLoader.loadOBJ(MODEL_PATH + "flash_drive");
        RawModel flashdriveRawModel = loader.loadToVAO(flashdriveData.getVertices(), flashdriveData.getTextureCoords(),
                flashdriveData.getNormals(), flashdriveData.getIndices());
        flashdriveTexturedModel = new TexturedModel(flashdriveRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "flash_drive")));

        swipecardData = OBJFileLoader.loadOBJ(MODEL_PATH + "swipe_card");
        for (int i = 0; i < 5; i++) {
            RawModel swipecardRawModel = loader.loadToVAO(swipecardData.getVertices(), swipecardData.getTextureCoords(),
                    swipecardData.getNormals(), swipecardData.getIndices());
            swipecardTexturedModel[i] = new TexturedModel(swipecardRawModel,
                    new ModelTexture(loader.loadTexture(TEXTURES_PATH + "swipe_card" + i)));
        }


        portalData = OBJFileLoader.loadOBJ(MODEL_PATH + "portal");
        RawModel portalRawModel = loader.loadToVAO(portalData.getVertices(), portalData.getTextureCoords(),
                portalData.getNormals(), portalData.getIndices());
        portalTexturedModel = new TexturedModel(portalRawModel,
                new ModelTexture(loader.loadTexture(TEXTURES_PATH + "portal")));
        portalTexturedModel.getTexture().setReflectivity(0);
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
                } else if (color == -196864) {
                    makeEntity(terrain, i, j, "free_standing_whiteboard", false);
                } else if (color == -16713985) {
                    makeEntity(terrain, i, j, "table_with_drawer", false);
                } else if (color == -261889) {
                    makeEntity(terrain, i, j, "laptop", false);
                } else if (color == -28672) {
                    makeEntity(terrain, i, j, "bug", false);
                } else if (color == -16747777) {
                    makeEntity(terrain, i, j, "swipe_card", false);
                } else if (color == -16711810) {
                    makeEntity(terrain, i, j, "tablet", false);
                } else if (color == -4980481) {
                    makeEntity(terrain, i, j, "flash_drive", false);
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
            LightFactory.createEntityLight(new Vector3f(x, y + 12, z + 2));
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
        } else if (entityName.equals("free_standing_whiteboard")) {
            y += 8;
            entities.add(new CollidableEntity(whiteboardTexturedModel, new Vector3f(x, y, z), 0,
                    270f, 0, 1.5f, 0, whiteboardData));
        } else if (entityName.equals("table_with_drawer")) {
            y += 4;
            entities.add(new CollidableEntity(tableTexturedModel, new Vector3f(x, y, z), 0,
                    270f, 0, 1.5f, 0, tableData));
        }

        // Movable entities

        else if (entityName.equals("laptop")) {
            y += 6.5;
            movableEntities.put(EntityFactory.movableItemID, new Laptop(laptopTexturedModel, new Vector3f(x, y, z), 0,
                    270f, 0, 1f,  EntityFactory.movableItemID++, false, EntityFactory.laptopItemID++));
        } else if (entityName.equals("bug")) {
            y += 15;
            movableEntities.put(EntityFactory.movableItemID++, new Bug(bugTexturedModel, new Vector3f(x, y, z), 0,
                    270f, 0, 10f, 0));
        } else if (entityName.equals("swipe_card")) {
            y += 3.5;
            z += 4.5;
            x -= 2.5;
            movableEntities.put(EntityFactory.movableItemID, new SwipeCard(
                    swipecardTexturedModel[EntityFactory.swipecardItemID], new Vector3f(x, y, z), 0, 180, 0, 0.4f,
                    EntityFactory.movableItemID++, EntityFactory.swipecardItemID++));
        } else if (entityName.equals("tablet")) {
            y += 6.5;
            movableEntities.put(EntityFactory.movableItemID, new ReadMe(tabletTexturedModel, new Vector3f(x, y, z), 0,
                    270f, 0, 1f, EntityFactory.movableItemID++, "readme1" + EntityFactory.readmeItemID++));
        } else if (entityName.equals("flash_drive")) {
            y += 3.3;
            z += 2;
            x -= 2;
            movableEntities.put(EntityFactory.movableItemID, new FlashDrive(flashdriveTexturedModel, new Vector3f(x, y, z),
            0, 180, 0, 0.5f, EntityFactory.movableItemID++, "extImg" + EntityFactory.flashdriveItemID++));
        }
    }

    public StaticEntity makePortal(Vector3f position, Terrain terrain) {
        position.y += terrain.getTerrainHeight(position.getX(), position.getZ()) + 10;
        return new CollidableEntity(portalTexturedModel, position, 0, 0, 0, 10f, 0, portalData);
    }


    public static Commit createCommit(Vector3f position) {
        position.y += 10;
        return new Commit(EntityFactory.commitTexturedModel, position, 0, 0, 0, 1f, EntityFactory.movableItemID++);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Vector3f> getCommitPositions() {
        return commitPositions;
    }

    public Map<Integer, MovableEntity> getMovableEntities() {
        return movableEntities;
    }

    public TexturedModel getFlashdriveTexturedModel() {
        return flashdriveTexturedModel;
    }

    public static TexturedModel getCommitTexturedModel() {
        return commitTexturedModel;
    }

    public TexturedModel[] getSwipecardTexturedModel() {
        return swipecardTexturedModel;
    }

    public static TexturedModel getTabletTexturedModel() {
        return tabletTexturedModel;
    }

    public TexturedModel getBugTexturedModel() {
        return bugTexturedModel;
    }

    public TexturedModel getLaptopTexturedModel() {
        return laptopTexturedModel;
    }

    public int getMovableEntitiesID() {
        return EntityFactory.movableItemID;
    }

    public void increaseMovableEntitiesID() {
        EntityFactory.movableItemID++;
    }
}
