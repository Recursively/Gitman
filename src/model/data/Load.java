package model.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.entities.movableEntity.Player;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Load {

	// player camera elements
	private static String pitch = null;
	private static String roll = null;
	private static String yaw = null;
	
	// player id element
	private static String uid = null;
	
	// player position elements
	private static String posX = null;
	private static String posY = null;
	private static String posZ = null;
	
	// inventory elements
	// TODO	add these
	

	public static boolean loadGame() {
		
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

			pitch = getTextValue(pitch, doc, "pitch");
			roll = getTextValue(roll, doc, "roll");
			yaw = getTextValue(yaw, doc, "yaw");
			uid = getTextValue(uid, doc, "uid");
			posX = getTextValue(posX, doc, "posX");
			posY = getTextValue(posY, doc, "posY");
			posZ = getTextValue(posZ, doc, "posZ");
			
			return true;

		} catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		} catch (SAXException se) {
			System.out.println(se.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		return false;
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