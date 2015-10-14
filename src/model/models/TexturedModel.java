package model.models;

import model.textures.ModelTexture;

/**
 * A texture model is a raw model that has a model texture applied to it.
 *
 * @author Marcel van Workum - 300313949
 */
public class TexturedModel {

    private RawModel rawModel;
    private ModelTexture texture;

    /**
     * Instantiates a new Textured model.
     *
     * @param model   the model
     * @param texture the texture
     */
    public TexturedModel(RawModel model, ModelTexture texture) {
        this.rawModel = model;
        this.texture = texture;
    }

    /**
     * Gets raw model.
     *
     * @return the raw model
     */
    public RawModel getRawModel() {
        return rawModel;
    }

    /**
     * Gets texture.
     *
     * @return the texture
     */
    public ModelTexture getTexture() {
        return texture;
    }
}
