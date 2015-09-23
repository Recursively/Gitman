package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import model.network.Client;

public class ClientController extends Thread {

	private GameController gameController;
	private Socket socket;
	private Client client;

	public ClientController(GameController controller) {
		this.socket = null;
		this.client = null;
		this.gameController = controller;
	}

	public void run() {

		int port = 32768; // default

		try {
			socket = new Socket("localhost", port);
			System.out.println("Connected");

			client = new Client(socket, gameController);
			int uid = client.requestPlayersLength();
			System.out.println("THIS PLAYER UID: " + uid);
			gameController.getPlayer().setUid(uid);
			client.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
