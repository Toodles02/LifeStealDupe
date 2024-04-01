package org.toodles.lifestealdupe.utils;

import org.bukkit.inventory.ItemStack;

public class BlacklistEntry {

    private final String key;

    private final String item;


    public BlacklistEntry(String key, ItemStack itemStack) {
        this.key = key;
        this.item = Blacklist.serialize(itemStack);
    }

    public String getKey() {
        return key;
    }

    public String getItem() {
        return item;
    }
}
