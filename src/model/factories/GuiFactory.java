package model.factories;

import model.entities.movableEntity.LaptopItem;
import model.guiComponents.Inventory;
import model.textures.GuiTexture;
import model.toolbox.Loader;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

/**
 * Factory Game for creating Gui Components
 *
 * @author Marcel van Workum
 */
public class GuiFactory {

	private static final String GUI_PATH = "gui/";
	private final Loader loader;

	/**
	 * Create the Gui factory passing in the object loader
	 *
	 * @param loader
	 *            Object loader
	 */
	public GuiFactory(Loader loader) {
		this.loader = loader;
	}

	/**
	 * Creates a {@link GuiTexture} with the specified texture, position and
	 * scale.
	 *
	 * @param textureName
	 *            Texture
	 * @param position
	 *            Position on the display x(0-1) y(0-1)
	 * @param scale
	 *            scale of the texture on display x(0-1) y(0-1)
	 *
	 * @return The GuiTexture created
	 */

	public GuiTexture makeGuiTexture(String textureName, Vector2f position, Vector2f scale) {
		return new GuiTexture(loader.loadTexture(GUI_PATH + textureName), position, scale);
	}

	public ArrayList<GuiTexture> makeInventory(Inventory inventory) {
		ArrayList<GuiTexture> inventoryImages = new ArrayList<GuiTexture>();
		inventoryImages.add(makeGuiTexture("blankInventoryScreen", new Vector2f(0f, 0f), new Vector2f(0.8f, 1f)));
		if (inventory.getInventory() != null) {
			ArrayList<LaptopItem> inventoryList = inventory.getInventory();
			for (int i = 0; i < inventoryList.size(); i++) {
				makeGuiTexture(inventoryList.get(i).getName(), new Vector2f(0f + 100 * i, 0f), new Vector2f(1f, 1f));
			}
		}

		return inventoryImages;

	}
	
	public ArrayList<GuiTexture> makeLostScreen(){
		ArrayList<GuiTexture> lostScreen = new ArrayList<GuiTexture>();
		lostScreen.add(makeGuiTexture("youLostScreen", new Vector2f(0f, 0f), new Vector2f(1f, 1f)));
		return lostScreen;
	}
}
