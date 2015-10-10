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
		// react to the mouse click if it is not grabbed
		if(Mouse.isGrabbed()){
			// ensure single reaction to mouse event
			while(Mouse.next()){
				// carry out methods when mouse is pressed (not released)
				if(Mouse.getEventButtonState()){
					gameWorld.interactWithMovEntity();
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
        		
        		// deal with opening and closing viewing things in the inventory
        		if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
					gameWorld.getInventory().displayLaptopItem();
				}
        		
        		if(Keyboard.getEventKey() == Keyboard.KEY_UP ||
        			Keyboard.getEventKey() == Keyboard.KEY_DOWN ||
        			Keyboard.getEventKey() == Keyboard.KEY_RIGHT ||
        			Keyboard.getEventKey() == Keyboard.KEY_LEFT){
					gameWorld.getInventory().selectItem(Keyboard.getEventKey());
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
    			
    			// escape closes screen
    			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
    				DisplayManager.closeDisplay();
    			}
        	}
		}
	}
}
