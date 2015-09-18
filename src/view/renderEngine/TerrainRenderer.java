package view.renderEngine;

import model.models.RawModel;
import model.shaders.terrain.TerrainShader;
import model.terrains.Terrain;
import model.textures.TerrainTexturePack;
import model.toolbox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * Delegate renderer to handle the rendering of terrains.
 *
 * An {@link Terrain} acts as the surface of the game world and has altered vertical y based on the a heightMap
 *
 * @author Marcel van Workum
 */
public class TerrainRenderer {

    private TerrainShader shader;

    /**
     * Constructor
     *
     * @param shader Terrain shader
     * @param projectionMatrix 4x4 projection matrix for the Terrain
     */
    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    /**
     * Renders a list of terrains
     *
     * Each terrain is bound to the VAO and then a transformation matrix is applied before finally being drawn
     *
     * @param terrains Terrains to render
     */
    public void render(List<Terrain> terrains) {
        for (Terrain t : terrains) {

            // Bind the VAO
            prepareTerrain(t);

            // apply transformationMatrix
            loadModelMatrix(t);

            // finally draw the terrain
            GL11.glDrawElements(GL11.GL_TRIANGLES, t.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            // clean up by unbinding the Terrain from the VAO
            unbindTerrain();
        }
    }

    /**
     * Binds the {@link model.textures.TerrainTexture} of the {@link TerrainTexturePack} to the VAO
     *
     * @param terrain Terrain to render
     */
    private void bindTextures(Terrain terrain) {
        TerrainTexturePack texturePack = terrain.getTexturePack();

        // Background texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());

        // r texture
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());

        // g texture
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());

        // b texture
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());

        // texture blendMap
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
    }

    /**
     * Binds the Terrain to the VAO and binds each individual texture to the VAO Textures
     *
     * @param terrain Terrain to bind
     */
    private void prepareTerrain(Terrain terrain) {
        RawModel rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        bindTextures(terrain);
        shader.loadShineVariables(1, 0);
    }

    /**
     * Cleans up the TexturedModel unbinding the VAO
     */
    private void unbindTerrain() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    /**
     * Prepares the Terrain for rendering by loading the transformation matrix to the shader
     *
     * @param terrain Terrain to be rendered
     */
    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }

}
