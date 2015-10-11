package model.network;

import controller.GameController;
import model.entities.movableEntity.LaptopItem;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import org.lwjgl.util.vector.Vector3f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {

	private NetworkHandler networkHandler;

	private final Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	private GameController gameController;
	private int uid;

	public boolean running;
	private MovableEntity mostRecentEntity;
	private int mostRecentUpdate;

	public Client(Socket socket, GameController gameController) {
		this.socket = socket;
		this.gameController = gameController;
		this.running = true;
		this.mostRecentUpdate = -1;
		this.mostRecentEntity = null;
		initNetworkHandler();
		initStreams();

	}

	public void run() {
		try {
			while (running) {
				// send information
				sendPlayerID();
				sendPlayerLocation(gameController.getPlayer());

				// receive information
				int size = readNumberOfPlayers();
				ArrayList<Integer> receivedPlayers = new ArrayList<>();

				for (int i = 0; i < size; i++) {
					int playerID = readPlayerID();
					float[] position = readPlayerPosition();
					// if the player id received is a different player, update
					// it's
					// position accordingly
					if (playerID != gameController.getPlayer().getUID()) {
						updatePlayer(playerID, position);
					}
					receivedPlayers.add(playerID);
				}

				checkForRemovedPlayers(receivedPlayers);

				if (sendUpdateStatus() != -1) {
					System.out.println("INTERACTION CLIENT");
					sendUpdateEntity();
				}

				int updateType = checkUpdate();

				if (updateType != -1) {
					System.out.println("RECEIVED AN UPDATE");
					updateEntitiy(updateType);
				}

			}
		} catch (IOException e) {
			terminate();
		}

	}

	private void checkForRemovedPlayers(ArrayList<Integer> receivedPlayers) {
		for (Integer index : gameController.getPlayers().keySet()) {
			if (!receivedPlayers.contains(index)) {
				gameController.removePlayer(index);
			}
		}
	}

	private int updateEntitiy(int updateType) throws IOException {
		int id = inputStream.readInt();
		float x = inputStream.readFloat();
		float y = inputStream.readFloat();
		float z = inputStream.readFloat();

		networkHandler.dealWithUpdate(updateType, id, uid);

		return id;

	}

	private int checkUpdate() throws IOException {
		return inputStream.readInt();
	}

	public void terminate() {
		System.out.println("THE SERVER HAS BEEN DISCONNECTED");
		running = false;
		gameController.networkRunning = false;
		try {
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private int sendUpdateStatus() throws IOException {
		// send that there is an update to be made
		outputStream.writeInt(mostRecentUpdate);
		return mostRecentUpdate;
	}

	private void sendUpdateEntity() throws IOException {
		System.out.println("SENT UPDATE: " + mostRecentUpdate);
		outputStream.writeInt(mostRecentEntity.getUID());
		if (mostRecentUpdate != 8) {
			outputStream.writeFloat(mostRecentEntity.getPosition().getX());
			outputStream.writeFloat(mostRecentEntity.getPosition().getY());
			outputStream.writeFloat(mostRecentEntity.getPosition().getZ());
		} else {
			outputStream.writeFloat(gameController.getPlayer().getPosition().getX());
			outputStream.writeFloat(gameController.getPlayer().getPosition().getY());
			outputStream.writeFloat(gameController.getPlayer().getPosition().getZ());
		}

		// make sure we don't send the update again
		this.mostRecentUpdate = -1;
	}

	private int readNumberOfPlayers() throws IOException {
		return inputStream.readInt();
	}

	private void sendPlayerID() throws IOException {
		outputStream.writeInt(this.uid);
	}

	private float[] readPlayerPosition() throws IOException {
		float[] position = new float[3];
		position[0] = inputStream.readFloat();
		position[1] = inputStream.readFloat();
		position[2] = inputStream.readFloat();

		return position;
	}

	private void initStreams() {
		try {
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initNetworkHandler() {
		this.networkHandler = new NetworkHandler(gameController.getGameWorld());
	}

	public void updatePlayer(int playerID, float[] packet) {
		if (gameController.getPlayers().containsKey(playerID)) {
			gameController.getPlayerWithID(playerID).setPosition(new Vector3f(packet[0], packet[1], packet[2]));
		} else {
			gameController.createPlayer(playerID);
		}
	}

	public void sendPlayerLocation(Player player) throws IOException {
		outputStream.writeFloat(player.getPosition().getX());
		outputStream.writeFloat(player.getPosition().getY() + 10);
		outputStream.writeFloat(player.getPosition().getZ());

	}

	public int readPlayerID() throws IOException {
		return inputStream.readInt();
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public void setUpdate(int updateType, MovableEntity entity) {
		this.mostRecentUpdate = updateType;
		this.mostRecentEntity = entity;
	}

	public void updateGameInformation() throws IOException {
		int inventorySize = inputStream.readInt();

		for (int i = 0; i < inventorySize; i++) {
			gameController.getGameWorld().getMoveableEntities().get(inputStream.readInt())
					.interact(gameController.getGameWorld());
		}

		gameController.getGameWorld().setPatchProgress(inputStream.readInt());
	}

}