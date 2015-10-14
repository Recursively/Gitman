package view;

import controller.GameController;
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
 * Displays the Play Load Help screen and deals with simple key press logic as the
 * action controller is currently not created
 *
 * @author Ellie
 * @author Divya
 * @author Marcel
 */
public class PlayLoadHelpScreen {

    //networking
    private String hostname;
    private boolean isHost;

    //rendering
    private String PATH = "titleScreen";

    /**
     * Instantiates a new Play load help screen.
     *
     * @param isHost   whether this is the host
     * @param hostname name of Host
     */
    public PlayLoadHelpScreen(boolean isHost, String hostname) {
        this.hostname = hostname;
        this.isHost = isHost;

        Keyboard.enableRepeatEvents(false);
        blinkTitle();
    }

    /**
     * Blinks the title and handles key press logic for selecting wither play load or help
     */
    private void blinkTitle() {
        GuiRenderer guiRenderer = new GuiRenderer();

        long timer = System.currentTimeMillis();
        int index = 0;

        int selectionPointer = 0;
        GuiTexture[] selections = initTitleScreens();
        GuiTexture blankTitleImage = initBlankTitleScreen();

        boolean load = false;
        boolean closed = false;

        while (Keyboard.next()) {
            // polls
        }

        boolean pollOffReturn = false;

        while (!closed) {

            if (pollOffReturn) {
                while (Keyboard.next()) {
                    // polls
                }
                pollOffReturn = false;
            }

            // ticks time every half second
            long currentTime = System.currentTimeMillis();
            if (currentTime - timer > 500) {
                index++;
                timer += 500;
            }

            // converts to list and renders
            List<GuiTexture> guiList = new ArrayList<>();
            if (index % 2 == 0) {
                guiList.add(blankTitleImage);
            } else {
                guiList.add(selections[selectionPointer]);
            }

            // update the display
            guiRenderer.render(guiList);
            DisplayManager.updateDisplay();

            // user begins game
            if (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {

                    // close game
                    if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                        DisplayManager.closeDisplay();
                        closed = true;
                    }

                    // check arrow keys

                    else if (Keyboard.getEventKey() == Keyboard.KEY_UP) {

                        // decrease pointer position
                        if (selectionPointer == 0) {
                            selectionPointer = 2;
                        } else {
                            selectionPointer -= 1;
                            selectionPointer %= 3;
                        }
                    } else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {

                        // increase pointer position
                        selectionPointer += 1;
                        selectionPointer %= 3;
                    }

                    if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {

                        // do selected option
                        if (selectionPointer == 0) {
                            break;
                        } else if (selectionPointer == 1) {
                            load = true;
                            break;
                        } else if (selectionPointer == 2) {
                            HelpScreen helpScreen = new HelpScreen();
                            closed = helpScreen.wasClosed();
                            pollOffReturn = true;
                        }
                    }
                }
            }
        }

        if (!closed) {
            new GameController(isHost, hostname, load);
        } else {
            //kills music
            AL.destroy();
        }
    }

    /**
     * Initialises a blank title screen
     *
     * @return the GuiTexture initialised
     */
    private GuiTexture initBlankTitleScreen() {
        return new GuiTexture(Loader.loadTexture(PATH + File.separator + "playLoadHelp"), new Vector2f(0, 0),
                new Vector2f(1, 1));
    }

    /**
     * @return an Array of title screen images to render
     */
    private GuiTexture[] initTitleScreens() {
        GuiTexture[] images = new GuiTexture[3];
        images[0] = new GuiTexture(Loader.loadTexture(PATH + File.separator + "playLoadHelpPlayUnderscore"), new Vector2f(0, 0),
                new Vector2f(1, 1));

        images[1] = new GuiTexture(Loader.loadTexture(PATH + File.separator + "playLoadHelpLoadUnderscore"), new Vector2f(0, 0),
                new Vector2f(1, 1));


        images[2] = new GuiTexture(Loader.loadTexture(PATH + File.separator + "playLoadHelpHelpUnderscore"), new Vector2f(0, 0),
                new Vector2f(1, 1));
        return images;
    }
}


