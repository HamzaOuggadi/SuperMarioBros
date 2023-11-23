package net.hamzaouggadi.jaber;

import net.hamzaouggadi.jaber.listeners.KeyListener;
import net.hamzaouggadi.jaber.listeners.MouseListener;
import net.hamzaouggadi.utils.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int height;
    private int width;
    private String title;
    private long glfwWindow;
    private float r, g, b, a;
    private static Scene currentScene;

    private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Super Mario Bros";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static Window get() {
        if (window == null) {
            window = new Window();
        }
        return window;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0 :
                currentScene = new LevelEditorScene();
                // currentScene.init();
                break;
            case 1 :
                currentScene = new LevelScene();
                // currentScene.init();
                break;
            default:
                assert false : "Unknown Scene '" + newScene + "'";
        }
    }

    public void run() {
        System.out.println("Hello LWJGL Version : " + Version.getVersion());

        init();
        loop();

        // Free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to Initialize GLFW !");
        }

        // Configure GLFW Window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create GLFW Window
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create window !");
        }

        // Setting input Callbacks
        // Mouse Listeners
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        // Key Listener
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);


        // Make the OpenGL Context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    public void loop() {

        float beginTime = Time.getTime();
        float endTime = Time.getTime();

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Will be polling events here
            glfwPollEvents();

            // Setting window color to solid red
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
                b = Math.max(b - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
            }

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_1)) {
                System.out.println("Mouse Button Key 1 is pressed, in X position : " + MouseListener.getX());
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            float dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
