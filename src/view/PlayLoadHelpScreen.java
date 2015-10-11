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

public class PlayLoadHelpScreen {
	private String hostname;
	private boolean isHost;

	

	public PlayLoadHelpScreen(boolean isHost, String hostname) {
		this.hostname = hostname;
		this.isHost = isHost;

		DisplayManager.createDisplay();
		Keyboard.enableRepeatEvents(false);
		blinkTitle();
	}



	private void blinkTitle() {

			Loader loader = new Loader();
			GuiRenderer guiRenderer = new GuiRenderer(loader);

			long timer = System.currentTimeMillis();
			int index = 0;

			GuiTexture[] images = initTitleScreens(loader);
			
			boolean load = false;

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
				
				if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
					DisplayManager.closeDisplay();
					break;
				}
				else if(Keyboard.isKeyDown(Keyboard.KEY_L)){
					DisplayManager.closeDisplay();
					load = true;
					break;
					
				}else if(Keyboard.isKeyDown(Keyboard.KEY_H)){
					DisplayManager.closeDisplay();
					//TODO
					break;
					
				}
			}

			new GameController(isHost, hostname, load);
			// change to make new window
			// TODO
		}


	/**
	 * @return an Array of title screen images to render
	 */
	private GuiTexture[] initTitleScreens(Loader loader) {
		GuiTexture[] images = new GuiTexture[2];
		String PATH = "titleScreen";
		images[0] = new GuiTexture(loader.loadTexture(PATH + File.separator + "playLoadHelp"), new Vector2f(0, 0),
				new Vector2f(1, 1));

		images[1] = new GuiTexture(loader.loadTexture(PATH + File.separator + "playLoadHelpUnderscore"), new Vector2f(0, 0),
				new Vector2f(1, 1));
		return images;
	}

}


