package model.textures;

/**
 * Represents a Texture to be used on a obj model
 *
 * @author Marcel van Workum
 */
public class ModelTexture {

    // OPENGL binding id
    private int textureID;

    // Default values which should be overridden
    private float shineDamper = 10;
    private float reflectivity = 1;

    // Number of rows in the texture.
    // Used for atlassing
    private int numberOfRows = 1;

    // Used for double sided textures
    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;

    /**
     * Creates the Model texture
     *
     * @param id OpenGL binding id
     */
    public ModelTexture(int id) {
        this.textureID = id;
    }

    /**
     * Gets iD.
     *
     * @return the iD
     */
    public int getID() {
        return this.textureID;
    }

    /**
     * Gets number of rows.
     *
     * @return the number of rows
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Sets number of rows.
     *
     * @param numberOfRows the number of rows
     */
    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    /**
     * Gets shine damper.
     *
     * @return the shine damper
     */
    public float getShineDamper() {
        return shineDamper;
    }

    /**
     * Gets reflectivity.
     *
     * @return the reflectivity
     */
    public float getReflectivity() {
        return reflectivity;
    }

    /**
     * Sets shine damper.
     *
     * @param shineDamper the shine damper
     */
    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    /**
     * Sets reflectivity.
     *
     * @param reflectivity the reflectivity
     */
    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    /**
     * Has transparency.
     *
     * @return the boolean
     */
    public boolean hasTransparency() {
        return hasTransparency;
    }

    /**
     * Sets has transparency.
     *
     * @param hasTransparency the has transparency
     */
    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    /**
     * Is use fake lighting.
     *
     * @return the boolean
     */
    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    /**
     * Sets use fake lighting.
     *
     * @param useFakeLighting the use fake lighting
     */
    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }
}
