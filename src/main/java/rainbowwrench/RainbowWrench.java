package rainbowwrench;

import static rainbowwrench.Tags.MODID;
import static rainbowwrench.Tags.MODNAME;

import java.io.File;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import rainbowwrench.config.Config;
import rainbowwrench.config.ReloadConfig;

@Mod(
    modid = MODID,
    version = Tags.VERSION,
    name = MODNAME,
    acceptableRemoteVersions = "*",
    dependencies = "required-after:NotEnoughItems;",
    guiFactory = "rainbowwrench.config.ConfigGuiFactory",
    acceptedMinecraftVersions = "1.7.10")

public class RainbowWrench {

    @Mod.Instance
    public static RainbowWrench instance;
    public static final String MODID = "rainbowwrench";
    public static final String MODNAME = "RainbowWrench";
    public static final String VERSION = Tags.VERSION;
    public static final String Arthor = "HFstudio";
    public static final String RESOURCE_ROOT_ID = "rainbowwrench";

    static {
        File configDir = new File("config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        File mainConfigFile = new File(configDir, "RainbowWrench.cfg");
        Config.init(mainConfigFile);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadConfig());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance()
            .bus()
            .register(new ConfigEventHandler());
    }

    public static class ConfigEventHandler {

        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.modID.equals(RainbowWrench.MODID)) {

                ReloadConfig.reloadConfig();
            }
        }
    }
}
