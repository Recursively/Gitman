package model.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import controller.GameController;
import model.entities.movableEntity.Player;

public class Server extends Thread {

	private Socket socket;

	private GameController gameController;

	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	public Server(Socket socket, GameController gameController) throws IOException {
		this.socket = socket;
		this.gameController = gameController;
		initStreams();
	}

	public void run() {
		try {
			float[] array = new float[3];

			while (1 == 1) {

				// receive the player number
				int uid = inputStream.readInt();
				for (int i = 0; i < array.length; i++) {
					// receive the players coordinates
					array[i] = inputStream.readFloat();
				}
				// update that players coordinates accordingly
				updatePlayer(uid, array);

				// send the number of players
				sendGameInfo();
				
				// send all the other players information
				for (Player player : gameController.getPlayers()) {
					sendPlayerPosition(player);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initStreams() throws IOException {
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
	}

	private void sendPlayerPosition(Player player) throws IOException {
		outputStream.writeInt(player.getUid());
		outputStream.writeFloat(player.getPosition().x);
		outputStream.writeFloat(player.getPosition().y);
		outputStream.writeFloat(player.getPosition().z);
	}

	public void updatePlayer(int playerID, float[] packet) {
		gameController.getPlayers().get(playerID).setPosition(new Vector3f(packet[0], packet[1], packet[2]));
	}

	public void sendGameInfo() throws IOException {
		outputStream.writeInt(gameController.getPlayers().size());
	}

}