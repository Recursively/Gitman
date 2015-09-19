package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import model.Network.Client;
import model.entities.movableEntity.Player;

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
			socket = new Socket("130.195.6.51", port);
			System.out.println("Connected");

			client = new Client(socket, gameController);
			client.requestPlayersLength();
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		client.start();

	}
	

}
