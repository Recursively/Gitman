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

    /**
     * Instantiates a new Title screen.
     */
    public TitleScreen() {
        DisplayManager.createDisplay();
        blinkTitle();
    }

    /**
     * Cycles through the title screens making the _ blink
     */
    private void blinkTitle() {

        Loader loader = new Loader();
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        long timer = System.currentTimeMillis();
        int index = 0;

        GuiTexture[] images = initTitleScreens(loader);

        while (true) {

            // ticks time every half second
            long currentTime = System.currentTimeMillis();
            if (currentTime - timer > 500) {
                index++;
                timer += 500;
            }

            // converts to list and renders
            List<GuiTexture> guiList = new ArrayList<>();
            guiList.add(images[index % 2]);
            guiRenderer.render(guiList);
            DisplayManager.updateDisplay();

            // user begins game
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                DisplayManager.closeDisplay();
                break;
            }
        }

        // create the game now
        new GameController();
    }

    /**
     * @return an Array of title screen images to render
     */
    private GuiTexture[] initTitleScreens(Loader loader) {
        GuiTexture[] images = new GuiTexture[2];
        String PATH = "titleScreen";
        images[0] = new GuiTexture(loader.loadTexture(PATH + File.separator + "GitmanTitle1"),
                new Vector2f(0, 0), new Vector2f(1, 1));

        images[1] = new GuiTexture(loader.loadTexture(PATH + File.separator + "GitmanTitle2"),
                new Vector2f(0, 0), new Vector2f(1, 1));
        return images;
    }


}
