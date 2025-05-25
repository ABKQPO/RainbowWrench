package rainbowwrench.mixins.early.NotEnoughItems;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import codechicken.nei.drawable.DrawableBuilder;
import codechicken.nei.drawable.DrawableResource;
import rainbowwrench.api.mixinHelper.ExtendedDrawableBuilder;

@SuppressWarnings("UnusedMixin")
@Mixin(targets = "codechicken.nei.LayoutManager$1", remap = false)
public class LayoutManager_Mixin {

    @Redirect(
        method = "init",
        at = @At(
            value = "INVOKE",
            target = "Lcodechicken/nei/drawable/DrawableBuilder;build()Lcodechicken/nei/drawable/DrawableResource;",
            ordinal = 2))
    private DrawableResource redirectBuild(DrawableBuilder builder) {
        if (builder instanceof ExtendedDrawableBuilder extended) {
            return extended.build();
        }
        return new ExtendedDrawableBuilder("nei:textures/items/cheat_speical.png", 0, 0, 16, 16, 16, 16).build();
    }
}
