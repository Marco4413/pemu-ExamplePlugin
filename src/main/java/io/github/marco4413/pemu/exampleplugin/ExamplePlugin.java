package io.github.marco4413.pemu.exampleplugin;

import io.github.marco4413.pemu.application.Application;
import io.github.marco4413.pemu.config.ConfigEvent;
import io.github.marco4413.pemu.config.ConfigManager;
import io.github.marco4413.pemu.config.IConfigurable;
import io.github.marco4413.pemu.console.Console;
import io.github.marco4413.pemu.instructions.Instructions;
import io.github.marco4413.pemu.localization.ITranslatable;
import io.github.marco4413.pemu.localization.Translation;
import io.github.marco4413.pemu.localization.TranslationManager;
import io.github.marco4413.pemu.plugins.AbstractPlugin;
import io.github.marco4413.pemu.plugins.Plugin;
import io.github.marco4413.pemu.plugins.PluginUtils;
import io.github.marco4413.pemu.processor.IDummyProcessor;
import io.github.marco4413.pemu.processor.IProcessor;
import io.github.marco4413.pemu.processor.Processor;
import io.github.marco4413.pemu.processor.ProcessorConfig;
import io.github.marco4413.pemu.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@Plugin(
    // The Naming Convention for IDs is 'packages.to.plugin.class:PluginClassName'
    id = "io.github.marco4413.pemu:ExamplePlugin",
    // This is the name of the Plugin, note that this is used by the PluginUtils class
    //  to search Plugin resources so don't use special characters here, only spaces or dashes
    name = "Example Plugin",
    // Pretty simple, higher the number, higher the version :)
    version = "1.0"
)
public class ExamplePlugin extends AbstractPlugin implements ITranslatable, IConfigurable {
    private boolean isLoaded = false;
    private int loadTimes = 0;
    private Translation translation;

    public ExamplePlugin() { super(); }

    private static ExamplePlugin instance;
    public static @NotNull ExamplePlugin getInstance() {
        if (instance == null)
            throw new NullPointerException("Trying to get a plugin which has not yet been registered.");
        return instance;
    }

    @Override
    public @Nullable IProcessor onCreateProcessor(@NotNull ProcessorConfig config) {
        // Here the InstructionSet can be a custom one, using the default one for example purposes
        return new Processor(config, Instructions.SET);
    }

    @Override
    public @Nullable IDummyProcessor onCreateDummyProcessor(@NotNull ProcessorConfig config) {
        return Processor.getDummyProcessor(config, Instructions.SET);
    }

    private JMenu M_PLUGIN_MENU = null;
    private JCheckBoxMenuItem CB_TOGGLE_VERBOSE = null;
    private JMenuItem I_LOAD_TIMES = null;
    private boolean verboseLoad = true;

    @Override
    public @Nullable String onRegister() {
        instance = this;

        // Adding Config and Translation Listeners
        ConfigManager.addConfigListener(this);
        TranslationManager.addTranslationListener(this);

        // Doing Constructor logic here because this is the first method called on the Plugin
        assert getName() != null;
        M_PLUGIN_MENU = new JMenu(getName());
        M_PLUGIN_MENU.setMnemonic(getName().charAt(0));

        CB_TOGGLE_VERBOSE = new JCheckBoxMenuItem();
        CB_TOGGLE_VERBOSE.addActionListener(e -> verboseLoad = CB_TOGGLE_VERBOSE.getState());
        M_PLUGIN_MENU.add(CB_TOGGLE_VERBOSE);

        I_LOAD_TIMES = new JMenuItem();
        I_LOAD_TIMES.setEnabled(false);
        M_PLUGIN_MENU.add(I_LOAD_TIMES);

        // Calling updateTranslations here because we're not sure if it gets called on register
        //  and if no valid Translation is provided by the Config this doesn't get called
        updateTranslations(TranslationManager.getCurrentTranslation());

        return null;
    }

    @Override
    public void onLoad() {
        // Keeping track of the Plugin State
        isLoaded = true;
        // Adding the Plugin's menu to the Application's Menu Bar only if it's not headless
        // The headless check should be used otherwise Application#getGUI throws
        Application app = Application.getInstance();
        if (!app.isHeadless())
            app.getGUI().MENU_BAR.add(M_PLUGIN_MENU);

        // +1 to Load Times and updating menu entry
        loadTimes++;
        translation.translateComponent("examplePlugin.menu.loaded", I_LOAD_TIMES, loadTimes);

        // If Verbose Loading is active then Log the Load Times
        if (verboseLoad) {
            Console.Debug.println(StringUtils.format(
                    translation.getOrDefault("examplePlugin.loaded"), loadTimes
            ));
        }
    }

    @Override
    public void onUnload() {
        // Keeping track of the Plugin State
        isLoaded = false;
        // Removing the Plugin's menu to the Application's Menu Bar
        Application app = Application.getInstance();
        if (!app.isHeadless())
            app.getGUI().MENU_BAR.add(M_PLUGIN_MENU);

        if (verboseLoad) {
            Console.Debug.println(translation.getOrDefault("examplePlugin.unloaded"));
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void loadConfig(@NotNull ConfigEvent e) {
        // This method is called when loading config,
        //  if something here throws the config will be set to the default one
        loadTimes = e.config.getOrDefault(Integer.class, "examplePlugin.loadTimes", 0);
        verboseLoad = e.config.getOrDefault(Boolean.class, "examplePlugin.verbose", false);
        CB_TOGGLE_VERBOSE.setState(verboseLoad);
    }

    @Override
    public void saveConfig(@NotNull ConfigEvent e) {
        // This method is called when saving config
        e.config.put("examplePlugin.loadTimes", loadTimes);
        e.config.put("examplePlugin.verbose", verboseLoad);
    }

    @Override
    public void setDefaults(@NotNull ConfigEvent e) {
        // This method is used to set plugin defaults in case of an error
        e.config.put("examplePlugin.loadTimes", 0);
        e.config.put("examplePlugin.verbose", false);
    }

    @Override
    public void updateTranslations(@NotNull Translation newTranslation) {
        // Translating Plugin
        // NOTE: If you've translated your plugin for all languages available
        //  with PEMU you can use this method instead:
        //   PluginUtils.getPluginTranslation(this, newTranslation.getShortName())
        //  The method above loads the same locale as the one of the new translation
        translation = PluginUtils.getPluginTranslation(this);
        translation.translateComponent("examplePlugin.menu.verbose", CB_TOGGLE_VERBOSE);
        translation.translateComponent("examplePlugin.menu.loaded", I_LOAD_TIMES, loadTimes);
    }

    @Override
    public @NotNull String toString() {
        // This is called to get the name of the Plugin that will be shown on the Plugin Selection List
        if (isLoaded)
            return StringUtils.format(translation.getOrDefault("examplePlugin.active"), super.toString());
        return super.toString();
    }
}
