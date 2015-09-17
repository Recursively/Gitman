package model.Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private Socket socket;

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	public Server(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {

			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());

			while (1 == 1) {
				String read = inputStream.readUTF();
				System.out.println(socket.getLocalSocketAddress() + ": " + read);
				
				outputStream.writeUTF("HELLO");

				if (read.equals("EXIT")) {
					break;
				}

			}
			// server.close();
			// socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		ServerSocket ss = new ServerSocket(32768);
		while (true) {
			Socket socket = ss.accept();
			new Server(socket).start();

		}
	}

}