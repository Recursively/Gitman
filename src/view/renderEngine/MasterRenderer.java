package view.renderEngine;

import model.entities.Camera;
import model.entities.Entity;
import model.entities.Light;
import model.models.TexturedModel;
import model.toolbox.Loader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import model.shaders.entity.EntityShader;
import model.shaders.terrain.TerrainShader;
import model.terrains.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Master renderer handles all delegations to the individual render class.
 *
 * @see EntityRenderer
 * @see GuiRenderer
 * @see TerrainRenderer
 * @see SkyboxRenderer
 *
 * @author Marcel van Workum
 */
public class MasterRenderer {

    // Display Parameters
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 2000;

    // Fog colour values
    private static final float RED = 0.5444f;
    private static final float GREEN = 0.62f;
    private static final float BLUE = 0.69f;

    private Matrix4f projectionMatrix;

    // Entity
    private EntityShader entityShader = new EntityShader();
    private EntityRenderer entityRenderer;
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    // Terrain
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    private List<Terrain> terrains = new ArrayList<>();

    // Skybox
    private SkyboxRenderer skyboxRenderer;

    /**
     * Constructor
     *
     * Prepares the display for enabling culling, creating the transformationMatrix
     * and creating the separate Renderers
     *
     * @param loader Loader for the Skybox Renderer
     */
    public MasterRenderer(Loader loader) {
        enableCulling();
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    /**
     * Enables culling of the vertex back-faces
     */
    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /**
     * Disabled Culling of the vertex back-faces
     */
    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    /**
     * Prepares the Display for rendering and then delegates to the individual renderers
     *
     * @param lights List of light sources
     * @param camera Camera to render for
     */
    public void render(List<Light> lights, Camera camera) {

        // Clears the Display
        prepare();

        // Starts the entityShader
        entityShader.start();
        entityShader.loadSkyColour(RED, GREEN, BLUE);
        entityShader.loadLights(lights);
        entityShader.loadViewMatrix(camera);

        // Renders the entities
        entityRenderer.render(entities);

        // Must stop entityShader before rendering terrains
        entityShader.stop();

        // starts terrainShader and loads data
        terrainShader.start();
        terrainShader.loadSkyColour(RED, GREEN, BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);

        // Render the terrains
        terrainRenderer.render(terrains);

        // finally stop the shader
        terrainShader.stop();

        // Needs to render the skybox last otherwise you get some funky results
        skyboxRenderer.render(camera, RED, GREEN, BLUE);

        // Flush the terrains and entities, ready for the next frame
        terrains.clear();
        entities.clear();
    }

    /**
     * Adds an entity to the batch of entities to be rendered this frame
     *
     * @param entity Entity to add
     */
    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);

        // checks if first entity in the batch
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    /**
     * Adds the terrain to the list of terrains to be rendered
     *
     * @param terrain Terrain to be added
     */
    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    /**
     * Prepares the Display for rendering
     */
    public void prepare() {

        // Allows objects to have depth
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        // Clear the buffers
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Render fog background colour by clearing buffer and placing RGBA colour
        GL11.glClearColor(RED, GREEN, BLUE, 1);
    }

    /**
     * Delegate to the shaders to clean themselves up
     */
    public void cleanUp() {
        entityShader.cleanUp();
        terrainShader.cleanUp();
    }

    /**
     * Creates the display projection matrix, which gives a nice Field of View
     */
    private void createProjectionMatrix() {

        // Calculates the aspect ratio of the display and the frustum
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        // Loads the values to the projection matrix
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;
    }
    
    public static float getFOV(){
    	return FOV;
    }
}
