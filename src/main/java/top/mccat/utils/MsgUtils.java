package top.mccat.utils;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import top.mccat.enums.BaseDir;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.config.BaseConfig;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author Raven
 * @date 2022/09/05 18:42
 */
public class MsgUtils {
    private final CommandSender commandSender = Bukkit.getConsoleSender();
    private BaseConfig baseConfig = new BaseConfig();

    private MsgUtils(){
        reloadMsgConfig();
    }

    public static MsgUtils newInstance(){
        return new MsgUtils();
    }

    /**
     * 向玩家发送信息
     * @param msg 消息
     * @param player 玩家对象
     */
    public void sendToPlayer(@NotNull String msg, @NotNull Player player){
        player.sendMessage(ColorParseUtils.parseColorStr(baseConfig.getPluginName()+msg));
    }

    /**
     * 向玩家发送信息
     * @param msg 消息
     * @param player 玩家对象
     */
    public void sendToPlayer(@NotNull String title, @NotNull String msg, @NotNull Player player){
        player.sendMessage(ColorParseUtils.parseColorStr(title+msg));
    }

    /**
     * 发送到控制台
     * @param msg 消息
     */
    public void sendToConsole(@NotNull String msg){
        commandSender.sendMessage(ColorParseUtils.parseColorStr(baseConfig.getPluginName()+msg));
    }

    /**
     * 发送到控制台
     * @param title 标题
     * @param msg 消息
     */
    public void sendToConsole(@NotNull String title, @NotNull String msg){
        commandSender.sendMessage(ColorParseUtils.parseColorStr(title+msg));
    }

    /**
     * 发送广播信息
     * @param msg 消息
     */
    public void sendToBroadcast(@NotNull String msg){
        Bukkit.broadcastMessage(ColorParseUtils.parseColorStr(baseConfig.getPluginName()+msg));
    }

    /**
     * 发送广播信息
     * @param title 标题
     * @param msg 消息
     */
    public void sendToBroadcast(@NotNull String title, @NotNull String msg){
        Bukkit.broadcastMessage(ColorParseUtils.parseColorStr(title+msg));
    }

    /**
     * 重载配置文件
     */
    public void reloadMsgConfig(){
        /*try {
            Optional<Object> config = YamlLoadUtils.loadYamlAsObject("config.yml", BaseDir.BASE_DIR.getDir(),
                    "strengthPlus", BaseConfig.class, null);
            config.ifPresent(o -> baseConfig = (BaseConfig) o);
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            sendToConsole(BaseData.PLUGIN_PREFIX,"&c错误，io流读取失败或没有权限进行操作！");
        }*/
    }
}
