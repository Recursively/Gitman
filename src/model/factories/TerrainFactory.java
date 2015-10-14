package model.factories;

import model.terrains.Terrain;
import model.textures.TerrainTexture;
import model.textures.TerrainTexturePack;
import model.toolbox.Loader;

/**
 * Terrain factory to generate new terrains.
 *
 * @author Marcel van Workum - 300313949
 */
public class TerrainFactory {


    // outside textures
    private TerrainTexturePack outsideTexturePack;
    private TerrainTexture outsideBlendMap;

    // office textures
    private TerrainTexturePack officeTexturePack;
    private TerrainTexture officeBlendMap;

    /**
     * Instantiates a new Terrain factory.
     */
    public TerrainFactory() {
        initOutsideTextures();
        initOfficeTextures();
    }

    /**
     * Create the outside terrain
     */
    private void initOutsideTextures() {
        // Terrain creation
        TerrainTexture backgroundTexture = new TerrainTexture(Loader.loadTexture("textures/grass"));
        TerrainTexture rTexture = new TerrainTexture(Loader.loadTexture("textures/mud"));
        TerrainTexture gTexture = new TerrainTexture(Loader.loadTexture("textures/grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(Loader.loadTexture("textures/path"));

        // Bundle terrains into pack
        outsideTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        // Blend map for mixing terrains
        outsideBlendMap = new TerrainTexture(Loader.loadTexture("terrains/blendMap"));
    }

    /**
     * create office terrain
     */
    private void initOfficeTextures() {
        TerrainTexture backgroundTexture = new TerrainTexture(Loader.loadTexture("textures/wood_tiles"));
        TerrainTexture rTexture = new TerrainTexture(Loader.loadTexture("textures/wood_tiles"));
        TerrainTexture gTexture = new TerrainTexture(Loader.loadTexture("textures/wood_tiles"));
        TerrainTexture bTexture = new TerrainTexture(Loader.loadTexture("textures/wood_tiles"));

        // Bundle terrains into pack
        officeTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        // Blend map for mixing terrains
        officeBlendMap = new TerrainTexture(Loader.loadTexture("terrains/officeBlendMap"));
    }

    /**
     * Make outside terrain terrain.
     *
     * @param gridX the grid x
     * @param gridZ the grid z
     * @return the terrain
     */
    public Terrain makeOutsideTerrain(int gridX, int gridZ) {
        final String NAME = "Outside";
        final String DESCRIPTION = "It was supposed to be a fun adventure game but things got out of hand.";
        // Create the new terrain object, using pack blendMap and heightmap
        return new Terrain(gridX, gridZ, outsideTexturePack, outsideBlendMap, "terrains/heightMap", NAME, DESCRIPTION);
    }

    /**
     * Make office terrain terrain.
     *
     * @param gridX the grid x
     * @param gridZ the grid z
     * @return the terrain
     */
    public Terrain makeOfficeTerrain(int gridX, int gridZ) {
        final String NAME = "Office";
        final String DESCRIPTION = "Don't you wish there were windows? However you get a lot of work done here. The people" +
                "are nice, wherever they are.";
        // Create the new terrain object, using pack blendMap and heightmap
        return new Terrain(gridX, gridZ, officeTexturePack, officeBlendMap, "terrains/officeHeightMap", 128, NAME,
                DESCRIPTION);
    }
}
