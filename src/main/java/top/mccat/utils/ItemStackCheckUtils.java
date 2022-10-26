package top.mccat.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Kevin Li
 * @date 2022/10/8
 * @description
 */
public class ItemStackCheckUtils {
    /**
     * 非空 itemstack
     * @param stack 物品堆
     * @return
     */
    public static boolean notNullAndAir(ItemStack stack){
        return stack != null && stack.getType() != Material.AIR;
    }

    /**
     * 非空ItemMeta验证
     * @param stack 物品堆
     * @return 结果布尔
     */
    public static boolean notNullMeta(ItemStack stack){
        if(notNullAndAir(stack)){
            return stack.hasItemMeta();
        }
        return false;
    }
}
