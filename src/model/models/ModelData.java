package model.models;

/**
 * Data class containing all the data for a model object
 *
 * @author Marcel van Workum
 */
public class ModelData {

    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    private float furthestPoint;

    /**
     * initialises the model data
     */
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
                     float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
    }

    /**
     * Get vertices.
     *
     * @return the float [ ]
     */
    public float[] getVertices() {
        return vertices;
    }

    /**
     * Get texture coords.
     *
     * @return the float [ ]
     */
    public float[] getTextureCoords() {
        return textureCoords;
    }

    /**
     * Get normals.
     *
     * @return the float [ ]
     */
    public float[] getNormals() {
        return normals;
    }

    /**
     * Get indices.
     *
     * @return the int [ ]
     */
    public int[] getIndices() {
        return indices;
    }

    /**
     * Gets furthest point.
     *
     * @return the furthest point
     */
    public float getFurthestPoint() {
        return furthestPoint;
    }

}