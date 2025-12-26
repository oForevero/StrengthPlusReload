package top.mccat.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.mccat.perm.StrengthPlusPermission;
import top.mccat.pojo.config.BaseConfig;

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
    public void sendToPlayer(String msg, Player player){
        player.sendMessage(ColorParseUtils.parseColorStr(baseConfig.getPluginName()+msg));
    }

    /**
     * 向玩家发送信息
     * @param msg 消息
     * @param player 玩家对象
     */
    public void sendToPlayer(String title, String msg, Player player){
        player.sendMessage(ColorParseUtils.parseColorStr(title+msg));
    }

    /**
     * 向管理员发送信息
     * @param msg 消息
     * @param admin 管理员
     */
    public void sendToAdmin(String msg, Player admin){
        if(!admin.isOp() || !admin.hasPermission(StrengthPlusPermission.ADMIN_PERMISSIONS)){
            return;
        }
        if(!baseConfig.isDebug()){
            return;
        }
        admin.sendMessage(ColorParseUtils.parseColorStr("&b[&cStrengthPlus Logger&b]"+msg));
    }

    /**
     * 发送到控制台
     * @param msg 消息
     */
    public void sendToConsole(String msg){
        commandSender.sendMessage(ColorParseUtils.parseColorStr(baseConfig.getPluginName()+msg));
    }

    /**
     * 发送到控制台
     * @param title 标题
     * @param msg 消息
     */
    public void sendToConsole(String title, String msg){
        commandSender.sendMessage(ColorParseUtils.parseColorStr(title+msg));
    }

    /**
     * 向管理员发送信息
     * @param msg 消息
     */
    public void sendDebugMsgToConsole(String msg){
        if(!baseConfig.isDebug()){
            return;
        }
        commandSender.sendMessage(ColorParseUtils.parseColorStr("&b[&cStrengthPlus Logger&b]"+msg));
    }

    /**
     * 发送广播信息
     * @param msg 消息
     */
    public void sendToBroadcast(String msg){
        Bukkit.broadcastMessage(ColorParseUtils.parseColorStr(baseConfig.getPluginName()+msg));
    }

    /**
     * 发送广播信息
     * @param title 标题
     * @param msg 消息
     */
    public void sendToBroadcast(String title, String msg){
        Bukkit.broadcastMessage(ColorParseUtils.parseColorStr(title+msg));
    }

    /**
     * 重载配置文件
     */
    public void reloadMsgConfig(){
        baseConfig = baseConfig.reloadConfigFile();
    }
}
