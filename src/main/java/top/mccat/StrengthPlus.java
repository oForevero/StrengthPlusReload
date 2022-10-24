package top.mccat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import top.mccat.factory.ConfigFactory;
import top.mccat.handler.CommanderHandler;
import top.mccat.listener.AttackEventListener;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.bean.LevelValue;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.pojo.config.*;
import top.mccat.pojo.dao.YamlConfigObject;
import top.mccat.utils.MsgUtils;
import top.mccat.utils.YamlLoadUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author Raven
 * @date 2022/09/05 15:36
 */
public class StrengthPlus extends JavaPlugin {
    private MsgUtils msgUtils;
    private ConfigFactory factory;
    private CommanderHandler commanderHandler;
    @Override
    public void onLoad() {
        msgUtils = MsgUtils.newInstance();
        factory = ConfigFactory.newInstance(this);
        sendToConsole("&c 正在检查yaml配置文件对象...");
        factory.writeConfigFile();
        sendToConsole("&b 配置文件检验成功！");
    }

    @Override
    public void onEnable() {
        commanderHandler = new CommanderHandler(this);
        Objects.requireNonNull(Bukkit.getPluginCommand(BaseData.SP_COMMAND)).setExecutor(commanderHandler);
        getServer().getPluginManager().registerEvents(new AttackEventListener(),this);
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
