package net.aspecter.core.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class YamlUtil {

    private YamlConfiguration data;

    public YamlUtil(String filePath) {
        data = YamlConfiguration.loadConfiguration(new File(filePath));
    }

}
