package controller;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import model.GameWorld;
import model.data.Save;
import model.toolbox.Loader;

/**
 * Controller to handle mouse and key input by the player. The class identifies
 * what action has been carried out and calls the appropriate methods to make
 * updates to the game accordingly
 * 
 * @author Divya
 * @author Marcel van Workum
 * @author Ellie
 *
 */
public class ActionController {
	private GameWorld gameWorld;
	private GameController gameController;   // to check if game is still running

	public ActionController(Loader loader, GameWorld gameWorld,
			GameController gameController) {
		this.gameWorld = gameWorld;
		this.gameController = gameController;
	}


	/**
	 * Calls the relevant methods in the game world when certain
	 * key/mouse press actions are carried out.
	 */
	public void processActions(){		
		// react to the mouse click if it is not grabbed
		if(Mouse.isGrabbed()){
			// ensure single reaction to mouse event
			while (Mouse.next()) {
				// carry out methods when mouse is pressed (not released)

				if(Mouse.getEventButtonState()){
					gameWorld.interactWithMovEntity();
				}
			}
		}

		while(GameController.RUNNING && Keyboard.next()){
        	// carry out methods when key is pressed (not released)
        	if(Keyboard.getEventKeyState()){
        		
        		if(Keyboard.getEventKey() == Keyboard.KEY_I){
        			gameWorld.getInventory().displayInventory();
            	} 
        		
        		// deal with opening and closing viewing things in the inventory
        		if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
					gameWorld.getInventory().displayLaptopItem();
				}
        		
        		// selection of items in the inventory
        		if(Keyboard.getEventKey() == Keyboard.KEY_UP ||
        			Keyboard.getEventKey() == Keyboard.KEY_DOWN ||
        			Keyboard.getEventKey() == Keyboard.KEY_RIGHT ||
        			Keyboard.getEventKey() == Keyboard.KEY_LEFT){
					gameWorld.getInventory().selectItem(Keyboard.getEventKey());
				}
        		        		
        		if (Keyboard.getEventKey() == Keyboard.KEY_X){
    				int entity = gameWorld.getInventory().deleteItem(gameWorld);
    				
    				// if successful delete, send network update
    				if(entity != -1){
    					gameController.setNetworkUpdate(8, gameWorld.getMoveableEntities().get(entity));
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
    				GameWorld.setGuiMessage("gameSaved", 1500);
    			}
    			
    			if(gameWorld.getGameState() > -1){
					if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
						GameController.RUNNING = false;
						AudioController.stopGameWonLoop();
					}
				}    			
    			
    			// escape closes screen
    			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
					GameController.RUNNING = false;
    			}

				if (Keyboard.getEventKey() == Keyboard.KEY_J) {
					AudioController.playRandomEasterEggSound();
				}
        	}
		}
	}
}
