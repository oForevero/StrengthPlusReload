package top.mccat.utils;

import org.bukkit.ChatColor;

/**
 * @author Raven
 * @date 2022/09/05 17:31
 */
public class ColorParseUtils {
    /**
     * 颜色解析方法
     * @param strValue 包含 & 的颜色字符
     * @return 将 & 转换为 §
     */
    public static String parseColorStr(String strValue){
        return ChatColor.translateAlternateColorCodes('&',strValue);
    }
}
