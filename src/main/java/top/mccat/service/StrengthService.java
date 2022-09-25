package top.mccat.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Distance
 * @date 2022/9/6
 */
public interface StrengthService {
    /**
     * 强化物品方法
     * @param stack 物品堆对象
     * @param player 玩家对象
     * @return 物品堆
     */
    public ItemStack strengthItem(ItemStack stack, Player player);
}
