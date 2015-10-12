package model.factories;

import model.GameWorld;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.SwipeCard;
import model.guiComponents.Inventory;
import model.textures.GuiTexture;
import model.toolbox.Loader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.sun.tracing.dtrace.ProviderAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating Gui Components
 *
 * @author Marcel van Workum
 * @author Ellie Coyle
 * @author Divya
 */
public class GuiFactory {

	private static final String GUI_PATH = "gui/";
	private static final String ITEM_PATH = "itemImages/";
	
	// basic positions
	private static final Vector2f CENTER_POS = new Vector2f(0f, 0f);
	private static final Vector2f FULL_SCALE = new Vector2f(1f, 1f);
	
	// progress block positions and scale
	private static final float PROGRESS_START_X = -0.955f;
	private static final Vector2f PROGRESS_SCALE = new Vector2f(0.35f, 0.03f);
	private static final float PROGRESS_YPOS = 0.94f;
	
	// score scale and positions
	private static final float SCORE_START_X = -0.56f;
	private static final Vector2f SCORE_SCALE = new Vector2f(0.05f, 0.05f);
	private static final float SCORE_YPOS = 0.95f;
	
	// textures
	private final Loader loader;
	private GuiTexture inventoryScreen;
	private GuiTexture interactMessage;
	private GuiTexture infoPanel;
	private GuiTexture lostScreen;
	private GuiTexture winScreen;
	private GuiTexture progressBlock;
	
	// gui panel
	private int oldCardsSize;
	private List<GuiTexture> cards;
	
	private int oldProgress;
	private List<GuiTexture> progressBar;
	
	private int oldScore;
	private List<GuiTexture> scoreNum;
	
	//number
	
	

	/**
	 * Create the Gui factory passing in the object loader
	 *
	 * @param loader
	 *            Object loader
	 */
	public GuiFactory(Loader loader) {
		this.loader = loader;

		loadImages();
	}

	private void loadImages() {
		inventoryScreen = makeGuiTexture("blankInventoryScreen", CENTER_POS, new Vector2f(0.8f, 1f));
		interactMessage = makeGuiTexture("pressEToInteract", new Vector2f(0f, -0.3f), new Vector2f(0.5f, 0.5f));
		infoPanel = makeGuiTexture("topLeftCornerGUI", new Vector2f(-0.6875f, 0.8f), new Vector2f(0.4f, 0.4f));
		lostScreen = makeGuiTexture("youLostScreen", CENTER_POS, FULL_SCALE);
		winScreen = makeGuiTexture("youWonScreen", CENTER_POS, FULL_SCALE); 
		progressBlock = makeGuiTexture("progressBlock", CENTER_POS, PROGRESS_SCALE); 
		
		
		
		// info panel
		this.cards = new ArrayList<GuiTexture>();
		this.oldCardsSize = 0;
		this.progressBar = new ArrayList<GuiTexture>();
		this.oldProgress = 0;
		this.scoreNum = new ArrayList<GuiTexture>();
		this.oldScore = 0;

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
	
	public GuiTexture makeItemTexture(String textureName, Vector2f position, Vector2f scale) {
		return new GuiTexture(loader.loadTexture(ITEM_PATH + textureName), position, scale);
	}

	public ArrayList<GuiTexture> makeInventory(Inventory inventory) {
		ArrayList<GuiTexture> inventoryImages = new ArrayList<GuiTexture>();
		inventoryImages.add(inventoryScreen);
		if (inventory.getLaptopDisplay() != null) {
			LaptopItem[][] items = inventory.getLaptopDisplay();
			for(int x = 0; x < items.length; x++){
				for(int y = 0; y < items[0].length; y++){
					if(items[x][y] != null){
						float xPos = Inventory.START_X + y*Inventory.ICON_SCALE.getX()*3f;
						float yPos = Inventory.START_Y - x*Inventory.ICON_SCALE.getY()*1.5f;
						Vector2f pos = new Vector2f(xPos, yPos);
						GuiTexture img = makeItemTexture(items[x][y].getName(), pos, Inventory.ICON_SCALE);
						inventoryImages.add(img);
						
						// highlight selected image
						if(items[x][y] == inventory.getSelected()){
							GuiTexture select = makeItemTexture("selectBox", pos, Inventory.SELECT_SCALE);
							inventoryImages.add(select);
						}
					}
				}
			}
		}
		

		return inventoryImages;

	}
	
	public List<GuiTexture> getLostScreen(){
		List<GuiTexture> lostScreens = new ArrayList<GuiTexture>();
		lostScreens.add(lostScreen);
		return lostScreens;
		
	}
	
	public List<GuiTexture> getWinScreen() {
		List<GuiTexture> winScreens = new ArrayList<GuiTexture>();
		winScreens.add(winScreen);
		return winScreens;
	}

	public List<GuiTexture> getProgress(int progress) {  
		if(oldProgress != progress){
			// if progress has decreased, remove how many blocks it has decreased by
			if(progress < this.oldProgress){
				for(int i = this.progressBar.size() - 1; i > progress; i--){
					this.progressBar.remove(i);
				}
			}
			// else add how many blocks it has increased by
			else {
				for(int i = this.oldProgress; i < progress; i++){
					if(i <= GameWorld.MAX_PROGRESS){
						float xPos = PROGRESS_START_X + i*PROGRESS_SCALE.getX()*0.01f;
						Vector2f pos = new Vector2f(xPos, PROGRESS_YPOS);
						GuiTexture progBlock = progressBlock.copy();
						progBlock.setPosition(pos);
						this.progressBar.add(progBlock);
					}
				}
			}
			this.oldProgress = progress;
		}
		return progressBar;
	}
	
	public List<GuiTexture> getScore(int score){  
		if(this.oldScore != score){
			this.oldScore = score;
			this.scoreNum = new ArrayList<GuiTexture>();
			String num = this.oldScore + "";
			for(int i = 0; i < num.length(); i++){
				String charNum = num.substring(i, i+1);	
				float xPos = SCORE_START_X + i*SCORE_SCALE.getX()*0.8f;
				Vector2f pos = new Vector2f(xPos, SCORE_YPOS);
				GuiTexture img = makeGuiTexture(charNum, pos, SCORE_SCALE);
				this.scoreNum.add(img);
			}
		}
		return this.scoreNum;
	}
	
	public List<GuiTexture> getSwipeCards(ArrayList<SwipeCard> collected) {  
		if(this.oldCardsSize != collected.size()){
			this.oldCardsSize = collected.size();
			String name = collected.get(this.oldCardsSize-1).getImgName();	
			float xPos = SwipeCard.START_X + (this.oldCardsSize-1)*SwipeCard.CARD_SCALE.getX()*2f;
			Vector2f pos = new Vector2f(xPos, SwipeCard.CARD_YPOS);
			GuiTexture img = makeItemTexture(name, pos, SwipeCard.CARD_SCALE);
			this.cards.add(img);
		}
		return this.cards;
	}
	
	public List<GuiTexture> getPopUpInteract(Vector3f position) {
		List<GuiTexture> message = new ArrayList<GuiTexture>();
		message.add(interactMessage);
		return message;

	}

	public List<GuiTexture> getInfoPanel() {
		List<GuiTexture> infoPanels = new ArrayList<GuiTexture>();
		infoPanels.add(infoPanel);
		return infoPanels;
	}

	public List<GuiTexture> getHelpScreen() {
		List<GuiTexture> help = new ArrayList<GuiTexture>();
		help.add(makeGuiTexture("helpScreen", new Vector2f(0f,0f), new Vector2f(0.8f, 1f)));
		return help;
	}

}
