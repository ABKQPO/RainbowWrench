package rainbowwrench.mixins.early.NotEnoughItems;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import codechicken.nei.drawable.DrawableResource;

@SuppressWarnings("UnusedMixin")
@Mixin(value = DrawableResource.class, remap = false)
public interface DrawableResourceAccessor {

    @Accessor("resourceLocation")
    ResourceLocation getResourceLocation();

    @Accessor("textureWidth")
    int getTextureWidth();

    @Accessor("textureHeight")
    int getTextureHeight();

    @Accessor("paddingLeft")
    int getPaddingLeft();

    @Accessor("paddingTop")
    int getPaddingTop();
}
