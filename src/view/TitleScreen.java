package view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import controller.GameController;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import view.renderEngine.GuiRenderer;

/**
 * Produces the title screen for the game
 * 
 * @author Ellie
 *
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

		while (!Display.isCloseRequested()) {

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
		            new GameController();
		        }
			
		}
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
