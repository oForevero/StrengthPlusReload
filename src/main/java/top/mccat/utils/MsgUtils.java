package top.mccat.utils;

import org.bukkit.Color;
import org.bukkit.entity.Player;

/**
 * @author Raven
 * @date 2022/09/05 18:42
 */
public class MsgUtils {
    public static void sendToPlayer(String msg, Player player){
        player.sendMessage(ColorParseUtils.parseColorStr(msg));
    }

    public static void sendToConsole(){

    }

    public static void sendToBroadcast(){

    }
}
