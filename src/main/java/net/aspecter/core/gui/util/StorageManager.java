package net.aspecter.core.gui.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StorageManager {
    private static final String DATA_PATH = "plugins/Core/guis";

    private static final File DATA_DIR = new File(DATA_PATH);

    public List<String> getItemNames() {
        List<String> files = new ArrayList<>();
        for (File file : DATA_DIR.listFiles())
            files.add(file.getName().replace(".yml", ""));
        return files;
    }

    public void convertFilesToLowerCase() {
        for (File file : DATA_DIR.listFiles())
            convertFileToLowerCase(file);
    }

    private void convertFileToLowerCase(File file) {
        if (!Objects.equals(sanitizeItemName(file.getName()), file.getName())) {
            File newName = new File(file.getParentFile().getAbsolutePath() + File.separatorChar + sanitizeItemName(file.getName()));
            file.renameTo(newName);
        }
    }

    public String sanitizeItemName(String itemName) {
        return itemName.toLowerCase().trim();
    }

    private String convertToFileName(String itemName) {
        if (!itemName.endsWith(".yml"))
            itemName = itemName + ".yml";
        return sanitizeItemName(itemName);
    }

    public boolean doesItemExist(String itemOrFileName) {
        return getItemFile(itemOrFileName).exists();
    }

    private File getItemFile(String itemOrFileName) {
        return new File(Paths.get(DATA_PATH, new String[] { convertToFileName(itemOrFileName) }).toString());
    }

    public boolean delete(String itemName) {
        File file = getItemFile(itemName);
        if (file.exists() && file.isFile())
            return file.delete();
        return false;
    }

    public boolean save(String itemName, ItemStack itemStack) {
        File file = getItemFile(itemName);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("Item", ItemSerializer.toBase64(itemStack));
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ItemStack load(String itemName) {
        String values = YamlConfiguration.loadConfiguration(getItemFile(itemName)).getString("Item");
        ItemStack it = null;
        try {
            it = ItemSerializer.fromBase64(values);
        } catch (IllegalArgumentException|IOException e) {
            e.printStackTrace();
            return null;
        }
        return it;
    }
}
