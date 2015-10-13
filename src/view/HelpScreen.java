package view;

import model.factories.GuiFactory;
import model.textures.GuiTexture;
import org.lwjgl.input.Keyboard;
import view.renderEngine.GuiRenderer;

import java.util.List;

/**
 * A class for displaying the Help screen. Key press logic is included as the
 * action controller is not made yet and it is simple.
 *
 * @author Ellie
 */
public class HelpScreen {

    //holds flag to show whether or not esc was pressed in this menu
    private boolean closed;

    /**
     * Constructor to make new help screen object
     */
    public HelpScreen() {
        Keyboard.enableRepeatEvents(false);
        showScreen();
    }

    /**
     * Handles logic for either closing the window or moving back to the Play/Load/Help screen
     *
     */
    private void showScreen() {


        GuiRenderer guiRenderer = new GuiRenderer();
        GuiFactory guiFactory = new GuiFactory();
        List<GuiTexture> helpScreen = guiFactory.getHelpScreen();

        closed = false;

        while (!closed) {

            guiRenderer.render(helpScreen);
            DisplayManager.updateDisplay();

            if (Keyboard.isKeyDown(Keyboard.KEY_H) || Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                break;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                DisplayManager.closeDisplay();
                closed = true;
            }
        }
    }

    /**
     * Shows whether esc was pressed while viewing this window
     *
     * @return boolean closed
     */
    public boolean wasClosed() {
        return closed;
    }

}
