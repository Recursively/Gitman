package model.network;

import controller.GameController;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;
import org.lwjgl.util.vector.Vector3f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * 
 * Client Thread that communicates with the Server Thread that delegates to the
 * GameController and the NetworkHandler
 * 
 * @author Reuben Puketapu
 *
 */

public class Client extends Thread {

	private NetworkHandler networkHandler;

	private final Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	private GameController gameController;
	private int uid;
	
	public boolean running;

	/**
	 * Constructor for the Client
	 * 
	 * @param socket
	 *            the socket to bind the streams to
	 * @param gameController
	 *            gameController
	 */
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
					// it's position accordingly
					if (playerID != gameController.getPlayer().getUID()) {
						updatePlayerPosition(playerID, position);
					}
					receivedPlayers.add(playerID);
				}
				// check if the players received are the same as the current
				// list
				checkForRemovedPlayers(receivedPlayers);

				// send an update if there is one
				sendUpdateEntity();

				// read an update if there is one
				readUpdateEntitiy();

			}
		} catch (IOException e) {
			terminate();
		}

	}

	/**
	 * Checks that there aren't any players that don't exist anymore Removes the
	 * player if it doesn't exist according to the server
	 * 
	 * @param receivedPlayers
	 *            the list of Player UID's
	 */
	private void checkForRemovedPlayers(ArrayList<Integer> receivedPlayers) {
		int removeID = -1;
		for (Integer index : gameController.getPlayers().keySet()) {
			if (!receivedPlayers.contains(index)) {
				removeID = index;
			}
		}

		// if there is a PlayerID that doesn't exist in the received list
		// remove them from this Client's list
		if (removeID != -1) {
			gameController.removePlayer(removeID);
		}
	}

	/**
	 * Reads the update from the Server, if there is an update the update and id
	 * fields will return a -1 value
	 * 
	 * @throws IOException
	 */
	private void readUpdateEntitiy() throws IOException {

		int update = inputStream.readInt();
		int id = inputStream.readInt();
		int uid = inputStream.readInt();
		
		// deal with an update if there is a valid input
		if (update != -1 && id != -1) {
			networkHandler.dealWithUpdate(update, id, uid);
		}

	}

	/**
	 * If the update ClientUpdate field if Network handler has an update to be
	 * sent then write the correct int's to the output stream, otherwise write
	 * -1
	 * 
	 * @throws IOException
	 */
	private void sendUpdateEntity() throws IOException {
		// if there is a update made by the client send it accross the network
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

	/**
	 * Reads the number of Players in the game from the input stream
	 * 
	 * @return the number of players
	 * @throws IOException
	 */
	private int readNumberOfPlayers() throws IOException {
		return inputStream.readInt();
	}

	/**
	 * Sends the ID of this player / Client
	 * 
	 * @throws IOException
	 */
	private void sendPlayerID() throws IOException {
		outputStream.writeInt(this.uid);
	}

	/**
	 * Reads the position from a player given the x,y,z coordinates
	 * 
	 * @return returns a float array of the x,y,z coordinates
	 * @throws IOException
	 */
	private float[] readPlayerPosition() throws IOException {
		float[] position = new float[3];
		position[0] = inputStream.readFloat();
		position[1] = inputStream.readFloat();
		position[2] = inputStream.readFloat();

		return position;
	}

	/**
	 * Initializes the streams from the socket
	 */
	private void initStreams() {
		try {
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the NetworkHandler
	 */
	private void initNetworkHandler() {
		this.networkHandler = new NetworkHandler(gameController.getGameWorld());
	}

	/**
	 * Terminates the thread and ensures that all the Streams are closed, and
	 * sends a message to the GameController to end
	 */
	public void terminate() {
		System.out.println("THE SERVER HAS BEEN DISCONNECTED");
		running = false;
		GameController.NETWORK_DISCONNECTED = true;
		try {
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the player's position, and creates a new player if that player
	 * doesn't exist
	 * 
	 * @param playerID
	 *            ID of the player
	 * @param packet
	 *            array of floats (x,y,z)
	 */
	public void updatePlayerPosition(int playerID, float[] packet) {
		if (gameController.getPlayers().containsKey(playerID)) {
			gameController.getPlayerWithID(playerID).setPosition(new Vector3f(packet[0], packet[1], packet[2]));
		} else {
			gameController.createPlayer(playerID);
		}
	}

	/**
	 * Sends the location of this player / Client Sends y at + 10 to ensure a
	 * 'floating' player
	 * 
	 * @param player
	 *            this player/Client
	 * @throws IOException
	 */
	public void sendPlayerLocation(Player player) throws IOException {
		outputStream.writeFloat(player.getPosition().getX());
		outputStream.writeFloat(player.getPosition().getY() + 10);
		outputStream.writeFloat(player.getPosition().getZ());

	}

	/**
	 * Reads the ID of the player
	 * 
	 * @return the Player ID
	 * @throws IOException
	 */
	public int readPlayerID() throws IOException {
		return inputStream.readInt();
	}

	/**
	 * Sets the ID
	 * 
	 * @param uid
	 *            ID of this player
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * Called when this player pushes a button, sets the update to be sent
	 * accross the Network
	 * 
	 * @param updateType
	 *            type of update
	 * @param entity
	 *            Entitiy to be updated
	 */
	public void setUpdate(int updateType, MovableEntity entity) {
		networkHandler.setClientUpdate(new Update(updateType, entity.getUID(), uid));
	}

	/**
	 * When this Client connects receive all the information that has been
	 * already interacted with
	 * 
	 * @throws IOException
	 */
	public void updateGameInformation() throws IOException {
		
		// read the swipeCards that have been interacted with
		int swipeSize = inputStream.readInt();
		for (int i = 0; i < swipeSize; i++) {
			int id = inputStream.readInt();
			gameController.getGameWorld().getMoveableEntities().get(id).interact(gameController.getGameWorld());
		}

		// read the inventory items that have been interacted with
		int inventorySize = inputStream.readInt();
		for (int i = 0; i < inventorySize; i++) {
			int id = inputStream.readInt();
			gameController.getGameWorld().getMoveableEntities().get(id).interact(gameController.getGameWorld());
		}
		
		// read the laptop items that have been interacted with
		int laptopSize = inputStream.readInt();
		for (int i = 0; i < laptopSize; i++) {
			int id = inputStream.readInt();
			gameController.getGameWorld().getMoveableEntities().get(id).interact(gameController.getGameWorld());
		}
		
		// read the commits that have been interacted with
		int commitSize = inputStream.readInt();
		for(int i = 0; i < commitSize; i++){
			int id = inputStream.readInt();
			gameController.getGameWorld().getMoveableEntities().get(id).interact(gameController.getGameWorld());
		}

		int patchProgress = inputStream.readInt();
		gameController.getGameWorld().setProgress(patchProgress);

	}

	public void readIsCommitCollected() throws IOException{
		
		if(inputStream.readInt() == 1) throw new IOException();
		
		
	}

}