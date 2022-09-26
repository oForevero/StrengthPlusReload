package top.mccat.service.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.enums.StrengthType;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.config.StrengthItem;
import top.mccat.service.StrengthService;
import top.mccat.utils.LoreGenerateUtils;
import top.mccat.utils.RomaMathGenerateUtil;

import java.util.List;

/**
 * @author Raven
 * @date 2022/09/25 21:38
 */
public class StrengthServiceImpl implements StrengthService {
    private StrengthItem strengthItem;
    private StrengthAttribute strengthAttribute;
    private LoreGenerateUtils loreGenerateUtils;
    public StrengthServiceImpl() {
        this.strengthItem = StrengthItem.newInstance();
        this.strengthAttribute = StrengthAttribute.newInstance();
        this.loreGenerateUtils = LoreGenerateUtils.newInstance();
    }

    @Override
    public ItemStack strengthItem(ItemStack stack, Player player) {
        StrengthResult strengthLevel = getLevel(stack);
        int level = strengthLevel.getLevel();
        //loreGenerateUtils.generateAttributesLore(level, null,sa)

        return null;
    }

    private StrengthResult getLevel(ItemStack stack) {
        StrengthResult strengthResult = canBeStrength(stack);
        if(!strengthResult.isStrength()){
            return strengthResult;
        }
        if(Material.AIR.equals(stack.getType())){
            return new StrengthResult(false,-1);
        }
        if(!stack.hasItemMeta()){
            return new StrengthResult(false,-1);
        }
        ItemMeta itemMeta = stack.getItemMeta();
        if(itemMeta == null || !itemMeta.hasLore()){
            strengthResult.setLevel(0);
            return strengthResult;
        }
        List<String> lore = itemMeta.getLore();
        if(lore == null || lore.size() == 0){
            strengthResult.setLevel(0);
            return strengthResult;
        }
        //包含title即为有等级
        if(!lore.contains(strengthAttribute.getTitle())){
            switch(strengthResult.getType()){
                case 0:
                    strengthResult.setLevel(getLevelFromList(lore, strengthAttribute.getArmorDefence()));
                    break;
                case 1:
                    strengthResult.setLevel(getLevelFromList(lore, strengthAttribute.getMeleeDamage()));
                    break;
                case 2:
                    strengthResult.setLevel(getLevelFromList(lore, strengthAttribute.getRemotelyDamage()));
                    break;
                default:
                    break;
            }
        }
        return strengthResult;
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
    private int level;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "StrengthResult{" +
                "strength=" + strength +
                ", type=" + type +
                ", level=" + level +
                '}';
    }
}
