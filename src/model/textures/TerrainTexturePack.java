package model.textures;

/**
 * Texture pack containing four textures.
 *
 * @author Marcel van workum
 */
public class TerrainTexturePack {

    private TerrainTexture backgroundTexture;
    private TerrainTexture rTexture;
    private TerrainTexture gTexture;
    private TerrainTexture bTexture;

    /**
     * Instantiates a new Terrain texture pack.
     *
     * @param backgroundTexture the background texture
     * @param rTexture the r texture
     * @param gTexture the g texture
     * @param bTexture the b texture
     */
    public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture,
                              TerrainTexture bTexture) {
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    /**
     * Gets background texture.
     *
     * @return the background texture
     */
    public TerrainTexture getBackgroundTexture() {
        return backgroundTexture;
    }

    /**
     * Gets texture.
     *
     * @return the texture
     */
    public TerrainTexture getrTexture() {
        return rTexture;
    }

    /**
     * Gets texture.
     *
     * @return the texture
     */
    public TerrainTexture getgTexture() {
        return gTexture;
    }

    /**
     * Gets texture.
     *
     * @return the texture
     */
    public TerrainTexture getbTexture() {
        return bTexture;
    }
}