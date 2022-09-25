package top.mccat.service.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.enums.StrengthType;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.config.StrengthItem;
import top.mccat.service.StrengthService;
import top.mccat.utils.RomaMathGenerateUtil;

import java.util.List;

/**
 * @author Raven
 * @date 2022/09/25 21:38
 */
public class StrengthServiceImpl implements StrengthService {
    private StrengthItem strengthItem;
    private StrengthAttribute strengthAttribute;
    public StrengthServiceImpl() {
        this.strengthItem = StrengthItem.newInstance();
        this.strengthAttribute = StrengthAttribute.newInstance();
    }

    @Override
    public ItemStack strengthItem(ItemStack stack, Player player) {
        return null;
    }

    private int getLevel(ItemStack stack) {
        StrengthResult strengthResult = canBeStrength(stack);
        if(!strengthResult.isStrength()){
            return -1;
        }
        if(Material.AIR.equals(stack.getType())){
            return -1;
        }
        if(!stack.hasItemMeta()){
            return -1;
        }
        ItemMeta itemMeta = stack.getItemMeta();
        if(itemMeta == null || !itemMeta.hasLore()){
            return 0;
        }
        List<String> lore = itemMeta.getLore();
        if(lore == null || lore.size() == 0){
            return 0;
        }
        //包含title即为有等级
        if(!lore.contains(strengthAttribute.getTitle())){
            switch(strengthResult.getType()){
                case 0:
                    return getLevelFromList(lore, strengthAttribute.getArmorDefence());
                case 1:
                    return getLevelFromList(lore, strengthAttribute.getMeleeDamage());
                case 2:
                    return getLevelFromList(lore, strengthAttribute.getRemotelyDamage());
                default:
                    return 0;
            }
        }
        return -1;
    }

    private int getLevelFromList(List<String> lore, String attribute){
        int i = lore.indexOf(attribute);
        String romaLevel = lore.get(i).substring(attribute.length() + 2);
        return RomaMathGenerateUtil.romanToInt(romaLevel);
    }

    /**
     * 检测物品是否可以被强化
     * @param stack 物品堆对线
     * @return 物品是否可强化
     */
    private StrengthResult canBeStrength(ItemStack stack) {
        Material type = stack.getType();
        if(strengthItem.defenceCanBeStrength(type.name())){
            return new StrengthResult(true, StrengthType.ARMOR_TYPE.getType());
        }
        if(strengthItem.meleeCanBeStrength(type.name())){
            return new StrengthResult(true, StrengthType.WEAPON_TYPE.getType());
        }
        if(strengthItem.remoteCanBeStrength(type.name())){
            return new StrengthResult(true, StrengthType.BOW_TYPE.getType());
        }
        return new StrengthResult(false,-1);
    }
}

class StrengthResult {
    private boolean strength;
    private int type;

    public StrengthResult(boolean strength, int type) {
        this.strength = strength;
        this.type = type;
    }

    public boolean isStrength() {
        return strength;
    }

    public void setStrength(boolean strength) {
        this.strength = strength;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StrengthType{" +
                "strength=" + strength +
                ", type=" + type +
                '}';
    }
}
