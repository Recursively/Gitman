package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.Network.Server;
import model.entities.movableEntity.Player;

public class NetworkController extends Thread {

	private ServerSocket ss;
	private ArrayList<Player> players;

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
			new Server(socket, players).start();

		}
	}
}
