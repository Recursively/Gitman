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

	private boolean running;

	public ServerController(GameController gameController) {
		this.gameController = gameController;
		this.running = true;

		try {
			this.serverSocket = new ServerSocket(32768);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		// gameController.addHostPlayer();

		while (running) {
			Socket socket = null;

			try {
				// System.out.println("Accepting...");

				socket = serverSocket.accept();
				server = new Server(socket, gameController);
				server.sendInfoToNewPlayer();
				server.start();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void end() {
		running = false;
	}

}