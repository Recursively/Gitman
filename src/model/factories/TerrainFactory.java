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
    private final TerrainTexture backgroundTexture;
    private final TerrainTexture rTexture;
    private final TerrainTexture gTexture;
    private final TerrainTexture bTexture;
    private final TerrainTexturePack texturePack;
    private final TerrainTexture blendMap;

    /**
     * Instantiates a new Terrain factory.
     *
     * @param loader the loader
     */
    public TerrainFactory(Loader loader) {
        this.loader = loader;

        // TODO

        // Terrain creation
        backgroundTexture = new TerrainTexture(loader.loadTexture("textures/grass"));
        rTexture = new TerrainTexture(loader.loadTexture("textures/mud"));
        gTexture = new TerrainTexture(loader.loadTexture("textures/grassFlowers"));
        bTexture = new TerrainTexture(loader.loadTexture("textures/path"));

        // Bundle terrains into pack
        texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        // Blend map for mixing terrains
        blendMap = new TerrainTexture(loader.loadTexture("terrains/blendMap"));
    }

    /**
     * Make terrain.
     *
     * @return the terrain
     */
    public Terrain makeTerrain() {
        // Create the new terrain object, using pack blendermap and heightmap
        return new Terrain(0, -1, loader, texturePack, blendMap, "terrains/heightMap");
    }

}
