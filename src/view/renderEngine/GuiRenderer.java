package view.renderEngine;

import model.models.RawModel;
import model.shaders.gui.GuiShader;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import model.toolbox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

/**
 * The GuiRender is different from the other renderer classes, as it only needs to handle the rendering of 2D images.
 * <p/>
 * It Renders these images directly onto the players screen.
 *
 * @author Marcel van Workum - 300313949
 */
public class GuiRenderer {

    private final RawModel screen;
    private GuiShader shader;

    /**
     * Constructor
     * <p/>
     * Loads screen to VAO and creates shader
     */
    public GuiRenderer() {
        // GL triangle stripping
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        screen = Loader.loadToVAO(positions, 2);
        shader = new GuiShader();
    }

    /**
     * Renders all of the GuiImages to the screen
     *
     * @param guiImages list of images to render
     */
    public void render(List<GuiTexture> guiImages) {

        // start the shader and bind the screen to VAO
        shader.start();
        GL30.glBindVertexArray(screen.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        // removes transparent colours
        GL11.glEnable(GL11.GL_BLEND);

        // not sure what this does
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // disable depth testing so that gui elements can overlap
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (GuiTexture image : guiImages) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, image.getTexture());
            Matrix4f matrix = Maths.createTransformationMatrix(image.getPosition(), image.getScale());
            shader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, screen.getVertexCount());
        }

        // re-enable depth testing else bad shits gonna happen
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        // turns off transparent colour removal
        GL11.glDisable(GL11.GL_BLEND);

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    /**
     * Delegates to the shader to clean itself
     */
    public void cleanUp() {
        shader.cleanUp();
    }
}
