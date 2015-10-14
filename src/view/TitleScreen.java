package view;

import controller.AudioController;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.util.vector.Vector2f;
import view.renderEngine.GuiRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Produces the title screen for the game
 * and handles logic for key presses as the action controller class is not made yet
 *
 * @author Ellie
 * @author Marcel
 */

public class TitleScreen {

    private String hostname;
    private boolean isHost;

    /**
     * Instantiates a new Title screen.
     */
    public TitleScreen(boolean isHost, String hostname, boolean fullscreen) {
        this.hostname = hostname;
        this.isHost = isHost;
        DisplayManager.createDisplay(fullscreen);
        Keyboard.enableRepeatEvents(false);

        // Creates the audio controller
        new AudioController();
        AudioController.playMenuLoop();

        blinkTitle(fullscreen);
    }

    /**
     * Cycles through the title screens making the _ blink
     */
    private void blinkTitle(boolean fullscreen) {

        GuiRenderer guiRenderer = new GuiRenderer();
        long timer = System.currentTimeMillis();
        int index = 0;

        GuiTexture[] images = initTitleScreens();
        boolean closed = false;

        while (!closed) {

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
                break;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                DisplayManager.closeDisplay();
                closed = true;
            }
        }

        // create the game now
        if (!closed) {
            new PlayLoadHelpScreen(isHost, hostname, fullscreen);
        } else {
            //kills music
            AL.destroy();
        }
    }

    /**
     * @return an Array of title screen images to render
     */
    private GuiTexture[] initTitleScreens() {
        GuiTexture[] images = new GuiTexture[2];
        String PATH = "titleScreen";
        images[0] = new GuiTexture(Loader.loadTexture(PATH + File.separator + "GitmanTitle1"), new Vector2f(0, 0),
                new Vector2f(1, 1));

        images[1] = new GuiTexture(Loader.loadTexture(PATH + File.separator + "GitmanTitle2"), new Vector2f(0, 0),
                new Vector2f(1, 1));
        return images;
    }
}
