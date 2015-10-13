
package model.network;

import controller.GameController;
import model.GameWorld;
import model.entities.movableEntity.Laptop;
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

	public Client(Socket socket, GameController gameController) {
		this.socket = socket;
		this.gameController = gameController;
		this.running = true;
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

				sendUpdateEntity();

				readUpdateEntitiy();

			}
		} catch (IOException e) {
			terminate();
		}

	}

	private void checkForRemovedPlayers(ArrayList<Integer> receivedPlayers) {
		int removeID = -1;
		for (Integer index : gameController.getPlayers().keySet()) {
			if (!receivedPlayers.contains(index)) {
				removeID = index;
			}
		}

		if (removeID != -1) {
			gameController.removePlayer(removeID);
		}
	}

	private void readUpdateEntitiy() throws IOException {

		int update = inputStream.readInt();
		int id = inputStream.readInt();
		int uid = inputStream.readInt();

		if (update != -1 && id != -1) {
			networkHandler.dealWithUpdate(update, id, uid);
		}

	}

	public void terminate() {
		System.out.println("THE SERVER HAS BEEN DISCONNECTED");
		running = false;
		GameController.networkDisconnected = true;
		try {
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendUpdateEntity() throws IOException {
		if (networkHandler.getClientUpdate() != null) {
			outputStream.writeInt(networkHandler.getClientUpdate().update);
			outputStream.writeInt(networkHandler.getClientUpdate().id);
			outputStream.writeInt(uid);
			this.networkHandler.setClientUpdate(null);
		} else {
			outputStream.writeInt(-1);
			outputStream.writeInt(-1);
			outputStream.writeInt(-1);
		}
		// make sure we don't send the update again
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
		networkHandler.setClientUpdate(new Update(updateType, entity.getUID(), uid));
	}

	public void updateGameInformation() throws IOException {
		
		int swipeSize = inputStream.readInt();
		for (int i = 0; i < swipeSize; i++) {
			int id = inputStream.readInt();
			gameController.getGameWorld().getMoveableEntities().get(id).interact(gameController.getGameWorld());
		}
		
		int inventorySize = inputStream.readInt();
		for (int i = 0; i < inventorySize; i++) {
			int id = inputStream.readInt();
			gameController.getGameWorld().getMoveableEntities().get(id).interact(gameController.getGameWorld());
		}
		
		int laptopSize = inputStream.readInt();
		for(int i = 0 ; i < laptopSize; i++){
			int id = inputStream.readInt();
			gameController.getGameWorld().getMoveableEntities().get(id).interact(gameController.getGameWorld());
		}

		int patchProgress = inputStream.readInt();
		gameController.getGameWorld().setProgress(patchProgress);

		System.out.println("Patch: " + patchProgress);

	}
}