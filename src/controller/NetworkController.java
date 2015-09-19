package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import model.Network.Server;
import model.entities.movableEntity.Player;
import model.models.TexturedModel;

/**
 * Controller class to handle the delegations between the Model and View
 * package.
 *
 * Deals with Network logic
 *
 * @author Marcel van Workum
 */
public class NetworkController extends Thread {

	private GameController gameController;

	private ServerSocket ss;
	private Server server;

	private boolean running;

	public NetworkController(GameController gameController, ServerSocket ss) {
		this.gameController = gameController;
		this.ss = ss;
		this.running = true;
	}

	public void run() {
		while (running) {
			Socket socket = null;

			try {
				socket = ss.accept();
				server = new Server(socket, gameController);
				server.sendGameInfo();
				gameController.addPlayerServer();
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
