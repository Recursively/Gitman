package model.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.entities.Camera;
import model.entities.Entity;
import model.entities.movableEntity.Bug;
import model.entities.movableEntity.Commit;
import model.entities.movableEntity.FlashDrive;
import model.entities.movableEntity.Laptop;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.entities.movableEntity.ReadMe;
import model.entities.movableEntity.SwipeCard;
import model.factories.EntityFactory;
import model.guiComponents.Inventory;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Load {

	// player element
	private static Player player;

	// inventory elements
	private static ArrayList<LaptopItem> inventory;

	// movable entity elements
	private static ArrayList<MovableEntity> movableEntities;

	// swipeCard elements
	private static ArrayList<SwipeCard> swipeCards;

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

			parsePlayer(doc);
			parseInventory(doc);
			parseEntities(doc);
			parseCards(doc);

			return new Data(player, inventory, movableEntities, swipeCards);

		} catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		} catch (SAXException se) {
			System.out.println(se.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		return null;
	}

	private static String getTextValue(Element e, String tagName) {
		String value = null;
		NodeList nl = e.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			value = el.getFirstChild().getNodeValue();
		}

		return value;
	}

	private static void parsePlayer(Element doc) {

		TexturedModel model = null; // TODO =
									// EntityFactory.getPlayerTexturedModel();

		// player position
		int x = Integer.parseInt(getTextValue(doc, "posX"));
		int y = Integer.parseInt(getTextValue(doc, "posY"));
		int z = Integer.parseInt(getTextValue(doc, "posZ"));
		Vector3f pos = new Vector3f(x, y, z);

		// player camera elements
		float pitch = Float.parseFloat(getTextValue(doc, "pitch"));
		float roll = Float.parseFloat(getTextValue(doc, "roll"));
		float yaw = Float.parseFloat(getTextValue(doc, "yaw"));

		// player id element
		int uid = Integer.parseInt(getTextValue(doc, "uid"));

		Camera camera = new Camera(0, pos);
		camera.changePitch(pitch);
		camera.changeYaw(yaw);
		// TODO change roll?
		player = new Player(model, pos, 0, 0, 0, 0, uid, camera);
	}

	private static void parseInventory(Element doc) {

		// inventory items
		NodeList inventoryNodes = doc.getElementsByTagName("inventoryItem");
		if (inventoryNodes != null && inventoryNodes.getLength() > 0) {
			for (int i = 0; i < inventoryNodes.getLength(); i++) {

				// get the inventoryItem element
				Element e = (Element) inventoryNodes.item(i);
				
				String type = getTextValue(e, "type");

				TexturedModel model = ModelFromString(type);

				// item position
				int x = Integer.parseInt(getTextValue(e, "itemPosX"));
				int y = Integer.parseInt(getTextValue(e, "itemPosY"));
				int z = Integer.parseInt(getTextValue(e, "itemPosZ"));
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
				
				// placeholder to make EntityFromType() work
				int cardNum = 0;

				MovableEntity movableEntity = EntityFromType(type, model, pos,
						rotX, rotY, rotZ, scale, id, name, cardNum);
				
				inventory.add((LaptopItem) movableEntity);
			}
		}
	}

	private static void parseEntities(Element doc) {

		// entity items
		NodeList entityNodes = doc.getElementsByTagName("movableEntity");
		if (entityNodes != null && entityNodes.getLength() > 0) {
			for (int i = 0; i < entityNodes.getLength(); i++) {

				// get the inventoryItem element
				Element e = (Element) entityNodes.item(i);

				String type = getTextValue(e, "type");

				// textured model
				TexturedModel model = ModelFromString(type);

				// item position
				int x = Integer.parseInt(getTextValue(e, "itemPosX"));
				int y = Integer.parseInt(getTextValue(e, "itemPosY"));
				int z = Integer.parseInt(getTextValue(e, "itemPosZ"));
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

				// swipeCard cardNum
				int cardNum = 0;

				if (type == "SwipeCard") {
					cardNum = Integer.parseInt(getTextValue(e, "cardNum"));
				}

				// construct the entity
				MovableEntity movableEntity = EntityFromType(type, model, pos,
						rotX, rotY, rotZ, scale, id, name, cardNum);

				// add entity to entity list
				movableEntities.add(movableEntity);

			}
		}
	}

	private static void parseCards(Element doc) {

		// swipe card items
		NodeList cardNodes = doc.getElementsByTagName("swipeCard");
		if (cardNodes != null && cardNodes.getLength() > 0) {
			for (int i = 0; i < cardNodes.getLength(); i++) {

				// get the inventoryItem element
				Element e = (Element) cardNodes.item(i);
				
				String type = "SwipeCard";
				
				TexturedModel model = null; // TODO = EntityFactory.getSwipecardTexturedModel();
				
				// item position
				int x = Integer.parseInt(getTextValue(e, "cardPosX"));
				int y = Integer.parseInt(getTextValue(e, "cardPosY"));
				int z = Integer.parseInt(getTextValue(e, "cardPosZ"));
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

				// swipeCard cardNum
				int cardNum = Integer.parseInt(getTextValue(e, "cardNum"));
				
				// construct the entity
				MovableEntity movableEntity = EntityFromType(type, model, pos,
						rotX, rotY, rotZ, scale, id, name, cardNum);
				
				swipeCards.add((SwipeCard) movableEntity);
			}
		}
	}

	private static TexturedModel ModelFromString(String type) {
		if (type == "Bug") {
			return EntityFactory.getBugTexturedModel();
		} else if (type == "Commit") {
			return EntityFactory.getCommitTexturedModel();
		} else if (type == "FlashDrive") {
			return EntityFactory.getFlashdriveTexturedModel();
		} else if (type == "Laptop") {
			return EntityFactory.getLaptopTexturedModel();
		} else if (type == "ReadMe") {
			return EntityFactory.getTabletTexturedModel();
		} else if (type == "SwipeCard") {
			// TODO return EntityFactory.getSwipecardTexturedModel();
		}

		return null;
	}

	private static MovableEntity EntityFromType(String type,
			TexturedModel model, Vector3f pos, float rotX, float rotY,
			float rotZ, float scale, int id, String name, int cardNum) {
		if (type == "Bug") {
			return new Bug(model, pos, rotX, rotY, rotZ, scale, id);
		} else if (type == "Commit") {
			return new Commit(model, pos, rotX, rotY, rotZ, scale, id);
		} else if (type == "FlashDrive") {
			return new FlashDrive(model, pos, rotX, rotY, rotZ, scale, id, name);
		} else if (type == "Laptop") {
			// return new Laptop(model, pos, rotX, rotY, rotZ, scale, id);
		} else if (type == "ReadMe") {
			return new ReadMe(model, pos, rotX, rotY, rotZ, scale, id, name);
		} else if (type == "SwipeCard") {
			return new SwipeCard(model, pos, rotX, rotY, rotZ, scale, id,
					cardNum);
		}

		return null;
	}
}