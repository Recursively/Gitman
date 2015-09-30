package model.data;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Save {
	
	private String player = null;
	private String inventory = null;
	private String codeProgress = null;
	private String patchProgress = null;
	private String score = null;
	private String cards = null;

	public void saveToXML(String xml) {
		Document dom;
		Element e = null;

		// instance of a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use factory to get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// create instance of DOM
			dom = db.newDocument();

			// create the root element
			Element rootEle = dom.createElement("gamestate");

			// create data elements and place them under root
			e = dom.createElement("player");
			e.appendChild(dom.createTextNode(player));
			rootEle.appendChild(e);

			e = dom.createElement("inventory");
			e.appendChild(dom.createTextNode(inventory));
			rootEle.appendChild(e);

			e = dom.createElement("codeProgress");
			e.appendChild(dom.createTextNode(codeProgress));
			rootEle.appendChild(e);

			e = dom.createElement("patchProgress");
			e.appendChild(dom.createTextNode(patchProgress));
			rootEle.appendChild(e);
			
			e = dom.createElement("score");
			e.appendChild(dom.createTextNode(score));
			rootEle.appendChild(e);
			
			e = dom.createElement("cards");
			e.appendChild(dom.createTextNode(cards));
			rootEle.appendChild(e);

			dom.appendChild(rootEle);

			try {
				Transformer tr = TransformerFactory.newInstance()
						.newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
				tr.setOutputProperty(
						"{http://xml.apache.org/xslt}indent-amount", "4");

				// send DOM to file
				tr.transform(new DOMSource(dom), new StreamResult(
						new FileOutputStream(xml)));

			} catch (TransformerException te) {
				System.out.println(te.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		} catch (ParserConfigurationException pce) {
			System.out
					.println("UsersXML: Error trying to instantiate DocumentBuilder "
							+ pce);
		}
	}
	
}
