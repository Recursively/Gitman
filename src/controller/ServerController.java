package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.Network.Server;

/**
 * Controller class to handle the delegations between the Model and View
 * package.
 *
 * Deals with Network logic
 *
 * @author Marcel van Workum
 */
public class ServerController extends Thread {

	private NetworkController gameController;

	private ServerSocket serverSocket;
	private Server server;

	private boolean running;

	public ServerController(NetworkController gameController) throws IOException {
		this.gameController = gameController;
		this.serverSocket = new ServerSocket(32768);
		this.running = true;
	}

	public void run() {
		while (running) {
			Socket socket = null;

			try {
				// System.out.println("Accepting...");

				socket = serverSocket.accept();
				server = new Server(socket, gameController);
				server.sendGameInfo();
				gameController.addPlayerServer();
				System.out.println("GAME SIZE NOW: " + gameController.getPlayers().size());
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