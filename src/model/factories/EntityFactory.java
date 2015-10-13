package model.factories;

import model.entities.Entity;
import model.entities.movableEntity.*;
import model.entities.staticEntity.CollidableEntity;
import model.entities.staticEntity.OfficeEntity;
import model.entities.staticEntity.StaticEntity;
import model.models.EntityModel;
import model.models.TexturedModel;
import model.terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Entity factory which abstracts the creation of an entity. It loads the entity
 * map for a given terrain or randomly generates entities for testing.
 * <p/>
 * This is probably the ugliest class I have ever written.
 *
 * @author Marcel van Workum
 * @author Reuben Puketapu
 */
public class EntityFactory {
    // commit position is 10 above the ground
    private static final int COMMIT_OFFSET_Y = 0;
    private static final int DEFAULT_REFLECTIVITY = -1;

    // Paths to the object and textures files
    private static final String ENTITY_MAP = "terrains/entityMap";
    private static final String OFFICE_ENTITY_MAP = "terrains/officeEntityMap";
    private static final Random random = new Random();

    // Entity collections
    private Map<Integer, MovableEntity> movableEntities = new HashMap<>();
    private ArrayList<Vector3f> commitPositions = new ArrayList<>();
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> wallEntities = new ArrayList<>();

    // Entity models
    private static EntityModel pineEntity;
    private static EntityModel lampEntity;
    private static EntityModel wallEntity;
    private static EntityModel whiteboardEntity;
    private static EntityModel tableEntity;
    private static EntityModel laptopEntity;
    private static EntityModel bugEntity;
    private static EntityModel tabletEntity;
    private static EntityModel commitEntity;
    private static EntityModel flashDriveEntity;
    private static EntityModel portalEntity;
    private static EntityModel[] swipeCardEntities;
    private static EntityModel playerEntity;

    // Entity stats
    private static int movableItemID = 0;
    private static int laptopItemID = 0;
    private static int swipeCardItemID = 0;
    private static int readmeItemID = 0;
    private static int flashDriveItemID = 0;

    /**
     * Construct a new Entity factor with no models preloaded
     */
    public EntityFactory(Terrain terrain, Terrain office) {
        // Initialises the player model
        initModels();

        // parses the outside entity map
        BufferedImage image = getBufferedImage(ENTITY_MAP);
        parseEntityMap(terrain, image);

        // parses the office entity map
        image = getBufferedImage(OFFICE_ENTITY_MAP);
        parseEntityMap(office, image);
    }

    /**
     * Parses the entity models
     */
    private void initModels() {
        pineEntity = new EntityModel("pine", 0);
        lampEntity = new EntityModel("lamp", DEFAULT_REFLECTIVITY);
        wallEntity = new EntityModel("wall", 0.1f);
        whiteboardEntity = new EntityModel("free_standing_whiteboard", 0);
        tableEntity = new EntityModel("table_with_drawer", DEFAULT_REFLECTIVITY);
        laptopEntity = new EntityModel("laptop", DEFAULT_REFLECTIVITY);
        bugEntity = new EntityModel("bug", DEFAULT_REFLECTIVITY);
        tabletEntity = new EntityModel("tablet", DEFAULT_REFLECTIVITY);
        commitEntity = new EntityModel("commit_cube", DEFAULT_REFLECTIVITY);
        flashDriveEntity = new EntityModel("flash_drive", DEFAULT_REFLECTIVITY);
        portalEntity = new EntityModel("portal", 0);
        swipeCardEntities = new EntityModel[5];

        for (int i = 0; i < 5; i++) {
            swipeCardEntities[i] = new EntityModel("swipe_card", i, DEFAULT_REFLECTIVITY);
        }
    }

    /**
     * Parses the entity map
     *
     * @param terrain Terrain the map is for
     * @param image   Image of the entity map
     */
    private void parseEntityMap(Terrain terrain, BufferedImage image) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                // Switching it up ^_^
                switch (image.getRGB(i, j)) {
                    case -65536:
                        makeEntity(terrain, i, j, "pine", false);
                        break;
                    case -16776961:
                        makeEntity(terrain, i, j, "commit", false);
                        break;
                    case -16711936:
                        makeEntity(terrain, i, j, "lamp", false);
                        break;
                    case -16777216:
                        makeEntity(terrain, i, j, "wall", false);
                        break;
                    case -6908266:
                        makeEntity(terrain, i, j, "wall", true);
                        break;
                    case -196864:
                        makeEntity(terrain, i, j, "free_standing_whiteboard", false);
                        break;
                    case -16713985:
                        makeEntity(terrain, i, j, "table_with_drawer", false);
                        break;
                    case -261889:
                        makeEntity(terrain, i, j, "laptop", false);
                        break;
                    case -28672:
                        makeEntity(terrain, i, j, "bug", false);
                        break;
                    case -16747777:
                        makeEntity(terrain, i, j, "swipe_card", false);
                        break;
                    case -16711810:
                        makeEntity(terrain, i, j, "tablet", false);
                        break;
                    case -4980481:
                        makeEntity(terrain, i, j, "flash_drive", false);
                        break;
                }
            }
        }
    }

    /**
     * Makes a new entity and adds it to the respective collection
     *
     * @param terrain    Terrain the entity is one
     * @param i          x position
     * @param j          z position
     * @param entityName Name of entity model
     * @param rotate     rotate the entity
     */
    private void makeEntity(Terrain terrain, int i, int j, String entityName, boolean rotate) {

        float x = i + terrain.getGridX();
        float z = j + terrain.getGridZ();
        float y = terrain.getTerrainHeight(x, z);

        switch (entityName) {
            case "commit":
                //Create a new position where a commit can spawn
                commitPositions.add(new Vector3f(x, y + COMMIT_OFFSET_Y, z));
                break;

            /////////////////////
            // STATIC ENTITIES //
            /////////////////////

            case "lamp":
                // Create and add the entity
                entities.add(new CollidableEntity(lampEntity.getTexturedModel(), new Vector3f(x, y, z), 0,
                        random.nextFloat() * 256f, 0, 1f, 0, lampEntity.getModelData()));

                // Create the lamp's light source
                LightFactory.createEntityLight(new Vector3f(x, y + 12, z + 2));
                break;
            case "pine":
                // Ground offset
                y -= 2;

                // Create and add the entity
                entities.add(new CollidableEntity(pineEntity.getTexturedModel(), new Vector3f(x, y, z), 0,
                        random.nextFloat() * 256f, 0, random.nextFloat() + 1, 0, pineEntity.getModelData()));
                break;
            case "wall":
                // Check to see if wall is rotated 90
                if (rotate) {
                    // Create and add the entity
                    wallEntities.add(new Entity(wallEntity.getTexturedModel(), new Vector3f(x, y, z), 0,
                            90f, 0, 10f, 0));
                } else {
                    // Create and add the entity
                    entities.add(new CollidableEntity(wallEntity.getTexturedModel(), new Vector3f(x, y, z), 0,
                            0, 0, 10f, 0, wallEntity.getModelData()));
                }
                break;
            case "free_standing_whiteboard":
                // Again ground offset
                y += 8;

                // Create and add the entity
                entities.add(new OfficeEntity(whiteboardEntity.getTexturedModel(), new Vector3f(x, y, z), 0,
                        270f, 0, 1.5f, 0, whiteboardEntity.getModelData()));
                break;
            case "table_with_drawer":
                //Ground offset
                y += 4;

                // Create and add the entity
                entities.add(new OfficeEntity(tableEntity.getTexturedModel(), new Vector3f(x, y, z), 0,
                        270f, 0, 1.5f, 0, tableEntity.getModelData()));
                break;

            //////////////////////
            // MOVABLE ENTITIES //
            //////////////////////

            case "laptop":
                // ground offset
                y += 6.5;

                // Create and add the entity
                movableEntities.put(EntityFactory.movableItemID, new Laptop(laptopEntity.getTexturedModel(),
                        new Vector3f(x, y, z), 0, 270f, 0, 1f, EntityFactory.movableItemID++,
                        EntityFactory.laptopItemID++, true));
                break;
            case "bug":
                // Create and add the entity
                y += 15;
                movableEntities.put(EntityFactory.movableItemID++, new Bug(bugEntity.getTexturedModel(),
                        new Vector3f(x, y, z), 0, 0, 0, 10f, 0));
                break;
            case "swipe_card":
                // Terrible way to position this, but had to be done
                y += 3.5;
                z += 4.5;
                x -= 2.5;

                // Create and add the entity
                movableEntities.put(EntityFactory.movableItemID, new SwipeCard(
                        swipeCardEntities[EntityFactory.swipeCardItemID].getTexturedModel(), new Vector3f(x, y, z), 0,
                        180, 0, 0.4f, EntityFactory.movableItemID++, EntityFactory.swipeCardItemID++));
                break;
            case "tablet":
                // Create and add the entity
                y += 6.5;
                movableEntities.put(EntityFactory.movableItemID, new ReadMe(tabletEntity.getTexturedModel(),
                        new Vector3f(x, y, z), 0, 270f, 0, 1f, EntityFactory.movableItemID++,
                        "readme1" + EntityFactory.readmeItemID++));
                break;
            case "flash_drive":
                // Create and add the entity
                // Again bad way to do this but had to be done
                y += 3.3;
                z += 2;
                x -= 2;
                movableEntities.put(EntityFactory.movableItemID, new FlashDrive(flashDriveEntity.getTexturedModel(),
                        new Vector3f(x, y, z), 0, 180, 0, 0.5f, EntityFactory.movableItemID++,
                        "extImg" + EntityFactory.flashDriveItemID++));
                break;
        }
    }

    /**
     * Creates a commit at a given position
     *
     * @param position position
     * @return commit
     */
    public static Commit createCommit(Vector3f position) {
        position.y += 10;
        return new Commit(EntityFactory.commitEntity.getTexturedModel(), position, 0, 0, 0, 1.5f, EntityFactory.movableItemID++);
    }

    /**
     * Creates a new portal entity
     *
     * @param position position of the portal
     * @param terrain  Terrain to get y height from
     * @return Portal Entity
     */
    public StaticEntity makePortal(Vector3f position, Terrain terrain) {
        position.y += terrain.getTerrainHeight(position.getX(), position.getZ()) + 10;
        return new CollidableEntity(portalEntity.getTexturedModel(), position, 0, 0, 0, 10f, 0, portalEntity.getModelData());
    }

    /**
     * Initialises the player model. Static method as it is called before the other initialisations happen
     */
    public static void initPayerModel() {
        playerEntity = new EntityModel("orb", 0.4f);
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
            image = ImageIO.read(ResourceLoader.getResourceAsStream("res/" + entityMap + ".png"));
        } catch (IOException e) {
            System.err.println("Failed to load entity map");
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Gets player textured model.
     *
     * @return the player textured model
     */
    public static TexturedModel getPlayerTexturedModel() {
        return playerEntity.getTexturedModel();
    }

    /**
     * Gets entities.
     *
     * @return the entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Gets commit positions.
     *
     * @return the commit positions
     */
    public ArrayList<Vector3f> getCommitPositions() {
        return commitPositions;
    }

    /**
     * Gets movable entities.
     *
     * @return the movable entities
     */
    public Map<Integer, MovableEntity> getMovableEntities() {
        return movableEntities;
    }

    /**
     * Gets wall entities.
     *
     * @return the wall entities
     */
    public ArrayList<Entity> getWallEntities() {
        return wallEntities;
    }

    /**
     * Gets flashdrive textured model.
     *
     * @return the flashdrive textured model
     */
    public static TexturedModel getFlashdriveTexturedModel() {
        return flashDriveEntity.getTexturedModel();
    }

    /**
     * Gets commit textured model.
     *
     * @return the commit textured model
     */
    public static TexturedModel getCommitTexturedModel() {
        return commitEntity.getTexturedModel();
    }

    /**
     * Get swipecard textured model textured model [ ].
     *
     * @return the textured model [ ]
     */
    public static TexturedModel[] getSwipecardTexturedModel() {
        TexturedModel[] temp = new TexturedModel[5];
        for (int i = 0; i < 5; i++) {
            temp[i] = swipeCardEntities[i].getTexturedModel();
        }
        return temp;
    }

    /**
     * Gets tablet textured model.
     *
     * @return the tablet textured model
     */
    public static TexturedModel getTabletTexturedModel() {
        return tabletEntity.getTexturedModel();
    }

    /**
     * Gets bug textured model.
     *
     * @return the bug textured model
     */
    public static TexturedModel getBugTexturedModel() {
        return bugEntity.getTexturedModel();
    }

    /**
     * Gets laptop textured model.
     *
     * @return the laptop textured model
     */
    public static TexturedModel getLaptopTexturedModel() {
        return laptopEntity.getTexturedModel();
    }
}
