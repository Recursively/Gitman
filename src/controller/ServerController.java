package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.network.Server;

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

	private ServerSocket serverSocket;
	private Server server;
	private Socket socket;

	public boolean isRunning;

	public ServerController(GameController gameController) {
		this.gameController = gameController;
		this.isRunning = true;
		initServerSocket();
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
			gameController.getGameWorld().dropLaptopItem();
			break;
		case 10:
			gameController.getGameWorld().interactBug();
			break;
		case 11:
			gameController.getGameWorld().interactCommit();
			break;
//		case 12:
//			gameController.getGameWorld().interactDoor();
//			break;
		case 13:
			gameController.getGameWorld().interactLaptopItem();
			break;
//		case 14:
//			gameController.getGameWorld().interactNPCCharacter();
//			break;
//		case 15:
//			gameController.getGameWorld().interactPlayer();
//			break;
		case 16:
			gameController.getGameWorld().interactSwipeCard();
			break;
		case 17:
			break;
		default:
			break;
		}
		
	}

}