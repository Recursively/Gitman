package model.skybox;

import model.entities.Camera;
import model.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import view.renderEngine.Loader;

public class SkyboxRenderer {

    //TODO change this to fit skybox
    // Size of the skybox
    private static final float SIZE = 500f;
    // This is ugly, but essentially what it is doing
    // is pointing to all the stitching points of the skybox
    // so that the renderer knows where to sample the texture.
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

    // This has to be in a specific order to work as the Loader#loadCubeMap
    // method loads the textures very specifically
    // order is:
    // right
    // left
    // top
    // bottom
    // back
    // front
    private static final String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};

    private RawModel cubeModel;
    private int texture;
    private SkyboxShader shader;

    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
        cubeModel = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        shader = new SkyboxShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

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
}
