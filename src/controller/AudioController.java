package controller;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AudioController {

    private static final String audioPath = "audio/";

    private static Audio menuLoop;
    private static Audio gameWonLoop;
    private static Audio officeLoop;
    private static Audio gameWorldLoop;
    private static Audio gameOverSound;

    private static ArrayList<Audio> commitSounds = new ArrayList<>();
    private static Audio successfulUnlockSound;
    private static Audio unsuccessfulUnlockSound;

    private static Audio portalSound;
    private static Audio portalHum;
    private static Audio pickupSound;

    private static Audio easterEggLoop;

    private static Random random = new Random();

    public AudioController() {
        parseAudioResources();
    }

    private void parseAudioResources() {
        try {

            //TODO add different sounds?
            menuLoop = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "menuLoop" + ".ogg"));
            gameWonLoop = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "gameWonLoop" + ".ogg"));


            officeLoop = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "menuLoop" + ".ogg"));
            gameWorldLoop = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "menuLoop" + ".ogg"));


            gameOverSound = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "gameOverSound" + ".ogg"));
            successfulUnlockSound = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "successfulUnlockSound" + ".ogg"));
            unsuccessfulUnlockSound = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "unsuccessfulUnlockSound" + ".ogg"));
            portalSound = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "portalSound" + ".ogg"));
            portalHum = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "portalHumSound" + ".ogg"));
            pickupSound = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "pickupSound" + ".ogg"));

            easterEggLoop = AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "easterEggLoop" + ".ogg"));

            for (int i = 1; i < 6; i++) {
                commitSounds.add(AudioLoader.getAudio("OGG", new FileInputStream("res/" + audioPath + "commitSound" + i + ".ogg")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playMenuLoop() {
        menuLoop.playAsMusic(1f, 1f, true);
    }

    public static void stopMenuLoop() {
        menuLoop.stop();
    }

    public static void playRandomCommitSound() {
        int i = random.nextInt(5);
        commitSounds.get(i).playAsSoundEffect(1f, 1f, false);
    }

    public static void playPortalSound() {
        portalSound.playAsSoundEffect(1f, 1f, false);
    }

    public static void playSuccessfulUnlockSound() {
        successfulUnlockSound.playAsSoundEffect(1f, 2f, false);
    }

    public static void playUnsuccessfulUnlockSound() {
        unsuccessfulUnlockSound.playAsSoundEffect(1f, 2f, false);
    }

    public static void playGameOverSound() {
        gameOverSound.playAsSoundEffect(1f, 1f, false);
    }

    public static void playPickupSound() {
        pickupSound.playAsSoundEffect(1f, 1f, false);
    }

    public static void playPortalHum() {
        portalHum.playAsSoundEffect(1f, 1f, true);
    }

    public static void stopPortalHum() {
        portalHum.stop();
    }

    public static void playGameWonLoop() {
        gameWonLoop.playAsMusic(1f, 1f, true);
    }

    public static void stopGameWonLoop() {
        gameWonLoop.stop();
    }

    public static void playEasterEggLoop() {
        easterEggLoop.playAsMusic(1f, 1f, true);
    }

    public static void stopEasterEggLoop() {
        easterEggLoop.stop();
    }
}
