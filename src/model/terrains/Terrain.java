package model.terrains;

import model.models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import model.toolbox.Loader;
import model.textures.TerrainTexture;
import model.textures.TerrainTexturePack;
import model.toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {

    private static final float SIZE = 1000;
    private static final float MAX_HEIGHT = 40;
    private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;

    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    private float[][] heights;

    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texture, TerrainTexture blendMap,
                   String heightMap) {
        this.blendMap = blendMap;
        this.texturePack = texture;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMap);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public float getTerrainHeight(float worldX, float worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / (float) (heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1
                || gridX < 0 || gridZ < 0) {
            return 0;
        }

        // calculate the coordinate of the player in a square
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

        // running out of names
        float answer;

        // in top left triangle
        if (xCoord <= (1-zCoord)) {
            answer = Maths
                    .barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = Maths
                    .barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }

        return answer;
    }

    private RawModel generateTerrain(Loader loader, String heightMap) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        } catch (IOException e) {
            System.err.println("Failed to load height map");
            e.printStackTrace();
        }

        int vertexCount = image.getHeight();
        heights = new float[vertexCount][vertexCount];

        int count = vertexCount * vertexCount;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
        int vertexPointer = 0;
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * SIZE;
                float height = getHeight(j, i, image);
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                // shade terrain
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
                vertexPointer++;
            }
        }
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
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        // non optimal solution, but only occurs once
        float heightL = getHeight(x - 1, z, image);
        float heightR = getHeight(x + 1, z, image);
        float heightD = getHeight(x, z - 1, image);
        float heightU = getHeight(x, z + 1, image);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

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

}
