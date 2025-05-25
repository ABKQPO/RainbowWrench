package rainbowwrench.mixins.early.NotEnoughItems;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import codechicken.nei.Button;
import codechicken.nei.Image;
import codechicken.nei.LayoutStyleMinecraft;
import rainbowwrench.api.mixinHelper.IDrawableResourceAccessor;
import rainbowwrench.config.Config;

@SuppressWarnings("UnusedMixin")
@Mixin(value = LayoutStyleMinecraft.class, remap = false)
public class LayoutStyleMinecraft_Mixin {

    @Inject(
        method = "drawButton",
        at = @At(value = "INVOKE", target = "Lcodechicken/nei/LayoutManager;drawIcon(IILcodechicken/nei/Image;)V"),
        cancellable = true)
    private void beforeDrawIcon(Button b, int mousex, int mousey, CallbackInfo ci) {
        Image icon = b.getRenderIcon();
        if (icon instanceof DrawableResourceAccessor accessor && icon instanceof IDrawableResourceAccessor dr) {
            ResourceLocation rl = accessor.getResourceLocation();
            if ("nei:textures/items/cheat_speical.png".equals(rl.toString()) && Config.enableSpecialCheatIcon) {
                final int iconX = b.x + (b.w - icon.width) / 2;
                final int iconY = b.y + (b.h - icon.height) / 2;
                dr.draw(iconX, iconY, Config.specialIconType);
                ci.cancel();
            }
        }
    }
}
