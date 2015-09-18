package model.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import model.entities.movableEntity.Player;

public class Client extends Thread {

	private final Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private Player player;
	private float[] lastPacket;

	public Client(Socket socket, Player player) {
		this.socket = socket;
		this.player = player;
		lastPacket = new float[3];
	}

	public void run() {
		try {

			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

			while (1 == 1) {

				output.writeInt(player.getUid());

				sendLocation(player);
				for (float i : lastPacket) {
					output.writeFloat(i);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendLocation(Player player) {

		lastPacket[0] = player.getPosition().getX();
		lastPacket[1] = player.getPosition().getY();
		lastPacket[2] = player.getPosition().getZ();

	}

	// public static void main(String[] args) {
	// int port = 32768; // default
	// Socket sock = null;
	// try {
	// // host name and
	// System.out.println("DONE");
	// sock = new Socket("130.195.6.51", port);
	// System.out.println("Connected");
	// } catch (UnknownHostException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// new Client(sock).start();
	//
	// }

}