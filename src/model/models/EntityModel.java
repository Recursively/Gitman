package model.models;

import model.textures.ModelTexture;
import model.toolbox.Loader;
import model.toolbox.objParser.OBJFileLoader;

/**
 * Represents a complete entity model to be used by the renderer.
 *
 * @author Marcel van Workum
 */
public class EntityModel {

    private static final String MODEL_PATH = "models/";
    private static final String TEXTURES_PATH = "textures/";

    private final ModelData modelData;
    private final RawModel rawModel;
    private final TexturedModel texturedModel;

    /**
     * Instantiates a new Entity model.
     *
     * @param name               the name
     * @param reflectivityFactor the reflectivity factor
     */
    public EntityModel(String name, float reflectivityFactor) {

        // Loads the model data
        modelData = OBJFileLoader.loadOBJ(MODEL_PATH + name);

        // Loads the raw model
        rawModel = Loader.loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(),
                modelData.getIndices());

        // Loads the textured model
        texturedModel = new TexturedModel(rawModel, new ModelTexture(Loader.loadTexture(TEXTURES_PATH + name)));

        // finally set the reflectivity of the object
        if (reflectivityFactor != -1) {
            texturedModel.getTexture().setReflectivity(reflectivityFactor);
        }
    }

    /**
     * Instantiates a new Entity model.
     *
     * @param name               the name
     * @param textureCount       the texture count
     * @param reflectivityFactor the reflectivity factor
     */
    public EntityModel(String name, int textureCount, float reflectivityFactor) {

        // Loads the model data
        modelData = OBJFileLoader.loadOBJ(MODEL_PATH + name);

        // Loads the raw model
        rawModel = Loader.loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(),
                modelData.getIndices());

        // Loads the textured model
        texturedModel = new TexturedModel(rawModel, new ModelTexture(Loader.loadTexture(TEXTURES_PATH + name + textureCount)));

        // finally set the reflectivity of the object
        if (reflectivityFactor != -1) {
            texturedModel.getTexture().setReflectivity(reflectivityFactor);
        }
    }

    /**
     * Gets model data.
     *
     * @return the model data
     */
    public ModelData getModelData() {
        return modelData;
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
     * Gets textured model.
     *
     * @return the textured model
     */
    public TexturedModel getTexturedModel() {
        return texturedModel;
    }
}
