package model.textures;

/**
 * Terrain texture used to model the terrain texture....
 *
 * @author Marcel van Workum - 300313949
 */
public class TerrainTexture {

    // opengl binding of the texture
    private int textureID;

    /**
     * Instantiates a new Terrain texture.
     *
     * @param textureID the texture iD
     */
    public TerrainTexture(int textureID) {
        this.textureID = textureID;
    }

    /**
     * Gets texture iD.
     *
     * @return the texture iD
     */
    public int getTextureID() {
        return textureID;
    }
}
