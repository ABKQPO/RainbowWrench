package rainbowwrench;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import rainbowwrench.config.ReloadConfig;

public class CommandReloadConfig extends CommandBase {

    @Override
    public String getCommandName() {
        return "rainbowwrench_reloadconfig";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/rainbowwrench_reloadconfig - reload config";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            ReloadConfig.reloadConfig();
        } catch (Exception e) {
            sender.addChatMessage(new ChatComponentText("Error Reload config: " + e.getMessage()));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
