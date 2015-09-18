package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import model.Network.Server;
import model.entities.movableEntity.Player;
import model.models.TexturedModel;

public class NetworkController extends Thread {

	private ServerSocket ss;
	private ArrayList<Player> players;
	private TexturedModel playerModel;
	private int count;

	public NetworkController(ServerSocket ss, ArrayList<Player> players) {
		this.ss = ss;
		this.players = players;
	}

	public void run() {
		while (1 == 1) {
			Socket socket = null;
			try {
				socket = ss.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			players.add(new Player(playerModel, new Vector3f(50, 100, -50), 0, 180f, 0, 1, null, count));

			Server server = new Server(socket, players);
			try {
				server.sendPlayersLength();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			server.start();

		}
	}
}
