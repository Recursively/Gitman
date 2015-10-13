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

    private void initOfficeTextures() {
        //TODO only using one texture at the moment

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
     * Make terrain.
     *
     * @return the terrain
     */
    public Terrain makeOutsideTerrain(int gridX, int gridZ) {
        // Create the new terrain object, using pack blendermap and heightmap
        return new Terrain(gridX, gridZ,outsideTexturePack, outsideBlendMap, "terrains/heightMap");
    }

    public Terrain makeOfficeTerrain(int gridX, int gridZ) {
        // Create the new terrain object, using pack blendermap and heightmap
        return new Terrain(gridX, gridZ,officeTexturePack, officeBlendMap, "terrains/officeHeightMap", 128);
    }
}
