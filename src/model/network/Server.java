package model.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
				// System.out.println("Read:" + uid);
				for (int i = 0; i < array.length; i++) {
					// receive the players coordinates
					array[i] = inputStream.readFloat();
					// System.out.println("Read:" + array[i]);
				}
				// update that players coordinates accordingly
				if (uid != gameController.getPlayer().getUid()) {
					System.out.println("READ UID: " + uid);
					updatePlayer(uid, array);
				}

				// send the number of players
				sendGameSize();

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

		// System.out.println("Wrote: " + player.getUid() + " " +
		// player.getPosition().x + " " + player.getPosition().y
		// + " " + player.getPosition().z);
	}

	public void updatePlayer(int playerID, float[] packet) {
		if (playerID < gameController.getPlayers().size()) {
			gameController.getPlayers().get(playerID).setPosition(new Vector3f(packet[0], packet[1], packet[2]));
		} else {
			gameController.addPlayer(playerID, packet);
		}
	}

	public void sendInfoToNewPlayer() {
		try {
			// send to the player what the new UID will be
			outputStream.writeInt(gameController.getPlayers().size());
			// now add that new player to the servers final arraylist
			gameController.addClientPlayer(gameController.getPlayers().size());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendGameSize() throws IOException {
		outputStream.writeInt(gameController.getPlayers().size());
	}

}