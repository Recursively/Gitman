package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.GameWorld;
import model.entities.movableEntity.MovableEntity;
import model.network.Server;
import model.network.NetworkHandler;

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
	private NetworkHandler networkHandler;

	private ServerSocket serverSocket;
	private Server server;
	private Socket socket;

	public boolean isRunning;

	public ServerController(GameController gameController) {
		this.gameController = gameController;
		this.isRunning = true;
		initServerSocket();
		initNetworkHandler();

	}

	public void run() {

		createHostPlayer();
		gameController.READY = true;

		while (isRunning) {

			try {
				socket = serverSocket.accept();
				server = new Server(socket, gameController, networkHandler);
				int uid = createOtherPlayer();
				server.sendPlayerID(uid);
				server.setUid(uid);
				server.initNewPlayer();
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
	
	private void initNetworkHandler() {
		this.networkHandler = new NetworkHandler(gameController.getGameWorld());
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

	public void setNetworkUpdate(int status, MovableEntity entity) {
		if (gameController.getPlayers().size() != 1) {
			server.setUpdate(status, entity);
		}

	}




}