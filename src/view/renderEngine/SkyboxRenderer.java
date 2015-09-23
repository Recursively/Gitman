package view.renderEngine;

import model.entities.Camera;
import model.models.RawModel;
import model.shaders.skybox.SkyboxShader;
import model.toolbox.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Delegate renderer to handle the rendering of the world's skybox
 *
 * A skybox is a cube of png images wrapped around the players position in the game world
 * to simulate the feeling of a sky.
 *
 * @author Marcel van Workum
 */
public class SkyboxRenderer {

    /**
     * This has to be in a specific order to work as the {@link Loader#loadCubeMap(String[])}
     * method loads the textures very specifically
     *
     * order is: right -> left -> top -> bottom -> back ->front
     */
    private static final String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};

    private static final float SIZE = 500f;

    private RawModel cubeModel;
    private int texture;
    private SkyboxShader shader;

    /**
     * Constructor
     *
     * @param loader Used for loading the skybox
     * @param projectionMatrix 4x4 transformation matrix
     */
    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {

        // Loads pngs into VAO
        cubeModel = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);

        // Creates the shader, loads the 4x4 and stops
        shader = new SkyboxShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Renders the skybox around the camera
     *
     * @param camera Represents the center point of the skybox cube
     */
    public void render(Camera camera) {
        shader.start();
        shader.loadViewMatrix(camera);

        // need to bind VAO of cube
        GL30.glBindVertexArray(cubeModel.getVaoID());

        // positions are stored in VAO attrib 0 so need to enable it
        GL20.glEnableVertexAttribArray(0);

        // activate texture and bind it to the texture id
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);

        // now actually draw the skybox
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cubeModel.getVertexCount());

        // clean up bindings
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    // This is ugly, but essentially it's pointing to all
    // the corners of the cube, so the the direction vector
    // knows where to render each pixel of the skybox
    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };
}