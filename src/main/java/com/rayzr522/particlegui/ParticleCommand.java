/**
 * 
 */
package com.rayzr522.particlegui;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.perceivedev.perceivecore.util.JSONMessage;

/**
 * @author Rayzr
 *
 */
public class ParticleCommand implements CommandExecutor {

    private ParticleGUI plugin;

    public ParticleCommand(ParticleGUI plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this!");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 2 && args[0].equalsIgnoreCase("suggest")) {

            String pname = args[1];
            Player player = Bukkit.getPlayer(pname);
            if (player == null || !player.isOnline()) {
                plugin.msg(p, "No player was found by that name!");
                return true;
            }

            JSONMessage.create("")
                    .newline()
                    .bar() // Divider bar
                    .newline()
                    .newline()
                    .then("            ") // White space before name
                    .then(player.getName()) // Player name
                    .color(GREEN)
                    .then(" wants you to check out ") // Pretty text
                    .color(BLUE)
                    .style(ITALIC)
                    .then("ParticleGUI!") // Clickable text
                    .color(GREEN)
                    .tooltip("Click this!")
                    .runCommand("/particles")
                    .newline()
                    .newline()
                    .bar() // Divider bar
                    .newline()
                    .send(player); // Send it

            JSONMessage.create("Hello").color(GREEN).style(BOLD).title(40, 60, 10, player);
            JSONMessage.create("Subtitle").color(BLUE).style(BOLD).subtitle(player);

            JSONMessage.create("Actionbar messages").color(RED).then(" now supported!").color(BLUE).actionbar(player);

            return true;

        }

        if (!p.hasPermission("ParticleGUI.use")) {
            plugin.msg(p, ChatColor.RED + "You don't have permission to do this!");
            return true;
        }

        plugin.msg(p, ChatColor.GREEN + "Opening particles menu...");
        plugin.openGUI(p);

        return true;
    }

}
