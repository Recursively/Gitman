package model.textures;

import java.nio.ByteBuffer;

/**
 * Texture data containing a buffer of all the texture information.
 *
 * @author Marcel van Workum - 300313949
 */
public class TextureData {

    private int width;
    private int height;
    private ByteBuffer buffer;

    /**
     * Instantiates a new Texture data.
     *
     * @param buffer the buffer
     * @param width  the width
     * @param height the height
     */
    public TextureData(ByteBuffer buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    /**
     * Get width.
     *
     * @return the int
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get height.
     *
     * @return the int
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get buffer.
     *
     * @return the byte buffer
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }

}
