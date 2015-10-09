package model.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import model.guiComponents.Inventory;

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
	

	public static Data loadGame() {
		
		Document dom;
		// Make an instance of the DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use the factory to take an instance of the document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using the builder to get the DOM mapping of the
			// XML file
			dom = db.parse(System.getProperty("user.dir") + File.separator + "res" + File.separator + "data" + File.separator + "save.xml");

			Element doc = dom.getDocumentElement();
			
			// TODO iterate through saved elements
			
			Vector3f playerVec = new Vector3f(Integer.parseInt(posX), Integer.parseInt(posY), Integer.parseInt(posZ));
			
			return new Data(playerVec, pitch, roll, yaw, uid);

		} catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		} catch (SAXException se) {
			System.out.println(se.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		
		return null;
	}

	private static String getTextValue(String def, Element doc, String tag) {
		String value = def;
		NodeList nl;
		nl = doc.getElementsByTagName(tag);
		if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
			value = nl.item(0).getFirstChild().getNodeValue();
		}
		return value;
	}

}