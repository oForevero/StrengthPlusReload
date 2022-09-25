package top.mccat.handler;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.config.BaseConfig;
import top.mccat.pojo.config.StrengthMenu;
import top.mccat.ui.StrengthUi;
import top.mccat.utils.MsgUtils;

/**
 * @author Distance
 * @date 2022/9/6
 */
public class CommanderHandler implements CommandExecutor {
    private StrengthUi strengthUi;
    private final BaseConfig baseConfig;
    private final MsgUtils msgUtils;
    public CommanderHandler(JavaPlugin plugin) {
        this.baseConfig = BaseConfig.newInstance();
        this.msgUtils = MsgUtils.newInstance();
        if(baseConfig.isEnableMenu()){
            this.strengthUi = new StrengthUi(plugin);
            PluginManager pluginManager = plugin.getServer().getPluginManager();
            pluginManager.registerEvents(strengthUi,plugin);
            msgUtils.sendToConsole("&b UI模块&c已启用，请使用&a/sp menu&c进行UI强化功能");
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String mainCommand, @NotNull String[] commandArray) {
        if(commandSender instanceof Player){
            if (BaseData.SP_COMMAND.equalsIgnoreCase(mainCommand) || BaseData.QH_COMMAND.equalsIgnoreCase(mainCommand) || BaseData.DEFAULT_COMMAND  .equalsIgnoreCase(mainCommand)){
                Player player = (Player)commandSender;
                switch(commandArray[0]){
                    case "":
                        //执行菜单提示
                        break;
                    case "menu":
                        if(strengthUi == null){
                            break;
                        }
                        player.openInventory(strengthUi.getStrengthInventory());
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }
}
