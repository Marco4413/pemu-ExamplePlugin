package io.github.marco4413.pemu.exampleplugin;

import io.github.marco4413.pemu.plugins.PluginManager;

public class Main {
    public static void main(String[] args) {
        // The main function is only used to test the Plugin it won't be called by PEMU
        // If you see an error of duplicate plugin it's not an issue because it won't happen when installed properly
        // Registering the Plugin
        PluginManager.queueForRegister(new ExamplePlugin());
        // Calling PEMU's main method
        io.github.marco4413.pemu.Main.main(args);
    }
}
