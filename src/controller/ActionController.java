package controller;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import model.GameWorld;
import model.data.Save;
import model.toolbox.Loader;
import view.DisplayManager;
import view.PlayLoadOptionsScreen;

/**
 * Controller to handle mouse and key input by the player. The
 * class identifies what action has been carried out and 
 * calls the appropriate methods to make updates to the game
 * accordingly
 * 
 * @author Divya
 *
 */
public class ActionController {
	private Loader loader;
	private GameWorld gameWorld;
	
	public ActionController(Loader loader, GameWorld gameWorld) {
		this.loader = loader;		
		this.gameWorld = gameWorld;
	}

	public void processActions(){
		// react to the mouse if it is not grabbed
		if(!Mouse.isGrabbed()){
			
			// ensure single reaction to mouse event
			while(Mouse.next()){
				// carry out methods when mouse is pressed (not released)
				if(Mouse.getEventButtonState()){
					int x = Mouse.getX();
					int y = Mouse.getY();

					// TODO Note: point (0,0) is at the bottom, left corner of the display
					System.out.println("Mouse pressed:" + x + ", " + y);

					if(Mouse.isButtonDown(0)){  // left click
						gameWorld.getInventory().displayLaptopItem(x, y);
					}

					if(Mouse.isButtonDown(1)){  // right click
						gameWorld.getInventory().showSelected(x, y);
					}
				}
			}
		}
		
		// ensures single reaction to a key event
		while(Keyboard.next()){
        	// carry out methods when key is pressed (not released)
        	if(Keyboard.getEventKeyState()){
        		
        		if(Keyboard.getEventKey() == Keyboard.KEY_I){
        			gameWorld.getInventory().displayInventory();
            	} 
        		
        		if (Keyboard.getEventKey() == Keyboard.KEY_X){
    				gameWorld.getInventory().deleteItem(gameWorld);
    			}
        		
        		if(Keyboard.getEventKey() == Keyboard.KEY_E){
        			System.out.println("Interact");
        			gameWorld.interactWithMovEntity();
        		}
        		
    			if (Keyboard.getEventKey() == Keyboard.KEY_F){
    				Save.saveGame(gameWorld);
    			}
    			if(gameWorld.isGameLost()){
					if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
						DisplayManager.closeDisplay();
						//TODO networking idk what to put here help
						//is currently testing mode
						new PlayLoadOptionsScreen(false, "");
					}
				}
        	}
		}
	}
}
