package model.shaders.terrain;

import model.entities.Camera;
import model.entities.Light;
import model.shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import model.toolbox.Maths;

import java.util.List;

/**
 * Class to handle the open GL bindings for the terrain shader program
 *
 * @author Marcel van Workum
 */
public class TerrainShader extends ShaderProgram {

    //TODO this will need to change if more light sources are added
    private static final int MAX_LIGHT_SOURCES = 5;

    // actually shader files
    private static final String VERTEX_FILE = "src/model/shaders/terrain/terrainVertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/model/shaders/terrain/terrainFragmentShader.glsl";

    // locations of the uniform variables
    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationSkyColour;
    private int locationBackgroundTexture;
    private int locationRTexture;
    private int locationGTexture;
    private int locationBTexture;
    private int locationBlendMap;

    // location of uniform arrays
    private int locationLightPosition[];
    private int locationLightColour[];
    private int locationAttenuation[];

    /**
     * Instantiates a new Terrain shader.
     */
    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Gets the locations of all the uniform shader variables
     */
    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationShineDamper = super.getUniformLocation("shineDamper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationSkyColour = super.getUniformLocation("skyColour");
        locationBackgroundTexture = super.getUniformLocation("backgroundTexture");
        locationRTexture = super.getUniformLocation("rTexture");
        locationGTexture = super.getUniformLocation("gTexture");
        locationBTexture = super.getUniformLocation("bTexture");
        locationBlendMap = super.getUniformLocation("blendMap");

        locationLightPosition = new int[MAX_LIGHT_SOURCES];
        locationLightColour = new int[MAX_LIGHT_SOURCES];
        locationAttenuation = new int[MAX_LIGHT_SOURCES];

        for (int i = 0; i < MAX_LIGHT_SOURCES; i++) {
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    /**
     * bind the attributes to the open gl binding
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    /**
     * Connects each of the textures
     */
    public void connectTextureUnits() {
        super.loadInt(locationBackgroundTexture, 0);
        super.loadInt(locationRTexture, 1);
        super.loadInt(locationGTexture, 2);
        super.loadInt(locationBTexture, 3);
        super.loadInt(locationBlendMap, 4);
    }

    /**
     * Load sky colour.
     *
     * @param r the r
     * @param g the g
     * @param b the b
     */
    public void loadSkyColour(float r, float g, float b) {
        super.loadVector(locationSkyColour, new Vector3f(r, g, b));
    }

    /**
     * Load shine variables.
     *
     * @param damper the damper
     * @param reflectivity the reflectivity
     */
    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(locationShineDamper, damper);
        super.loadFloat(locationReflectivity, reflectivity);
    }

    /**
     * Load transformation matrix.
     *
     * @param matrix the matrix
     */
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    /**
     * Load lights.
     *
     * @param lights the lights
     */
    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHT_SOURCES; i++) {
            if (i < lights.size()) {
                super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
                super.loadVector(locationLightColour[i], lights.get(i).getColour());
                super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
            }

            // loads all lights
            // TODO slows performance
            else {
                super.loadVector(locationLightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(locationLightColour[i], new Vector3f(0f, 0f, 0f));
                super.loadVector(locationAttenuation[i], new Vector3f(1, 0.01f, 0.002f));
            }
        }
    }

    /**
     * Load view matrix.
     *
     * @param camera the camera
     */
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(locationViewMatrix, viewMatrix);
    }

    /**
     * Load projection matrix.
     *
     * @param projection the projection
     */
    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(locationProjectionMatrix, projection);
    }
}
