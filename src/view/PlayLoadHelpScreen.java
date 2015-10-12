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

public class PlayLoadHelpScreen {
    private String hostname;
    private boolean isHost;
    private String PATH = "titleScreen";


    public PlayLoadHelpScreen(boolean isHost, String hostname, boolean fullscreen) {
        this.hostname = hostname;
        this.isHost = isHost;

        Keyboard.enableRepeatEvents(false);
        blinkTitle(fullscreen);
    }


    private void blinkTitle(boolean fullscreen) {

        Loader loader = new Loader();
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        long timer = System.currentTimeMillis();
        int index = 0;

        int selectionPointer = 0;
        GuiTexture[] selections = initTitleScreens(loader);
        GuiTexture blankTitleImage = initBlankTitleScreen(loader);

        boolean load = false;
        boolean closed = false;

        while (Keyboard.next()) {
            // polls
        }

        boolean pollOffReturn = false;

        while (!closed) {

            if (pollOffReturn) {
                while (Keyboard.next()){

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
            guiRenderer.render(guiList);
            DisplayManager.updateDisplay();

            // user begins game
            if (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                        DisplayManager.closeDisplay();
                        closed = true;
                    }

                    // check arrow keys

                    else if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
                        if (selectionPointer == 0) {
                            selectionPointer = 2;
                        } else {
                            selectionPointer -= 1;
                            selectionPointer %= 3;
                        }
                    } else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
                        selectionPointer += 1;
                        selectionPointer %= 3;
                    }

                    if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                        if (selectionPointer == 0) {
                            break;
                        } else if (selectionPointer == 1) {
                            load = true;
                            break;
                        } else if (selectionPointer == 2) {
                            HelpScreen helpScreen = new HelpScreen(isHost, hostname, fullscreen);
                            closed = helpScreen.wasClosed();
                            pollOffReturn = true;
                        }
                    }
                }
            }
        }

        if (!closed) {
            new GameController(isHost, hostname, load, fullscreen);
        } else {
            AL.destroy();
        }
    }

    private GuiTexture initBlankTitleScreen(Loader loader) {
        return new GuiTexture(loader.loadTexture(PATH + File.separator + "playLoadHelp"), new Vector2f(0, 0),
                new Vector2f(1, 1));
    }


    /**
     * @return an Array of title screen images to render
     */
    private GuiTexture[] initTitleScreens(Loader loader) {
        GuiTexture[] images = new GuiTexture[3];
        images[0] = new GuiTexture(loader.loadTexture(PATH + File.separator + "playLoadHelpPlayUnderscore"), new Vector2f(0, 0),
                new Vector2f(1, 1));

        //TODO make load underscore
        images[1] = new GuiTexture(loader.loadTexture(PATH + File.separator + "playLoadHelpLoadUnderscore"), new Vector2f(0, 0),
                new Vector2f(1, 1));

        //TODO make help underscore
        images[2] = new GuiTexture(loader.loadTexture(PATH + File.separator + "playLoadHelpHelpUnderscore"), new Vector2f(0, 0),
                new Vector2f(1, 1));
        return images;
    }

}


