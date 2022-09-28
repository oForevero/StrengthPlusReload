package top.mccat.dao;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Kevin Li
 * @date 2022/9/28
 * @description
 */
public interface SendStoneDao {
    /**
     * 发送给玩家，设置到单个位置
     * @param index 需要更改的位置
     * @param stack 物品堆
     * @param player 玩家对象
     */
    public void send(int index,ItemStack stack, Player player);
}
