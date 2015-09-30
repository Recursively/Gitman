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

	private String player = null;
	private String inventory = null;
	private String codeProgress = null;
	private String patchProgress = null;
	private String score = null;
	private String cards = null;
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

			player = getTextValue(player, doc, "player");
			if (player != null) {
				if (!player.isEmpty())
					states.add(player);
			}
			inventory = getTextValue(inventory, doc, "inventory");
			if (inventory != null) {
				if (!inventory.isEmpty())
					states.add(inventory);
			}
			codeProgress = getTextValue(codeProgress, doc, "codeProgress");
			if (codeProgress != null) {
				if (!codeProgress.isEmpty())
					states.add(codeProgress);
			}
			patchProgress = getTextValue(patchProgress, doc, "patchProgress");
			if (patchProgress != null) {
				if (!patchProgress.isEmpty())
					states.add(patchProgress);
			}
			score = getTextValue(score, doc, "score");
			if (score != null) {
				if (!score.isEmpty())
					states.add(score);
			}
			cards = getTextValue(cards, doc, "cards");
			if (cards != null) {
				if (!cards.isEmpty())
					states.add(cards);
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