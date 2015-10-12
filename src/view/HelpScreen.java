package view;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;

import model.factories.GuiFactory;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import view.renderEngine.GuiRenderer;

public class HelpScreen {

	private boolean isHost;
	private String hostname;

	private boolean fullscreen;
	private boolean closed;
	
	public HelpScreen(boolean isHost, String hostname, boolean fullscreen) {
		this.fullscreen = fullscreen;
		this.hostname = hostname;
		this.isHost = isHost;
	
		
		Keyboard.enableRepeatEvents(false);
		showScreen(fullscreen);
	}



	private void showScreen(boolean fullscreen) {

			Loader loader = new Loader();
			GuiRenderer guiRenderer = new GuiRenderer(loader);
			GuiFactory guiFactory = new GuiFactory(loader);
			List<GuiTexture> helpScreen = guiFactory.getHelpScreen();
			

			closed = false;
			
			while (!closed ) {

				guiRenderer.render(helpScreen);
				DisplayManager.updateDisplay();

				if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
					break;
				}
				
				else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
					DisplayManager.closeDisplay();
					closed = true;
				}
			}
			if(!closed){
			new PlayLoadHelpScreen(isHost, hostname, fullscreen);
			}

		}



	public boolean wasClosed() {
		return closed;
	}


	}




