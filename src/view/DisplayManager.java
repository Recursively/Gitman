package view;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * Display management class used to handle the resolution and FPS of the game.
 * <p/>
 * Arguably this class could be in the model, however it fits nicely here and the resolution
 * and fps values are static for the time being
 *
 * @author Marcel van Workum - 300313949
 */
public class DisplayManager {

    /**
     * Whether the Display should use VSync to smooth out vertical tearing
     */
    public static final boolean VSYNC_ENABLED = true;

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
     * <p/>
     * And fullscreen
     *
     * @param fullscreen the fullscreen
     */
    public static void createDisplay(boolean fullscreen) {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true).withProfileCore(true);

        try {
            if (fullscreen) {

                // Parse the display mode
                DisplayMode current = getDisplayMode();

                // Gets width and height
                WIDTH = current.getWidth();
                HEIGHT = current.getHeight();

                // Create full screen display
                Display.setDisplayMode(current);
                Display.setFullscreen(true);
                Display.create(new PixelFormat(), attribs);
            } else {
                // Create windowed display
                Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
                Display.create(new PixelFormat(), attribs);
            }

            Display.setTitle(GAME_TITLE);

            // For smoothness
            Display.setVSyncEnabled(VSYNC_ENABLED);
        } catch (LWJGLException e) {
            System.err.println("Failed to create GL Display");
        }

        // Initialise the viewport
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    /**
     * Parses the display mode.
     * <p/>
     * Tries to get a 720p display, otherwise it will default to lowest res possible
     *
     * @return DisplayMode parsed
     */
    private static DisplayMode getDisplayMode() {
        DisplayMode[] modes = new DisplayMode[0];
        try {
            modes = Display.getAvailableDisplayModes();
        } catch (LWJGLException e) {
            System.err.println("Failed to get display modes");
        }

        DisplayMode current = modes[0];

        for (DisplayMode mode : modes) {
            current = mode;
        }

        // Try get a 720p display mode
        for (DisplayMode d : modes) {
            if (d.getWidth() == 1280) {
                current = d;
            }
        }
        return current;
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

    // TEST METHODS

    /**
     * Creates a test display to initialise the open gl context
     */
    public static void createTestDisplay() {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true).withProfileCore(true);
        try {
            Display.setDisplayMode(new DisplayMode(0, 0));
            Display.create(new PixelFormat(), attribs);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        GL11.glViewport(0, 0, 1, 1);
    }
}
