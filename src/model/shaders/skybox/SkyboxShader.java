package model.shaders.skybox;

import model.entities.Camera;
import model.shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;
import model.toolbox.Maths;

/**
 * Class to handle the open GL bindings for the skybox shader program
 *
 * @author Marcel van Workum
 */
public class SkyboxShader extends ShaderProgram {

    // actually shader files
    private static final String VERTEX_FILE = "src/model/shaders/skybox/skyboxVertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/model/shaders/skybox/skyboxFragmentShader.glsl";

    // locations of the uniform variables
    private int location_projectionMatrix;
    private int location_viewMatrix;

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
        super.loadMatrix(location_viewMatrix, matrix);
    }

    /**
     * Gets the locations of all the uniform shader variables
     */
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    /**
     * bind the attributes to the open gl binding
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}