package top.mccat.factory;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Raven
 * @date 2022/09/05 15:44
 */
public class ConfigFactory {
    private final FileConfiguration yamlConfiguration = new YamlConfiguration();
    private ConfigFactory(){

    }

    public static ConfigFactory newInstance(JavaPlugin javaPlugin) {
        return new ConfigFactory();
    }

    public boolean configFileIsExist(){

    }
}
