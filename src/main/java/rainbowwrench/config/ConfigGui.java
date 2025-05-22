package rainbowwrench.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import cpw.mods.fml.client.config.GuiConfig;
import rainbowwrench.RainbowWrench;

public class ConfigGui extends GuiConfig {

    public ConfigGui(GuiScreen parentScreen) {
        super(
            parentScreen,
            new ConfigElement(Config.config.getCategory("not_enough_items")).getChildElements(),
            RainbowWrench.MODID,
            false,
            false,
            GuiConfig.getAbridgedConfigPath(Config.config.toString()));
    }
}
