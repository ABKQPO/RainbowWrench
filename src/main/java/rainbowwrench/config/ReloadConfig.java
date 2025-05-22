package rainbowwrench.config;

import java.io.File;

public class ReloadConfig {

    public static void init(File mainConfigFile) {
        Config.init(mainConfigFile);
    }

    public static void reloadConfig() {
        Config.reloadConfig();
    }
}
