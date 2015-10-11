package model.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

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
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.SwipeCard;
import model.guiComponents.Inventory;

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
		Element playerPosition = doc.createElement("playerPosition");
		player.appendChild(playerPosition);
		
		// position x element
		Element posX = doc.createElement("posX");
		posX.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getPosition().getX())));
		playerPosition.appendChild(posX);
		
		// position x element
		Element posY = doc.createElement("posY");
		posY.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getPosition().getY())));
		playerPosition.appendChild(posY);
		
		// position x element
		Element posZ = doc.createElement("posZ");
		posZ.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getPosition().getZ())));
		playerPosition.appendChild(posZ);

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
		uid.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getUID())));
		player.appendChild(uid);
		
		// inventory elements
		Element inventory = doc.createElement("inventory");
		rootElement.appendChild(inventory);
		
		// laptop item elements
		for (LaptopItem item : gameWorld.getInventory().getInventory()){
			
			Element inventoryItem = doc.createElement("inventoryItem");
			inventory.appendChild(inventoryItem);
			
			Element itemPosX = doc.createElement("itemPosX");
			itemPosX.appendChild(doc.createTextNode(String.valueOf(item.getPosition().x)));
			inventoryItem.appendChild(itemPosX);
			
			Element itemPosY = doc.createElement("itemPosY");
			itemPosY.appendChild(doc.createTextNode(String.valueOf(item.getPosition().y)));
			inventoryItem.appendChild(itemPosY);
			
			Element itemPosZ = doc.createElement("itemPosZ");
			itemPosZ.appendChild(doc.createTextNode(String.valueOf(item.getPosition().z)));
			inventoryItem.appendChild(itemPosZ);
			
			Element rotX = doc.createElement("rotX");
			rotX.appendChild(doc.createTextNode(String.valueOf(item.getRotX())));
			inventoryItem.appendChild(rotX);
			
			Element rotY = doc.createElement("rotY");
			rotY.appendChild(doc.createTextNode(String.valueOf(item.getRotY())));
			inventoryItem.appendChild(rotY);
			
			Element rotZ = doc.createElement("rotZ");
			rotZ.appendChild(doc.createTextNode(String.valueOf(item.getRotZ())));
			inventoryItem.appendChild(rotZ);
			
			Element scale = doc.createElement("scale");
			scale.appendChild(doc.createTextNode(String.valueOf(item.getScale())));
			inventoryItem.appendChild(scale);
			
			Element id = doc.createElement("id");
			rotZ.appendChild(doc.createTextNode(String.valueOf(item.getUID())));
			inventoryItem.appendChild(id);
			
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(item.getName()));
			inventoryItem.appendChild(name);
			
			
		}
		
		// movable entity elements
		Element movableEntities = doc.createElement("movableEntities");
		rootElement.appendChild(movableEntities);
		
		// movable entity elements
		for (MovableEntity e : gameWorld.getMoveableEntities().values()){
			
			Element movableEntity = doc.createElement("movableEntity");
			movableEntities.appendChild(movableEntity);
			
			Element entityPosX = doc.createElement("entityPosX");
			entityPosX.appendChild(doc.createTextNode(String.valueOf(e.getPosition().x)));
			movableEntity.appendChild(entityPosX);
			
			Element entityPosY = doc.createElement("entityPosY");
			entityPosY.appendChild(doc.createTextNode(String.valueOf(e.getPosition().y)));
			movableEntity.appendChild(entityPosY);
			
			Element entityPosZ = doc.createElement("entityPosZ");
			entityPosZ.appendChild(doc.createTextNode(String.valueOf(e.getPosition().z)));
			movableEntity.appendChild(entityPosZ);
			
			Element rotX = doc.createElement("rotX");
			rotX.appendChild(doc.createTextNode(String.valueOf(e.getRotX())));
			movableEntity.appendChild(rotX);
			
			Element rotY = doc.createElement("rotY");
			rotY.appendChild(doc.createTextNode(String.valueOf(e.getRotY())));
			movableEntity.appendChild(rotY);
			
			Element rotZ = doc.createElement("rotZ");
			rotZ.appendChild(doc.createTextNode(String.valueOf(e.getRotZ())));
			movableEntity.appendChild(rotZ);
			
			Element scale = doc.createElement("scale");
			scale.appendChild(doc.createTextNode(String.valueOf(e.getScale())));
			movableEntity.appendChild(scale);
			
			Element id = doc.createElement("id");
			rotZ.appendChild(doc.createTextNode(String.valueOf(e.getUID())));
			movableEntity.appendChild(id);
			
			/*
			Element type = doc.createElement("type");
			movableEntity.appendChild(doc.createTextNode(String.valueOf(e.getType())));
			movableEntity.appendChild(type);
			*/
			
		}
		
		// swipe card elements
		Element swipeCards = doc.createElement("swipeCards");
		rootElement.appendChild(swipeCards);
		
		// swipe card elements
		for (SwipeCard card : gameWorld.getSwipeCards()){
			
			Element swipeCard = doc.createElement("swipeCard");
			swipeCard.appendChild(swipeCards);
			
			Element cardPos = doc.createElement("cardPos");
			swipeCard.appendChild(cardPos);
			
			Element cardPosX = doc.createElement("cardPosX");
			cardPosX.appendChild(doc.createTextNode(String.valueOf(card.getPosition().x)));
			swipeCard.appendChild(cardPosX);
			
			Element cardPosY = doc.createElement("cardPosY");
			cardPosY.appendChild(doc.createTextNode(String.valueOf(card.getPosition().y)));
			swipeCard.appendChild(cardPosY);
			
			Element cardPosZ = doc.createElement("cardPosZ");
			cardPosZ.appendChild(doc.createTextNode(String.valueOf(card.getPosition().z)));
			swipeCard.appendChild(cardPosZ);
			
			Element rotX = doc.createElement("rotX");
			rotX.appendChild(doc.createTextNode(String.valueOf(card.getRotX())));
			swipeCard.appendChild(rotX);
			
			Element rotY = doc.createElement("rotY");
			rotY.appendChild(doc.createTextNode(String.valueOf(card.getRotY())));
			swipeCard.appendChild(rotY);
			
			Element rotZ = doc.createElement("rotZ");
			rotZ.appendChild(doc.createTextNode(String.valueOf(card.getRotZ())));
			swipeCard.appendChild(rotZ);
			
			Element scale = doc.createElement("scale");
			scale.appendChild(doc.createTextNode(String.valueOf(card.getScale())));
			swipeCard.appendChild(scale);
			
			Element id = doc.createElement("id");
			rotZ.appendChild(doc.createTextNode(String.valueOf(card.getUID())));
			swipeCard.appendChild(id);
			
			// TODO Add cardNum?
		}
		

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