package model.factories;

import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.util.vector.Vector2f;

/**
 * Factory Game for creating Gui Components
 *
 * @author Marcel van Workum
 */
public class GuiFactory {

    private static final String GUI_PATH = "gui/";
    private final Loader loader;

    /**
     * Create the Gui factory passing in the object loader
     *
     * @param loader Object loader
     */
    public GuiFactory(Loader loader) {
        this.loader = loader;
    }

    /**
     * Creates a {@link GuiTexture} with the specified texture, position and scale.
     *
     * @param textureName Texture
     * @param position Position on the display x(0-1) y(0-1)
     * @param scale scale of the texture on display x(0-1) y(0-1)
     *
     * @return The GuiTexture created
     */
    public GuiTexture makeGuiTexture(String textureName, Vector2f position, Vector2f scale) {
        return new GuiTexture(loader.loadTexture(GUI_PATH + textureName), position, scale);
    }
}
