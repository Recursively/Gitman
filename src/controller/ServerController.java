package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.GameWorld;
import model.network.Server;
import model.network.ServerHandler;

/**
 * Controller class to handle the delegations between the Model and View
 * package.
 *
 * Deals with Network logic
 *
 * @author Marcel van Workum
 */
public class ServerController extends Thread {

	private GameController gameController;
	private ServerHandler serverHandler;

	private ServerSocket serverSocket;
	private Server server;
	private Socket socket;

	public boolean isRunning;

	public ServerController(GameController gameController) {
		this.gameController = gameController;
		this.isRunning = true;
		initServerSocket();
		initServerHandler();
	}

	public void run() {

		createHostPlayer();
		gameController.READY = true;

		while (isRunning) {

			try {
				socket = serverSocket.accept();
				server = new Server(socket, gameController, this);
				int uid = createOtherPlayer();
				server.sendPlayerID(uid);
				server.setUid(uid);
				server.start();

			} catch (IOException e) {
				System.out.println("SOCKET IS CLOSED");
			}

		}
	}

	private void initServerSocket() {
		try {
			this.serverSocket = new ServerSocket(32768);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initServerHandler() {
		this.serverHandler = new ServerHandler(gameController.getGameWorld());
	}

	public void terminate() {

		isRunning = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("CLOSED SOCKET");
		}
		if (server != null) {
			server.terminate();
		}

	}

	private void createHostPlayer() {
		gameController.createPlayer(0, true);
	}

	private int createOtherPlayer() {
		int uid = gameController.gameSize();
		gameController.createPlayer(uid);
		return uid;
	}
	

	// when an update is sent to the server about an entity update process it here
	public void dealWithUpdate(int type, int id, float x, float y, float z) {
		
		switch (type) {
		case 8:
			serverHandler.dropLaptopItem(id, x, y, z);
			break;
		case 10:
			serverHandler.interactBug();
			break;
		case 11:
			serverHandler.interactCommit();
			break;
		case 13:
			serverHandler.interactLaptopItem(id);
			break;
		case 16:
			serverHandler.interactSwipeCard(id);
			break;
		case 17:
			
			break;
		default:
			break;
		}
		
	}

}