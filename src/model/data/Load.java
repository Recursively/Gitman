package model.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.entities.movableEntity.Bug;
import model.entities.movableEntity.Commit;
import model.entities.movableEntity.FlashDrive;
import model.entities.movableEntity.Laptop;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.ReadMe;
import model.entities.movableEntity.SwipeCard;
import model.factories.EntityFactory;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class used to load all data from the xml save file. The data is read, then
 * used to construct appropriate objects, which are then passed into a data
 * object.
 *
 * @author Finn Kinnear
 */

public class Load {

	// gamestate elements
	private static boolean isProgramCompiled;
	private static boolean isOutside;
	private static int codeProgress;
	private static int patchProgress;
	private static int score;
	private static boolean canApplyPatch;
	private static int commitIndex;
	private static long timer;

	// player element
	private static Vector3f playerPos;
	private static float pitch;
	private static float roll;
	private static float yaw;
	private static int uid;

	// inventory elements
	private static ArrayList<LaptopItem> inventory;
	private static int storageUsed;

	// movable entity elements
	private static ArrayList<MovableEntity> movableEntities;

	// swipeCard elements
	private static ArrayList<SwipeCard> swipeCards;

	/**
	 * Loads gamestate fields, and delegates loading player, inventory, movable
	 * entities, and swipe cards to helper methods.
	 * 
	 * @return data object storing all gamestate information
	 */

	public static Data loadGame() {

		Document dom;
		// Make an instance of the DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use the factory to take an instance of the document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using the builder to get the DOM mapping of the
			// XML file
			dom = db.parse(System.getProperty("user.dir") + File.separator
					+ "res" + File.separator + "data" + File.separator
					+ "save.xml");

			Element doc = dom.getDocumentElement();

			// various gamestate fields
			isProgramCompiled = Boolean.parseBoolean(getTextValue(doc, "isProgramCompiled"));
			isOutside = Boolean.parseBoolean(getTextValue(doc, "isOutside"));
			codeProgress = Integer.parseInt(getTextValue(doc, "codeProgress"));
			patchProgress = Integer
					.parseInt(getTextValue(doc, "patchProgress"));
			score = Integer.parseInt(getTextValue(doc, "score"));
			canApplyPatch = Boolean.parseBoolean(getTextValue(doc,
					"canApplyPatch"));
			
			commitIndex = Integer.parseInt(getTextValue(doc, "commitIndex"));
			
			timer = Long.parseLong(getTextValue(doc, "timer"));
			storageUsed = Integer.parseInt(getTextValue(doc, "storageUsed"));
			
			// create collections
			inventory = new ArrayList<LaptopItem>();
			movableEntities = new ArrayList<MovableEntity>();
			swipeCards = new ArrayList<SwipeCard>();

			// delegation of large groupings of loading to smaller, helper methods
			parsePlayer(doc);
			parseInventory(doc);
			parseEntities(doc);
			parseCards(doc);

			// returns a new Data object with all necessary information
			return new Data(playerPos, pitch, roll, yaw, uid, inventory, movableEntities, swipeCards,
					isProgramCompiled, isOutside, codeProgress, patchProgress, score,
					canApplyPatch, commitIndex, timer, storageUsed);

		} catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		} catch (SAXException se) {
			System.out.println(se.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		
		// loading was unsuccessful
		System.out.println("Loading failed");
		return null;
	}

	/**
	 * Helper methods that gets the text value associated with any given tag.
	 * 
	 * @param e
	 *            the element being searched
	 * @param tagName
	 *            the identifying text tag
	 * 
	 * @return the String associated with the given tag
	 */

	private static String getTextValue(Element e, String tagName) {
		String value = null;
		NodeList nl = e.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			Node firstChild = el.getFirstChild();
			if (firstChild != null) {
				value = firstChild.getNodeValue();
			}
		}

		return value;
	}

	/**
	 * Helper methods that parses all player data and sets the class variable
	 * player.
	 * 
	 * @param e
	 *            the element being searched
	 */

	private static void parsePlayer(Element doc) {

		// player position
		float x = Float.parseFloat(getTextValue(doc, "playerPosX"));
		float y = Float.parseFloat(getTextValue(doc, "playerPosY"));
		float z = Float.parseFloat(getTextValue(doc, "playerPosZ"));
		playerPos = new Vector3f(x, y, z);

		// player camera elements
		pitch = Float.parseFloat(getTextValue(doc, "pitch"));
		roll = Float.parseFloat(getTextValue(doc, "roll"));
		yaw = Float.parseFloat(getTextValue(doc, "yaw"));

		// player id element
		uid = Integer.parseInt(getTextValue(doc, "uid"));

	}

	/**
	 * Helper methods that parses all inventory data and sets the class variable
	 * inventory.
	 * 
	 * @param e
	 *            the element being searched
	 */

	private static void parseInventory(Element doc) {

		// inventory items
		NodeList inventoryNodes = doc.getElementsByTagName("inventoryItem");
		if (inventoryNodes != null && inventoryNodes.getLength() > 0) {
			for (int i = 0; i < inventoryNodes.getLength(); i++) {

				// get the inventoryItem element
				Element e = (Element) inventoryNodes.item(i);

				String type = getTextValue(e, "type");

				TexturedModel model = ModelFromString(type, 0);

				// item position
				float x = Float.parseFloat(getTextValue(e, "posX"));
				float y = Float.parseFloat(getTextValue(e, "posY"));
				float z = Float.parseFloat(getTextValue(e, "posZ"));
				Vector3f pos = new Vector3f(x, y, z);

				// item rotation
				float rotX = Float.parseFloat(getTextValue(e, "rotX"));
				float rotY = Float.parseFloat(getTextValue(e, "rotY"));
				float rotZ = Float.parseFloat(getTextValue(e, "rotZ"));

				// item scale
				float scale = Float.parseFloat(getTextValue(e, "scale"));

				// item id
				int id = Integer.parseInt(getTextValue(e, "id"));

				// item name
				String name = getTextValue(e, "name");

				MovableEntity movableEntity = EntityFromType(type, model, pos,
						rotX, rotY, rotZ, scale, id, name, 0, 0, false);

				inventory.add((LaptopItem) movableEntity);
			}
		}
	}

	/**
	 * Helper methods that parses all entity data and sets the class variable
	 * movableEntities.
	 * 
	 * @param e
	 *            the element being searched
	 */

	private static void parseEntities(Element doc) {

		// entity items
		NodeList entityNodes = doc.getElementsByTagName("movableEntity");
		if (entityNodes != null && entityNodes.getLength() > 0) {
			for (int i = 0; i < entityNodes.getLength(); i++) {

				// get the inventoryItem element
				Element e = (Element) entityNodes.item(i);

				String type = getTextValue(e, "type");

				// item position
				float x = Float.parseFloat(getTextValue(e, "posX"));
				float y = Float.parseFloat(getTextValue(e, "posY"));
				float z = Float.parseFloat(getTextValue(e, "posZ"));
				Vector3f pos = new Vector3f(x, y, z);

				// item rotation
				float rotX = Float.parseFloat(getTextValue(e, "rotX"));
				float rotY = Float.parseFloat(getTextValue(e, "rotY"));
				float rotZ = Float.parseFloat(getTextValue(e, "rotZ"));

				// item scale
				float scale = Float.parseFloat(getTextValue(e, "scale"));

				// item id
				int id = Integer.parseInt(getTextValue(e, "id"));

				// item name
				String name = getTextValue(e, "name");

				// swipeCard cardNum
				int cardNum = 0;

				// if swipeCard - set its cardNum
				if (type.equals("SwipeCard")) {
					cardNum = Integer.parseInt(getTextValue(e, "cardNum"));
				}
				
				// laptop cardID
				int cardID = 0;
				boolean hasCode = true;

				// if laptop - set its cardID
				if (type.equals("Laptop")) {
					cardID = Integer.parseInt(getTextValue(e, "cardID"));					
					hasCode = Boolean.parseBoolean(getTextValue(e, "hasCode"));
				}
				
				// textured model
				TexturedModel model = ModelFromString(type, cardNum);

				// construct the entity
				MovableEntity movableEntity = EntityFromType(type, model, pos,
						rotX, rotY, rotZ, scale, id, name, cardNum, cardID, hasCode);
				// add entity to entity list
				movableEntities.add(movableEntity);

			}
		}
	}

	/**
	 * Helper methods that parses all swipeCard data and sets the class variable
	 * swipeCards.
	 * 
	 * @param e
	 *            the element being searched
	 */

	private static void parseCards(Element doc) {

		// swipe card items
		NodeList cardNodes = doc.getElementsByTagName("swipeCard");
		if (cardNodes != null && cardNodes.getLength() > 0) {
			for (int i = 0; i < cardNodes.getLength(); i++) {

				// get the inventoryItem element
				Element e = (Element) cardNodes.item(i);

				String type = "SwipeCard";

				// swipeCard cardNum
				int cardNum = Integer.parseInt(getTextValue(e, "cardNum"));

				// item position
				float x = Float.parseFloat(getTextValue(e, "posX"));
				float y = Float.parseFloat(getTextValue(e, "posY"));
				float z = Float.parseFloat(getTextValue(e, "posZ"));
				Vector3f pos = new Vector3f(x, y, z);

				// item rotation
				Float rotX = Float.parseFloat(getTextValue(e, "rotX"));
				Float rotY = Float.parseFloat(getTextValue(e, "rotY"));
				Float rotZ = Float.parseFloat(getTextValue(e, "rotZ"));

				// item scale
				float scale = Float.parseFloat(getTextValue(e, "scale"));

				// item id
				int id = Integer.parseInt(getTextValue(e, "id"));

				// item name
				String name = getTextValue(e, "name");

				// construct the entity
				MovableEntity movableEntity = EntityFromType(type, null, pos,
						rotX, rotY, rotZ, scale, id, name, cardNum, 0, false);

				swipeCards.add((SwipeCard) movableEntity);
			}
		}
	}

	/**
	 * Helper methods that gets the appropriate model for the given entity type.
	 * 
	 * @param type
	 *            the type of entity
	 * @param cardNum
	 *            identifier for swipeCard models
	 *            
	 * @return the model for given entity type
	 */

	private static TexturedModel ModelFromString(String type, int cardNum) {
		type = type.trim();
		if (type.equals("Bug")) {
			return EntityFactory.getBugTexturedModel();
		} else if (type.equals("Commit")) {
			return EntityFactory.getCommitTexturedModel();
		} else if (type.equals("FlashDrive")) {
			return EntityFactory.getFlashdriveTexturedModel();
		} else if (type.equals("Laptop")) {
			return EntityFactory.getLaptopTexturedModel();
		} else if (type.equals("ReadMe")) {
			return EntityFactory.getTabletTexturedModel();
		} else if (type.equals("SwipeCard")) {
			return EntityFactory.getSwipecardTexturedModel()[cardNum];
		}
		System.out.println("Model not found for given type");
		return null;
	}

	/**
	 * Helper methods that get creates the appropriate entity for the given
	 * entity type.
	 * 
	 * @return a MovableEntity constructed from the method parameters
	 */

	private static MovableEntity EntityFromType(String type,
			TexturedModel model, Vector3f pos, float rotX, float rotY,
			float rotZ, float scale, int id, String name, int cardNum,
			int cardID, boolean hasCode) {
		type = type.trim();
		if (type.equals("Bug")) {
			return new Bug(model, pos, rotX, rotY, rotZ, scale, id);
		} else if (type.equals("Commit")) {
			return new Commit(model, pos, rotX, rotY, rotZ, scale, id);
		} else if (type.equals("FlashDrive")) {
			return new FlashDrive(model, pos, rotX, rotY, rotZ, scale, id, name);
		} else if (type.equals("Laptop")) {
			return new Laptop(model, pos, rotX, rotY, rotZ, scale, id, cardID, hasCode);
		} else if (type.equals("ReadMe")) {
			return new ReadMe(model, pos, rotX, rotY, rotZ, scale, id, name);
		} else if (type.equals("SwipeCard")) {
			return new SwipeCard(model, pos, rotX, rotY, rotZ, scale, id,
					cardNum);
		}
		System.out.println("Entity construction unsuccessful");
		return null;
	}
}