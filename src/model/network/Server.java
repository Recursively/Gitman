package model.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.BreakIterator;

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

	private boolean isRunning;

	public Server(Socket socket, GameController gameController) {
		this.socket = socket;
		this.gameController = gameController;
		this.isRunning = true;
		initStreams();
	}

	public void run() {
		try {
			while (isRunning) {
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
				// updateEntityPosition();

				// TODO send items information
				for (Entity entity : gameController.getGameWorld().getMoveableEntities()) {
					// sendEntityPosition(entity);
				}
			}
		} catch (IOException e) {
			terminate();
		}

	}

	

	public void initStreams()  {
		try {
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPlayerID(int id) throws IOException {
		outputStream.writeInt(id);
	}

	private void updateEntityPosition() throws IOException {
		float x = inputStream.readFloat();
		float y = inputStream.readFloat();
		float z = inputStream.readFloat();
		// TODO UPDATE THE POSITION WITH CORRESPONDING COORDINATES

	}

	private void sendEntityPosition(Entity entity) throws IOException {
		// FIXME outputStream.writeInt(entity.getUid());
		outputStream.writeFloat(entity.getPosition().x);
		outputStream.writeFloat(entity.getPosition().y);
		outputStream.writeFloat(entity.getPosition().z);

	}

	private void sendPlayerPosition(Player player) throws IOException {
		outputStream.writeInt(player.getUid());
		outputStream.writeFloat(player.getPosition().x);
		outputStream.writeFloat(player.getPosition().y);
		outputStream.writeFloat(player.getPosition().z);
	}

	private void sendNumberOfPlayers() throws IOException {
		outputStream.writeInt(gameController.gameSize());
	}

	public int readPlayerID() throws IOException {
		return inputStream.readInt();
	}

	private void checkExistingPlayer() {
		if (!gameController.getPlayers().containsKey(uid) && uid != -1) {
			System.out.println("CREATED NEW PLAYER ID: " + uid);
			gameController.createPlayer(uid);
		}
	}

	private void updatePlayerPosition(int uid) throws IOException {
		float x = inputStream.readFloat();
		float y = inputStream.readFloat();
		float z = inputStream.readFloat();
		gameController.getPlayerWithID(uid).setPosition(new Vector3f(x, y, z));
	}

	public void terminate() {
		System.out.println("CONNECTION TERMINATED TO PLAYER WITH ID: " + uid);
		gameController.networkRunning = false;
		isRunning = false;
		try {
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

}