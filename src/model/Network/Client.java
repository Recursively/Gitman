package model.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import model.entities.movableEntity.Player;

public class Client extends Thread {

	private final Socket socket;
	private DataOutputStream output;
	private DataInputStream input;

	private ArrayList<Player> players;
	private float[] lastPacket;

	public Client(Socket socket, ArrayList<Player> players) {
		this.socket = socket;
		this.players = players;
		lastPacket = new float[3];
		initStreams();

	}

	public void run() {
		try {

			while (1 == 1) {

				output.writeInt(0);

				sendLocation(players.get(0));
				
				for (float i : lastPacket) {
					output.writeFloat(i);
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

	public void sendLocation(Player player) {

		lastPacket[0] = player.getPosition().getX();
		lastPacket[1] = player.getPosition().getY();
		lastPacket[2] = player.getPosition().getZ();

	}

	public int receivePlayersLength() throws IOException {
		return input.readInt();
	}


}