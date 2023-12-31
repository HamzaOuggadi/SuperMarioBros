package net.hamzaouggadi.jaber.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];



    // Uses Singleton design pattern
    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
            return instance;
        }
        return instance;
    }

    public static void keyCallBack(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keycode) {
        return get().keyPressed[keycode];
    }

}
