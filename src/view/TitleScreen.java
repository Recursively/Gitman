package view;

import controller.GameController;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import view.renderEngine.GuiRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Produces the title screen for the game
 *
 * @author Ellie
 */

public class TitleScreen {

    private final GuiRenderer guiRenderer;
    private final Loader loader;

    public TitleScreen() {

        DisplayManager.createDisplay();
        loader = new Loader();
        guiRenderer = new GuiRenderer(loader);
        blinkTitle();

    }

    private void blinkTitle() {

        long timer = System.currentTimeMillis();
        int index = 0;
        GuiTexture[] images = initTitleScreens();

        while (true) {

            long currentTime = System.currentTimeMillis();
            if (currentTime - timer > 500) {
                index++;
                timer += 500;
            }
            List<GuiTexture> guiList = new ArrayList<GuiTexture>();
            guiList.add(images[index % 2]);
            guiRenderer.render(guiList);
            DisplayManager.updateDisplay();

            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                DisplayManager.closeDisplay();
                break;
            }
        }

        new GameController();
    }

    /**
     * @return an Array of images to render
     */
    private GuiTexture[] initTitleScreens() {
        GuiTexture[] images = new GuiTexture[2];
        images[0] = new GuiTexture(loader.loadTexture("screenimages" + File.separator + "GitmanTitle1"),
                new Vector2f(0, 0), new Vector2f(1, 1));

        images[1] = new GuiTexture(loader.loadTexture("screenimages" + File.separator + "GitmanTitle2"),
                new Vector2f(0, 0), new Vector2f(1, 1));
        return images;
    }


}
