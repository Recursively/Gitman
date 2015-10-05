package model.factories;

import model.terrains.Terrain;
import model.textures.TerrainTexture;
import model.textures.TerrainTexturePack;
import model.toolbox.Loader;

/**
 * Terrain factory to generate new terrains.
 *
 * @author Marcel van Workum
 */
public class TerrainFactory {

    private final Loader loader;

    // outside textures
    private TerrainTexturePack outsideTexturePack;
    private TerrainTexture outsideBlendMap;

    // office textures
    private TerrainTexturePack officeTexturePack;
    private TerrainTexture officeBlendMap;

    /**
     * Instantiates a new Terrain factory.
     *
     * @param loader the loader
     */
    public TerrainFactory(Loader loader) {
        this.loader = loader;

        initOutsideTextures(loader);

        initOfficeTextures(loader);
    }

    private void initOutsideTextures(Loader loader) {
        // Terrain creation
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("textures/mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("textures/grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("textures/path"));

        // Bundle terrains into pack
        outsideTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        // Blend map for mixing terrains
        outsideBlendMap = new TerrainTexture(loader.loadTexture("terrains/blendMap2"));
    }

    private void initOfficeTextures(Loader loader) {
        //TODO only using one texture at the moment

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/path"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("textures/path"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("textures/path"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("textures/path"));

        // Bundle terrains into pack
        officeTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        // Blend map for mixing terrains
        officeBlendMap = new TerrainTexture(loader.loadTexture("terrains/officeBlendMap"));
    }

    /**
     * Make terrain.
     *
     * @return the terrain
     */
    public Terrain makeOutsideTerrain(int gridX, int gridZ) {
        // Create the new terrain object, using pack blendermap and heightmap
        return new Terrain(gridX, gridZ, loader, outsideTexturePack, outsideBlendMap, "terrains/heightMap2");
    }

    public Terrain makeOfficeTerrain(int gridX, int gridZ) {
        // Create the new terrain object, using pack blendermap and heightmap
        return new Terrain(gridX, gridZ, loader, officeTexturePack, officeBlendMap, "terrains/officeHeightMap");
    }
}
