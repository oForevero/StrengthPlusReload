package top.mccat.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;

/**
 * 版本兼容工具类
 * 用于处理不同Minecraft版本间的差异
 * 
 * @author Raven
 * @date 2022/10/24
 */
public class VersionUtils {
    
    private static final int MAJOR_VERSION;
    private static final int MINOR_VERSION;
    
    static {
        int major = 1;
        int minor = 13; // 默认假设为1.13版本
        try {
            String version = Bukkit.getBukkitVersion();
            // 格式类似: 1.16.5-R0.1-SNAPSHOT 或 1.12.2-R0.1-SNAPSHOT
            String[] parts = version.split("-")[0].split("\\.");
            major = Integer.parseInt(parts[0]);
            minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        } catch (Exception e) {
            // 版本解析失败，使用默认值
            // 不在静态块中输出日志以避免循环依赖
        }
        MAJOR_VERSION = major;
        MINOR_VERSION = minor;
    }
    
    /**
     * 获取主版本号
     * @return 主版本号（如1.16中的1）
     */
    public static int getMajorVersion() {
        return MAJOR_VERSION;
    }
    
    /**
     * 获取次版本号
     * @return 次版本号（如1.16中的16）
     */
    public static int getMinorVersion() {
        return MINOR_VERSION;
    }
    
    /**
     * 检查当前版本是否大于等于指定版本
     * @param major 主版本号
     * @param minor 次版本号
     * @return 是否大于等于指定版本
     */
    public static boolean isVersionAtLeast(int major, int minor) {
        if (MAJOR_VERSION > major) {
            return true;
        }
        return MAJOR_VERSION == major && MINOR_VERSION >= minor;
    }
    
    /**
     * 获取兼容的材质
     * 对于1.13以下版本，需要使用旧版材质名称
     * @param newName 新版材质名称（1.13+）
     * @param legacyName 旧版材质名称（1.12.2及以下）
     * @return Material对象，如果都不存在则返回null
     */
    public static Material getMaterial(String newName, String legacyName) {
        Material material = null;
        try {
            material = Material.valueOf(newName);
        } catch (IllegalArgumentException e) {
            // 新版材质不存在，尝试旧版
            try {
                material = Material.valueOf(legacyName);
            } catch (IllegalArgumentException e2) {
                // 旧版也不存在
            }
        }
        return material;
    }
    
    /**
     * 获取材质，优先使用新名称，失败则尝试备用名称
     * @param name 材质名称
     * @return Material对象，如果不存在则返回null
     */
    public static Material getMaterialSafe(String name) {
        try {
            return Material.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * 获取染色玻璃板材质
     * 注意：1.12.2及以下版本使用STAINED_GLASS_PANE需要配合data value设置颜色
     * 此方法在1.12.2及以下版本仅返回基础材质，颜色将默认为白色
     * 建议在1.12.2服务器上使用此插件时升级到1.13+以获得完整的颜色支持
     * @param color 颜色名称（如WHITE, LIME, YELLOW, PINK, BLACK）
     * @return Material对象，如果找不到合适的材质则返回GLASS
     */
    public static Material getStainedGlassPane(String color) {
        // 1.13+ 使用 COLOR_STAINED_GLASS_PANE
        if (isVersionAtLeast(1, 13)) {
            Material mat = getMaterialSafe(color + "_STAINED_GLASS_PANE");
            if (mat != null) {
                return mat;
            }
        }
        // 1.12.2及以下使用 STAINED_GLASS_PANE (需要data value设置颜色)
        Material stainedGlass = getMaterialSafe("STAINED_GLASS_PANE");
        if (stainedGlass != null) {
            return stainedGlass;
        }
        // 最终备用方案
        Material glassPane = getMaterialSafe("GLASS_PANE");
        if (glassPane != null) {
            return glassPane;
        }
        return getMaterialSafe("GLASS");
    }
    
    /**
     * 获取染色玻璃板的data value（仅1.12.2及以下版本需要）
     * @param color 颜色名称
     * @return data value (0-15)，如果是1.13+版本返回0
     */
    public static short getStainedGlassPaneData(String color) {
        if (isVersionAtLeast(1, 13)) {
            return 0; // 1.13+不需要data value
        }
        // 1.12.2及以下的颜色data值
        switch (color.toUpperCase()) {
            case "WHITE": return 0;
            case "ORANGE": return 1;
            case "MAGENTA": return 2;
            case "LIGHT_BLUE": return 3;
            case "YELLOW": return 4;
            case "LIME": return 5;
            case "PINK": return 6;
            case "GRAY": return 7;
            case "LIGHT_GRAY": return 8;
            case "CYAN": return 9;
            case "PURPLE": return 10;
            case "BLUE": return 11;
            case "BROWN": return 12;
            case "GREEN": return 13;
            case "RED": return 14;
            case "BLACK": return 15;
            default: return 0;
        }
    }
    
    /**
     * 检查是否支持灵魂营火（1.16+）
     * @return 是否支持
     */
    public static boolean supportsSoulCampfire() {
        return isVersionAtLeast(1, 16);
    }
    
    /**
     * 获取火焰/营火材质（版本兼容）
     * @return Material对象
     */
    public static Material getFireMaterial() {
        if (isVersionAtLeast(1, 16)) {
            Material soulCampfire = getMaterialSafe("SOUL_CAMPFIRE");
            if (soulCampfire != null) {
                return soulCampfire;
            }
        }
        if (isVersionAtLeast(1, 14)) {
            Material campfire = getMaterialSafe("CAMPFIRE");
            if (campfire != null) {
                return campfire;
            }
        }
        // 1.14以下使用火焰或熔炉
        Material fire = getMaterialSafe("FIRE");
        if (fire != null) {
            return fire;
        }
        return getMaterialSafe("FURNACE");
    }
}
