package model.textures;

import org.lwjgl.util.vector.Vector2f;

/**
 * A 2d image texture used to render the gui
 *
 * @author Marcel van Workum
 */
public class GuiTexture {

    // OPENGL texture binding
    private int texture;
    private Vector2f position;
    private Vector2f scale;

    /**
     * Constructor
     *
     * @param texture opengl texture binding
     * @param position Position of the screen of the texture
     * @param scale scale of the texture
     */
    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    /**
     * Gets texture.
     *
     * @return the texture
     */
    public int getTexture() {
        return texture;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Vector2f getPosition() {
        return position;
    }
    
    /**
     * Sets position.
    *
    * 
    */
   public void setPosition(Vector2f position) {
       this.position = position;
   }

    /**
     * Gets scale.
     *
     * @return the scale
     */
    public Vector2f getScale() {
        return scale;
    }

    /**
     * Creates a copy of the current gui texture
     *
     * @return A copy of the gui texture
     */
	public GuiTexture copy() {
		return new GuiTexture(texture, position, scale);
	}
}
