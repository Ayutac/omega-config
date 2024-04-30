package draylar.omegaconfiggui;

import draylar.omegaconfig.OmegaConfig;
import draylar.omegaconfig.api.Config;
import draylar.omegaconfiggui.api.screen.OmegaModMenu;
import draylar.omegaconfiggui.api.screen.OmegaScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class OmegaConfigGui {

    private static final Map<Config, OmegaScreenFactory<Screen>> REGISTERED_CONFIGURATIONS = new HashMap<>();
    public static boolean modMenuInitialized = false;

    /**
     * Registers a ModMenu configuration screen for the given {@link Config} instance.
     *
     * @param config registered config to create a ModMenu screen for
     * @param <T>    config type
     */
    public static <T extends Config> void registerConfigScreen(T config) {
        if(FabricLoader.getInstance().isModLoaded("modmenu")) {
            // Ensure the config has a valid modid.
            if(config.getModid() != null) {
                OmegaScreenFactory<Screen> factory = OmegaConfigGui.getConfigScreenFactory(config);

                if(modMenuInitialized) {
                    OmegaModMenu.injectScreen(config, factory);
                } else {
                    REGISTERED_CONFIGURATIONS.put(config, factory);
                }
            } else {
                OmegaConfig.LOGGER.warn(String.format("Skipping config screen registration for '%s' - you must implement getModid() in your config class!", config.getName()));
            }
        }
    }

    /**
     * Returns a factory which provides new Cloth Config Lite {@link Screen} instances for the given {@link Config}.
     *
     * @param config Omega Config instance to create the screen factory for
     * @return a factory which provides new Cloth Config Lite {@link Screen} instances for the given {@link Config}.
     */
    public static OmegaScreenFactory<Screen> getConfigScreenFactory(final Config config) {
        return parent -> {
            try {
                final Config defaultConfig = config.getClass().getDeclaredConstructor().newInstance();
                final ConfigBuilder screen = ConfigBuilder.create()
                        .setParentScreen(parent)
                        .setTitle(Text.translatable(String.format("config.%s.%s", config.getModid(), config.getName())));
                screen.setSavingRunnable(config::save);
                final ConfigCategory general = screen.getOrCreateCategory(Text.translatable(String.format("config.%s.%s.general", config.getModid(), config.getName())));

                // translate fields in the config to entries in the GUI
                for (final Field field : config.getClass().getDeclaredFields()) {
                    final ConfigEntryBuilder entryBuilder = screen.entryBuilder();
                    final Class<?> entryType = field.getType();
                    if (entryType == int.class) {
                        int defaultVal = 0;
                        try {
                            defaultVal = field.getInt(defaultConfig);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        int currentVal = defaultVal;
                        try {
                            currentVal = field.getInt(config);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        general.addEntry(entryBuilder.startIntField(Text.of(field.getName()), currentVal)
                                .setDefaultValue(defaultVal)
                                .setSaveConsumer(newValue -> setConfigValue(config, field, newValue))
                                .build());
                    }
                    if (entryType == long.class) {
                        long defaultVal = 0l;
                        try {
                            defaultVal = field.getLong(defaultConfig);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        long currentVal = defaultVal;
                        try {
                            currentVal = field.getLong(config);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        general.addEntry(entryBuilder.startLongField(Text.of(field.getName()), currentVal)
                                .setDefaultValue(defaultVal)
                                .setSaveConsumer(newValue -> setConfigValue(config, field, newValue))
                                .build());
                    }
                    else if (entryType == double.class) {
                        double defaultVal = 0d;
                        try {
                            defaultVal = field.getDouble(defaultConfig);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        double currentVal = defaultVal;
                        try {
                            currentVal = field.getDouble(config);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        general.addEntry(entryBuilder.startDoubleField(Text.of(field.getName()), currentVal)
                                .setDefaultValue(defaultVal)
                                .setSaveConsumer(newValue -> setConfigValue(config, field, newValue))
                                .build());
                    }
                    else if (entryType == float.class) {
                        float defaultVal = 0f;
                        try {
                            defaultVal = field.getFloat(defaultConfig);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        float currentVal = defaultVal;
                        try {
                            currentVal = field.getFloat(config);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        general.addEntry(entryBuilder.startDoubleField(Text.of(field.getName()), currentVal)
                                .setDefaultValue(defaultVal)
                                .setSaveConsumer(newValue -> setConfigValue(config, field, newValue))
                                .build());
                    }
                    else if (entryType == boolean.class) {
                        boolean defaultVal = false;
                        try {
                            defaultVal = field.getBoolean(defaultConfig);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        boolean currentVal = defaultVal;
                        try {
                            currentVal = field.getBoolean(config);
                        }
                        catch (IllegalAccessException | IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                        general.addEntry(entryBuilder.startBooleanToggle(Text.of(field.getName()), currentVal)
                                .setDefaultValue(defaultVal)
                                .setSaveConsumer(newValue -> setConfigValue(config, field, newValue))
                                .build());
                    }
                    else if (entryType == String.class) {
                        String defaultVal = "";
                        try {
                            defaultVal = (String)field.get(defaultConfig);
                        }
                        catch (IllegalAccessException | IllegalArgumentException | ClassCastException ex) {
                            ex.printStackTrace();
                        }
                        String currentVal = defaultVal;
                        try {
                            currentVal = (String)field.get(config);
                        }
                        catch (IllegalAccessException | IllegalArgumentException | ClassCastException ex) {
                            ex.printStackTrace();
                        }
                        general.addEntry(entryBuilder.startStrField(Text.of(field.getName()), currentVal)
                                .setDefaultValue(defaultVal)
                                .setSaveConsumer(newValue -> setConfigValue(config, field, newValue))
                                .build());
                    }
                    else {
                        OmegaConfig.LOGGER.error(String.format("Configuration item %s is of unknown type %s!", field.getName(), entryType.getName()));
                    }
                }
                return screen.build();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                OmegaConfig.LOGGER.error(String.format("Configuration class for mod %s must have a no-argument constructor for retrieving default values.", config.getModid()));
            }

            // todo: is this a bad idea
            return null;
        };
    }

    private static void setConfigValue(Config config, Field field, Object newValue) {
        try {
            field.set(config, newValue);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        config.save();
    }

    public static Map<Config, OmegaScreenFactory<Screen>> getConfigScreenFactories() {
        return REGISTERED_CONFIGURATIONS;
    }
}
