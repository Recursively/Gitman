package model.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.lwjgl.util.vector.Vector3f;

import controller.GameController;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;

public class Server extends Thread {

	private Socket socket;

	private GameController gameController;

	private NetworkHandler networkHandler;

	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	private int uid;

	private int mostRecentUpdate = -1;

	private boolean isRunning;

	public Server(Socket socket, GameController gameController, NetworkHandler networkHandler) {
		this.socket = socket;
		this.gameController = gameController;
		this.isRunning = true;
		this.networkHandler = networkHandler;
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

				int check = checkUpdate();
				if (!networkHandler.serverUpdate) {
					networkHandler.setUpdate(check);
				}


				if (networkHandler.getUpdate() != -1 && networkHandler.getUpdate() != 0
						&& check == networkHandler.getUpdate()) {
					networkHandler.setMostRecentEntity(updateEntitiy(networkHandler.getUpdate()));
				}
				// NEED TO SET THE UPDATE TYPE OUTSIDE OF THIS THREAD!!!!!
				if (sendUpdateStatus(networkHandler.getUpdate()) != -1 && networkHandler.getUpdate() != 0) {
					sendUpdateEntity(networkHandler.getUpdate(), networkHandler.getMostRecentEntity());
					networkHandler.setUpdate(-1);
				}

			}
		} catch (IOException e) {
			terminate();
		}

	}

	private void sendUpdateEntity(int mostRecentUpdate, MovableEntity mostRecentEntity) throws IOException {
		outputStream.writeInt(mostRecentEntity.getUID());

		networkHandler.sentDone(uid);

	}

	private int sendUpdateStatus(int status) throws IOException {
		// send that there is an update to be made
		if (status != -1) {
			outputStream.writeInt(status);
			return status;
		} else {
			outputStream.writeInt(mostRecentUpdate);
			return mostRecentUpdate;
		}
	}

	public void initStreams() {
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

	private int checkUpdate() throws IOException {
		return inputStream.readInt();
	}

	private MovableEntity updateEntitiy(int updateType) throws IOException {

		int id = inputStream.readInt();

		MovableEntity temp = null;
		if (updateType != 8) {
			temp = gameController.getGameWorld().getMoveableEntities().get(id);

		} else {
			temp = gameController.getGameWorld().getInventory().getItem(id);
		}

		networkHandler.dealWithUpdate(updateType, id, uid);

		return temp;

	}

	private void sendPlayerPosition(Player player) throws IOException {
		outputStream.writeInt(player.getUID());
		outputStream.writeFloat(player.getPosition().x);
		
		if (player.getUID() == gameController.getPlayer().getUID()) {
			outputStream.writeFloat(player.getPosition().y + 10);
		} else {
			outputStream.writeFloat(player.getPosition().y);
		}
		
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
		gameController.removePlayer(uid);
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

	public void setUpdate(int status, MovableEntity entity) {
		this.networkHandler.serverUpdate = true;
		this.mostRecentUpdate = status;
		this.networkHandler.setMostRecentEntity(entity);

	}

	public void initNewPlayer() throws IOException {
		int inventorySize = gameController.getGameWorld().getInventory().getItems().size();

		outputStream.writeInt(inventorySize);
		for (LaptopItem entity : gameController.getGameWorld().getInventory().getItems()) {
			outputStream.writeInt(entity.getUID());
		}

		outputStream.writeInt(gameController.getGameWorld().getPatchProgress());

	}

}