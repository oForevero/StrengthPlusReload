package top.mccat.dao.impl;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.dao.StrengthDao;
import top.mccat.pojo.list.LoreList;

/**
 * @author Raven
 * @date 2022/09/25 21:28
 */
public class StrengthDaoImpl implements StrengthDao {
    @Override
    public ItemStack strengthItem(ItemStack stack, LoreList<String> stringLoreList) {
        ItemMeta itemMeta = stack.getItemMeta();
        //在进行强化前会进行校验，故断言必不为空
        assert itemMeta != null;
        itemMeta.setLore(stringLoreList);
        stack.setItemMeta(itemMeta);
        return stack;
    }
}
