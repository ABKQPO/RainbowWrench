package rainbowwrench;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 1) {
            registerAllIcons(event.map);
        }
    }

    public static String haloNoiseIconTexture = "nei:halonoise";
    public static IIcon haloNoiseIcon;

    public static void registerAllIcons(IIconRegister ir) {
        haloNoiseIcon = ir.registerIcon(haloNoiseIconTexture);
    }
}
