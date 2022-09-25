package top.mccat.dao;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.pojo.list.LoreList;

/**
 * @author Distance
 * @date 2022/9/6
 */
public interface StrengthDao {
    /**
     * 执行强化操作接口
     * @param stack 强化物品堆数据
     * @param stringLoreList 强化Lore
     * @return 执行强化操作
     */
    public ItemStack strengthItem(ItemStack stack, LoreList<String> stringLoreList);
}
