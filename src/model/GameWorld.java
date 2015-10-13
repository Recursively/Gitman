package model;

import controller.AudioController;
import controller.GameController;
import model.data.Data;
import model.data.Load;
import model.entities.Camera;
import model.entities.Entity;
import model.entities.Light;
import model.entities.movableEntity.*;
import model.factories.*;
import model.guiComponents.GuiMessages;
import model.guiComponents.Inventory;
import model.terrains.Terrain;
import model.textures.GuiTexture;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import view.DisplayManager;
import view.renderEngine.MasterRenderer;

import java.util.*;

/**
 * Delegate class used to represent all the current components of the game
 * world.
 *
 * @author Marcel van Workum
 * @author Divya
 * @author Reuben
 * @author Ellie
 */
public class GameWorld {

    public static final int GAME_WIN = 1;      // game state value for won game
    public static final int CODE_VALUE = 20;
    public static final int MAX_PROGRESS = 100;

    private static final int START_PATCH = 10; // starting patch progress value
    private static final double PATCH_DECREASE = 0.1;
    private static final double PATCH_TIMER = 30000; // time before decrease
    private static final int AVG_COMMIT_COLLECT = 5; // by each player

    // interaction distances
    private static final int MIN_INTERACT = 15;
    private static final int COMMIT_INTERACT = 20;
    private static final int BUG_INTERACT = 40;

    private static final float Y_OFFSET = 2; // y offset to place deleted items
    public static final Vector3f SPAWN_POSITION = new Vector3f(30, 100, -20);
    public static final Vector3f OFFICE_SPAWN_POSITION = new Vector3f(128060,
            100, -127930);

    // need to update y position when initialised
    private static final Vector3f OUTSIDE_PORTAL_POSITION = new Vector3f(6, 19,
            -35);
    public static final int PORTAL_LOWER_BOUND_OUTSIDE_Z = -30;
    public static final int PORTAL_UPPER_BOUND_OUTSIDE_Z = -40;
    public static final int PORTAL_EDGE_BOUND_OUTSIDE_X = 12;

    private static final Vector3f OFFICE_PORTAL_POSITION = new Vector3f(
            128011f, 0, -127930);
    public static final int PORTAL_LOWER_BOUND_OFFICE_Z = -127920;
    public static final int PORTAL_UPPER_BOUND_OFFICE_Z = -127940;
    public static final int PORTAL_EDGE_BOUND_OFFICE_X = 128016;

    private static float worldTime = 0;
    private static boolean isProgramCompiled = false;
    private static boolean isOutside = false;

    // Object creation factories
    private EntityFactory entityFactory;
    private TerrainFactory terrainFactory;
    private LightFactory lightFactory;
    private GuiFactory guiFactory;
    private PlayerFactory playerFactory;

    // Collection of guiImages to render to the screen
    private List<GuiTexture> guiImages;
    private static GuiMessages guiMessages;

    // collection of entities in the game
    private ArrayList<Entity> staticEntities;
    private Map<Integer, MovableEntity> movableEntities;
    private ArrayList<SwipeCard> cards;

    // Terrain the world is on and other terrain
    private static Terrain currentTerrain;
    private static Terrain otherTerrain;

    // The actual player
    private static Player player;

    // Collection of other players stored separately
    private Map<Integer, Player> allPlayers;

    // Constant sun light-source
    private static Light sun;
    private static Light blackHoleSun;
    private Light officeLight;

    // Collection of attenuating light-sources
    private ArrayList<Light> lights;


    // reference to the gameController
    private GameController gameController;

    // game state elements
    private Inventory inventory;
    private int progress;
    private int score; // overall score is different to progress
    private boolean canApplyPatch;
    private int commitIndex;
    private long timer;
    private int interactDistance;
    private int commitCollected;

    // game state
    private int gameState; // -1 is playing. 0 is lost. 1 is won
    private boolean helpVisible;

    // information from saved file, if game loaded in

    private Data load;

    private ArrayList<Entity> wallEntities;

    /**
     * Creates the game world
     */
    public GameWorld(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Initialises the game by setting up the lighting, factories and
     * currentTerrain
     *
     * @param isHost
     */
    public void initGame(boolean isHost, boolean load) {
        // initialise factories and data structures
        initFactories();
        initDataStructures();

        // creates the gui to be displayed on the display
        initGui();

        // initialises the currentTerrain
        initTerrain();

        entityFactory = new EntityFactory(otherTerrain, currentTerrain);

        // Adds lighting to game world
        setupLighting();

        initPlayerModel();

        staticEntities = entityFactory.getEntities();
        wallEntities = entityFactory.getWallEntities();
        inventory = new Inventory(guiFactory);

        if (load) {
            load = initLoadGame();
            if (!load) {
                setGuiMessage("failedToLoad", 2000);
            }

        }

        // if not loading in, or load game failed, create normal game
        if (!load) {
            movableEntities = entityFactory.getMovableEntities();

            // game state
            this.progress = 0;
            this.cards = new ArrayList<SwipeCard>();
            this.canApplyPatch = false;
            this.interactDistance = MIN_INTERACT;
            this.gameState = -1;
            this.commitCollected = 0;
            // create commits
            initCommits();
        }

        this.helpVisible = false;
        staticEntities.add(entityFactory.makePortal(OUTSIDE_PORTAL_POSITION,
                currentTerrain));
    }

    /**
     * Load in currently saved game
     * 
     * @return true if successful load, false otherwise
     */
    private boolean initLoadGame() {
        this.load = Load.loadGame();

        // loading failed. Don't load game, just return false
        if (load == null) {
            return false;
        }
        // load in movable entities and their saved positions
        movableEntities = new HashMap<>();
        for (MovableEntity e : load.getMovableEntities()) {
            this.movableEntities.put(e.getUID(), e);
        }

        // inventory state
        inventory.setStorageUsed(load.getStorageUsed());
        inventory.setInLaptop(load.getInventory());

        // swipe cards
        this.cards = load.getSwipeCards();

        // score and game state
        this.progress = load.getProgress();
        this.canApplyPatch = load.isCanApplyPatch();
        this.commitIndex = load.getCommitIndex();
        this.score = load.getScore();
        this.gameState = load.getGameState();
        this.commitCollected = load.getCommitCollected();
        GameWorld.isOutside = load.isIsOutside();
        GameWorld.isProgramCompiled = load.isIsCodeCompiled();

        if (this.canApplyPatch) {
            this.interactDistance = BUG_INTERACT;
        } else if (GameWorld.isProgramCompiled) {
            this.interactDistance = COMMIT_INTERACT;
            enablePortal();
        } else {
            this.interactDistance = MIN_INTERACT;
        }

        if (!isOutside) {
            if (isProgramCompiled) {
                AudioController.playPortalHum();
            }
        }
        return true;
    }

    /**
     * Initialize/Create commits
     */
    private void initCommits() {
        int count = 0;
        for (Vector3f position : entityFactory.getCommitPositions()) {
            if (count == 10)
                break;
            Commit newCommit = EntityFactory.createCommit(position);
            this.movableEntities.put(newCommit.getUID(), newCommit);
            count++;
        }
        this.commitIndex = count;
    }

    /**
     * Adds the light sources to the game worlds list of lights to be rendered
     */
    private void setupLighting() {
        sun = lightFactory.createSun();
        blackHoleSun = lightFactory.createSun();
        officeLight = lightFactory.createOfficeLight();
        lights.add(officeLight);

        lights.addAll(LightFactory.getStaticEntityLights());
    }

    /**
     * initialises the Gui to be rendered to the display
     */
    private void initGui() {
        guiImages = new ArrayList<GuiTexture>();
        guiImages = guiFactory.getInfoPanel();
        guiMessages = new GuiMessages(guiFactory);
    }

    /**
     * Initialises all the terrains of the gameworld
     */
    private void initTerrain() {
        otherTerrain = terrainFactory.makeOutsideTerrain(0, -1);
        currentTerrain = terrainFactory.makeOfficeTerrain(1000, -1000);
    }

    /**
     * initialises the data structures which hold all of the world data
     */
    private void initDataStructures() {
        guiImages = new ArrayList<>();
        staticEntities = new ArrayList<>();
        movableEntities = new HashMap<>();
        allPlayers = new HashMap<>();
        lights = new ArrayList<>();

    }

    /**
     * initialises the factories
     */
    private void initFactories() {
        playerFactory = new PlayerFactory(this);
        lightFactory = new LightFactory();
        terrainFactory = new TerrainFactory();
        guiFactory = new GuiFactory();
    }

    /**
     * Gets lights.
     *
     * @return the lights
     */
    public ArrayList<Light> getLights() {
        ArrayList<Light> collectionOfLights = new ArrayList<>();
        if (isOutside) {
            collectionOfLights.add(sun);
        } else {
            collectionOfLights.add(blackHoleSun);
        }

        ArrayList<Light> possibleLights = new ArrayList<>();
        possibleLights.add(officeLight);
        possibleLights.addAll(lights);

        Collections.sort(possibleLights);

        for (int i = 0; i < 4; i++) {
            collectionOfLights.add(possibleLights.get(i));
        }

        return collectionOfLights;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets gui images.
     *
     * @return the gui images
     */
    public List<GuiTexture> getGuiImages() {
        updateGui();
        return guiImages;
    }

    /**
     * Gets currentTerrain.
     *
     * @return the currentTerrain
     */
    public Terrain getTerrain() {
        return currentTerrain;
    }

    /**
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    public ArrayList<Entity> getStaticEntities() {
        return staticEntities;
    }

    public void setPlayer(Player player) {
        GameWorld.player = player;
    }

    /**
     * @return the otherPlayers
     */
    public Map<Integer, Player> getAllPlayers() {
        return allPlayers;
    }

    public Map<Integer, MovableEntity> getMoveableEntities() {
        return movableEntities;
    }

    public ArrayList<SwipeCard> getSwipeCards() {
        return this.cards;
    }

    public int getGameState() {
        return this.gameState;
    }

    public boolean canApplyPatch() {
        return this.canApplyPatch;
    }

    public boolean isHelpVisible() {
        return this.helpVisible;
    }

    public void updateGui() {
        this.guiImages = this.guiFactory.getInfoPanel();
        this.guiImages.addAll(this.guiFactory.getProgress(this.progress));
        this.guiImages.addAll(this.guiFactory.getScore(this.score));
        this.guiImages.addAll(this.guiFactory.getSwipeCards(this.cards));
    }

    /**
     * Find item that player is trying to interact with and then carry out
     * interaction
     */
    public void interactWithMovEntity() {
        if (inventory.isVisible())
            return;
        MovableEntity entity = findMovEntity(player.getCamera());
        if (entity != null) {
            int type = entity.interact(this);
            sendInteraction(type, entity);
        }
    }

    private void sendInteraction(int type, MovableEntity entity) {
        gameController.setNetworkUpdate(type, entity);
    }

    /**
     * Go through all movable entities and find the movable entity that is the
     * closest to the player, and also within the players field of view.
     *
     * @return closest movable entity found
     */
    public MovableEntity findMovEntity(Camera camera) {
        MovableEntity closest = null;
        double closestDiff = interactDistance * interactDistance;

        for (Map.Entry<Double, MovableEntity> e : this.withinDistance()
                .entrySet()) {
            if (e.getKey() <= closestDiff) {
                closestDiff = e.getKey();
                closest = e.getValue();
            }
        }
        return closest;
    }

    /**
     * Check if entities in the movable entities map
     * are within distance of the current player, and if they are
     * add to a map
     * 
     * @return map that maps distance from player to entity, to entity
     */
    public Map<Double, MovableEntity> withinDistance() {
        HashMap<Double, MovableEntity> interactable = new HashMap<Double, MovableEntity>();

        // get position of player
        Camera camera = player.getCamera();
        float px = camera.getPosition().getX();
        float pz = camera.getPosition().getZ();

        for (MovableEntity e : this.movableEntities.values()) {
            // check that entity is 'intractable'
            if (!e.canInteract()) {
                continue;
            }

            float ex = e.getPosition().getX();
            float ez = e.getPosition().getZ();
            double diff = (ex - px) * (ex - px) + (ez - pz) * (ez - pz);

            // if within interactable distance, add to map
            if (diff <= (interactDistance * interactDistance)
                    && Entity.isInFrontOfPlayer(e.getPosition(), camera)) {
                interactable.put(diff, e);
            }
        }
        return interactable;
    }

    /**
     * Remove a movable entity from the game
     *
     * @param entity to remove
     */
    public void removeMovableEntity(MovableEntity entity) {
        movableEntities.remove(entity.getUID());
    }

    /**
     * Add a commit to the movable entities map. Check that position
     * of commit is not already taken first. If it is, keep searching 
     * through positions until one is found that is not 'in use'
     * 
     */
    public void addCommit() {
        ArrayList<Vector3f> commitPos = entityFactory.getCommitPositions();
        
        // ensure that position to place commit at is not already taken
        boolean found = false;
        for (int i = 0; i < commitPos.size(); i++) {
            commitIndex = (commitIndex + i) % commitPos.size();
            Vector3f pos = commitPos.get(commitIndex);
            for (MovableEntity e : this.movableEntities.values()) {
                if (e.getPosition().equals(pos)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                break;
            }
        }

        // create commit at unused position
        Commit newCommit = EntityFactory.createCommit(commitPos
                .get(commitIndex));
        this.movableEntities.put(newCommit.getUID(), newCommit);

        // increment commitIndex
        commitIndex = (commitIndex + 1) % commitPos.size();
    }

    /**
     * Add the given item to the inventory
     *
     * @param item to add
     * @return true if add is successful
     */
    public boolean addToInventory(LaptopItem item) {
        if (this.inventory.addItem(item)) {
            this.removeMovableEntity(item);
            return true;
        }
        setGuiMessage("laptopMemoryFull", 3000);
        return false;
    }

    /**
     * Remove the given item from the inventory, and drop the item at the player
     * position
     *
     * @param item to remove
     * @return true if remove was successful
     */
    public void removeFromInventory(LaptopItem item) {
        if (item != null) {
            Vector3f playerPos = player.getPosition();
            float y = currentTerrain.getTerrainHeight(playerPos.getX(),
                    playerPos.getZ());
            float scale = item.getScale();
            item.setScale(scale);
            item.setPosition(new Vector3f(playerPos.getX(), y + Y_OFFSET,
                    playerPos.getZ()));
            this.movableEntities.put(item.getUID(), item);
        }
    }

    /**
     * Remove the given item from the inventory, and drop the item at the player
     * position of the given player (find player with given player id)
     *
     * @param item to remove
     * @param playerID id of player that has removed item from inventory
     * @return true if remove was successful
     */
    public void removeFromInventory(LaptopItem item, int playerID) {
        if (item != null) {
            Vector3f playerPos = gameController.getPlayerWithID(playerID)
                    .getPosition();
            float y = currentTerrain.getTerrainHeight(playerPos.getX(),
                    playerPos.getZ());
            
            // make sure scaling of item is fine
            float scale = item.getScale();
            item.setScale(scale);
            
            // set player position
            item.setPosition(new Vector3f(playerPos.getX(), y + Y_OFFSET,
                    playerPos.getZ()));
            this.movableEntities.put(item.getUID(), item);
        }
    }

    /**
     * Add card to list of swipe cards
     *
     * @param swipeCard
     */
    public void addCard(SwipeCard swipeCard) {
        this.cards.add(swipeCard);
    }

    /**
     * Decreases patch progress bar steadily by 10% of current progress
     */
    public void decreasePatch() {
        // if not compiled program do nothing
        if (!GameWorld.isProgramCompiled)
            return;

        // decrease patch in relation to how much time has passed since last
        // decrease
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.timer > PATCH_TIMER) {

            if (this.progress >= MAX_PROGRESS) {
                return; // do nothing if reached 100%
            }
            double value = this.progress * PATCH_DECREASE;
            this.progress = (int) (this.progress - value);

            // if patch progress reaches zero, players lose
            if (this.progress <= 0) {
                gameState = 0;
                AudioController.playGameOverSound();
            }

            // update new time
            this.timer = System.currentTimeMillis();
        }
    }

    /**
     * Updates patch progress by "commitScore" ( a score that takes into account
     * how many commits are expected to be collected by each player depending on
     * the number of players trying to 'fix' the bug)
     */
    public void incrementPatch() {
        int commitScore = MAX_PROGRESS
                / ((allPlayers.size() + 1) * AVG_COMMIT_COLLECT);

        this.progress += commitScore;
        // 100% reached, game almost won...display message with last task

        if (this.progress >= MAX_PROGRESS) {
            this.canApplyPatch = true;
            this.interactDistance = BUG_INTERACT;
            setGuiMessage("patchComplete", 3000);
            AudioController.playGameWonLoop();
        }
    }

    /**
     * Updates game score (players get points for interacting with items)
     *
     * @param score is score of item in game
     */
    public void updateScore(int score) {
        this.score += score;
    }

    /**
     * Code progess reached 100 means all bits of code collected. Player is
     * given the option of multiplayer or single player, and the environment
     * they are displayed in changes in
     */
    public void updateCodeProgress() {
        this.progress += CODE_VALUE;
        inventory.increaseStorageUsed(CODE_VALUE);

        // player has cloned all bits of code
        if (this.progress >= MAX_PROGRESS) {
            compileProgram();
        }
    }

    /**
     * Code progress reached 100 means all bits of code collected. Player is
     * given the option of multiplayer or single player, and the environment
     * they are displayed in changes in
     */
    public void compileProgram() {
        setGuiMessage("codeCompiledMessage", 5000);
        this.timer = System.currentTimeMillis(); // start timer
        this.interactDistance = COMMIT_INTERACT;
        this.progress = START_PATCH;

        // adds the portal to the game
        enablePortal();
        AudioController.playPortalHum();
        GameWorld.isProgramCompiled = true;
    }

    private void enablePortal() {
        officeLight.setColour(new Vector3f(6, 1, 1));
        staticEntities.add(entityFactory.makePortal(OFFICE_PORTAL_POSITION,
                currentTerrain));
    }

    /**
     * @return end state screen depending on current game state
     *  (i.e. if game has been won or lost)
     */
    public List<GuiTexture> getEndStateScreen() {
        if (this.gameState == GAME_WIN) {
            return guiFactory.getWinScreen();

        } else {
            return guiFactory.getLostScreen();
        }
    }

    public void addNewPlayer(Vector3f position, int uid) {
        Player player = playerFactory.makeNewPlayer(position,
                EntityFactory.getPlayerTexturedModel(), uid, null);
        allPlayers.put(uid, player);

        System.out.println("ADDED NEW PLAYER, ID: " + uid);
    }

    public void addPlayer(Vector3f position, int uid) {
        if (load != null) {
            player = playerFactory.makeNewPlayer(load.getPlayerPos(),
                    EntityFactory.getPlayerTexturedModel(), uid, load);

            // set player up in the outside world if they are outside
            if (GameWorld.isOutside) {
                setPlayerOutside();
            }
        } else {
            player = playerFactory.makeNewPlayer(position,
                    EntityFactory.getPlayerTexturedModel(), uid, null);
        }
        allPlayers.put(uid, player);
        System.out.println("ADDED THIS PLAYER, ID: " + uid);
    }

    private static void setPlayerOutside() {
        Terrain temp = currentTerrain;
        currentTerrain = otherTerrain;
        otherTerrain = temp;
        player.setCurrentTerrain(currentTerrain);
        MasterRenderer.setRenderSkybox(true);
    }

    private void initPlayerModel() {
        EntityFactory.initPayerModel();
    }

    /**
     * Swaps out the terrains for the players game world
     */
    public static void teleportToOutside() {
        setPlayerOutside();
        player.getPosition().x = SPAWN_POSITION.getX();
        player.getPosition().z = SPAWN_POSITION.getZ();
        player.getCamera().changeYaw(160f);
        isOutside = true;
        AudioController.stopPortalHum();
        AudioController.playPortalSound();
        AudioController.stopOfficeLoop();
        AudioController.playGameWorldLoop();
    }

    public static void teleportToOffice() {
        Terrain temp = currentTerrain;
        currentTerrain = otherTerrain;
        otherTerrain = temp;
        player.setCurrentTerrain(currentTerrain);
        player.getPosition().x = OFFICE_SPAWN_POSITION.getX();
        player.getPosition().z = OFFICE_SPAWN_POSITION.getZ();
        player.getCamera().changeYaw(180f);
        MasterRenderer.setRenderSkybox(false);
        isOutside = false;
        AudioController.playPortalHum();
        AudioController.playPortalSound();
        AudioController.stopGameWorldLoop();
        AudioController.playOfficeLoop();
    }

    /**
     * If help screen is not visible, display it, 
     * otherwise close help screen
     */
    public void displayHelp() {
        if (this.helpVisible) {
            this.helpVisible = false;
            Mouse.setGrabbed(true);
        } else {
            this.helpVisible = true;
            Mouse.setGrabbed(false);
        }
    }

    public List<GuiTexture> eInteractMessage(MovableEntity e) {
        return guiFactory.getPopUpInteract(e.getPosition());
    }

    public static boolean isProgramCompiled() {
        return isProgramCompiled;
    }

    public static void setIsProgramCompiled(boolean isProgramCompiled) {
        GameWorld.isProgramCompiled = isProgramCompiled;
    }

    public List<GuiTexture> displayMessages() {
        return guiMessages.getMessages();
    }

    public static void setGuiMessage(String msg, long time) {
        guiMessages.setMessage(msg, time);
    }

    public void setGameState(int state) {
        this.gameState = state;
    }

    public List<GuiTexture> helpMessage() {
        return guiFactory.getHelpScreen();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getScore() {
        return score;
    }

    public boolean isCanApplyPatch() {
        return canApplyPatch;
    }

    public int getCommitIndex() {
        return commitIndex;
    }

    public long getTimer() {
        return timer;
    }

    public static Vector3f getPlayerPosition() {
        return player.getPosition();
    }

    public static float getWorldTime() {
        return worldTime;
    }

    public static void increaseTime(float worldTime) {
        worldTime += worldTime;
        worldTime %= 24000;
    }

    /**
     * @return true if player is on the outside terrain
     */
    public static boolean isOutside() {
        return isOutside;
    }

    public static void updateSun() {
        if (GameWorld.getWorldTime() < 5000) {
            sun.setColour(new Vector3f(0, 0, 0));
        } else if (GameWorld.getWorldTime() < 8000) {
            sun.increaseColour(0.0001f, 0.0001f, 0.0001f);
        } else if (GameWorld.getWorldTime() > 21000) {
            sun.decreaseColour(0.0001f, 0.0001f, 0.0001f);
        }

    }

    public ArrayList<Entity> getWallEntities() {
        return wallEntities;
    }

    public void rotateCommits() {
        for (MovableEntity e : movableEntities.values()) {
            if (e instanceof Commit) {
                e.increaseRotation(0.5f, 0.5f, 0.5f);
            }
        }
    }

    /**
     * @return disconnected screen gui texture
     */
    public List<GuiTexture> getDisconnectedScreen() {
        return guiFactory.getDisconnectedScreen();
    }
    
    /**
     * @return 1 if commits have been collected
     */
    public int getCommitCollected() {
		return this.commitCollected;
	}
    
    /**
     * @param collected to set commitsCollected to
     */
    public void setCommitCollected(int collected) {
		this.commitCollected = collected;
	}

    // -------------------------------------------------
    // TEST METHODS
    // -------------------------------------------------

    /**
     * Testing Constructor
     */
    public GameWorld() {
        initTestWorld();
    }

    private void initTestWorld() {
        DisplayManager.createTestDisplay();

        // initialise factories and data structures
        initFactories();
        initDataStructures();


        // initialises the currentTerrain
        initTerrain();

        entityFactory = new EntityFactory(otherTerrain, currentTerrain);

        // Adds lighting to game world
        setupLighting();

        initPlayerModel();

        staticEntities = entityFactory.getEntities();
        wallEntities = entityFactory.getWallEntities();
        inventory = new Inventory(guiFactory);


        // if not loading in, or load game failed, create normal game
        movableEntities = entityFactory.getMovableEntities();

        // game state
        this.progress = 0;
        this.cards = new ArrayList<SwipeCard>();
        this.canApplyPatch = false;
        this.interactDistance = MIN_INTERACT;
        this.gameState = -1;
        // create commits
        initCommits();


        this.helpVisible = false;
        staticEntities.add(entityFactory.makePortal(OUTSIDE_PORTAL_POSITION,
                currentTerrain));
    }
}
