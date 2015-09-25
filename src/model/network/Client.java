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

	public Client(Socket socket, GameController gameController) {
		this.socket = socket;
		this.gameController = gameController;
		initStreams();

	}

	public void run() {

		while (1 == 1) {
			// send information
			sendPlayerID();
			sendPlayerLocation(gameController.getPlayer());

			// receive information
			int size = readNumberOfPlayers();

			for (int i = 0; i < size; i++) {
				int playerID = readPlayerID();
				float[] position = readPlayerPosition();
				// if the player id received is a different player, update it's
				// position accordingly
				if (playerID != gameController.getPlayer().getUid()) {
					updatePlayer(playerID, position);
				}
			}

		}

	}

	private int readNumberOfPlayers() {
		try {
			return inputStream.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return 0;
		}
	}

	private void sendPlayerID() {

		try {
			outputStream.writeInt(this.uid);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private float[] readPlayerPosition() {
		float[] position = new float[3];
		try {
			position[0] = inputStream.readFloat();
			position[1] = inputStream.readFloat();
			position[2] = inputStream.readFloat();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return position;
	}

	private void initStreams() {
		try {
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void updatePlayer(int playerID, float[] packet) {
		if (gameController.getPlayers().containsKey(playerID)) {
			gameController.getPlayerWithID(playerID).setPosition(new Vector3f(packet[0], packet[1], packet[2]));
		} else {
			gameController.createPlayer(playerID);
		}
	}

	public void sendPlayerLocation(Player player) {
		try {
			outputStream.writeFloat(player.getPosition().getX());
			outputStream.writeFloat(player.getPosition().getY());
			outputStream.writeFloat(player.getPosition().getZ());

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public int readPlayerID() {
		try {
			return inputStream.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return -1;
		}
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

}