package top.mccat.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import top.mccat.exception.ItemStrengthException;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.service.impl.StrengthServiceImpl;

/**
 * @author Distance
 * @date 2022/9/6
 */
public interface StrengthService {
    /**
     * 强化物品方法
     * @param stack 物品堆对象
     * @param player 玩家对象
     * @param strengthResult 等级参数
     */
    boolean strengthItemInUi(ItemStack stack, Player player, StrengthServiceImpl.StrengthResult strengthResult);

    /**
     * 获取等级
     * @param stack 物品堆
     * @param strengthStone 强化石
     * @param strengthExtraStone 强化石物品堆
     * @param stoneExtra 强化券物品堆
     * @return 强化结果集
     * @throws ItemStrengthException 物品不能被强化异常
     */
    StrengthServiceImpl.StrengthResult getLevel(ItemStack stack, ItemStack[] strengthStone, StrengthStone strengthExtraStone, ItemStack stoneExtra) throws ItemStrengthException;

    /**
     * 物品是否可被强化
     * @param stack 物品堆
     * @return 强化结果集
     */
    StrengthServiceImpl.StrengthResult canBeStrength(ItemStack stack);

    /**
     * 重载配置
     */
    void reloadConfig();
}
