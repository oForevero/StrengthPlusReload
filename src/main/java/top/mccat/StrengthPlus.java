package top.mccat;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import top.mccat.pojo.BaseData;
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
    private MsgUtils msgUtils;
    @Override
    public void onLoad() {
        msgUtils = MsgUtils.newInstance();
        sendToConsole("&c 正在初始化StrengthPlus中，请稍后...");
    }

    @Override
    public void onEnable() {
        sendToConsole("&c 正在注入yaml配置文件对象...");
        /*demo测试
        MsgUtils msgUtils = MsgUtils.newInstance();
        msgUtils.sendToBroadcast("demo");
        System.out.println(this.getDataFolder());
        try {
            Optional<Object> o = YamlLoadUtils.loadYamlAsObject("strength-extra.yml", String.valueOf(this.getDataFolder()), "strength-extra", StrengthExtra.class);
            System.out.println(o.toString());
        } catch (IOException | InvalidConfigurationException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }*/
        try {
            YamlLoadUtils.loadYamlArrayAsObject("strength-stone.yml", String.valueOf(this.getDataFolder()), "strength-stone", StrengthExtra.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        sendToConsole("&c 正在卸载插件配置...");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
    }

    private void sendToConsole(String msg){
        msgUtils.sendToConsole(BaseData.PLUGIN_PREFIX,msg);
    }
}
