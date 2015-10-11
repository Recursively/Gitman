package model.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.entities.Entity;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.entities.movableEntity.SwipeCard;
import model.guiComponents.Inventory;
import model.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Load {

	// player position elements
	private static String posX;
	private static String posY;
	private static String posZ;

	// player camera elements
	private static String pitch;
	private static String roll;
	private static String yaw;

	// player id element
	private static String uid;

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

			posX = getTextValue(doc, "posX");
			posY = getTextValue(doc, "posY");
			posZ = getTextValue(doc, "posZ");

			// player camera elements
			pitch = getTextValue(doc, "pitch");
			roll = getTextValue(doc, "roll");
			yaw = getTextValue(doc, "yaw");

			// player id element
			uid = getTextValue(doc, "uid");

			// inventory items
			NodeList inventoryNodes = doc.getElementsByTagName("inventoryItem");
			if (inventoryNodes != null && inventoryNodes.getLength() > 0) {
				for (int i = 0; i < inventoryNodes.getLength(); i++) {

					// get the inventoryItem element
					Element e = (Element) inventoryNodes.item(i);

					// textured model
					TexturedModel model; // TODO get the model getting working
					
					// item position
					int x = Integer
							.parseInt(getTextValue(e, "itemPosX"));
					int y = Integer
							.parseInt(getTextValue(e, "itemPosY"));
					int z = Integer
							.parseInt(getTextValue(e, "itemPosZ"));
					Vector3f pos = new Vector3f(x, y, z);
					
					// item rotation
					Float rotX = Float.parseFloat(getTextValue(e, "rotX"));
					Float rotY = Float.parseFloat(getTextValue(e, "rotY"));
					Float rotZ = Float.parseFloat(getTextValue(e, "rotZ"));
					
					// item scale
					int scale = Integer.parseInt(getTextValue(e, "scale"));
					
					// item id
					int id = Integer.parseInt(getTextValue(e, "id"));
					
					// item name
					String name = getTextValue(e, "name");
					
					// TODO inventory.add(new LaptopItem(model, pos, rotX, rotY, rotZ, scale, id, name));
				}
				
			}

			// entity items
			NodeList entityNodes = doc.getElementsByTagName("movableEntity");
			if (entityNodes != null && entityNodes.getLength() > 0) {
				for (int i = 0; i < entityNodes.getLength(); i++) {

					// get the inventoryItem element
					Element e = (Element) entityNodes.item(i);
					
				}
					
			}
			
			// swipe card items
			NodeList cardNodes = doc.getElementsByTagName("swipeCard");
			if (cardNodes != null && cardNodes.getLength() > 0) {
				for (int i = 0; i < cardNodes.getLength(); i++) {

					// get the inventoryItem element
					Element e = (Element) cardNodes.item(i);
					
				}
					
			}

			return null; // TODO

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

}