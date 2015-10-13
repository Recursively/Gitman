package controller;

import model.entities.movableEntity.Laptop;
import model.entities.movableEntity.MovableEntity;
import model.network.NetworkHandler;
import model.network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Controller class to handle the Server, that waits and accepts connections
 * when a client connects to the ip-address of the server computer.
 * 
 * Extends Thread which sits along side the gameController that deals with
 * Network logic
 *
 *
 * @author Reuben Puketapu
 * @author Marcel van Workum
 * 
 */
public class ServerController extends Thread {

	/*
	 * ServerSocket that is bound to a port and listens to connections
	 */
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
		servers = new ArrayList<>();
		this.isRunning = true;
		initServerSocket();
		initNetworkHandler();

	}

	public void run() {

		createHostPlayer();
		GameController.READY = true;

		while (isRunning) {

			try {
				socket = serverSocket.accept();
				Server server = new Server(socket, gameController, networkHandler);
				int uid = createOtherPlayer();
				server.sendPlayerID(uid);
				server.setUid(uid);
				server.initNewPlayer();
				servers.add(server);
				server.start();

			} catch (IOException e) {
				System.out.println("SOCKET IS CLOSED");
			}

		}
	}

	/*
	 * Initializes the server socket
	 */
	private void initServerSocket() {
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Initializes the network handler
	 */
	private void initNetworkHandler() {
		this.networkHandler = new NetworkHandler(gameController.getGameWorld());
	}

	/*
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

	/*
	 * Creates the Host Server player with the UID of 0
	 */
	private void createHostPlayer() {
		gameController.createPlayer(0, true);
	}

	/*
	 * Creates another Client player with a UID of the size of the game
	 */
	private int createOtherPlayer() {
		int uid = gameController.gameSize();
		gameController.createPlayer(uid);
		return uid;
	}
	
	/**
	 * 
	 * Sets the update in the ArrayList of Servers when a button has been pushed.
	 * If the entity is a laptop, add it to the interacted Laptops. 
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
		if(status == 17){
			networkHandler.getInteractedLaptops().add((Laptop) entity);
		}
	}

}