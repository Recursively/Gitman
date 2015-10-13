package main;

import view.TitleScreen;

/**
 * Main method delegate class
 *
 * @author Marcel van Workum
 */
public class Main {

    /**
     * Create the game
     *
     * @param args FULLSCREEN (Y/N) IPADDRESS(XXX.XXX.XXX.X.X)
     */
    public static void main(String[] args) {

        // Checks that the args are valid
        if (args.length < 1 || args.length > 2) {
            System.out.println("Invalid number of arguments");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Arguments are: \n   fullscreen (Y/N)\n   ipAddress (xxx.xxx.xxx.x.x)");

        } else {

            // Parse the full screen args
            boolean fullscreen = false;
            if (args[0].equalsIgnoreCase("Y") || args[0].equalsIgnoreCase("True")) {
                fullscreen = true;
            }

            // checks if the player tried to enter an ip address
            boolean isHost = args.length == 1;

            // Creates the correct game client type
            if (!isHost) {
                String ipAddress = args[1];
                new TitleScreen(false, ipAddress, fullscreen);
            } else {
                new TitleScreen(true, "", fullscreen);
            }
        }
        
    }
}

