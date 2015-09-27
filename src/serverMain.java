import controller.GameController;


public class serverMain {

	// ARGS ARE BOOLEAN: IS HOST OF THE GAME, STRING: IPADDRESS TO CONNECT TO
    public static void main(String[] args) {
        new GameController(true, "");
    }

}
