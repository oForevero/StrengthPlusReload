package top.mccat.service.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.enums.StrengthType;
import top.mccat.exception.ItemCanBeStrengthException;
import top.mccat.pojo.bean.LevelValue;
import top.mccat.pojo.config.BaseConfig;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.config.StrengthItem;
import top.mccat.pojo.config.StrengthLevel;
import top.mccat.service.StrengthService;
import top.mccat.utils.LoreGenerateUtils;
import top.mccat.utils.MsgUtils;
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
    private List<LevelValue> levelValues;
    private MsgUtils msgUtils;
    private final RomaMathGenerateUtil romaMathGenerateUtil;
    public StrengthServiceImpl() {
        this.strengthItem = StrengthItem.newInstance();
        this.strengthAttribute = StrengthAttribute.newInstance();
        this.loreGenerateUtils = LoreGenerateUtils.newInstance();
        this.msgUtils = MsgUtils.newInstance();
        this.levelValues = LevelValue.newInstance();
        this.romaMathGenerateUtil = new RomaMathGenerateUtil();
    }

    @Override
    public void strengthItemInUi(ItemStack stack, Player player, StrengthResult strengthResult) {
        int level = strengthResult.getLevel();
        if(level == levelValues.size()){
            msgUtils.sendToPlayer("&c当前强化物品等级已满，无法进行强化操作！",player);
        }
        List<String> lore = null;
        switch (strengthResult.getType()){
            case 0:
                lore = loreGenerateUtils.generateAttributesLore(level, null, strengthAttribute.getArmorDefence(), StrengthType.ARMOR_TYPE);
                break;
            case 1:
                lore = loreGenerateUtils.generateAttributesLore(level, null, strengthAttribute.getMeleeDamage(), StrengthType.WEAPON_TYPE);
                break;
            default:
                break;
        }
        ItemMeta itemMeta = stack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setLore(lore);
        stack.setItemMeta(itemMeta);
    }

    @Override
    public StrengthResult getLevel(ItemStack stack, ItemStack[] strengthStone, ItemStack strengthExtraStone) throws ItemCanBeStrengthException {
        StrengthResult strengthResult = canBeStrength(stack);
        if(!strengthResult.isStrength() || Material.AIR.equals(stack.getType())){
            throw new ItemCanBeStrengthException("&c 错误，物品无法被强化，请检查当前物品是否为" +
                    "可强化物品，或强化物品为空");
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
            int level = 0;
            switch(strengthResult.getType()){
                case 0:
                    level = getLevelFromList(lore, strengthAttribute.getArmorDefence());
                    break;
                case 1:
                    level = getLevelFromList(lore, strengthAttribute.getMeleeDamage());
                    break;
                case 2:
                    level = getLevelFromList(lore, strengthAttribute.getRemotelyDamage());
                    break;
                default:
                    break;
            }
            LevelValue levelValue = levelValues.get(level + 1);
            //进行强化操作
        }
        return strengthResult;
    }

    private int getLevelFromList(List<String> lore, String attribute){
        int i = lore.indexOf(attribute);
        String romaLevel = lore.get(i).substring(attribute.length() + 2);
        return romaMathGenerateUtil.romanToInt(romaLevel);
    }

    /**
     * 检测物品是否可以被强化
     * @param stack 物品堆对线
     * @return 物品是否可强化
     */
    @Override
    public StrengthResult canBeStrength(ItemStack stack) {
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
        return new StrengthResult(false, -1);
    }

    public static class StrengthResult {
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
}