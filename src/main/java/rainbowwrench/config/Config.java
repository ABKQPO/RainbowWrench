package rainbowwrench.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

// spotless:off
public class Config {

    // NotEnoughItems
    public static boolean enableSpecialCheatIcon = true;
    public static int specialIconType = 0;

    public static Configuration config;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }

    }

    public static void reloadConfig() {
        if (config.hasChanged()) {
            config.save();
        }
        if (config != null) {
            config.load();
            loadConfig();
        }
    }

    public static void loadConfig() {
        // Not Enough Items
        enableSpecialCheatIcon = config
            .get(
                "not_enough_items",
                "EnableSpecialCheatIcon",
                enableSpecialCheatIcon,
                "Enable a special icon for cheat mode")
            .getBoolean(enableSpecialCheatIcon);

        specialIconType = config
            .get("not_enough_items", "SpecialIconType", specialIconType, "Specify the type of the special cheat icon")
            .getInt(specialIconType);

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static Configuration getConfiguration() {
        return config;
    }

}
