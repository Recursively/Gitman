package control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private Socket socket;

	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	public Server(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {

			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());

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
