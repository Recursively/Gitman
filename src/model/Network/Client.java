package model.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector3f;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import controller.GameController;
import model.entities.movableEntity.Player;

public class Client extends Thread {

	private final Socket socket;
	private DataOutputStream output;
	private DataInputStream input;

	private GameController gameController;

	public Client(Socket socket, GameController gameController) {
		this.socket = socket;
		this.gameController = gameController;
		initStreams();

	}

	public void run() {
		try {
			System.out.println("Client ID: " + gameController.getPlayer().getUid());
			while (1 == 1) {
				// send which player this is
				output.writeInt(gameController.getPlayer().getUid());
				// System.out.println("Wrote :" +
				// gameController.getPlayer().getUid());
				// send the players location
				sendLocation(gameController.getPlayer());

				// receieve how many other players there are
				int length = requestPlayersLength();

				// System.out.println("Read players: " + length);
				// receive the other players
				for (int i = 0; i < length; i++) {
					float[] packet = new float[3];
					// read the player number
					int playerID = input.readInt();
					// System.out.println("Read: " + playerID);
					for (int f = 0; f < packet.length; f++) {
						// read the coordinates
						packet[f] = input.readFloat();
						// System.out.println("Read: " + packet[f]);
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

		if (!gameController.getPlayers().isEmpty() && gameController.getPlayers().size() > playerID) {
			gameController.getPlayers().get(playerID).setPosition(new Vector3f(packet[0], packet[1], packet[2]));
		}
		// if the player doesn't exits create it
		else {
			System.out.println("*******************************");
			gameController.addPlayerClient(playerID, packet);
		}
	}

	public void sendLocation(Player player) {
		try {
			output.writeFloat(player.getPosition().getX());
			output.writeFloat(player.getPosition().getY());
			output.writeFloat(player.getPosition().getZ());

			// System.out.println(
			// "Sent: " + player.getPosition().x + " " + player.getPosition().y
			// + " " + player.getPosition().z);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int requestPlayersLength() throws IOException {
		int length = input.readInt();
		// System.out.println("Read: " + length);
		return length;
	}

}