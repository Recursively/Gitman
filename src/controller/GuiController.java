package controller;

import org.lwjgl.input.Keyboard;

import model.guiComponents.Inventory;

/**
 * 
 * @author Divya
 *
 */
public class GuiController {
	private static boolean isKeyDown = false;
	
	//GuiFactory = new 
	// TODO add key press inventory/quit/
	// Things that make gui things happen
	public static void displayInventory() {
		//Keyboard.enableRepeatEvents(false);
		if (Keyboard.getEventKey() == Keyboard.KEY_I && !GuiController.isKeyDown) {
			
				GuiController.isKeyDown = true;
				Inventory.isVisible = !Inventory.isVisible;
				System.out.println(GuiController.isKeyDown);
			
			
		}
		
	}

}
