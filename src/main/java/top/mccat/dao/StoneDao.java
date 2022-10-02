package top.mccat.dao;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.pojo.bean.StrengthStone;

/**
 * @author Distance
 * @date 2022/10/2
 */
public interface StoneDao {
    /**
     * 给予石头方法
     * @param strengthStone 强化石对象
     * @return 强化石物品
     */
    ItemStack giveStoneMeta(StrengthStone strengthStone);
}
