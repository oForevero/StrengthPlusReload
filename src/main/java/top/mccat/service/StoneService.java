package top.mccat.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import top.mccat.exception.ItemStrengthException;
import top.mccat.exception.StrengthItemNotFountException;

/**
 * @author Distance
 * @date 2022/10/2
 */
public interface StoneService {
    /**
     * 给与强化石物品
     * @param stoneName 强化石名称
     * @param playerInventory 玩家仓库
     * @param amount 数量
     * @throws ItemStrengthException 物品未找到异常
     */
    void sendStoneToPlayer(String stoneName, PlayerInventory playerInventory, int amount) throws ItemStrengthException;
}
