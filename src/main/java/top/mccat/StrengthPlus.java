package top.mccat;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import top.mccat.pojo.config.StrengthExtra;
import top.mccat.utils.MsgUtils;
import top.mccat.utils.YamlLoadUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author Raven
 * @date 2022/09/05 15:36
 */
public class StrengthPlus extends JavaPlugin {
    @Override
    public void onLoad() {
        super.onLoad();

    }

    @Override
    public void onEnable() {
        super.onEnable();
        //demo测试
        MsgUtils msgUtils = MsgUtils.newInstance();
        msgUtils.sendToBroadcast("demo");
        System.out.println(this.getDataFolder());
        try {
            Optional<Object> o = YamlLoadUtils.loadYamlAsObject("strength-extra.yml", String.valueOf(this.getDataFolder()), "strength-extra", StrengthExtra.class);
            System.out.println(o.toString());
        } catch (IOException | InvalidConfigurationException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
    }
}
