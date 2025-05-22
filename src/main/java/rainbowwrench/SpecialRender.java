package rainbowwrench;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.Loader;
import fox.spiteful.avaritia.render.CosmicRenderShenanigans;

public class SpecialRender {

    private float rotation = 0f;
    private float hue = 0f;
    private long tick = 0;

    private Random rand = new Random();

    private double offsetRed = 0;
    private double offsetCyan = 0;

    private final int[] red = new int[] { 255, 50, 50, 192 };
    private final int[] cyan = new int[] { 0, 220, 220, 160 };

    private final long frameTimeNanos = 20_000_000L;
    private final int loopFrameCount = 200;
    private final int glitchedDurationCount = 60;
    private final int glitchMoveFrameCount = 4;

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

        switch (special) {
            case 1:
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
}
