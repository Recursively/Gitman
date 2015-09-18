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

	public NetworkController(GameController gameController, ServerSocket ss) {
		this.gameController = gameController;
		this.ss = ss;
	}

	public void run() {
		while (1 == 1) {
			Socket socket = null;
			Server server = null;

			try {
				socket = ss.accept();
				gameController.addPlayer();
				server = new Server(socket, gameController.getPlayers());
				server.start();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
