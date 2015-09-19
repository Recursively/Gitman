package model.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector3f;

import controller.GameController;
import model.entities.movableEntity.Player;

public class Client extends Thread {

	private final Socket socket;
	private DataOutputStream output;
	private DataInputStream input;

	private GameController gameController;

	public Client(Socket socket, GameController gameController) {
		this.socket = socket;
		initStreams();

	}

	public void run() {
		try {

			while (1 == 1) {
				// send which player this is
				output.writeInt(gameController.getPlayer().getUid());
				// send the players location
				sendLocation(gameController.getPlayer());

				// receieve how many other players there are
				int length = requestPlayersLength();

				// receive the other players
				for (int i = 0; i < length; i++) {
					float[] packet = new float[3];
					// read the player number
					int playerID = input.readInt();
					for (int f = 0; f < packet.length; f++) {
						// read the coordinates
						packet[f] = input.readFloat();
					}
					// finally update the player
					updatePlayer(playerID, packet);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initStreams() {
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updatePlayer(int playerID, float[] packet) {
		// if this player exists then update it
		if (gameController.getPlayers().get(playerID) != null) {
			gameController.getPlayers().get(playerID).setPosition(new Vector3f(packet[0], packet[1], packet[2]));
		} 
		// if the player doesn't exits create it
		else {
			gameController.addPlayerClient(playerID, packet);
		}
	}

	public void sendLocation(Player player) {
		try {
			output.writeFloat(player.getPosition().getX());
			output.writeFloat(player.getPosition().getY());
			output.writeFloat(player.getPosition().getZ());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int requestPlayersLength() throws IOException {
		return input.readInt();
	}

}