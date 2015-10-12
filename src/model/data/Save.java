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
		
		// codeProgress element
		Element codeProgress = doc.createElement("codeProgress");
		codeProgress.appendChild(doc.createTextNode(String.valueOf(gameWorld.getCodeProgress())));
		rootElement.appendChild(codeProgress);
		
		// patchProgress element
		Element patchProgress = doc.createElement("patchProgress");
		patchProgress.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPatchProgress())));
		rootElement.appendChild(patchProgress);
		
		// score element
		Element score = doc.createElement("score");
		score.appendChild(doc.createTextNode(String.valueOf(gameWorld.getScore())));
		rootElement.appendChild(score);
		
		// inProgram element
		Element inProgram = doc.createElement("inProgram");
		inProgram.appendChild(doc.createTextNode(String.valueOf(gameWorld.isInProgram())));
		rootElement.appendChild(inProgram);
		
		// canApplyPatch element
		Element canApplyPatch = doc.createElement("canApplyPatch");
		canApplyPatch.appendChild(doc.createTextNode(String.valueOf(gameWorld.isCanApplyPatch())));
		rootElement.appendChild(canApplyPatch);
		
		// commitIndex element
		Element commitIndex = doc.createElement("commitIndex");
		commitIndex.appendChild(doc.createTextNode(String.valueOf(gameWorld.getCommitIndex())));
		rootElement.appendChild(commitIndex);
		
		// timer element
		Element timer = doc.createElement("timer");
		timer.appendChild(doc.createTextNode(String.valueOf(gameWorld.getTimer())));
		rootElement.appendChild(timer);

		// player elements
		Element player = doc.createElement("player");
		rootElement.appendChild(player);

		// set attribute to player element
		Attr attr = doc.createAttribute("id");
		attr.setValue("1");
		player.setAttributeNode(attr);
		
		// position x element
		Element posX = doc.createElement("posX");
		posX.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getPosition().getX())));
		player.appendChild(posX);
		
		// position x element
		Element posY = doc.createElement("posY");
		posY.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getPosition().getY())));
		player.appendChild(posY);
		
		// position x element
		Element posZ = doc.createElement("posZ");
		posZ.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getPosition().getZ())));
		player.appendChild(posZ);

		// pitch element
		Element pitch = doc.createElement("pitch");
		pitch.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getCamera().getPitch())));
		player.appendChild(pitch);

		// roll element
		Element roll = doc.createElement("roll");
		roll.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getCamera().getRoll())));
		player.appendChild(roll);

		// yaw element
		Element yaw = doc.createElement("yaw");
		yaw.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getCamera().getYaw())));
		player.appendChild(yaw);
		
		// uid element
		Element uid = doc.createElement("uid");
		uid.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer().getUID())));
		player.appendChild(uid);
		
		// inventory elements
		Element inventory = doc.createElement("inventory");
		rootElement.appendChild(inventory);
		
		Element storageUsed = doc.createElement("storageUsed");
		storageUsed.appendChild(doc.createTextNode(String.valueOf(gameWorld.getInventory().getStorageUsed())));
		inventory.appendChild(storageUsed);
		
		// laptop item elements
		for (LaptopItem item : gameWorld.getInventory().getItems()){
			
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
			
			Element type = doc.createElement("type");
			type.appendChild(doc.createTextNode(String.valueOf(item.getType())));
			inventoryItem.appendChild(type);
			
			
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
			
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(String.valueOf(e.getName())));
			movableEntity.appendChild(id);
			
			Element type = doc.createElement("type");
			movableEntity.appendChild(doc.createTextNode(String.valueOf(e.getType())));
			movableEntity.appendChild(type);
			
			if (e.getType() == "SwipeCard"){
				Element cardNum = doc.createElement("cardNum");
				movableEntity.appendChild(doc.createTextNode(String.valueOf(e.getCardNum())));
				movableEntity.appendChild(cardNum);
			}
			
			if (e.getType() == "Laptop"){
				Element cardID = doc.createElement("cardID");
				movableEntity.appendChild(doc.createTextNode(String.valueOf(e.getCardID())));
				movableEntity.appendChild(cardID);
			}
			
		}
		
		// swipe card elements
		Element swipeCards = doc.createElement("swipeCards");
		rootElement.appendChild(swipeCards);
		
		// swipe card elements
		for (SwipeCard card : gameWorld.getSwipeCards()){
			
			Element swipeCard = doc.createElement("swipeCard");
			swipeCard.appendChild(swipeCards);
			
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
			
			Element cardNum = doc.createElement("cardNum");
			swipeCard.appendChild(doc.createTextNode(String.valueOf(card.getCardNum())));
			swipeCard.appendChild(cardNum);
			
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