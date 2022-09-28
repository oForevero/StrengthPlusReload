package top.mccat.dao.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import top.mccat.dao.SendStoneDao;

/**
 * @author Kevin Li
 * @date 2022/9/28
 * @description
 */
public class SendStoneDaoImpl implements SendStoneDao {
    @Override
    public void send(int index ,ItemStack stack, Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setItem(index, stack);
    }
}
