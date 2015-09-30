package model.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.crypto.spec.PSource;

import org.lwjgl.util.vector.Vector3f;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import controller.GameController;
import model.entities.movableEntity.Player;

public class Client extends Thread {

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

				for (int i = 0; i < size; i++) {
					int playerID = readPlayerID();
					float[] position = readPlayerPosition();
					// if the player id received is a different player, update
					// it's
					// position accordingly
					if (playerID != gameController.getPlayer().getUid()) {
						updatePlayer(playerID, position);
					}
				}

			}
		} catch (IOException e) {
			terminate();
		}

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

	public void updatePlayer(int playerID, float[] packet) {
		if (gameController.getPlayers().containsKey(playerID)) {
			gameController.getPlayerWithID(playerID).setPosition(new Vector3f(packet[0], packet[1], packet[2]));
		} else {
			gameController.createPlayer(playerID);
		}
	}

	public void sendPlayerLocation(Player player) throws IOException {
		outputStream.writeFloat(player.getPosition().getX());
		outputStream.writeFloat(player.getPosition().getY());
		outputStream.writeFloat(player.getPosition().getZ());

	}

	public int readPlayerID() throws IOException {
		return inputStream.readInt();
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

}