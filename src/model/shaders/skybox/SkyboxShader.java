package model.shaders.skybox;

import model.entities.Camera;
import model.shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;
import model.toolbox.Maths;
import org.lwjgl.util.vector.Vector3f;
import view.DisplayManager;

/**
 * Class to handle the open GL bindings for the skybox shader program
 *
 * @author Marcel van Workum
 */
public class SkyboxShader extends ShaderProgram {

    // actually shader files
    private static final String VERTEX_FILE = "src/model/shaders/skybox/skyboxVertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/model/shaders/skybox/skyboxFragmentShader.glsl";

    private static final float ROTATION_SPEED = 1f;

    // locations of the uniform variables
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColour;

    private float rotation = 0;

    /**
     * Instantiates a new Skybox shader.
     */
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Load projection matrix.
     *
     * @param matrix the matrix
     */
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    /**
     * Load view matrix.
     *
     * @param camera the camera
     */
    /*

        [ 1, 0, 0, x ]
        [ 0, 1, 0, y ]
        [ 0, 0, 1, z ]
        [ 0, 0, 0, 1 ]

        COMP261 paid off

        Last column of matrix determines the translation of skybox
    */
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;

        // update rotation by number of seconds passed
        rotation += ROTATION_SPEED * DisplayManager.getFrameTimeSeconds();

        // apply the rotation to the view matrix of the skybox
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);

        super.loadMatrix(location_viewMatrix, matrix);
    }

    /**
     * Load fog colour.
     *
     * @param r the r
     * @param g the g
     * @param b the b
     */
    public void loadFogColour(float r, float g, float b) {
        super.loadVector(location_fogColour, new Vector3f(r, g, b));
    }

    /**
     * Gets the locations of all the uniform shader variables
     */
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColour = super.getUniformLocation("fogColour");
    }

    /**
     * bind the attributes to the open gl binding
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}