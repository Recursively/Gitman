package controller;

import model.entities.movableEntity.MovableEntity;
import model.network.Client;

import java.io.IOException;
import java.net.Socket;

/**
 * When the game is started as a Client a new Client Controller is made and
 * creates a new player with a requested UID. Client Controller creates a Client
 * thread which communicates with a Server Thread.
 *
 * @author Reuben Puketapu - 300310939
 */
public class ClientController {

    private GameController gameController;
    private Socket socket;
    private Client client;
    private String ipAddres;

    private static int port = 32768;

    /**
     * Instantiates a new Client controller.
     *
     * @param gameController the game controller
     * @param ipAddress      the ip address
     */
    public ClientController(GameController gameController, String ipAddress) {
        this.gameController = gameController;
        this.ipAddres = ipAddress;
    }

    /**
     * Connects this client to the Server at the given port then starts the new
     * Client thread
     */
    public void start() {

        try {
            socket = new Socket(ipAddres, port);
            client = new Client(socket, gameController);
            int uid = client.readPlayerID();

            // initialize the Player and gameController so they're ready for the
            // Client Thread to start
            client.readIsCommitCollected();
            client.setUid(uid);
            createPlayer(uid);
            client.updateGameInformation();
            GameController.READY = true;

            // start the Client Thread
            client.start();

        } catch (IOException e) {
            // if there is a problem with the connection, disconnect
            GameController.NETWORK_DISCONNECTED = true;
            GameController.READY = true;
        }
    }

    /**
     * Creates a new player with the given UID
     *
     * @param uid the ID of the new player
     */
    private void createPlayer(int uid) {
        gameController.createPlayer(uid, true);
    }

    /**
     * Terminates the Client thread
     */
    public void terminate() {
        try {
            client.terminate();
        } catch (RuntimeException e) {
            System.err.println("Failed to terminate the client thread");
        }
    }

    /**
     * Sends a network update to the Client thread when a button has been pushed
     *
     * @param status the type of update
     * @param entity the entity that has been interacted with
     */
    public void setNetworkUpdate(int status, MovableEntity entity) {
        client.setUpdate(status, entity);
    }
}
