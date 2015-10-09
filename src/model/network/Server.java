package model.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.lwjgl.util.vector.Vector3f;

import com.sun.xml.internal.bind.v2.model.core.ID;

import controller.GameController;
import controller.ServerController;
import model.entities.Entity;
import model.entities.movableEntity.MovableEntity;
import model.entities.movableEntity.Player;

public class Server extends Thread {

	private Socket socket;

	private GameController gameController;
	
	private NetworkHandler networkHandler;
	
	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	private int uid;
	private int entityID;

	private boolean isRunning;

	public Server(Socket socket, GameController gameController) {
		this.socket = socket;
		this.gameController = gameController;
		this.isRunning = true;
		initStreams();
		initNetworkHandler();
	}

	private void initNetworkHandler() {
		this.networkHandler = new NetworkHandler(gameController.getGameWorld());
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
				
				int updateType = checkUpdate();
				
				if (updateType != -1) {
					entityID = updateEntitiy(updateType);
				}

				if (sendUpdateStatus(updateType) != -1) {
					System.out.println("INTERACTION SERVER");
					sendUpdateEntity(updateType, gameController.getGameWorld().getMoveableEntities().get(entityID));
				}

			}
		} catch (IOException e) {
			terminate();
		}

	}

	private void sendUpdateEntity(int mostRecentUpdate, MovableEntity mostRecentEntity) throws IOException {
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

	}

	private int sendUpdateStatus(int status) throws IOException {
		// send that there is an update to be made
		outputStream.writeInt(status);
		return status;
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

	private int updateEntitiy(int updateType) throws IOException {
		int id = inputStream.readInt();
		float x = inputStream.readFloat();
		float y = inputStream.readFloat();
		float z = inputStream.readFloat();

		networkHandler.dealWithUpdate(updateType, id, x, y, z);
		
		return id;

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

}