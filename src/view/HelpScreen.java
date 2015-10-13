package view;

import model.factories.GuiFactory;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.input.Keyboard;
import view.renderEngine.GuiRenderer;

import java.util.List;

public class HelpScreen {

    private boolean closed;

    public HelpScreen(boolean fullscreen) {
        Keyboard.enableRepeatEvents(false);
        showScreen(fullscreen);
    }


    private void showScreen(boolean fullscreen) {

        Loader loader = new Loader();
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        GuiFactory guiFactory = new GuiFactory(loader);
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


    public boolean wasClosed() {
        return closed;
    }


}




