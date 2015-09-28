package model.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.lwjgl.util.vector.Vector3f;

import controller.GameController;
import model.entities.Entity;
import model.entities.movableEntity.Item;
import model.entities.movableEntity.Player;

public class Server extends Thread {

	private Socket socket;

	private GameController gameController;

	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	private int uid;

	public Server(Socket socket, GameController gameController) {
		this.socket = socket;
		this.gameController = gameController;
		initStreams();
	}

	public void run() {

		while (1 == 1) {
			// receive player information
			uid = readPlayerID();
			checkExistingPlayer();
			updatePlayerPosition(uid);

			// send player information
			sendNumberOfPlayers();
			for (Player player : gameController.getPlayers().values()) {
				sendPlayerPosition(player);
			}

			// TODO receive items information
			//updateEntityPosition();

			// TODO send items information
			for (Entity entity : gameController.getGameWorld().getMoveableEntities()) {
				//sendEntityPosition(entity);
			}

		}

	}

	public void initStreams() {
		try {
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void sendPlayerID(int id) {
		try {
			outputStream.writeInt(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateEntityPosition() {
		try {
			float x = inputStream.readFloat();
			float y = inputStream.readFloat();
			float z = inputStream.readFloat();
			//TODO UPDATE THE POSITION WITH CORRESPONDING COORDINATES

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void sendEntityPosition(Entity entity) {
		try {
			//FIXME outputStream.writeInt(entity.getUid());
			outputStream.writeFloat(entity.getPosition().x);
			outputStream.writeFloat(entity.getPosition().y);
			outputStream.writeFloat(entity.getPosition().z);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sendPlayerPosition(Player player) {
		try {
			outputStream.writeInt(player.getUid());
			outputStream.writeFloat(player.getPosition().x);
			outputStream.writeFloat(player.getPosition().y);
			outputStream.writeFloat(player.getPosition().z);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendNumberOfPlayers() {
		try {
			outputStream.writeInt(gameController.gameSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int readPlayerID() {
		try {
			return inputStream.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private void checkExistingPlayer() {
		if (!gameController.getPlayers().containsKey(uid)) {
			System.out.println("CREATED NEW PLAYER ID: " + uid);
			gameController.createPlayer(uid);
		}
	}

	private void updatePlayerPosition(int uid) {
		try {
			float x = inputStream.readFloat();
			float y = inputStream.readFloat();
			float z = inputStream.readFloat();
			gameController.getPlayerWithID(uid).setPosition(new Vector3f(x, y, z));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setUid(int uid) {
		this.uid = uid;

	}

}