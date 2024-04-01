package org.toodles.lifestealdupe.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.toodles.lifestealdupe.LifeStealDupe;

import java.io.*;
import java.util.*;

public class Blacklist {


    private static File file;

    private static Set<BlacklistEntry> BLACKLIST = new HashSet<>();
    public static void setup() throws IOException {

        file = new File(LifeStealDupe.getInstance().getDataFolder(), "blacklist.json");


        if (!file.exists()) {
            file.createNewFile();
        }

        BLACKLIST = getCache();

    }
    private static Set<BlacklistEntry> getCache() {

        Set<BlacklistEntry> blacklist = new HashSet<>();

        try (FileReader reader = new FileReader(file)) {


            JsonElement element = JsonParser.parseReader(reader);

            Gson gson = new Gson();

            if (element.isJsonArray()) {

                for (JsonElement object : element.getAsJsonArray()) {

                    blacklist.add(gson.fromJson(object, BlacklistEntry.class));

                }


            }



        } catch (IOException ignored) {}

        return blacklist;

    }

    public static Set<BlacklistEntry> getBlacklist() {
        return BLACKLIST;
    }

    public static String serialize(ItemStack itemStack){

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BukkitObjectOutputStream bs = new BukkitObjectOutputStream(os);
            bs.writeObject(itemStack);
            bs.flush();

            return Base64.getEncoder().encodeToString(os.toByteArray());


        } catch (IOException ignored) {}

        return null;

    }

    public static ItemStack deserialize(String encodedB64) {

        try {

            ByteArrayInputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(encodedB64));
            BukkitObjectInputStream bs = new BukkitObjectInputStream(is);

            return (ItemStack) bs.readObject();

        } catch (IOException | ClassNotFoundException ignored) {}

        return null;
    }

    public static void addBlacklist(String key, ItemStack itemStack) {
        BLACKLIST.add(new BlacklistEntry(key, itemStack));
    }

    public static void removeBlacklist(String key) {
        BLACKLIST.removeIf(blacklistEntry -> blacklistEntry.getKey().equals(key));
    }

    public static void cache() {

        try (FileWriter writer = new FileWriter(file, false)) {

            String blacklist = new Gson().toJson(BLACKLIST);
            writer.write(blacklist);


        } catch (IOException ignored) {}

    }

    public static boolean isBlacklisted(ItemStack itemStack) {
        try {
            return BLACKLIST.stream().anyMatch(blacklistEntry -> deserialize(blacklistEntry.getItem()).isSimilar(itemStack));
        } catch (NullPointerException e) {
            return false;
        }
    }

}
