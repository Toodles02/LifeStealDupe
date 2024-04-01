package org.toodles.lifestealdupe;

import org.bukkit.plugin.java.JavaPlugin;
import org.toodles.lifestealdupe.commands.BlackListCommand;
import org.toodles.lifestealdupe.commands.DupeCommand;
import org.toodles.lifestealdupe.utils.Blacklist;
import org.toodles.lifestealdupe.utils.BlacklistEntry;

import java.io.IOException;

public final class LifeStealDupe extends JavaPlugin {

    @Override
    public void onEnable() {

        DupeCommand dupe = new DupeCommand();
        BlackListCommand blacklist = new BlackListCommand();

        getServer().getPluginCommand("dupe").setExecutor(dupe);
        getServer().getPluginCommand("dupe").setTabCompleter(dupe);
        getServer().getLogger().info("Registered the dupe command!");

        getServer().getPluginCommand("blacklist").setExecutor(blacklist);
        getServer().getPluginCommand("blacklist").setTabCompleter(blacklist);


        getDataFolder().mkdirs();

        try {
            Blacklist.setup();
        } catch (IOException e) {
            getLogger().warning("could not create blacklist.json");
        }

    }

    @Override
    public void onDisable() {
        Blacklist.cache();
    }

    public static LifeStealDupe getInstance() {
        return JavaPlugin.getPlugin(LifeStealDupe.class);
    }

}
