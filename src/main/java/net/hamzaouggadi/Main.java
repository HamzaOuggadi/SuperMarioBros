package net.hamzaouggadi;

import net.hamzaouggadi.jaber.Window;

public class Main {
    public static void main(String[] args) {

        if (StartupHelper.startNewJvmIfRequired()) return;

        Window window = Window.get();
        window.run();


/*        long pid = LibC.getpid();
        System.out.println(System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid));
        System.out.println(System.getProperty("file.separator"));
        System.out.println(ProcessHandle.current().info().command().orElseThrow());
        System.out.println(ManagementFactory.getRuntimeMXBean().getInputArguments());
        System.out.println(System.getProperty("java.class.path"));
        System.out.println(System.getenv("JAVA_MAIN_CLASS_" + pid));

        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("os.arch"));*/
    }
}