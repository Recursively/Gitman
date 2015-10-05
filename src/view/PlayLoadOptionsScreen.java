package view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import controller.GameController;
import model.data.Load;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import view.renderEngine.GuiRenderer;

public class PlayLoadOptionsScreen {
	private String hostname;
	private boolean isHost;

	

	public PlayLoadOptionsScreen(boolean isHost, String hostname) {
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
					Load.loadGame();
					break;
					
				}else if(Keyboard.isKeyDown(Keyboard.KEY_O)){
					//TODO bring up options screen
					break;
					
				}
			}

			// create the game now
			//new PlayLoadOptionsScreen()
			new GameController(isHost, hostname);
			// change to make new window
			// TODO
		}






	/**
	 * @return an Array of title screen images to render
	 */
	private GuiTexture[] initTitleScreens(Loader loader) {
		GuiTexture[] images = new GuiTexture[2];
		String PATH = "titleScreen";
		images[0] = new GuiTexture(loader.loadTexture(PATH + File.separator + "playLoadOptions"), new Vector2f(0, 0),
				new Vector2f(1, 1));

		images[1] = new GuiTexture(loader.loadTexture(PATH + File.separator + "playLoadOptionsUnderscore"), new Vector2f(0, 0),
				new Vector2f(1, 1));
		return images;
	}

}


