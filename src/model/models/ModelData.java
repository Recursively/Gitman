package model.models;

import model.entities.staticEntity.BoundingBox;

/**
 * Data class containing all the information about a wavefront obj model
 *
 * @author Marcel van Workum - 300313949
 */
public class ModelData {

    private final float[] vertices;
    private final float[] textureCoords;
    private final float[] normals;
    private final int[] indices;

    // BOUNDS INFO
    private float minX = Float.MAX_VALUE;
    private float minY = Float.MAX_VALUE;
    private float minZ = Float.MAX_VALUE;

    private float maxX = Float.MIN_VALUE;
    private float maxY = Float.MIN_VALUE;
    private float maxZ = Float.MIN_VALUE;

    /**
     * Instantiates a new Model data.
     *
     * @param vertices      the vertices
     * @param textureCoords the texture coords
     * @param normals       the normals
     * @param indices       the indices
     */
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;

        parseBounds();
    }

    /**
     * Parses the bounds of the model
     */
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
     * Gets bounding box.
     *
     * @return the bounding box
     */
    public BoundingBox getBoundingBox() {
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}