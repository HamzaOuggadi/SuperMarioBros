package net.hamzaouggadi;

import net.hamzaouggadi.jaber.Window;

public class Main {
    public static void main(String[] args) {

        if (StartupHelper.startNewJvmIfRequired()) return;

        Window window = Window.get();
        window.run();

    }
}