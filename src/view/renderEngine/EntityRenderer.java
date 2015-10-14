package view.renderEngine;

import model.entities.Entity;
import model.models.RawModel;
import model.models.TexturedModel;
import model.shaders.entity.EntityShader;
import model.textures.ModelTexture;
import model.toolbox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

/**
 * Delegate renderer to handle the rendering of entities.
 * <p/>
 * An {@link Entity} is any object within the game that is not part of the world (ie the terrain or skybox)
 *
 * @author Marcel van Workum
 */
public class EntityRenderer {

    private EntityShader shader;

    /**
     * Constructor
     *
     * @param shader           ShaderProgram to handle shader for each entity rendered
     * @param projectionMatrix 4x4 Projection Matrix defining how the entity is projected
     */
    public EntityRenderer(EntityShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Renders the game entities in batch, reducing draw calls
     * <p/>
     * Saves time and space using a map, rather than each entity having a unique texture
     *
     * @param entities Map of {@link TexturedModel} to a List of {@link Entity}, as each textured model may have many                 entities using that texture.
     */
    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {

            // Must prepared model by binding VAO and enabling lighting
            prepareTextureModel(model);
            List<Entity> batch = entities.get(model);

            // batch process the entities
            for (Entity e : batch) {

                // Loads the transformation, projection and offset matrices to the model
                prepareInstance(e);

                // Actually renders the model to the Display
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            // Finally cleans up the textured model by unbinding the VAO
            unbindTexturedModel();
        }
    }

    /**
     * Prepares the {@link TexturedModel} by binding the VAO, loading the Atlas to the shader,
     * loading the lighting to the shader and finally binding the texture
     *
     * @param model Texture Model to prepare
     */
    private void prepareTextureModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();

        // Bind the VAO
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        // Atlas
        ModelTexture texture = model.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());

        // If a model is transparent we don't want to cull the back-faces of the model
        if (texture.hasTransparency()) {
            MasterRenderer.disableCulling();
        }

        // loads fake shine to entity if required
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());

        // Loads damper and reflectivity for specular lighting to the shader
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        // finally bind the texture to the VAO
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }

    /**
     * Cleans up the TexturedModel by re-enabling culling and unbinding the VAO
     */
    private void unbindTexturedModel() {
        MasterRenderer.enableCulling(); //TODO is this slowing the render process down?
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    /**
     * Prepares the entity for rendering by loading the 4x4 transformation matrix and x/y offset to the shader
     *
     * @param entity Entity to prepare
     */
    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
                entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }


}
