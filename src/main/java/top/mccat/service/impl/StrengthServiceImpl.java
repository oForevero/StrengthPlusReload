package top.mccat.service.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.enums.StrengthType;
import top.mccat.exception.ItemStrengthException;
import top.mccat.pojo.bean.LevelValue;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.config.StrengthItem;
import top.mccat.service.StrengthService;
import top.mccat.utils.LoreGenerateUtils;
import top.mccat.utils.MsgUtils;
import top.mccat.utils.RomaMathGenerateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private Map<String, StrengthStone> strengthStoneMap;
    private final RomaMathGenerateUtil romaMathGenerateUtil;
    public StrengthServiceImpl() {
        this.strengthItem = StrengthItem.newInstance();
        this.strengthAttribute = StrengthAttribute.newInstance();
        this.loreGenerateUtils = LoreGenerateUtils.newInstance();
        this.msgUtils = MsgUtils.newInstance();
        this.levelValues = LevelValue.newInstance();
        this.romaMathGenerateUtil = new RomaMathGenerateUtil();
        this.strengthStoneMap = StrengthStone.newInstance();
    }

    @Override
    public void strengthItemInUi(ItemStack stack, Player player, StrengthResult strengthResult) {
        int level = strengthResult.getLevel();
        if(level == levelValues.size()){
            msgUtils.sendToPlayer("&c当前强化物品等级已满，无法进行强化操作！",player);
        }
        if(level == -1){
            msgUtils.sendToPlayer("&c当前强化物品无法进行强化操作！",player);
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
    public StrengthResult getLevel(ItemStack stack, ItemStack[] strengthStone, ItemStack strengthExtraStone) throws ItemStrengthException {
        StrengthResult strengthResult = canBeStrength(stack);
        if(!strengthResult.isStrength() || Material.AIR.equals(stack.getType())){
            throw new ItemStrengthException("&c 错误，物品无法被强化，请检查当前物品是否为" +
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
        if(lore.contains(strengthAttribute.getTitle())){
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
            List<String> strengthStones = levelValue.getStrengthStones();
            stoneCheckAndCost(strengthStones, strengthStone);
        }
        return strengthResult;
    }

    /**
     * 进行强化石检测，当前等级是否能强化
     * @param stoneKeys 强化石lore
     * @param strengthStones 强化石
     */
    private void stoneCheckAndCost(List<String> stoneKeys, ItemStack[] strengthStones) throws ItemStrengthException {
        List<StrengthStone> stoneList = new ArrayList<>();
        StringBuilder stoneName = new StringBuilder();
        for (String stoneKey : stoneKeys) {
            StrengthStone strengthStone = strengthStoneMap.get(stoneKey);
            if(strengthStone!=null){
                stoneList.add(strengthStone);
                stoneName.append(strengthStone.getName()).append(" ");
            }
        }
        int count = 0;
        //执行强化石校验操作
        for (ItemStack strengthStone : strengthStones) {
            if(strengthStone==null){
                continue;
            }
            if(strengthStone.getType().equals(Material.AIR) || !strengthStone.hasItemMeta()){
                continue;
            }
            ItemMeta itemMeta = strengthStone.getItemMeta();
            for (StrengthStone stone : stoneList) {
                assert itemMeta != null;
                if (!strengthStone.getItemMeta().getDisplayName().equals(stone.getName())) {
                    continue;
                }
                if (!stone.getName().equals(itemMeta.getDisplayName())) {
                    continue;
                }
                if (!stone.getLore().equals(itemMeta.getLore())) {
                    continue;
                }
                count++;
                //进行强化石扣除，如果两个全扣则无问题，扣单个则进行补偿
                strengthStone.setAmount(strengthStone.getAmount()-1);
                //如果已经符合单个强化石数量，直接跳出循环
                if(count == stoneKeys.size()){
                    break;
                }
            }
        }
        if(count != stoneKeys.size()){
            //进行强化石补偿
            strengthStones[0].setAmount(strengthStones[0].getAmount()+1);
            throw new ItemStrengthException("&c 强化失败，您的强化石不匹配，请确保您有："+ stoneName);
        }
    }

    private int getLevelFromList(List<String> lore, String attribute) throws ItemStrengthException {
        int i = lore.indexOf(strengthAttribute.getTitle());
        if(i == -1){
            throw new ItemStrengthException("&c 强化失败，您的等级lore参数不匹配，无法进行强化操作");
        }
        List<String> subLore = lore.subList(i, lore.size());
        String romaLevel;
        for (String s : subLore) {
            if (s.contains(attribute)) {
                romaLevel = s.substring(attribute.length() + 2);
                return romaMathGenerateUtil.romanToInt(romaLevel);
            }
        }
        throw new ItemStrengthException("&c 强化失败，查找等级参数失败");
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
