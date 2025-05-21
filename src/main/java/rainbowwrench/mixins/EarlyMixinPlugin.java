package rainbowwrench.mixins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class EarlyMixinPlugin {

    public static List<String> getEarlyMixins(Set<String> loadedMods) {
        final List<String> mixins = new ArrayList<>();
        if (!isModPresent("com.science.gtnl.asm.GTNLEarlyCoreMod")) {
            mixins.add("NotEnoughItems.DrawableBuilderAccessor");
            mixins.add("NotEnoughItems.DrawableResourceAccessor");
            mixins.add("NotEnoughItems.DrawableResource_Mixin");
            mixins.add("NotEnoughItems.LayoutStyleMinecraft_Mixin");
            mixins.add("NotEnoughItems.LayoutManager_Mixin");
        }
        return mixins;
    }

    public static boolean isModPresent(String className) {
        try {
            Class.forName(className, false, EarlyMixinPlugin.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
