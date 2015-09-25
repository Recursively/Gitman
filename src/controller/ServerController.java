package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.entities.movableEntity.Player;
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

	public ServerController(GameController gameController) {
		this.gameController = gameController;
		try {
			this.serverSocket = new ServerSocket(32768);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		createHostPlayer();

		while (1 == 1) {
			Socket socket = null;

			try {
				socket = serverSocket.accept();
				server = new Server(socket, gameController);
				int uid = createOtherPlayer();
				server.sendPlayerID(uid);
				server.setUid(uid);
				server.start();

			} catch (IOException e) {
				e.printStackTrace();
			}

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

}