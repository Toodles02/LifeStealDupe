package org.toodles.lifestealdupe.commands;

import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.toodles.lifestealdupe.utils.Blacklist;

import java.util.List;

public class DupeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        try {

            int multiplier;
            if (args.length == 1 && Integer.parseInt(args[0]) > 2) {
                multiplier = Integer.parseInt(args[0]) - 1;
            } else if (args.length == 0) {
                multiplier = 1;
            } else {
                return false;
            }

            if (player.hasPermission("LifeStealDupe.dupe."+(multiplier+1)) || player.hasPermission("LifeStealDupe.dupe.*")) {

                ItemStack item = player.getInventory().getItemInMainHand();

                if (Blacklist.isBlacklisted(item)) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>That item cannot be duped!").decoration(TextDecoration.ITALIC, false));
                    return true;
                }

                if (item.getType() == Material.AIR) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>There is no item in your hand to dupe!").decoration(TextDecoration.ITALIC, false));
                    return true;
                }

                if (player.getInventory().firstEmpty() == -1) {

                    player.getWorld().dropItem(player.getLocation(), item.asQuantity(item.getAmount() * multiplier));
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Multiplied your item by "+(multiplier+1)+"x!").decoration(TextDecoration.ITALIC, false));
                    return true;

                }
                player.getInventory().addItem(item.asQuantity(item.getAmount() * multiplier));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Multiplied your item by "+(multiplier+1)+"x!").decoration(TextDecoration.ITALIC, false));
                return true;

            } else {

                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Sorry, but you do not have permission to run this command!").decoration(TextDecoration.ITALIC, false));
            }


        } catch (NumberFormatException e) {
            return false;
        }

        return false;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {

            return List.of("3", "4", "5");

        }

        return null;
    }


}
