package rainbowwrench.asm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import rainbowwrench.mixins.EarlyMixinPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({ "rainbowwrench.asm" })
@IFMLLoadingPlugin.Name("Rainbow Wrench core plugin")
public class RainbowWrenchCoreMod implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String getMixinConfig() {
        return "mixins.rainbowwrench.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        return EarlyMixinPlugin.getEarlyMixins(loadedCoreMods);
    }
}
