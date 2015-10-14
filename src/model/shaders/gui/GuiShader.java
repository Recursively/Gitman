package model.shaders.gui;

import model.shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Class to handle the open GL bindings for the GuiShader shader program
 *
 * @author Marcel van Workum - 300313949
 */
public class GuiShader extends ShaderProgram {

    // actually shader files
    private static final String VERTEX_FILE = "src/model/shaders/gui/guiVertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/model/shaders/gui/guiFragmentShader.glsl";

    // locations of the uniform transform variable
    private int location_transformationMatrix;

    /**
     * Instantiates a new Gui shader.
     */
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Load transformation.
     *
     * @param matrix the matrix
     */
    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    /**
     * Gets the locations of all the uniform shader variables
     */
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    /**
     * bind the attributes to the open gl binding
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}