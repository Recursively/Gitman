package model.toolbox.objParser;

import org.lwjgl.util.vector.Vector3f;

/**
 * Vertex class containing all the information about a given vertex
 *
 * @author Marcel van Workum - 300313949
 */
public class Vertex {

    private static final int NO_INDEX = -1;

    private Vector3f position;
    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private Vertex duplicateVertex = null;
    private int index;
    private float length;

    /**
     * Instantiates a new Vertex.
     *
     * @param index    the index
     * @param position the position
     */
    public Vertex(int index, Vector3f position) {
        this.index = index;
        this.position = position;
        this.length = position.length();
    }

    /**
     * Get index.
     *
     * @return the int
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get length.
     *
     * @return the float
     */
    public float getLength() {
        return length;
    }

    /**
     * Is set.
     *
     * @return the boolean
     */
    public boolean isSet() {
        return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
    }

    /**
     * Checks to see if the texture and normal are equal
     *
     * @param textureIndexOther the texture index other
     * @param normalIndexOther  the normal index other
     * @return the boolean
     */
    public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
        return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
    }

    /**
     * Set texture index.
     *
     * @param textureIndex the texture index
     */
    public void setTextureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
    }

    /**
     * Set normal index.
     *
     * @param normalIndex the normal index
     */
    public void setNormalIndex(int normalIndex) {
        this.normalIndex = normalIndex;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Gets texture index.
     *
     * @return the texture index
     */
    public int getTextureIndex() {
        return textureIndex;
    }

    /**
     * Gets normal index.
     *
     * @return the normal index
     */
    public int getNormalIndex() {
        return normalIndex;
    }

    /**
     * Gets duplicate vertex.
     *
     * @return the duplicate vertex
     */
    public Vertex getDuplicateVertex() {
        return duplicateVertex;
    }

    /**
     * Sets duplicate vertex.
     *
     * @param duplicateVertex the duplicate vertex
     */
    public void setDuplicateVertex(Vertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }

}