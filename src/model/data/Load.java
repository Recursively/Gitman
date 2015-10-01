package model.data;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Load {

	// player camera elements
	private String pitch = null;
	private String roll = null;
	private String yaw = null;
	
	// player id element
	private String uid = null;
	
	// player position elements
	private String rotX = null;
	private String rotY = null;
	private String rotZ = null;
	
	private ArrayList<String> states;

	public boolean readXML(String xml) {
		states = new ArrayList<String>();
		Document dom;
		// Make an instance of the DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use the factory to take an instance of the document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using the builder to get the DOM mapping of the
			// XML file
			dom = db.parse(xml);

			Element doc = dom.getDocumentElement();

			pitch = getTextValue(pitch, doc, "player");
			if (pitch != null) {
				if (!pitch.isEmpty())
					states.add(pitch);
			}
			roll = getTextValue(roll, doc, "inventory");
			if (roll != null) {
				if (!roll.isEmpty())
					states.add(roll);
			}
			yaw = getTextValue(yaw, doc, "codeProgress");
			if (yaw != null) {
				if (!yaw.isEmpty())
					states.add(yaw);
			}
			uid = getTextValue(uid, doc, "patchProgress");
			if (uid != null) {
				if (!uid.isEmpty())
					states.add(uid);
			}
			rotX = getTextValue(rotX, doc, "score");
			if (rotX != null) {
				if (!rotX.isEmpty())
					states.add(rotX);
			}
			rotY = getTextValue(rotY, doc, "cards");
			if (rotY != null) {
				if (!rotY.isEmpty())
					states.add(rotY);
			}
			
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

	private String getTextValue(String def, Element doc, String tag) {
		String value = def;
		NodeList nl;
		nl = doc.getElementsByTagName(tag);
		if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
			value = nl.item(0).getFirstChild().getNodeValue();
		}
		return value;
	}

}