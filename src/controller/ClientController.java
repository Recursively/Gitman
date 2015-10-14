package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import model.entities.movableEntity.MovableEntity;
import model.network.Client;

public class ClientController {

	private GameController gameController;
	private Socket socket;
	private Client client;
	private String ipAddres;
	
	private static int port = 32768;
	
	public ClientController(GameController controller, String ipAddress) {
		this.gameController = controller;
		this.ipAddres = ipAddress;
	}
	
	/**
	 * Connects this client to the Server at the given port
	 */
	public void start() {

		try {
			socket = new Socket(ipAddres, port);
			client = new Client(socket, gameController);
			int uid = client.readPlayerID();
			client.setUid(uid);
			createPlayer(uid);
			client.updateGameInformation();
			gameController.READY = true;
			client.start();

		} catch (UnknownHostException e) {
			GameController.NETWORK_DISCONNECTED = true;
			gameController.READY = true;
		} catch (IOException e) {
			GameController.NETWORK_DISCONNECTED = true;
			gameController.READY = true;
		} 
	}

	private void createPlayer(int uid) {
		gameController.createPlayer(uid, true);
	}

	public void terminate() {
		try {
			client.terminate();
		} catch (RuntimeException e){
			// client was not made
		}
	}

	public void setNetworkUpdate(int status, MovableEntity entity) {
		client.setUpdate(status, entity);

	}

}
