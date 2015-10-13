package model.terrains;

import model.models.RawModel;
import model.textures.TerrainTexture;
import model.textures.TerrainTexturePack;
import model.toolbox.Loader;
import model.toolbox.Maths;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A Terrain is an area of a location. A terrain depends on a blend map and height map to know how to style the
 * terrain. The terrain is made up of a texture pack
 *
 * @author Marcel van workum
 */
public class Terrain {

    // Map parameters : change these to shape terrain
    private float SIZE = 512;
    private static final float MAX_HEIGHT = 40;
    private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;

    private float[][] heights;
    private float gridX;
    private float gridZ;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private float[] vertices;
    private float[] normals;
    private float[] textureCoords;
    private int[] indices;

    /**
     * Constructor
     * <p/>
     * Creates a new terrain at the given gridX/gridZ grid
     *
     * @param gridX     gridX position
     * @param gridZ     gridZ position
     * @param texture   texture pack to make up the terrain
     * @param blendMap  blend map for mixing textures
     * @param heightMap height map to style terrain y values
     */
    public Terrain(int gridX, int gridZ, TerrainTexturePack texture, TerrainTexture blendMap,
                   String heightMap) {
        this.blendMap = blendMap;
        this.texturePack = texture;
        this.gridX = gridX * SIZE;
        this.gridZ = gridZ * SIZE;

        // finally generate the terrain from the height map
        this.model = generateTerrain(heightMap);
    }

    /**
     * Constructor
     * <p/>
     * Creates a new terrain at the given gridX/gridZ grid
     *
     * @param gridX     gridX position
     * @param gridZ     gridZ position
     * @param texture   texture pack to make up the terrain
     * @param blendMap  blend map for mixing textures
     * @param heightMap height map to style terrain y values
     */
    public Terrain(int gridX, int gridZ, TerrainTexturePack texture, TerrainTexture blendMap,
                   String heightMap, int size) {
        SIZE = size;
        this.blendMap = blendMap;
        this.texturePack = texture;
        this.gridX = gridX * SIZE;
        this.gridZ = gridZ * SIZE;

        // finally generate the terrain from the height map
        this.model = generateTerrain(heightMap);
    }

    /**
     * Gets the y height value for a specified gridX and gridZ co-ordinate
     *
     * @param worldX gridX coordinate in the world
     * @param worldZ gridZ coordinate in the wolrd
     * @return y position
     */
    public float getTerrainHeight(float worldX, float worldZ) {
        // get the gridX and gridZ on the terrain, not the world
        float terrainX = worldX - this.gridX;
        float terrainZ = worldZ - this.gridZ;

        // Calculates the grid siz
        float gridSquareSize = SIZE / (float) (heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        // checks that you're actually on the terrain
        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1
                || gridX < 0 || gridZ < 0) {
            return 0;
        }

        // calculate the coordinate of the player in a square
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

        // running out of names
        float answer;

        // uses the barry centric formula to calculate the y position on the triangle
        // breaks each square up into two triangles

        // in top left triangle
        if (xCoord <= (1 - zCoord)) {
            answer = Maths
                    .barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        // bottom right triangle
        else {
            answer = Maths
                    .barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }

        return answer;
    }

    /**
     * Generates the terrain
     *
     * @param heightMap Height map to use when generating the terrain
     * @return A raw model representing the terrain information
     */
    private RawModel generateTerrain(String heightMap) {

        // Gets the buffered image for the height map
        BufferedImage image = getBufferedImage(heightMap);

        // Loads heights and counts
        int vertexCount = image.getHeight();
        heights = new float[vertexCount][vertexCount];

        // Parses a terrain and indices
        parseTerrainInformation(image, vertexCount);
        loadTerrainIndices(vertexCount);

        return Loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    /**
     * Parses the information of the terrain into a data structures
     *
     * @param image       Height map
     * @param vertexCount number of vertices in the terrain
     */
    private void parseTerrainInformation(BufferedImage image, int vertexCount) {
        // inits the structures
        initStructures(vertexCount);

        int vertexPointer = 0;
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {

                // parses the vertices
                vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * SIZE;
                float height = getHeight(j, i, image);
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;

                // parses the normals of the terrain used to shade
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

                // parse the textures coordinates
                textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
                vertexPointer++;
            }
        }
    }

    /**
     * Simply inits the structures
     *
     * @param vertexCount number of vertices in the terrain
     */
    private void initStructures(int vertexCount) {
        int count = vertexCount * vertexCount;
        vertices = new float[count * 3];
        normals = new float[count * 3];
        textureCoords = new float[count * 2];
        indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
    }

    /**
     * Loads the terrain Indices to the terrain data
     *
     * @param vertexCount number of vertices in the terrain
     */
    private void loadTerrainIndices(int vertexCount) {
        int pointer = 0;
        for (int gz = 0; gz < vertexCount - 1; gz++) {
            for (int gx = 0; gx < vertexCount - 1; gx++) {
                int topLeft = (gz * vertexCount) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * vertexCount) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
    }

    /**
     * Attempts to parse the height map
     *
     * @param heightMap Height map
     * @return Buffered image
     */
    private BufferedImage getBufferedImage(String heightMap) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(ResourceLoader.getResourceAsStream("res/" + heightMap + ".png"));
        } catch (IOException e) {
            System.err.println("Failed to load height map");
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Calculates the normal for the Point on the terrain
     *
     * @param x     gridX of terrain
     * @param z     gridZ of terrain
     * @param image heightmap
     * @return Normal vector
     */
    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        // non optimal solution, but only occurs once
        float heightL = getHeight(x - 1, z, image);
        float heightR = getHeight(x + 1, z, image);
        float heightD = getHeight(x, z - 1, image);
        float heightU = getHeight(x, z + 1, image);

        // Creates the normal vector and normalises
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();

        return normal;
    }

    /**
     * Parses the terrain y height for the given pixel of the height map
     *
     * @param x     gridX position
     * @param z     gridZ position
     * @param image height map
     * @return y position
     */
    private float getHeight(int x, int z, BufferedImage image) {
        if (x < 0 || x >= image.getHeight() | z < 0 || z >= image.getHeight()) {
            return 0;
        }

        float height = image.getRGB(x, z);

        // gives us a reasonable range
        height += MAX_PIXEL_COLOUR / 2f;

        // now gives us a range of -1 -> 1
        height /= MAX_PIXEL_COLOUR / 2f;

        // gives a value between max height -> -max height
        height *= MAX_HEIGHT;

        return height;
    }

    /**
     * Gets gridX.
     *
     * @return the gridX
     */
    public float getGridX() {
        return gridX;
    }

    /**
     * Gets gridZ.
     *
     * @return the gridZ
     */
    public float getGridZ() {
        return gridZ;
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public RawModel getModel() {
        return model;
    }

    /**
     * Gets texture pack.
     *
     * @return the texture pack
     */
    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    /**
     * Gets blend map.
     *
     * @return the blend map
     */
    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public float getSIZE() {
        return SIZE;
    }

}
