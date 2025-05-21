package rainbowwrench.api.mixinHelper;

import codechicken.nei.drawable.DrawableBuilder;
import rainbowwrench.mixins.early.NotEnoughItems.DrawableBuilderAccessor;

public class ExtendedDrawableBuilder extends DrawableBuilder {

    public ExtendedDrawableBuilder(String resourceLocation, int u, int v, int width, int height, int textureWidth,
        int textureHeight) {
        super(resourceLocation, u, v, width, height);
        DrawableBuilderAccessor accessor = (DrawableBuilderAccessor) this;
        accessor.setTextureWidth(textureWidth);
        accessor.setTextureHeight(textureHeight);
    }
}
