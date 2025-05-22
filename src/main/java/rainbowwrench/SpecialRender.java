package rainbowwrench;

import static rainbowwrench.ClientProxy.haloNoiseIcon;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.Loader;
import fox.spiteful.avaritia.render.CosmicRenderShenanigans;

public class SpecialRender {

    private float rotation = 0f;
    private float hue = 0f;
    private long tick = 0;

    private final Random rand = new Random();

    private double offsetRed = 0;
    private double offsetCyan = 0;

    private final int[] red = new int[] { 255, 50, 50, 192 };
    private final int[] cyan = new int[] { 0, 220, 220, 160 };

    private static final ResourceLocation HALO_TEXTURE = new ResourceLocation("avaritia", "textures/items/halo.png");

    /**
     * 绘制方法，写满参数，在内部根据 special 调用不同绘制逻辑。
     *
     * @param xOffset          绘制X偏移
     * @param yOffset          绘制Y偏移
     * @param special          特殊绘制类型，1走特殊动画，2走故障动画，否则默认旋转动画
     * @param resourceLocation 纹理资源
     * @param paddingLeft      纹理偏移Left
     * @param paddingTop       纹理偏移Top
     * @param x                纹理区域X
     * @param y                纹理区域Y
     * @param width            绘制宽度
     * @param height           绘制高度
     * @param textureWidth     纹理宽度
     * @param textureHeight    纹理高度
     */
    public void draw(int xOffset, int yOffset, int special, ResourceLocation resourceLocation, int paddingLeft,
        int paddingTop, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(resourceLocation);

        final int drawX = xOffset + paddingLeft;
        final int drawY = yOffset + paddingTop;

        hue += 0.005f;
        if (hue > 1f) hue = 0f;
        int rgb = Color.HSBtoRGB(hue, 1f, 1f);
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;

        GL11.glPushMatrix();

        float centerX = drawX + width / 2.0f;
        float centerY = drawY + height / 2.0f;

        long frameTimeNanos = 20_000_000L;
        int loopFrameCount = 200;
        int glitchedDurationCount = 60;
        int glitchMoveFrameCount = 4;
        switch (special) {
            case 1:
                tick++;
                GL11.glEnable(GL11.GL_DEPTH_TEST);
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
                GL11.glTranslatef((float) drawX / width, (float) drawY / height, 0.0f);

                ItemRenderer.renderItemIn2D(
                    Tessellator.instance,
                    (float) (x + textureWidth) / 16,
                    (float) y / 16,
                    (float) x / 16,
                    (float) (y + textureHeight) / 16,
                    width,
                    height,
                    2f);

                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                break;

            case 2:
                long currentFrame = (System.nanoTime() % (frameTimeNanos * loopFrameCount)) / frameTimeNanos;
                boolean timing = currentFrame <= glitchedDurationCount;

                if (timing && currentFrame % glitchMoveFrameCount == 0) {
                    offsetRed = rand.nextDouble() * 1.7 * Math.signum(rand.nextGaussian());
                    offsetCyan = rand.nextDouble() * 1.7 * Math.signum(rand.nextGaussian());
                }

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                GL11.glColor4f(1.0f, 0.4706f, 0.0784f, 1.0f);
                Gui.func_146110_a(drawX, drawY, x, y, width, height, textureWidth, textureHeight);

                if (timing) {
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    applyGlitchEffect(drawX, drawY, offsetCyan, cyan, x, y, width, height, textureWidth, textureHeight);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    applyGlitchEffect(drawX, drawY, offsetRed, red, x, y, width, height, textureWidth, textureHeight);
                }

                GL11.glDisable(GL11.GL_BLEND);
                break;

            case 3:
                if (Loader.isModLoaded("Avaritia")) {
                    CosmicRenderShenanigans.setLightLevel(1.2f);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    RenderHelper.enableGUIStandardItemLighting();

                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);

                    Gui.func_146110_a(drawX, drawY, x, y, width, height, textureWidth, textureHeight);

                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    RenderHelper.enableGUIStandardItemLighting();

                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);

                    CosmicRenderShenanigans.cosmicOpacity = 0.8f;
                    CosmicRenderShenanigans.inventoryRender = true;
                    CosmicRenderShenanigans.useShader();

                    GL11.glColor4d(1, 1, 1, 1);
                    Tessellator t = Tessellator.instance;
                    float u = (float) x / textureWidth;
                    float v = (float) y / textureHeight;
                    float uWidth = (float) width / textureWidth;
                    float vHeight = (float) height / textureHeight;

                    t.startDrawingQuads();
                    t.addVertexWithUV(drawX, drawY, 0, u, v);
                    t.addVertexWithUV(drawX, drawY + height, 0, u, v + vHeight);
                    t.addVertexWithUV(drawX + width, drawY + height, 0, u + uWidth, v + vHeight);
                    t.addVertexWithUV(drawX + width, drawY, 0, u + uWidth, v);
                    t.draw();

                    CosmicRenderShenanigans.releaseShader();
                    CosmicRenderShenanigans.inventoryRender = false;
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDisable(GL11.GL_BLEND);
                }
                break;

            case 4:
                if (Loader.isModLoaded("Avaritia")) {
                    int passes = 2;

                    for (int pass = 0; pass < passes; pass++) {
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);

                        if (pass == 0) {
                            renderHalo(drawX, drawY, width, height);
                        }

                        renderPulse(drawX, drawY, width, height, x, y, textureWidth, textureHeight);

                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                        Minecraft.getMinecraft()
                            .getTextureManager()
                            .bindTexture(resourceLocation);
                        GL11.glColor4f(1.0f, 0.3333f, 0.3333f, 1.0f);
                        Gui.func_146110_a(drawX, drawY, x, y, width, height, textureWidth, textureHeight);

                        GL11.glDisable(GL11.GL_BLEND);
                    }
                }
                break;

            case 5:
                if (Loader.isModLoaded("Avaritia")) {
                    float ca = (float) (0xE6FFFFFF >> 24 & 255) / 255.0F;
                    float cr = (float) (0xE6FFFFFF >> 16 & 255) / 255.0F;
                    float cg = (float) (0xE6FFFFFF >> 8 & 255) / 255.0F;
                    float cb = (float) (0xE6FFFFFF & 255) / 255.0F;
                    GL11.glColor4f(cr, cg, cb, ca);

                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);

                    IIcon halonoiseIcon = haloNoiseIcon;

                    if (halonoiseIcon != null) {
                        Minecraft.getMinecraft()
                            .getTextureManager()
                            .bindTexture(TextureMap.locationItemsTexture);

                        float minU = halonoiseIcon.getMinU();
                        float maxU = halonoiseIcon.getMaxU();
                        float minV = halonoiseIcon.getMinV();
                        float maxV = halonoiseIcon.getMaxV();

                        int noiseSpread = 10;
                        float haloDrawX = drawX - noiseSpread;
                        float haloDrawY = drawY - noiseSpread;
                        float haloWidth = width + 2 * noiseSpread;
                        float haloHeight = height + 2 * noiseSpread;

                        Tessellator t = Tessellator.instance;
                        t.startDrawingQuads();
                        t.addVertexWithUV(haloDrawX, haloDrawY + haloHeight, 0, minU, maxV);
                        t.addVertexWithUV(haloDrawX + haloWidth, haloDrawY + haloHeight, 0, maxU, maxV);
                        t.addVertexWithUV(haloDrawX + haloWidth, haloDrawY, 0, maxU, minV);
                        t.addVertexWithUV(haloDrawX, haloDrawY, 0, minU, minV);
                        t.draw();
                    }

                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);

                    GL11.glColor4f(1.0f, 0.3333f, 0.3333f, 1.0F);

                    Minecraft.getMinecraft()
                        .getTextureManager()
                        .bindTexture(resourceLocation);
                    Gui.func_146110_a(drawX, drawY, x, y, width, height, textureWidth, textureHeight);
                }
                break;

            default:
                rotation += 5f;
                if (rotation >= 360f) rotation = 0f;

                GL11.glTranslatef(centerX, centerY, 0f);
                GL11.glRotatef(rotation, 0f, 0f, 1f);
                GL11.glTranslatef(-centerX, -centerY, 0f);

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(r, g, b, 1f);

                Gui.func_146110_a(drawX, drawY, x, y, width, height, textureWidth, textureHeight);
                break;
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private void applyGlitchEffect(int baseX, int baseY, double offset, int[] color, int texX, int texY, int width,
        int height, int texWidth, int texHeight) {
        Tessellator t = Tessellator.instance;

        t.startDrawingQuads();
        t.setColorRGBA_F(color[0] / 255.0F, color[1] / 255.0F, color[2] / 255.0F, color[3] / 255.0F);
        t.addVertexWithUV(baseX + offset, baseY + offset, 0, (float) texX / 16f, (float) texY / 16f);
        t.addVertexWithUV(
            baseX + offset,
            baseY + height + offset,
            0,
            (float) texX / 16f,
            (float) (texY + texHeight) / 16f);
        t.addVertexWithUV(
            baseX + width + offset,
            baseY + height + offset,
            0,
            (float) (texX + texWidth) / 16f,
            (float) (texY + texHeight) / 16f);
        t.addVertexWithUV(
            baseX + width + offset,
            baseY + offset,
            0,
            (float) (texX + texWidth) / 16f,
            (float) texY / 16f);
        t.draw();
    }

    /**
     * Simulates the renderHalo method from GregTech's InfinityRenderer.
     * Uses the specific Avaritia halo texture.
     *
     * @param drawX  The starting X coordinate for drawing.
     * @param drawY  The starting Y coordinate for drawing.
     * @param width  The width of the item being rendered.
     * @param height The height of the item being rendered.
     */
    private void renderHalo(int drawX, int drawY, int width, int height) {
        GL11.glPushMatrix();
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(HALO_TEXTURE);

        int spread = 10;
        int haloAlpha = 0xFF000000;

        Tessellator t = Tessellator.instance;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glColor4f(20 / 255.0f, 20 / 255.0f, 20 / 255.0f, (float) (haloAlpha >> 24 & 255) / 255.0F);

        float haloDrawX = drawX - spread;
        float haloDrawY = drawY - spread;
        float haloWidth = width + 2 * spread;
        float haloHeight = height + 2 * spread;

        t.startDrawingQuads();
        t.addVertexWithUV(haloDrawX, haloDrawY, 0, 0.0, 0.0);
        t.addVertexWithUV(haloDrawX, haloDrawY + haloHeight, 0, 0.0, 1.0);
        t.addVertexWithUV(haloDrawX + haloWidth, haloDrawY + haloHeight, 0, 1.0, 1.0);
        t.addVertexWithUV(haloDrawX + haloWidth, haloDrawY, 0, 1.0, 0.0);
        t.draw();
        GL11.glPopMatrix();
    }

    /**
     * Simulates the renderPulse method from GregTech's InfinityRenderer.
     * Applies a pulsing effect to the specified texture region.
     *
     * @param drawX         The starting X coordinate for drawing.
     * @param drawY         The starting Y coordinate for drawing.
     * @param width         The width of the item being rendered.
     * @param height        The height of the item being rendered.
     * @param x             X coordinate of the texture region on the texture sheet.
     * @param y             Y coordinate of the texture region on the texture sheet.
     * @param textureWidth  Total width of the texture sheet.
     * @param textureHeight Total height of the texture sheet.
     */
    private void renderPulse(int drawX, int drawY, int width, int height, int x, int y, int textureWidth,
        int textureHeight) {
        Tessellator t = Tessellator.instance;
        double random = rand.nextGaussian();
        double scale = (random * 0.15) + 0.95;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glTranslated(drawX + width / 2.0, drawY + height / 2.0, 1.0);
        GL11.glScaled(scale, scale, 1.0);
        GL11.glTranslated(-(drawX + width / 2.0), -(drawY + height / 2.0), 0);

        t.startDrawingQuads();
        t.setColorRGBA_F(1.0f, 0.3333f, 0.3333f, 0.6f);

        float u = (float) x / textureWidth;
        float v = (float) y / textureHeight;
        float uWidth = (float) width / textureWidth;
        float vHeight = (float) height / textureHeight;

        t.addVertexWithUV(drawX, drawY, 0, u, v);
        t.addVertexWithUV(drawX, drawY + height, 0, u, v + vHeight);
        t.addVertexWithUV(drawX + width, drawY + height, 0, u + uWidth, v + vHeight);
        t.addVertexWithUV(drawX + width, drawY, 0, u + uWidth, v);
        t.draw();

        GL11.glPopMatrix();
    }
}
