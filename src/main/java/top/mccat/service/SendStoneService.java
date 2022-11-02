package top.mccat.service;

import org.bukkit.entity.Player;
import top.mccat.exception.ItemStrengthException;

/**
 * @author Kevin Li
 * @date 2022/9/28
 * @description
 */
public interface SendStoneService {
    /**
     * 生成强化石，并发送给用户
     * @param count 数量
     * @param player 玩家对象
     * @param key 从map中获取key
     */
    public void sendStoneToPlayer(int count, Player player, String key) throws ItemStrengthException;
}
