package top.mccat;

import org.bukkit.plugin.java.JavaPlugin;
import top.mccat.factory.ConfigFactory;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.bean.LevelValue;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.pojo.config.*;
import top.mccat.pojo.dao.YamlConfigObject;
import top.mccat.utils.MsgUtils;
import top.mccat.utils.YamlLoadUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Raven
 * @date 2022/09/05 15:36
 */
public class StrengthPlus extends JavaPlugin {
    private MsgUtils msgUtils;
    private ConfigFactory factory;
    @Override
    public void onLoad() {
        msgUtils = MsgUtils.newInstance();
        factory = ConfigFactory.newInstance(this);
        factory.writeConfigFile();
        sendToConsole("&c 正在初始化StrengthPlus中，请稍后...");
    }

    @Override
    public void onEnable() {
        sendToConsole("&c 正在注入yaml配置文件对象...");
        MsgUtils msgUtils = MsgUtils.newInstance();
        msgUtils.sendToBroadcast("demo");
        try {
            /*Optional<Object> o = YamlLoadUtils.loadConfigObject("strength-attribute.yml", String.valueOf(this.getDataFolder()),
                    "strength-attribute", StrengthAttribute.class);
            System.out.println(o.get());*/
            /*Optional<YamlConfigObject> o1 = YamlLoadUtils.loadConfigObject("strength-stone.yml", String.valueOf(this.getDataFolder()),
                    "strength-stone", StrengthStone.class);
            Map<String, StrengthStone> strengthStoneMap = (Map<String, StrengthStone>) o1.get();
            Set<String> keySet = strengthStoneMap.keySet();
            for (String s : keySet) {
                System.out.println(strengthStoneMap.get(s));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*try {
            Optional<Object> o = YamlLoadUtils.loadConfigObject("strength-level.yml", String.valueOf(this.getDataFolder()),
                    "strength-level", LevelValue.class);
            System.out.println(o.get());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
