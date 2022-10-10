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
import java.util.Random;

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
    /**
     * 定义强化随机几率变量
     */
    private final Random random = new Random();
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
    public boolean strengthItemInUi(ItemStack stack, Player player, StrengthResult strengthResult) {
        boolean result = false;
        int level = strengthResult.getLevel();
        if(level == -1){
            msgUtils.sendToPlayer("&c当前强化物品无法进行强化操作！", player);
            return false;
        }
        LevelValue levelValue = levelValues.get(level);
        //如果是admin直接升级到满级
        if(strengthResult.isAdmin()){
            level = levelValues.size();
        }else {
            //如果不是必定成功则进行强化判断
            if(strengthResult.isSuccess()){
                level += 1;
                result = true;
            }else {
                if(strengthResult(levelValue, strengthResult)){
                    level += 1;
                    result = true;
                }else{
                    //如果设置允许丢失等级，则使其丢失等级，如果默认为0则清除lore
                    if(levelValue.isLoseLevel()){
                        if(level > 0){
                            level -= 1;
                        }else{
                            ItemMeta itemMeta = stack.getItemMeta();
                            itemMeta.setLore(null);
                            stack.setItemMeta(itemMeta);
                        }
                    }
                    //如果允许破坏则设置为空气
                    if(levelValue.isCanBreak()) {
                        //如果为不安全，则设置其为空气
                        if(!strengthResult.isSafe()){
                            stack.setType(Material.AIR);
                            player.getOpenInventory().setItem(19,stack);
                            msgUtils.sendToPlayer("&c很遗憾，您的强化失败了，并且没有保护石的保护，您的武器被摧毁了！",player);
                            return false;
                        }
                        //允许广播通知
                        msgUtils.sendToConsole("&a[&b"+player.getName()+"&a]&c 在强化Ta的武器时，强化炉发生了爆炸，所幸有强化保护卷的存在装备并没有损坏");
                    }
                    msgUtils.sendToPlayer("&c很遗憾，您的强化失败了！",player);
                }
            }
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
        return result;
    }

    @Override
    public StrengthResult getLevel(ItemStack stack, ItemStack[] strengthStone, StrengthStone strengthExtraStone, ItemStack stoneExtra) throws ItemStrengthException {
        StrengthResult strengthResult = canBeStrength(stack);
        if(!strengthResult.isStrength() || Material.AIR.equals(stack.getType())){
            throw new ItemStrengthException("&c 错误，物品无法被强化，请检查当前物品是否为" +
                    "可强化物品，或强化物品为空");
        }
        ItemMeta itemMeta = stack.getItemMeta();
        //进行额外强化几率设置
        parseStrengthExtraStone(strengthExtraStone,strengthResult);
        if(itemMeta == null || !itemMeta.hasLore()){
            strengthResult.setLevel(0);
            LevelValue levelValue = levelValues.get(0);
            List<String> strengthStones = levelValue.getStrengthStones();
            stoneCheckAndCost(strengthStones, strengthStone, strengthExtraStone, stoneExtra, strengthResult);
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
            if(level == levelValues.size()){
                throw new ItemStrengthException("&c当前强化物品等级已满，无法进行强化操作！");
            }
            strengthResult.setLevel(level);
            LevelValue levelValue = levelValues.get(level);
            List<String> strengthStones = levelValue.getStrengthStones();
            stoneCheckAndCost(strengthStones, strengthStone, strengthExtraStone, stoneExtra, strengthResult);
        }
        return strengthResult;
    }

    /**
     * 将额外强化石/强化券参数传递给强化方法
     *
     * @param strengthStone  强化石
     * @param strengthResult 强化结果对象
     */
    private void parseStrengthExtraStone(StrengthStone strengthStone, StrengthResult strengthResult){
        //如果上级map取出为空则直接return
        if(strengthStone == null){
            return;
        }
        if(strengthStone.isAdmin()){
            strengthResult.setAdmin(true);
        }
        if(strengthStone.isSuccess()){
            strengthResult.setSuccess(true);
        }
        if(strengthStone.isSafe()){
            strengthResult.setSafe(true);
        }
    }

    /**
     * 进行强化几率返回的方法
     * @param levelValue 强化等级对象
     * @param strengthResult 强化结果对象
     * @return 是否强化成功
     */
    private boolean strengthResult(LevelValue levelValue, StrengthResult strengthResult){
        int chance = levelValue.getChance();
        //如果有额外机率，进行增加
        if(strengthResult.getChanceExtra() > 0) {
            chance += strengthResult.getChanceExtra();
        }
        int randomChance = random.nextInt(100);
        return randomChance < chance;
    }

    /**
     * 进行强化石检测，当前等级是否能强化
     * @param stoneKeys 强化石lore
     * @param strengthStones 强化石
     */
    private void stoneCheckAndCost(List<String> stoneKeys, ItemStack[] strengthStones, StrengthStone strengthExtraStone, ItemStack stoneExtra, StrengthResult strengthResult) throws ItemStrengthException {
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
        ItemStack bufferStack = null;
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
                //执行count++即存在符合强化石，执行相对应逻辑代码
                count++;
                if(stone.getChanceExtra() > 0){
                    strengthResult.setChanceExtra(stone.getChanceExtra());
                }
                //进行强化石扣除，这里将可能被补偿的强化石进行buffer缓冲存储
                bufferStack = strengthStone;
                strengthStone.setAmount(strengthStone.getAmount()-1);
                break;
            }
            //如果已经符合单个强化石数量，直接跳出循环
            if(count == stoneKeys.size()){
                break;
            }
        }
        if(count < stoneKeys.size()){
            //进行强化石补偿
            if(bufferStack != null){
                strengthStones[0].setAmount(strengthStones[0].getAmount()+1);
                //strengthResult.setChanceExtra(0);
                //只有执行强化石退回才会退回保护券，不然是不会消耗保护券的
                if(strengthExtraStone!=null){
                    stoneExtra.setAmount(stoneExtra.getAmount()+1);
                }
            }
            throw new ItemStrengthException("&c 强化失败，您的强化石不匹配，请确保您有："+ stoneName);
        }
        if(strengthExtraStone!=null){
            stoneExtra.setAmount(stoneExtra.getAmount()-1);
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
        private boolean isAdmin = false;
        private boolean isSuccess = false;
        private boolean isSafe = false;
        private int chanceExtra = 0;

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

        public int getChanceExtra() {
            return chanceExtra;
        }

        public void setChanceExtra(int chanceExtra) {
            this.chanceExtra = chanceExtra;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public boolean isSafe() {
            return isSafe;
        }

        public void setSafe(boolean safe) {
            isSafe = safe;
        }

        @Override
        public String toString() {
            return "StrengthResult{" +
                    "strength=" + strength +
                    ", type=" + type +
                    ", level=" + level +
                    ", isAdmin=" + isAdmin +
                    ", isSuccess=" + isSuccess +
                    ", isSafe=" + isSafe +
                    ", chanceExtra=" + chanceExtra +
                    '}';
        }
    }
}
