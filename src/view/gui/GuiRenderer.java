package view.gui;

import model.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import view.renderEngine.Loader;
import toolbox.Maths;

import java.util.List;

public class GuiRenderer {

    private final RawModel quad;
    private GuiShader shader;

    public GuiRenderer(Loader loader) {
        // GL triangle stripping
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions);
        shader = new GuiShader();
    }

    public void render(List<GuiTexture> guiImages) {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
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
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        // re-enable depth testing else bad shits gonna happen
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        // turns off transparent colour removal
        GL11.glDisable(GL11.GL_BLEND);

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp() {
        shader.cleanUp();
    }

}
