package view;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * Display management class used to handle the resolution and FPS of the game.
 *
 * Arguably this class could be in the model, however it fits nicely here and the resolution
 * and fps values are static for the time being
 *
 * //TODO update this if it changes
 *
 * @author Marcel van Workum
 */
public class DisplayManager {

    /**
     * Whether the Display should use VSync to smooth out vertical tearing
     */
    public static boolean VSYNC_ENABLED = true;

    // Game window parameters
    private static int WIDTH = 1280;
    private static int HEIGHT = 720;
    private static final int FPS_CAP = 120;
    private static final String GAME_TITLE = "Gitman: An EXCEPTIONal Adventure";

    // Used to calculate the tick rate of the game and lock to 60 ticks per second
    private static long lastFrameTime;
    private static float delta;

    /**
     * Create a Display window with the specified resolution and fps cap
     *
     * //TODO This will support full screen at some point
     */
    public static void createDisplay() {
        // Use OpenGL 3.2 and make it forward compatible so 3.2+ versions
        // will work as well
        ContextAttribs contextAttribs = new ContextAttribs(3,2)
                .withForwardCompatible(true).withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), contextAttribs);
            Display.setTitle(GAME_TITLE);

            // Enabled VSync by default to smooth vertical tearing
            Display.setVSyncEnabled(VSYNC_ENABLED);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        // Create the viewport and set first tick time
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    /**
     * Tick method for the display which updates the content of the Display window
     */
    public static void updateDisplay() {
        // lock framerate
        Display.sync(FPS_CAP);
        Display.update();

        // Update tick time information
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    /**
     * Attempts to set the resolution of the game to the specified parameters
     *
     * This will not always succeed, as the resolution might not be support full screen for this machine
     *
     * //TODO create a more dynamic way of showing full screen options?
     *
     * @param width Width of Display
     * @param height Height of Display
     * @param fullscreen Whether to attempt to make the Display full screen
     */
    public static void setDisplayMode(int width, int height, boolean fullscreen) {
        // CODE taken from the LWJGL wiki page.

        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width) &&
                (Display.getDisplayMode().getHeight() == height) &&
                (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i=0;i<modes.length;i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                                (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width,height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
        }
    }

    /**
     * Gets the time delta since the last tick
     *
     * @return tick delta
     */
    public static float getFrameTimeSeconds() {
        return delta;
    }

    /**
     * Closes the LWJGL Display window
     */
    public static void closeDisplay() {
        Display.destroy();
    }

    /**
     * Gets the current time in seconds
     *
     * @return time in seconds
     */
    private static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }
}
