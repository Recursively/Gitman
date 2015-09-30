package controller;

import org.lwjgl.input.Keyboard;

import model.guiComponents.Inventory;

/**
 * 
 * @author Ellie
 *
 */
public class GuiController {
	public static boolean isKeyDown = false;

	// GuiFactory = new
	// TODO add key press inventory/quit/
	// Things that make gui things happen
	public static void displayInventory() {
		Keyboard.enableRepeatEvents(false);
		if (Keyboard.isKeyDown(Keyboard.KEY_I) && !GuiController.isKeyDown) {

			GuiController.isKeyDown = true;
			Inventory.isVisible = true;

		}

	}

	public static void hideInventory() {
		if (Keyboard.isKeyDown(Keyboard.KEY_O) && !GuiController.isKeyDown) {

			GuiController.isKeyDown = true;
			Inventory.isVisible = false;

		}

	}
}
