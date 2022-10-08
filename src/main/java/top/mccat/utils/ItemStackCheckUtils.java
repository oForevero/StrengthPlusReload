package top.mccat.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Kevin Li
 * @date 2022/10/8
 * @description
 */
public class ItemStackCheckUtils {
    public static boolean notNullAndAir(ItemStack stack){
        return stack != null && stack.getType() != Material.AIR;
    }
}
