package model.data;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.GameWorld;
import model.entities.movableEntity.LaptopItem;

public class Save {
	
	private final static String FILE_NAME = "save.xml";

	public static boolean saveGame(GameWorld gameWorld) {

	  try {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// gamestate elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("gamestate");
		doc.appendChild(rootElement);

		// player elements
		Element player = doc.createElement("player");
		rootElement.appendChild(player);

		// set attribute to player element
		Attr attr = doc.createAttribute("id");
		attr.setValue("1");
		player.setAttributeNode(attr);
		
		// position elements
		Element position = doc.createElement("position");
		player.appendChild(position);
		
		// rotation x element
		Element rotX = doc.createElement("rotX");
		rotX.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getRotX())));
		position.appendChild(rotX);
		
		// rotation x element
		Element rotY = doc.createElement("rotY");
		rotY.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getRotY())));
		position.appendChild(rotY);
		
		// rotation x element
		Element rotZ = doc.createElement("rotZ");
		rotZ.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getRotZ())));
		position.appendChild(rotZ);

		// camera elements
		Element camera = doc.createElement("camera");
		player.appendChild(camera);

		// pitch element
		Element pitch = doc.createElement("pitch");
		pitch.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getCamera().getPitch())));
		camera.appendChild(pitch);

		// roll element
		Element roll = doc.createElement("roll");
		roll.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getCamera().getRoll())));
		camera.appendChild(roll);

		// yaw element
		Element yaw = doc.createElement("yaw");
		yaw.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getCamera().getYaw())));
		camera.appendChild(yaw);
		
		// uid element
		Element uid = doc.createElement("uid");
		uid.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getUid())));
		player.appendChild(uid);
		
		// inventory elements
		Element inventory = doc.createElement("inventory");
		rootElement.appendChild(inventory);
		
		/* TODO Add getters for inventory
		
		ArrayList<LaptopItem> inventoryArray = gameWorld.getInventory();
		
		for (LaptopItem item : inventoryArray){
			Element laptopItem = doc.createElement("laptopItem");
			inventory.appendChild(laptopItem);
			
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(item.getName()));
			laptopItem.appendChild(name);
			
			Element size = doc.createElement("size");
			size.appendChild(doc.createTextNode(String.valueOf(item.getSize())));
			laptopItem.appendChild(size);
		}
		*/

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(System.getProperty("user.dir") + File.separator + "res" + File.separator + "data" + File.separator + FILE_NAME));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		System.out.println("File saved!");

	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	  
	  return false; // TODO fix this
	}
}