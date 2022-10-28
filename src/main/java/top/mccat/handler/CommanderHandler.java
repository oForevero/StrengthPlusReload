package top.mccat.handler;

import com.sun.istack.internal.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import top.mccat.exception.ItemStrengthException;
import top.mccat.perm.StrengthPlusPermission;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.pojo.config.BaseConfig;
import top.mccat.service.StoneService;
import top.mccat.service.impl.StoneServiceImpl;
import top.mccat.ui.StrengthUi;
import top.mccat.utils.MsgUtils;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Distance
 * @date 2022/9/6
 */
public class CommanderHandler implements CommandExecutor {
    private StrengthUi strengthUi;
    private BaseConfig baseConfig;
    private MsgUtils msgUtils;
    private Map<String,StrengthStone> strengthStoneMap;
    private StoneService stoneService;
    private JavaPlugin plugin;
    /**
     * 判断数字的正则表达式
     */
    private final Pattern pattern = Pattern.compile("0|([-]?[1-9][0-9]*)");


    public CommanderHandler(JavaPlugin plugin) {
        this.baseConfig = BaseConfig.newInstance();
        this.msgUtils = MsgUtils.newInstance();
        this.strengthStoneMap = StrengthStone.newInstance();
        stoneService = new StoneServiceImpl();
        this.plugin = plugin;
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
            Player player = (Player)commandSender;
            switch(commandArray[0]){
                case "menu":
                    if(strengthUi == null){
                        break;
                    }
                    player.openInventory(strengthUi.getStrengthInventory());
                    break;
                case "givestone":
                    //当length为3，即没有指定玩家，默认给自己发送
                    if(commandArray.length == 3){
                        //强化石int值判断
                        if(!isInt(commandArray[2])){
                            msgUtils.sendToPlayer("&c 发送强化石失败，请确定您输入的是整型数值！",player);
                            return true;
                        }
                        try {
                            stoneService.sendStoneToPlayer(commandArray[1], player.getInventory(), Integer.parseInt(commandArray[2]));
                            msgUtils.sendToPlayer("&b 已向您发送了&a[&c"+Integer.parseInt(commandArray[2])+"&a]&b个"+
                                    strengthStoneMap.get(commandArray[1]).getName(),player);
                        } catch (ItemStrengthException e) {
                            msgUtils.sendToPlayer(e.getMessage(), player);
                            return true;
                        }
                    }else if(commandArray.length == 4){
                        //为4时即为指定玩家
                        if(!isInt(commandArray[3])){
                            msgUtils.sendToPlayer("&c 发送强化石失败，请确定您输入的是整型数值！",player);
                            return true;
                        }
                        //获取玩家位置
                        Player receivePlayer = plugin.getServer().getPlayer(commandArray[2]);
                        if(receivePlayer == null){
                            msgUtils.sendToPlayer("&c 玩家不存在，请确保当前玩家是否在线或存在！",player);
                            return true;
                        }
                        try {
                            stoneService.sendStoneToPlayer(commandArray[1], receivePlayer.getInventory(), Integer.parseInt(commandArray[3]));
                            msgUtils.sendToPlayer("&b 已向&a[&c"+receivePlayer.getName()+"&a]&b发送了&a[&c"+Integer.parseInt(commandArray[3])+"&a]&b个"
                                    +strengthStoneMap.get(commandArray[1]).getName(),receivePlayer);
                        } catch (ItemStrengthException e) {
                            msgUtils.sendToPlayer(e.getMessage(), player);
                            return true;
                        }
                    }
                    return true;
                case "reload":
                    plugin.reloadConfig();
                    msgUtils.sendToPlayer("&c 配置文件重载成功", player);
                    return true;
                default:
                    menuInfo(player);
                    break;
            }
        }
        return true;
    }

    /**
     * 判断是否为int
     * @param value 参数
     * @return 结果
     */
    private boolean isInt(String value){
        return pattern.matcher(value).matches();
    }

    /**
     * 发送指令 menu
     * @param player player.
     */
    public void menuInfo(Player player){
        msgUtils.sendToPlayer("","&4&l===----------&6&l[strengthPlus]&4&l----------===",player);
        msgUtils.sendToPlayer("","&a>>>          &a/sp 或 qh &b打开此帮助菜单", player);
        msgUtils.sendToPlayer("","&a>>>          &b/sp menu &b打开强化界面", player);
        msgUtils.sendToPlayer("","&a>>>          &a/sp help &b打开此帮助菜单", player);
        if(player.hasPermission(StrengthPlusPermission.ADMIN_PERMISSIONS) || player.isOp()){
            msgUtils.sendToPlayer("","&4&l===-------&6&l[strengthPlusAdmin]&4&l--------===", player);
            msgUtils.sendToPlayer("","&c>>>    &e/sp givestone &b获取强化石 可指定玩家和数量",player);
            msgUtils.sendToPlayer("","&c>>>          &c/sp reload &b重载配置文件",player);
        }
        msgUtils.sendToPlayer("","&4&l===&6&l--------------------------------&4&l===", player);
    }

    public void reloadConfig(){
        this.baseConfig = baseConfig.reloadConfigFile();
        msgUtils.reloadMsgConfig();
        this.strengthStoneMap = StrengthStone.newInstance();
        stoneService.reloadConfig();
        if(baseConfig.isEnableMenu()){
            if(this.strengthUi == null){
                this.strengthUi = new StrengthUi(plugin);
            }
            msgUtils.sendToConsole("&b UI模块&c已启用，请使用&a/sp menu&c进行UI强化功能！");
        }else{
            msgUtils.sendToConsole("&b UI模块&c已关闭！");
        }
        if(strengthUi != null) {
            strengthUi.reloadConfig();
        }
    }
}
