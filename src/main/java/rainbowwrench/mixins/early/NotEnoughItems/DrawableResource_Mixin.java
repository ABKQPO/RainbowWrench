package rainbowwrench.mixins.early.NotEnoughItems;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import codechicken.nei.Image;
import codechicken.nei.drawable.DrawableResource;
import rainbowwrench.api.mixinHelper.IDrawableResourceAccessor;

@SuppressWarnings("UnusedMixin")
@Mixin(value = DrawableResource.class, remap = false)
public abstract class DrawableResource_Mixin extends Image
    implements IDrawableResourceAccessor, DrawableResourceAccessor {

    @Unique
    private float rotation = 0;

    @Unique
    private float hue = 0;

    @Unique
    private long tick = 0;

    @Shadow
    private ResourceLocation resourceLocation;
    @Shadow
    private int textureWidth;
    @Shadow
    private int textureHeight;

    @Shadow
    private int paddingTop;
    @Shadow
    private int paddingLeft;

    public DrawableResource_Mixin(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ResourceLocation resourceLocation, int u, int v, int width, int height, int paddingTop,
        int paddingBottom, int paddingLeft, int paddingRight, int textureWidth, int textureHeight, CallbackInfo ci) {}

    @Unique
    public void draw(int xOffset, int yOffset, int special) {
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(this.resourceLocation);

        final int x = xOffset + this.paddingLeft;
        final int y = yOffset + this.paddingTop;
        final int textureX = this.x;
        final int textureY = this.y;
        final int width = this.width;
        final int height = this.height;

        hue += 0.005f;
        if (hue > 1f) hue = 0f;
        int rgb = Color.HSBtoRGB(hue, 1f, 1f);
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        GL11.glPushMatrix();
        float centerX = x + width / 2.0f;
        float centerY = y + height / 2.0f;

        if (special == 1) {
            tick++;

            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glTranslatef(centerX, centerY, 20f);
            GL11.glRotatef((tick) % 360, 0.3f, 0.5f, 0.2f);
            GL11.glRotatef(180, 0.5f, 0.0f, 0.0f);
            GL11.glTranslatef(0.0f, 0.0f, 0.03125F);
            GL11.glTranslatef(-centerX, -centerY, 10f);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(r, g, b, 1f);

            GL11.glScalef(width, height, 1.0f);
            GL11.glTranslatef((float) x / width, (float) y / height, 0.0f);

            ItemRenderer.renderItemIn2D(
                Tessellator.instance,
                (float) (textureX + this.textureWidth) / 16,
                (float) textureY / 16,
                (float) textureX / 16,
                (float) (textureY + this.textureHeight) / 16,
                width,
                height,
                2f);

            GL11.glEnable(GL11.GL_CULL_FACE);
        } else {
            rotation += 5f;
            if (rotation >= 360f) rotation = 0f;

            GL11.glTranslatef(centerX, centerY, 0f);

            GL11.glRotatef(rotation, 0f, 0f, 1f);

            GL11.glTranslatef(-centerX, -centerY, 0f);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(r, g, b, 1f);

            Gui.func_146110_a(x, y, textureX, textureY, width, height, textureWidth, textureHeight);

        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
