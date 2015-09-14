package control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {

	private final Socket socket;
	private DataOutputStream output;
	private DataInputStream input;

	public Client(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {

			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

			Scanner scanner = new Scanner(System.in);

			while (1 == 1) {
				String text = scanner.next();
				output.writeUTF(text);

				if (text.equals("EXIT")) {
					break;
				}

			}

			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int port = 32768; // default
		Socket sock = null;
		try {
			// host name and
			sock = new Socket("localhost", port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Client(sock).start();
		;
	}

}
