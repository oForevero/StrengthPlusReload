package top.mccat.factory;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import top.mccat.pojo.BaseData;

import java.io.File;

/**
 * @author Raven
 * @date 2022/09/05 15:44
 */
public class ConfigFactory {
    private final FileConfiguration yamlConfiguration = new YamlConfiguration();
    private final JavaPlugin plugin;
    private static final String[] CONFIG_FILES = {"config.yml","strength-extra.yml","strength-item.yml","strength-level.yml"
            ,"strength-menu.yml","strength-msg.yml","strength-stone.yml"};

    private ConfigFactory(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public static ConfigFactory newInstance(JavaPlugin javaPlugin) {
        return new ConfigFactory(javaPlugin);
    }

    public void writeConfigFile(){
        for (String fileName : CONFIG_FILES){
            String fileDir = BaseData.BASE_DIR+"/"+fileName;
            File file = new File(fileDir);
            if(!file.exists()){
                plugin.saveResource(fileDir, false);
            }
        }
    }


}
