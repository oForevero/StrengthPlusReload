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
    private final JavaPlugin plugin;
    private ConfigFactory(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public static ConfigFactory newInstance(JavaPlugin javaPlugin) {
        return new ConfigFactory(javaPlugin);
    }

    public void writeConfigFile(){
        plugin.saveResource("config.yml",false);
        plugin.saveResource("strength-extra.yml",false);
        plugin.saveResource("strength-item.yml",false);
        plugin.saveResource("strength-level.yml",false);
        plugin.saveResource("strength-menu.yml",false);
        plugin.saveResource("strength-msg.yml",false);
        plugin.saveResource("strength-stone.yml",false);
    }
}
