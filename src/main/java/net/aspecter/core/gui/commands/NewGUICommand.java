package net.aspecter.core.gui.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandAlias("gui")
@CommandPermission("gui.modify")
public class NewGUICommand {

    @Default
    public void onGuiCommand(Player player) {
        player.sendMessage(ChatColor.GRAY + "Running EasyGUI - made with " + ChatColor.DARK_RED + "<3" + ChatColor.GRAY + " by Aspecting");
    }

    @CommandAlias("gui create")
    public void onCreate(String[] args) {

    }


}
