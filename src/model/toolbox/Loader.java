package model.toolbox;

import de.matthiasmann.twl.utils.PNGDecoder;
import model.models.RawModel;
import model.textures.TextureData;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader class to handle the loading of wavefront obj files into OPENGL VAO and VBO
 *
 * @author Marcel van Workum
 */
public class Loader {

    //TODO this should be static

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    /**
     *  Loads the obj model to a opengl VAO and returns a rawmodel of the vao
     *
     * @param positions vertex positions
     * @param textureCoords vertex texture coordinates
     * @param normals vertex texture normals
     * @param indices vertex indices
     *
     * @return A raw model containing the vao data
     */
    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        // first crate the VAO
        int vaoID = createVAO();

        // binds the indices to the VBO
        bindIndicesBuffer(indices);

        // Binds the attribs to the VAO's VBOs
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);

        // finally unbind the vao
        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    /**
     * Handles the loading of VAO which are not 3d image buffers, eg skybox or gui
     *
     * @param positions positon data
     * @param dimensions dimensions of the vao
     *
     * @return Raw model of VAO data
     */
    public RawModel loadToVAO(float[] positions, int dimensions) {
        // generate VAO
        int vaoID = createVAO();

        //  store data into VBO
        this.storeDataInAttributeList(0, dimensions, positions);

        // finally unbind the vao
        unbindVAO();

        return new RawModel(vaoID, positions.length / dimensions);
    }

    /**
     * Loads a texture using the slick util
     *
     * @param fileName filename of the texture
     * @return textureID
     */
    public int loadTexture(String fileName) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));

            // Mipmapping to lower resolution of distance textures
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

            // Changes the level of detail to change the mipmapping to a slightly higher resolution
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Safety check
        int textureID = 0;
        if (texture != null) {
            textureID = texture.getTextureID();
        } else {
            try {
                throw new FileNotFoundException("Failed to load texture file");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.err.println("Failed to load texture file in Loader");
            }
        }

        // adds texture to list of textures
        textures.add(textureID);

        return textureID;
    }

    /**
     * Attempts to load a cube map using PNGDecoder
     *
     * @param textureFiles List of texture files
     *
     * @return textureID
     */
    public int loadCubeMap(String[] textureFiles) {

        // generates empty texture
        int textureID = GL11.glGenTextures();

        // bind that texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);

        for (int i = 0; i < textureFiles.length; i++) {
            TextureData data = decodeSkyboxTexture("res/skyboxes/" + textureFiles[i] + ".png");

            // gets the GL enum for the cube map faces so that we can easily iterate
            // this does however mean that the order of the texture files has to be exact
            // order as follows

            // +X
            // -X
            // +Y
            // -Y
            // +Z
            // -Z

            int baseNumber = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
            GL11.glTexImage2D(baseNumber + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        // Smooths the textures of the skybox
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        textures.add(textureID);

        // helps to prevent tearing of skybox textures
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        return textureID;
    }

    /**
     * Cleans up the VAOs, VBOs and Textures
     */
    public void cleanUp() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }

        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    /**
     * Creates a new VAO to store the VBOs
     *
     * @return id of vao
     */
    private int createVAO() {
        // generates vao
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);

        // binds it to opengl
        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    /**
     * Stores data into a VAO attrib list as a VBO
     *
     * @param attributeNumber attrib list number to store in
     * @param coordinateSize size of coordinate
     * @param data the actual data to store
     */
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        // first generate the VBO
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);

        // bind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        // Create float buffer
        FloatBuffer buffer = storeDataInFloatBuffer(data);

        // bind buffer to VBO
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        // Finally bind to the VAO
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Unbinds the VAO
     */
    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    /**
     * Generates a VBO and adds it to the list of vbos
     *
     * @param indices indices to bin to buffer
     */
    private void bindIndicesBuffer(int[] indices) {
        // generates the VBO and adds to lsit
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);

        // Bind the vbo
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        // stores buffer data into VBO
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * Creates a int buffer to be stored in a vbo
     *
     * @param data data to store in buffer
     *
     * @return IntBuffer
     */
    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);

        // need to flip so that the buffer can now be read
        buffer.flip();

        return buffer;
    }

    /**
     * Creates a float buffer to be stored in a vbo
     *
     * @param data data to store in buffer
     *
     * @return float buffer
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);

        // need to flip so that the buffer can be read
        buffer.flip();

        return buffer;
    }

    /**
     * Helper method to decode a Skybox texture
     *
     * @param fileName fileName of skybox texture
     *
     * @return Texture data for the skybox texture
     */
    private TextureData decodeSkyboxTexture(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);

            // delegates to PNGDecoder to decode the image
            PNGDecoder decoder = new PNGDecoder(in);

            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);

            // flips the buffer so that it can be read
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
        }

        return new TextureData(buffer, width, height);
    }

}
