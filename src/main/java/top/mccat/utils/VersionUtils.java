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
            MsgUtils.newInstance().sendToConsole("&c[StrengthPlus] 版本解析失败，使用默认兼容模式 (1.13)");
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
     * @param color 颜色名称（如WHITE, LIME, YELLOW, PINK, BLACK）
     * @return Material对象
     */
    public static Material getStainedGlassPane(String color) {
        // 1.13+ 使用 COLOR_STAINED_GLASS_PANE
        // 1.12.2及以下使用 STAINED_GLASS_PANE (带数据值)
        if (isVersionAtLeast(1, 13)) {
            return getMaterialSafe(color + "_STAINED_GLASS_PANE");
        } else {
            return getMaterialSafe("STAINED_GLASS_PANE");
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
