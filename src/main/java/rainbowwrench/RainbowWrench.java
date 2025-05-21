package rainbowwrench;

import static rainbowwrench.Tags.MODID;
import static rainbowwrench.Tags.MODNAME;

import cpw.mods.fml.common.Mod;

@Mod(
    modid = MODID,
    version = Tags.VERSION,
    name = MODNAME,
    acceptableRemoteVersions = "*",
    dependencies = "required-after:NotEnoughItems;",
    acceptedMinecraftVersions = "1.7.10")

public class RainbowWrench {

    @Mod.Instance
    public static RainbowWrench instance;
    public static final String MODID = "rainbowwrench";
    public static final String MODNAME = "RainbowWrench";
    public static final String VERSION = Tags.VERSION;
    public static final String Arthor = "HFstudio";
    public static final String RESOURCE_ROOT_ID = "rainbowwrench";

}
