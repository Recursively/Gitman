package model.factories;

import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

/**
 * Created by Marcel on 19/09/15.
 */
public class GuiFactory {

    private ArrayList<GuiTexture> guiImages;

    public GuiFactory(Loader loader) {
        // Create gui elements

        guiImages = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("gui/panel_brown"), new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f));
        guiImages.add(gui);
    }

    public ArrayList<GuiTexture> getGuiImages() {
        return guiImages;
    }
}
