package top.mccat;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import top.mccat.factory.ConfigFactory;
import top.mccat.handler.CommanderHandler;
import top.mccat.listener.AttackEventListener;
import top.mccat.perm.StrengthPlusPermission;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.utils.MsgUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Raven
 * @date 2022/09/05 15:36
 */
public class StrengthPlus extends JavaPlugin {
    private MsgUtils msgUtils;
    private ConfigFactory factory;
    private CommanderHandler commanderHandler;
    private Map<String,StrengthStone> strengthStone;
    private AttackEventListener attackEventListener;
    @Override
    public void onLoad() {
        msgUtils = MsgUtils.newInstance();
        factory = ConfigFactory.newInstance(this);
        strengthStone = StrengthStone.newInstance();
        sendToConsole("&c 正在检查yaml配置文件对象...");
        factory.writeConfigFile();
        sendToConsole("&b 配置文件检验成功！");
        attackEventListener = new AttackEventListener();
    }

    @Override
    public void onEnable() {
        commanderHandler = new CommanderHandler(this);
        //设置tab联想
        Objects.requireNonNull(Bukkit.getPluginCommand("sp")).setTabCompleter(this);
        Objects.requireNonNull(Bukkit.getPluginCommand(BaseData.SP_COMMAND)).setExecutor(commanderHandler);
        getServer().getPluginManager().registerEvents(attackEventListener,this);
    }

    @Override
    public void onDisable() {
        sendToConsole("&c 正在卸载插件配置...");
    }

    @Override
    public void reloadConfig() {
        sendToConsole("&a 正在重载插件配置文件...");
        super.reloadConfig();
        strengthStone = StrengthStone.newInstance();
        factory = ConfigFactory.newInstance(this);
        commanderHandler.reloadConfig();
        attackEventListener.reloadConfig();
        sendToConsole("&c 重载成功！");
    }

    private void sendToConsole(String msg){
        msgUtils.sendToConsole(BaseData.PLUGIN_PREFIX,msg);
    }

    /**
     * 子命令联想
     */
    private final String[] subUserCommands = {"menu", "help"};
    private final String[] subCommands = {"menu", "help", "givestone", "reload"};
    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 3){
            return getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        if (args.length == 2) {
            return strengthStone.keySet().stream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
        }
        if(args.length == 1) {
            if(sender.hasPermission(StrengthPlusPermission.ADMIN_PERMISSIONS)){
                return Arrays.stream(subCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            }
        }
        if (args.length == 0) {
            return Arrays.asList(subUserCommands);
        }
        return Arrays.stream(subUserCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }

}
