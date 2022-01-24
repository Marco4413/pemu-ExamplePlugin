package io.github.hds.pemu.exampleplugin;

import io.github.hds.pemu.plugins.PluginManager;

public class Main {
    public static void main(String[] args) {
        // The main function is only used to test the Plugin it won't be called by PEMU
        // If you see an error of duplicate plugin it's not an issue because it won't happen when installed properly
        // Registering the Plugin
        PluginManager.queueForRegister(ExamplePlugin.getInstance());
        // Calling PEMU's main method
        io.github.hds.pemu.Main.main(args);
    }
}
