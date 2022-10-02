package top.mccat.dao.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.dao.StoneDao;
import top.mccat.pojo.bean.StrengthStone;

import java.util.Objects;

/**
 * @author Distance
 * @date 2022/10/2
 */
public class StoneDaoImpl implements StoneDao {

    @Override
    public ItemStack giveStoneMeta(StrengthStone strengthStone) {
        Material material = Objects.requireNonNull(Material.getMaterial(strengthStone.getItem()));
        ItemStack stack = new ItemStack(material);
        ItemMeta itemMeta = stack.getItemMeta();
        //断定传来的必定是非空，请在service进行判断
        assert itemMeta != null;
        itemMeta.setLore(strengthStone.getLore());
        itemMeta.setDisplayName(strengthStone.getName());
        stack.setItemMeta(itemMeta);
        return stack;
    }

}
