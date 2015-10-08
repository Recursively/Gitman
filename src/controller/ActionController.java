package controller;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import main.ServerMain;
import model.GameWorld;
import model.data.Save;
import model.entities.movableEntity.MovableEntity;
import model.toolbox.Loader;
import view.DisplayManager;

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
	private GameWorld gameWorld;
	private GameController gameController;
	
	public ActionController(Loader loader, GameWorld gameWorld, GameController gameController) {
		this.gameWorld = gameWorld;
		this.gameController = gameController;
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
        			
    				MovableEntity entity = gameWorld.getInventory().deleteItem(gameWorld);
    				
    				if(entity != null){
    					gameController.setNetworkUpdate(8, entity);
    				}
    				
    			}
        		
        		if(Keyboard.getEventKey() == Keyboard.KEY_H){
        			gameWorld.displayHelp();
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
						new ServerMain();
					}
				}
        	}
		}
	}
}
