package top.mccat.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import top.mccat.exception.ItemCanBeStrengthException;
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
    public void strengthItemInUi(ItemStack stack, Player player, StrengthServiceImpl.StrengthResult strengthResult);

    /**
     * 获取等级
     * @param stack 物品堆
     * @return 强化结果集
     * @throws ItemCanBeStrengthException 物品不能被强化异常
     */
    public StrengthServiceImpl.StrengthResult getLevel(ItemStack stack) throws ItemCanBeStrengthException;

    /**
     * 物品是否可被强化
     * @param stack 物品堆
     * @return 强化结果集
     */
    public StrengthServiceImpl.StrengthResult canBeStrength(ItemStack stack);
}
