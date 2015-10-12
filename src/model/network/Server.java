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
import model.entities.movableEntity.SwipeCard;

public class Server extends Thread {

	private Socket socket;

	private GameController gameController;

	private NetworkHandler networkHandler;

	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	private int uid;

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

				Update update = readEntityUpdate();

				if (update != null) {
					networkHandler.setClientUpdate(update);
				}

				// NEED TO SET THE UPDATE TYPE OUTSIDE OF THIS THREAD!!!!!
				sendUpdateEntity(networkHandler.getServerUpdate(), networkHandler.getClientUpdate());

				networkHandler.setServerUpdate(null);

			}
		} catch (IOException e) {
			terminate();
		}

	}

	private Update readEntityUpdate() throws IOException {
		int update = inputStream.readInt();
		int id = inputStream.readInt();
		int uid = inputStream.readInt();

		if (update != -1 && id != -1) {
			networkHandler.dealWithUpdate(update, id, uid);
			return new Update(update, id, uid);

		} else {
			System.out.println("Nothing to update");
			return null;
		}

	}

	private void sendUpdateEntity(Update serverUpdate, Update clientUpdate) throws IOException {
		if (serverUpdate != null) {
			outputStream.writeInt(serverUpdate.update);
			outputStream.writeInt(serverUpdate.id);
			outputStream.writeInt(0);
		} else if (clientUpdate != null) {
			outputStream.writeInt(clientUpdate.update);
			outputStream.writeInt(clientUpdate.id);
			outputStream.writeInt(0);
		} else {
			outputStream.writeInt(-1);
			outputStream.writeInt(-1);
			outputStream.writeInt(-1);
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
		// gameController.removePlayer(uid);
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
		this.networkHandler.setServerUpdate(new Update(status, entity.getUID(), 0));
	}

	public void initNewPlayer() throws IOException {
		int inventorySize = gameController.getGameWorld().getInventory().getItems().size();

		outputStream.writeInt(inventorySize);
		for (LaptopItem entity : gameController.getGameWorld().getInventory().getItems()) {
			outputStream.writeInt(entity.getUID());
		}

		int swipeSize = gameController.getGameWorld().getSwipeCards().size();
		outputStream.writeInt(swipeSize);
		for (SwipeCard swipeCard : gameController.getGameWorld().getSwipeCards()) {
			outputStream.writeInt(swipeCard.getUID());
		}

		outputStream.writeInt(gameController.getGameWorld().getPatchProgress());

	}

}