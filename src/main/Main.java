package main;

import view.TitleScreen;

// MAIN METHOD
public class Main {
    // ARGS ARE BOOLEAN: IS HOST OF THE GAME, STRING: IPADDRESS TO CONNECT TO

    public static void main(String[] args) {

        if (args.length < 1 || args.length > 2) {
            System.out.println("Invalid number of arguments");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Arguments are: \n   fullscreen (Y/N)\n   ipAddress (xxx.xxx.xxx.x.x)");
        }

        boolean fullscreen = false;

        if (args[0].equalsIgnoreCase("Y") || args[1].equalsIgnoreCase("True")) {
            fullscreen = true;
        }

        boolean isHost = args.length == 1;

        if (!isHost) {
            String ipAddress = args[2];
            new TitleScreen(false, ipAddress, fullscreen);
        } else {
            new TitleScreen(true, "", fullscreen);
        }
    }
}

