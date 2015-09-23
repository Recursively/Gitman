package model.models;

/**
 * A raw model is a link to the OPEN GL vaoID and a count of the vertices
 *
 * @author Marcel van Workum
 */
public class RawModel {

    private int vaoID;
    private int vertexCount;

    /**
     * Instantiates a new Raw model.
     *
     * @param vaoID the vao iD
     * @param vertexCount the vertex count
     */
    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    /**
     * Gets vertex count.
     *
     * @return the vertex count
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Gets vao iD.
     *
     * @return the vao iD
     */
    public int getVaoID() {
        return vaoID;
    }

}
