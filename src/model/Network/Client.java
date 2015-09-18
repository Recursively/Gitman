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
		lastPacket = new float[6];
	}

	public void run() {
		try {

			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

			while (1 == 1) {
				sendLocation(player);
				for (float i : lastPacket) {
					output.writeFloat(i);
				}
				sleep(100);

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendLocation(Player player) {

		lastPacket[0] = player.getPosition().getX();
		lastPacket[1] = player.getPosition().getY();
		lastPacket[2] = player.getPosition().getZ();
		lastPacket[3] = player.getRotX();
		lastPacket[4] = player.getRotY();
		lastPacket[5] = player.getRotZ();

	}
	//
	// public static void main(String[] args) {
	// int port = 32768; // default
	// Socket sock = null;
	// try {
	// // host name and
	// System.out.println("DONE");
	// sock = new Socket("localhost", port);
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