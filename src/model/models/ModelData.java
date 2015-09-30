package model.models;

import model.entities.staticEntity.BoundingBox;

/**
 * Data class containing all the information about a wavefront obj model
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
     * Instantiates a new Model data.
     *
     * @param vertices the vertices
     * @param textureCoords the texture coords
     * @param normals the normals
     * @param indices the indices
     * @param furthestPoint the furthest point
     */
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
                     float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;

        parseBounds();
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

    // BOUNDS INFO
    private float minX = Float.MAX_VALUE;
    private float minY = Float.MAX_VALUE;
    private float minZ = Float.MAX_VALUE;

    private float maxX = Float.MIN_VALUE;
    private float maxY = Float.MIN_VALUE;
    private float maxZ = Float.MIN_VALUE;

    private void parseBounds() {
        for (int i = 0; i < vertices.length; i++) {
            if (i % 3 == 0) {
                // x
                float vert = vertices[i];

                if (vert < minX) {
                    minX = vert;
                }

                if (vert > maxX) {
                    maxX = vert;
                }

            } else if (i % 3 == 1) {
                // y
                float vert = vertices[i];

                if (vert < minY) {
                    minY = vert;
                }

                if (vert > maxY) {
                    maxY = vert;
                }

            } else {
                // z
                float vert = vertices[i];

                if (vert < minZ) {
                    minZ = vert;
                }

                if (vert > maxZ) {
                    maxZ = vert;
                }
            }
        }
    }

    public BoundingBox getBoundingBox() {
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}