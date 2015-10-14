package model.network;

import controller.GameController;
import model.entities.movableEntity.*;
import org.lwjgl.util.vector.Vector3f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The Server Thread that sends all the information to a single Client,
 * including Player positions and Entity updates, then delegates to the
 * GameController
 *
 * @author Reuben Puketapu - 300310939
 */
public class Server extends Thread {

    private Socket socket;

    private GameController gameController;

    private NetworkHandler networkHandler;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private Update serverUpdate;

    private int uid;
    private boolean isRunning;

    /**
     * Constructor for the Server Thread
     *
     * @param socket         socket to bind streams to
     * @param gameController game controller
     * @param networkHandler networkHandler
     */
    public Server(Socket socket, GameController gameController, NetworkHandler networkHandler) {
        this.socket = socket;
        this.gameController = gameController;
        this.isRunning = true;
        this.networkHandler = networkHandler;
        initStreams();
    }

    /**
     * Thread main loop that runs alongside the gameLoop and sends and listens
     * information to the corresponding Client Thread
     */
    public void run() {
        try {
            while (isRunning) {
                // receive player information
                this.uid = readPlayerID();
                checkExistingPlayer();
                updatePlayerPosition(uid);

                // send player information
                sendNumberOfPlayers();
                for (Player player : gameController.getPlayers().values()) {
                    sendPlayerPosition(player);
                }

                Update update = readEntityUpdate();

                if (update != null) {
                    networkHandler.setClientUpdate(update);
                }

                // NEED TO SET THE UPDATE TYPE OUTSIDE OF THIS THREAD!!!!!
                sendUpdateEntity(serverUpdate, networkHandler.getClientUpdate());

                serverUpdate = null;

            }
        } catch (IOException e) {
            terminate();
        }

    }

    /**
     * Reads the information from the input streams about any Entity updates
     *
     * @return the new Update received
     * @throws IOException
     */
    private Update readEntityUpdate() throws IOException {
        int update = inputStream.readInt();
        int id = inputStream.readInt();
        int uid = inputStream.readInt();

        if (update != -1 && id != -1) {
            networkHandler.dealWithUpdate(update, id, uid);
            return new Update(update, id, uid);

        } else {
            return null;
        }

    }

    /**
     * Sends an update of integers to the Client when there is an update either
     * from another Client or the Server Player itself. Sends -1 if there is no
     * update to be sent
     *
     * @param serverUpdate the update from the Server Player
     * @param clientUpdate the update from another Client Player
     * @throws IOException
     */
    private void sendUpdateEntity(Update serverUpdate, Update clientUpdate) throws IOException {
        if (serverUpdate != null) {
            outputStream.writeInt(serverUpdate.update);
            outputStream.writeInt(serverUpdate.id);
            outputStream.writeInt(serverUpdate.uid);
        } else if (clientUpdate != null && clientUpdate.uid != this.uid) {
            outputStream.writeInt(clientUpdate.update);
            outputStream.writeInt(clientUpdate.id);
            outputStream.writeInt(clientUpdate.uid);
        } else {
            outputStream.writeInt(-1);
            outputStream.writeInt(-1);
            outputStream.writeInt(-1);
        }
    }

    /**
     * Sends the position of a player
     *
     * @param player the player to be sent
     * @throws IOException
     */
    private void sendPlayerPosition(Player player) throws IOException {
        outputStream.writeInt(player.getUID());
        outputStream.writeFloat(player.getPosition().x);

        // if this player is the current player send it's position (y+10)
        // otherwise just y
        if (player.getUID() == gameController.getPlayer().getUID()) {
            outputStream.writeFloat(player.getPosition().y + 10);
        } else {
            outputStream.writeFloat(player.getPosition().y);
        }

        outputStream.writeFloat(player.getPosition().z);
    }

    /**
     * Checks if this player has connected before, otherwise creates the new
     * player
     */
    private void checkExistingPlayer() {
        if (!gameController.getPlayers().containsKey(uid) && uid != -1) {
            gameController.createPlayer(uid);
        }
    }

    /**
     * Updates the player's position with the given UID, then listens to the
     * inputStream and update the position accordingly
     *
     * @param uid
     * @throws IOException
     */
    private void updatePlayerPosition(int uid) throws IOException {
        float x = inputStream.readFloat();
        float y = inputStream.readFloat();
        float z = inputStream.readFloat();
        gameController.getPlayerWithID(uid).setPosition(new Vector3f(x, y, z));
    }

    /**
     * Sends the number of players in the Game to the Client
     *
     * @throws IOException
     */
    private void sendNumberOfPlayers() throws IOException {
        outputStream.writeInt(gameController.gameSize());
    }

    /**
     * Reads the ID of the player who is sending this information
     *
     * @return the ID of the Player
     * @throws IOException failed to read from the inputStream
     */
    public int readPlayerID() throws IOException {
        return inputStream.readInt();
    }

    /**
     * Terminates this Thread then tells the gameController to end all
     * activities and removes this player from the Servers Map of players
     */
    public void terminate() {
        System.out.println("CONNECTION TERMINATED TO PLAYER WITH ID: " + uid);
        gameController.removePlayer(uid);
        isRunning = false;
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the UID of this Server
     *
     * @param uid the uid
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Sets the update of this
     *
     * @param status the status
     * @param entity the entity
     */
    public void setUpdate(int status, MovableEntity entity) {
        serverUpdate = (new Update(status, entity.getUID(), 0));

        // adds the laptop the interacted laptops
        if (status == 8) {
            networkHandler.getInteractedLaptops().add((Laptop) entity);
        } else if (status == 11) {
            networkHandler.getInteractedCommits().add((Commit) entity);
        }
    }

    /**
     * Initializes the streams
     */
    public void initStreams() {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the player ID to the corresponding Client
     *
     * @param id the ID of the Player
     * @throws IOException failed to write to the outputStream
     */
    public void sendPlayerID(int id) throws IOException {
        outputStream.writeInt(id);
    }

    /**
     * When a new Player connects to the game this sends the information about
     * Entities that have been interacted with
     *
     * @throws IOException failed to write to the outputStream
     */
    public void initNewPlayer() throws IOException {

        // sends the number of swipe cards in the inventory then which ones
        int swipeSize = gameController.getGameWorld().getSwipeCards().size();
        outputStream.writeInt(swipeSize);
        for (SwipeCard swipeCard : gameController.getGameWorld().getSwipeCards()) {
            outputStream.writeInt(swipeCard.getUID());
        }

        // sends the number of items in the inventory then which ones
        int inventorySize = gameController.getGameWorld().getInventory().getItems().size();
        outputStream.writeInt(inventorySize);
        for (LaptopItem entity : gameController.getGameWorld().getInventory().getItems()) {
            outputStream.writeInt(entity.getUID());
        }

        // sends the number of laptops then which ones exactly
        int laptopSize = networkHandler.getInteractedLaptops().size();
        outputStream.writeInt(laptopSize);
        for (Laptop laptop : networkHandler.getInteractedLaptops()) {
            outputStream.writeInt(laptop.getUID());
        }

        int commitSize = networkHandler.getInteractedCommits().size();
        outputStream.writeInt(commitSize);
        for (Commit commit : networkHandler.getInteractedCommits()) {
            outputStream.writeInt(commit.getUID());
        }

        // sends the progress
        outputStream.writeInt(gameController.getGameWorld().getProgress());

    }

    /**
     * Sends if there has been a collected commit
     *
     * @throws IOException the io exception
     */
    public void sendIsCommitCollected() throws IOException {
        outputStream.writeInt(gameController.getGameWorld().getCommitCollected());

    }

}