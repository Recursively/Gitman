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
	 * Connects this client to the Server at the given port then starts
	 * the new Client thread
	 */
	
	public void start() {

		try {
			socket = new Socket(ipAddres, port);
			client = new Client(socket, gameController);
			int uid = client.readPlayerID();
			client.setUid(uid);
			createPlayer(uid);
			client.updateGameInformation();
			GameController.READY = true;
			client.start();

		} catch (UnknownHostException e) {
			GameController.networkDisconnected = true;
			GameController.READY = true;
		} catch (IOException e) {
			GameController.networkDisconnected = true;
			GameController.READY = true;
		} 
	}
	
	/**
	 * Creates a new player with the given UID
	 * 
	 * @param uid the ID of the new player
	 */
	private void createPlayer(int uid) {
		gameController.createPlayer(uid, true);
	}
	
	/**
	 * Terminates the Client thread
	 */
	public void terminate() {
		try {
			client.terminate();
		} catch (RuntimeException e){
			// client was not made
		}
	}
	
	/**
	 * Sends a network update to the Client thread when a 
	 * button has been pushed
	 * 
	 * @param status the type of update
	 * @param entity the entity that has been interacted with
	 */
	public void setNetworkUpdate(int status, MovableEntity entity) {
		client.setUpdate(status, entity);

	}

}
