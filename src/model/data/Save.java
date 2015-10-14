package model.data;

import model.GameWorld;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.SwipeCard;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Class for the saving of important gamestate variables. Data is stored in an
 * xml format.
 *
 * @author Finn Kinnear
 * @author Divya
 */

public class Save {

    private final static String FILE_NAME = "save.xml";

    /**
     * Saves gamestate fields, and delegates saving player, inventory, movable
     * entities, and swipe cards to helper methods.
     *
     * @param gameWorld the world of the game
     * @return whether or not the save was successful
     */
    public static boolean saveGame(GameWorld gameWorld) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // gamestate elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("gamestate");
            doc.appendChild(rootElement);

            // delegation of large groupings of saving to smaller, helper
            // methods
            savePlayer(doc, rootElement, gameWorld);
            saveInventory(doc, rootElement, gameWorld);
            saveEntities(doc, rootElement, gameWorld);
            saveSwipeCards(doc, rootElement, gameWorld);

            // isProgramCompiled element
            Element isProgramCompiled = doc.createElement("isProgramCompiled");
            isProgramCompiled.appendChild(doc.createTextNode(String
                    .valueOf(GameWorld.isProgramCompiled())));
            rootElement.appendChild(isProgramCompiled);

            // isOutside element
            Element isOutside = doc.createElement("isOutside");
            isOutside.appendChild(doc.createTextNode(String
                    .valueOf(GameWorld.isOutside())));
            rootElement.appendChild(isOutside);

            // progress element
            Element progress = doc.createElement("progress");
            progress.appendChild(doc.createTextNode(String
                    .valueOf(gameWorld.getProgress())));
            rootElement.appendChild(progress);

            // gameState element
            Element gameState = doc.createElement("gameState");
            gameState.appendChild(doc.createTextNode(String
                    .valueOf(gameWorld.getGameState())));
            rootElement.appendChild(gameState);

            // gameState element
            Element commitCollected = doc.createElement("commitCollected");
            commitCollected.appendChild(doc.createTextNode(String
                    .valueOf(gameWorld.getCommitCollected())));
            rootElement.appendChild(commitCollected);

            // score element
            Element score = doc.createElement("score");
            score.appendChild(doc.createTextNode(String.valueOf(gameWorld
                    .getScore())));
            rootElement.appendChild(score);

            // canApplyPatch element
            Element canApplyPatch = doc.createElement("canApplyPatch");
            canApplyPatch.appendChild(doc.createTextNode(String
                    .valueOf(gameWorld.isCanApplyPatch())));
            rootElement.appendChild(canApplyPatch);

            // commitIndex element
            Element commitIndex = doc.createElement("commitIndex");
            commitIndex.appendChild(doc.createTextNode(String.valueOf(gameWorld
                    .getCommitIndex())));
            rootElement.appendChild(commitIndex);

            // timer element
            Element timer = doc.createElement("timer");
            timer.appendChild(doc.createTextNode(String.valueOf(gameWorld
                    .getTimer())));
            rootElement.appendChild(timer);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(
                    System.getProperty("user.dir") + File.separator + "res"
                            + File.separator + "data" + File.separator
                            + FILE_NAME));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);
            return true;

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }

        // the game was not successfully saved
        return false;
    }

    /**
     * Helper methods that saves all player data into xml format.
     *
     * @param doc         the document in which the xml heirachy is stored
     * @param rootElement the element to which all others are appended
     */

    public static void savePlayer(Document doc, Element rootElement,
                                  GameWorld gameWorld) {
        // player elements
        Element player = doc.createElement("player");
        rootElement.appendChild(player);

        // set attribute to player element
        Attr attr = doc.createAttribute("id");
        attr.setValue("1");
        player.setAttributeNode(attr);

        // position x element
        Element playerPosX = doc.createElement("playerPosX");
        playerPosX.appendChild(doc.createTextNode(String.valueOf(gameWorld
                .getPlayer().getPosition().getX())));
        player.appendChild(playerPosX);

        // position x element
        Element playerPosY = doc.createElement("playerPosY");
        playerPosY.appendChild(doc.createTextNode(String.valueOf(gameWorld
                .getPlayer().getPosition().getY())));
        player.appendChild(playerPosY);

        // position x element
        Element playerPosZ = doc.createElement("playerPosZ");
        playerPosZ.appendChild(doc.createTextNode(String.valueOf(gameWorld
                .getPlayer().getPosition().getZ())));
        player.appendChild(playerPosZ);

        // pitch element
        Element pitch = doc.createElement("pitch");
        pitch.appendChild(doc.createTextNode(String.valueOf(gameWorld
                .getPlayer().getCamera().getPitch())));
        player.appendChild(pitch);

        // roll element
        Element roll = doc.createElement("roll");
        roll.appendChild(doc.createTextNode(String.valueOf(gameWorld
                .getPlayer().getCamera().getRoll())));
        player.appendChild(roll);

        // yaw element
        Element yaw = doc.createElement("yaw");
        yaw.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer()
                .getCamera().getYaw())));
        player.appendChild(yaw);

        // uid element
        Element uid = doc.createElement("uid");
        uid.appendChild(doc.createTextNode(String.valueOf(gameWorld.getPlayer()
                .getUID())));
        player.appendChild(uid);
    }

    /**
     * Helper methods that saves all inventory data into xml format.
     *
     * @param doc         the document in which the xml heirachy is stored
     * @param rootElement the element to which all others are appended
     */

    public static void saveInventory(Document doc, Element rootElement,
                                     GameWorld gameWorld) {

        // laptop items container element
        Element inventory = doc.createElement("inventory");
        rootElement.appendChild(inventory);

        // storage used in the inventory element
        Element storageUsed = doc.createElement("storageUsed");
        storageUsed.appendChild(doc.createTextNode(String.valueOf(gameWorld
                .getInventory().getStorageUsed())));
        inventory.appendChild(storageUsed);

        // individual laptop item elements
        for (LaptopItem item : gameWorld.getInventory().getItems()) {

            Element inventoryItem = doc.createElement("inventoryItem");
            inventory.appendChild(inventoryItem);

            Element itemPosX = doc.createElement("posX");
            itemPosX.appendChild(doc.createTextNode(String.valueOf(item
                    .getPosition().x)));
            inventoryItem.appendChild(itemPosX);

            Element itemPosY = doc.createElement("posY");
            itemPosY.appendChild(doc.createTextNode(String.valueOf(item
                    .getPosition().y)));
            inventoryItem.appendChild(itemPosY);

            Element itemPosZ = doc.createElement("posZ");
            itemPosZ.appendChild(doc.createTextNode(String.valueOf(item
                    .getPosition().z)));
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
            id.appendChild(doc.createTextNode(String.valueOf(item.getUID())));
            inventoryItem.appendChild(id);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(item.getName()));
            inventoryItem.appendChild(name);

            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode(String.valueOf(item.getType())));
            inventoryItem.appendChild(type);

        }
    }

    /**
     * Helper methods that saves all entity data into xml format.
     *
     * @param doc         the document in which the xml heirachy is stored
     * @param rootElement the element to which all others are appended
     */

    public static void saveEntities(Document doc, Element rootElement,
                                    GameWorld gameWorld) {

        // movable entity container element
        Element movableEntities = doc.createElement("movableEntities");
        rootElement.appendChild(movableEntities);

        // individual movable entity elements
        for (MovableEntity e : gameWorld.getMoveableEntities().values()) {

            Element movableEntity = doc.createElement("movableEntity");
            movableEntities.appendChild(movableEntity);

            Element entityPosX = doc.createElement("posX");
            entityPosX.appendChild(doc.createTextNode(String.valueOf(e
                    .getPosition().x)));
            movableEntity.appendChild(entityPosX);

            Element entityPosY = doc.createElement("posY");
            entityPosY.appendChild(doc.createTextNode(String.valueOf(e
                    .getPosition().y)));
            movableEntity.appendChild(entityPosY);

            Element entityPosZ = doc.createElement("posZ");
            entityPosZ.appendChild(doc.createTextNode(String.valueOf(e
                    .getPosition().z)));
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
            id.appendChild(doc.createTextNode(String.valueOf(e.getUID())));
            movableEntity.appendChild(id);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(String.valueOf(e.getName())));
            movableEntity.appendChild(name);

            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode(String.valueOf(e
                    .getType())));
            movableEntity.appendChild(type);

            if (e.getType().equals("SwipeCard")) {
                Element cardNum = doc.createElement("cardNum");
                cardNum.appendChild(doc.createTextNode(String.valueOf(e
                        .getCardNum())));
                movableEntity.appendChild(cardNum);
            }

            if (e.getType().equals("Laptop")) {
                Element cardID = doc.createElement("cardID");
                cardID.appendChild(doc.createTextNode(String.valueOf(e
                        .getCardID())));
                movableEntity.appendChild(cardID);

                Element hasCode = doc.createElement("hasCode");
                hasCode.appendChild(doc.createTextNode(String.valueOf(e
                        .getHasCode())));
                movableEntity.appendChild(hasCode);
            }

        }
    }

    /**
     * Helper methods that saves all swipeCard data into xml format.
     *
     * @param doc         the document in which the xml heirachy is stored
     * @param rootElement the element to which all others are appended
     */

    public static void saveSwipeCards(Document doc, Element rootElement,
                                      GameWorld gameWorld) {

        // swipe card container element
        Element swipeCards = doc.createElement("swipeCards");
        rootElement.appendChild(swipeCards);

        // individual swipe card elements
        for (SwipeCard card : gameWorld.getSwipeCards()) {

            Element swipeCard = doc.createElement("swipeCard");
            swipeCards.appendChild(swipeCard);

            Element cardPosX = doc.createElement("posX");
            cardPosX.appendChild(doc.createTextNode(String.valueOf(card
                    .getPosition().x)));
            swipeCard.appendChild(cardPosX);

            Element cardPosY = doc.createElement("posY");
            cardPosY.appendChild(doc.createTextNode(String.valueOf(card
                    .getPosition().y)));
            swipeCard.appendChild(cardPosY);

            Element cardPosZ = doc.createElement("posZ");
            cardPosZ.appendChild(doc.createTextNode(String.valueOf(card
                    .getPosition().z)));
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
            id.appendChild(doc.createTextNode(String.valueOf(card.getUID())));
            swipeCard.appendChild(id);

            Element cardNum = doc.createElement("cardNum");
            cardNum.appendChild(doc.createTextNode(String.valueOf(card
                    .getCardNum())));
            swipeCard.appendChild(cardNum);

        }
    }
}