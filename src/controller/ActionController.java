package controller;

import org.lwjgl.input.Keyboard;

import model.GameWorld;
import model.toolbox.Loader;

/**
 * 
 * @author Ellie
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
		// ensures single reaction to a key press event when dealing with items
		while(Keyboard.next()){
        	// carry out methods when key is pressed (not released)
        	if(Keyboard.getEventKeyState()){
        		if(Keyboard.getEventKey() == Keyboard.KEY_I){
        			System.out.println("Inventory");
        			gameWorld.getInventory().displayInventory();
            	} 
        		if(Keyboard.getEventKey() == Keyboard.KEY_E){
        			System.out.println("Interact");
        			gameWorld.interactWithItem();
        		}
        		// TODO delete items in inventory 
        	}
		}
	}
}
