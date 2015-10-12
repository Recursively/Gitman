package main;
import view.TitleScreen;

public class ServerMain {

	// ARGS ARE BOOLEAN: IS HOST OF THE GAME, STRING: IPADDRESS TO CONNECT TO
	public static void main(String[] args) {
		boolean fullscreen = true;
		new TitleScreen(true, "", fullscreen);
	}

}
