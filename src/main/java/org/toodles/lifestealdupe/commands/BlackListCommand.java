package org.toodles.lifestealdupe.commands;

import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.toodles.lifestealdupe.utils.Blacklist;
import org.toodles.lifestealdupe.utils.BlacklistEntry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlackListCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length == 2 && (args[0].equals("add") || args[0].equals("remove"))) {


            String key = args[1];
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == Material.AIR) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You have no item in your hand!"));
                return true;
            }

            if (args[0].equals("add")) {

                if (Blacklist.isBlacklisted(item)) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>That item is already blacklisted!").decoration(TextDecoration.ITALIC, false));
                    return true;
                }

                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully blacklisted item with key "+args[1]+"!").decoration(TextDecoration.ITALIC, false));

                Blacklist.addBlacklist(key, item);
                return true;


            } else if (args[0].equals("remove")) {

                if (!Blacklist.isBlacklisted(item)) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>That item isn't blacklisted!").decoration(TextDecoration.ITALIC, false));
                    return true;
                }

                Blacklist.removeBlacklist(key);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Successfully removed item with key "+args[1]+" from the blacklist!").decoration(TextDecoration.ITALIC, false));
                return true;


            }

        } else if (args.length == 1 && (args[0].equals("items"))) {

            player.sendMessage(MiniMessage.miniMessage().deserialize("<gold>Blacklisted Items:").decoration(TextDecoration.ITALIC, false));

            if (Blacklist.getBlacklist().isEmpty()) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow> - []").decoration(TextDecoration.ITALIC, false));
                return true;
            }

            for (BlacklistEntry entry : Blacklist.getBlacklist()) {

                player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow> - "+entry.getKey()).decoration(TextDecoration.ITALIC, false));

            }

            return true;

        }

        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            return List.of("add", "remove", "items");
        }

        return null;
    }
}
