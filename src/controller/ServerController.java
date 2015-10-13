package controller;

import model.entities.movableEntity.Laptop;
import model.entities.movableEntity.MovableEntity;
import model.network.NetworkHandler;
import model.network.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Controller class to handle the Server, that waits and accepts connections
 * when a client connects to the ip-address of the server computer.
 * <p/>
 * Extends Thread which sits along side the gameController that deals with
 * Network logic
 *
 * @author Reuben Puketapu
 */
public class ServerController extends Thread {

    private ServerSocket serverSocket;
    private ArrayList<Server> servers;
    private Socket socket;

    private static int port = 32768;

    private GameController gameController;
    private NetworkHandler networkHandler;

    public boolean isRunning;

    /**
     * Constructor for the ServerController
     *
     * @param gameController gameController
     */
    public ServerController(GameController gameController) {
        this.gameController = gameController;
        this.servers = new ArrayList<>();
        this.isRunning = true;
        initServerSocket();
        initNetworkHandler();
    }

    /**
     * The main thread loop that listens to the port and accepts connections
     */
    public void run() {
        createHostPlayer();
        GameController.READY = true;

        while (isRunning) {

            try {
                socket = serverSocket.accept();
                Server server = new Server(socket, gameController, networkHandler);
                int uid = createOtherPlayer();

                // send all the information to the client to ensure the server
                // is ready for the server thread to start
                server.sendPlayerID(uid);
                server.sendIsCommitCollected();
                server.setUid(uid);
                server.initNewPlayer();
                servers.add(server);

                // start the server thread
                server.start();

            } catch (IOException e) {
                // There was no connections to be made
                System.out.println("SOCKET IS CLOSED");
            }

        }
    }

    /**
     * Initialises the socket and prints the ip address
     */
    private void initServerSocket() {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("\n********************");
            System.out.println("SERVER IP ADDRESS: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("********************\n");

        } catch (IOException e) {
            System.err.println("Failed to get ip address of host");
        }
    }

    /**
     * Initializes the network handler
     */
    private void initNetworkHandler() {
        this.networkHandler = new NetworkHandler(gameController.getGameWorld());
    }

    /**
     * When the Server Controller or the Server have an error terminate is
     * called to handle the disconnections nicely. Closes the serverSocket and
     * tells all the Server threads to terminate.
     */
    public void terminate() {

        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("CLOSED SOCKET");
        }
        if (servers != null) {
            for (Server server : servers) {
                server.terminate();
            }
        }
    }

    /**
     * Creates the Host Server player with the UID of 0
     */
    private void createHostPlayer() {
        gameController.createPlayer(0, true);
    }

    /**
     * Creates another Client player with a UID of the size of the game
     */
    private int createOtherPlayer() {
        int uid = gameController.gameSize();
        gameController.createPlayer(uid);
        return uid;
    }

    /**
     * Sets the update in the ArrayList of Servers when a button has been
     * pushed. If the entity is a laptop, add it to the interacted Laptops.
     *
     * @param status which type of interaction has occurred
     * @param entity a reference to the actual entity that has been interacted with
     */
    public void setNetworkUpdate(int status, MovableEntity entity) {
        if (gameController.getPlayers().size() != 1) {
            for (Server server : servers) {
                server.setUpdate(status, entity);
            }
        }

        // if there was a laptop interacted with, add this entity to the
        // interacted Laptops
        if (status == 17) {
            networkHandler.getInteractedLaptops().add((Laptop) entity);
        }
    }

}