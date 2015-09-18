package model.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import model.entities.movableEntity.Player;

public class Server extends Thread {

	private Socket socket;

	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private ArrayList<Player> players;

	public Server(Socket socket, ArrayList<Player> players) {
		this.socket = socket;
		this.players = players;
	}

	public void run() {
		try {

			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());

			float[] array = new float[3];

			while (1 == 1) {

				int uid = inputStream.readInt();

				for (int i = 0; i < array.length; i++) {
					array[i] = inputStream.readFloat();
				}

				updatePlayer(uid, array);

			}
			// server.close();
			// socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updatePlayer(int playerID, float[] packet) {
		players.get(playerID).setPosition(new Vector3f(packet[0], packet[1], packet[2]));
	}

}