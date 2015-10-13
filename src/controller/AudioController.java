package controller;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Static class to handle the audio controls in the game.
 *
 * @author Marcel van Workum
 */
public final class AudioController {

    // resource path to the audio directory
    private static final String audioPath = "audio/";

    private static ArrayList<Audio> commitSounds = new ArrayList<>();
    private static ArrayList<Audio> inventorySounds = new ArrayList<>();
    private static ArrayList<Audio> easterEggSounds = new ArrayList<>();

    private static Audio menuLoop;
    private static Audio gameWonLoop;
    private static Audio officeLoop;
    private static Audio gameWorldLoop;
    private static Audio gameOverSound;
    private static Audio successfulUnlockSound;
    private static Audio unsuccessfulUnlockSound;
    private static Audio portalSound;
    private static Audio portalHum;
    private static Audio pickupSound;
    private static Audio jumpSound;
    private static Audio deleteSound;
    private static Audio coolStuffSound;
    private static Audio easterEggLoop;

    private static Random random = new Random();

    /**
     * Creates the audio controller and parses the audio files
     */
    public AudioController() {
        parseAudioResources();
    }

    /**
     * Play menu loop.
     */
    public static void playMenuLoop() {
        menuLoop.playAsMusic(1f, 1f, true);
    }

    /**
     * Stop menu loop.
     */
    public static void stopMenuLoop() {
        menuLoop.stop();
    }

    /**
     * Play random commit sound.
     */
    public static void playRandomCommitSound() {
        int i = random.nextInt(commitSounds.size());
        commitSounds.get(i).playAsSoundEffect(1f, 1f, false);
    }

    /**
     * Play random inventory sound.
     */
    public static void playRandomInventorySound() {
        int i = random.nextInt(inventorySounds.size());
        inventorySounds.get(i).playAsSoundEffect(1f, 1f, false);
    }

    /**
     * Play random easter egg sound.
     */
    public static void playRandomEasterEggSound() {
        int i = random.nextInt(easterEggSounds.size());
        easterEggSounds.get(i).playAsSoundEffect(1f, 1f, false);
    }

    /**
     * Play portal sound.
     */
    public static void playPortalSound() {
        portalSound.playAsSoundEffect(1f, 1f, false);
    }

    /**
     * Play successful unlock sound.
     */
    public static void playSuccessfulUnlockSound() {
        successfulUnlockSound.playAsSoundEffect(1f, 200f, false);
    }

    /**
     * Play unsuccessful unlock sound.
     */
    public static void playUnsuccessfulUnlockSound() {
        unsuccessfulUnlockSound.playAsSoundEffect(1f, 2f, false);
    }

    /**
     * Play game over sound.
     */
    public static void playGameOverSound() {
        gameOverSound.playAsSoundEffect(1f, 1f, false);
    }

    /**
     * Play pickup sound.
     */
    public static void playPickupSound() {
        pickupSound.playAsSoundEffect(1f, 1f, false);
    }

    /**
     * Play portal hum.
     */
    public static void playPortalHum() {
        portalHum.playAsSoundEffect(1f, 1f, true);
    }

    /**
     * Stop portal hum.
     */
    public static void stopPortalHum() {
        portalHum.stop();
    }

    /**
     * Play game won loop.
     */
    public static void playGameWonLoop() {
        gameWonLoop.playAsMusic(1f, 1f, true);
    }

    /**
     * Stop game won loop.
     */
    public static void stopGameWonLoop() {
        gameWonLoop.stop();
    }

    /**
     * Play easter egg loop.
     */
    public static void playEasterEggLoop() {
        easterEggLoop.playAsSoundEffect(1f, 1f, true);
    }

    /**
     * Stop easter egg loop.
     */
    public static void stopEasterEggLoop() {
        easterEggLoop.stop();
    }

    /**
     * Play office loop.
     */
    public static void playOfficeLoop() {
        officeLoop.playAsMusic(1f, 1f, true);
    }

    /**
     * Stop office loop.
     */
    public static void stopOfficeLoop() {
        officeLoop.stop();
    }

    /**
     * Play game world loop.
     */
    public static void playGameWorldLoop() {
        gameWorldLoop.playAsMusic(1f, 1f, true);
    }

    /**
     * Stop game world loop.
     */
    public static void stopGameWorldLoop() {
        gameWorldLoop.stop();
    }

    /**
     * Play jump sound.
     */
    public static void playJumpSound() {
        jumpSound.playAsSoundEffect(1f, 1f, false);
    }

    /**
     * Play delete sound.
     */
    public static void playDeleteSound() {
        deleteSound.playAsSoundEffect(1f, 1f, false);
    }

    /**
     * Play cool stuff sound.
     */
    public static void playCoolStuffSound() {
        coolStuffSound.playAsSoundEffect(1f, 1f, false);
    }

    /**
     * Attempts to parse the audio files
     */
    private void parseAudioResources() {
        try {
            menuLoop = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "menuLoop" + ".ogg"));
            gameWonLoop = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "gameWonLoop" + ".ogg"));
            officeLoop = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "officeLoop" + ".ogg"));
            gameWorldLoop = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "gameWorldLoop" + ".ogg"));
            gameOverSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "gameOverSound" + ".ogg"));
            successfulUnlockSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "successfulUnlockSound" + ".ogg"));
            unsuccessfulUnlockSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "unsuccessfulUnlockSound" + ".ogg"));
            portalSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "portalSound" + ".ogg"));
            portalHum = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "portalHumSound" + ".ogg"));
            pickupSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "pickupSound" + ".ogg"));
            easterEggLoop = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "easterEggLoop" + ".ogg"));
            jumpSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "jumpSound" + ".ogg"));
            deleteSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "deleteSound" + ".ogg"));
            coolStuffSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "coolStuffSound" + ".ogg"));
            inventorySounds.add(AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "helpSound" + ".ogg")));
            inventorySounds.add(AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "hmmmSound" + ".ogg")));
            inventorySounds.add(AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "interestingSound" + ".ogg")));
            easterEggSounds.add(AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "anyQuestionsSound" + ".ogg")));
            easterEggSounds.add(AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "bestBitSound" + ".ogg")));
            easterEggSounds.add(AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "bestThingSound" + ".ogg")));
            easterEggSounds.add(AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "gonnaBeGoodSound" + ".ogg")));

            for (int i = 1; i < 6; i++) {
                commitSounds.add(AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/" + audioPath + "commitSound" + i + ".ogg")));
            }

        } catch (IOException e) {
            System.out.println("Failed to load audio assets.");
        }
    }
}
