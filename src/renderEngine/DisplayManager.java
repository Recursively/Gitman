package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class DisplayManager {

    private static int WIDTH = 1280;
    private static int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3,2)
                .withForwardCompatible(true).withProfileCore(true);

        DisplayMode[] modes = new DisplayMode[0];
        try {
            modes = Display.getAvailableDisplayModes();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        DisplayMode current = modes[0];

        for (int i=0;i<modes.length;i++) {
            current = modes[i];
            System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
                    current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
        }

        WIDTH = current.getWidth();
        HEIGHT = current.getHeight();

        try {
            Display.setDisplayMode(current);
            Display.setFullscreen(true);
            Display.create(new PixelFormat(), attribs);
//            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
//            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Our first display");

            Display.setVSyncEnabled(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
    }

    public static void closeDisplay() {
        Display.destroy();
    }
}
