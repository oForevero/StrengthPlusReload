package top.mccat.service.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.mccat.enums.StrengthType;
import top.mccat.exception.ItemStrengthException;
import top.mccat.pojo.bean.Attribute;
import top.mccat.pojo.bean.LevelValue;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.config.StrengthItem;
import top.mccat.pojo.config.StrengthMsg;
import top.mccat.service.StrengthService;
import top.mccat.utils.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Raven
 * @date 2022/09/25 21:38
 */
public class StrengthServiceImpl implements StrengthService {
    private final StrengthItem strengthItem;
    private StrengthAttribute strengthAttribute;
    private final LoreGenerateUtils loreGenerateUtils;
    private List<LevelValue> levelValues;
    private final MsgUtils msgUtils;
    private StrengthMsg strengthMsg;
    private final Map<String, StrengthStone> strengthStoneMap;
    private final RomaMathGenerateUtil romaMathGenerateUtil;
    private List<Attribute> enableMeleeAttribute = new ArrayList<>();
    private List<Attribute> enableDefenceAttribute= new ArrayList<>();
    private List<Attribute> enableRemoteAttribute= new ArrayList<>();
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
        this.strengthMsg = StrengthMsg.newInstance();
        //获取允许特殊强化的list数据
        enableMeleeAttribute = strengthAttribute.getDefenceAttribute().values().stream().filter(Attribute::isEnable).collect(Collectors.toList());
        enableDefenceAttribute = strengthAttribute.getDefenceAttribute().values().stream().filter(Attribute::isEnable).collect(Collectors.toList());
        enableRemoteAttribute = strengthAttribute.getRemoteAttribute().values().stream().filter(Attribute::isEnable).collect(Collectors.toList());
        /*initEnableAttributeList(strengthAttribute.getDefenceAttribute(),enableDefenceAttribute);
        initEnableAttributeList(strengthAttribute.getMeleeAttribute(),enableMeleeAttribute);
        initEnableAttributeList(strengthAttribute.getRemoteAttribute(),enableRemoteAttribute);*/
    }

    /*private void initEnableAttributeList(Map<String, Attribute> attributeMap, List<Attribute> attributeList){
        Set<String> keySet = attributeMap.keySet();
        for(String key : keySet) {
            Attribute attribute = attributeMap.get(key);
            if(attribute.isEnable()){
                attributeList.add(attribute);
            }
        }
    }*/

    @Override
    public boolean strengthItemInUi(ItemStack stack, Player player, StrengthResult strengthResult) {
        boolean result = false;
        int level = strengthResult.getLevel();
        //获取额外强化lore
        assert stack.getItemMeta() != null;
        List<String> itemLore = stack.getItemMeta().getLore();
        if(level == -1){
            msgUtils.sendToPlayer("&c当前强化物品无法进行强化操作！", player);
            return false;
        }
        LevelValue levelValue = levelValues.get(level);
        //如果是admin直接升级到满级
        if(strengthResult.isAdmin()){
            level = levelValues.size();
            result = true;
        }else {
            //如果不是必定成功则进行强化判断
            if(strengthResult.isSuccess()){
                msgUtils.sendToPlayer(variableTranslation(strengthMsg.getNotifySuccess(),player,level), player);
                level += 1;
                result = true;
                if(levelValue.isCanBreak()){
                    msgUtils.sendToBroadcast(variableTranslation(strengthMsg.getBroadcastSuccess(),player,level));
                }
                if(level == levelValues.size()){
                    msgUtils.sendToBroadcast(variableTranslation(strengthMsg.getBroadcastMaxLevel(),player,level));
                }
            }else {
                if(strengthResult(levelValue, strengthResult)){
                    level += 1;
                    result = true;
                    if(levelValue.isCanBreak()){
                        msgUtils.sendToBroadcast(variableTranslation(strengthMsg.getBroadcastSuccess(),player,level));
                    }
                    if(level == levelValues.size()){
                        msgUtils.sendToBroadcast(variableTranslation(strengthMsg.getBroadcastMaxLevel(),player,level));
                    }
                    msgUtils.sendToPlayer(variableTranslation(strengthMsg.getNotifySuccess(),player,level), player);
                }else{
                    //如果设置允许丢失等级，则使其丢失等级，如果默认为0则清除lore
                    if(levelValue.isLoseLevel()){
                        if(level > 0){
                            level -= 1;
                            if(levelValues.get(level).isEspecialAttribute()){
                                //如果当前等级-1有额外属性，则删除额外属性lore
                                //如果不存在额外属性
                                if(itemLore!=null){
                                    if(itemLore.size() >= 5){
                                        itemLore.remove(itemLore.size() - 2);
                                    }
                                }
                            }
                        }else{
                            ItemMeta itemMeta = stack.getItemMeta();
                            itemMeta.setLore(null);
                            stack.setItemMeta(itemMeta);
                        }
                    }
                    //如果允许破坏则设置为空气
                    //所有失败的消息广播应当设置等级+2，因为level是强化结束后的等级，并且额外-1，故+2
                    if(levelValue.isCanBreak()) {
                        //如果为不安全，则设置其为空气
                        if(!strengthResult.isSafe()){
                            stack.setType(Material.AIR);
                            player.getOpenInventory().setItem(19,stack);
                            msgUtils.sendToBroadcast(variableTranslation(strengthMsg.getBroadcastFail(),player,level+2));
                            return false;
                        }
                        //允许广播通知
                        msgUtils.sendToBroadcast(variableTranslation(strengthMsg.getBroadcastSafe(),player,level+2));
                    }
                    msgUtils.sendToPlayer(variableTranslation(strengthMsg.getNotifyFail(),player,level+1), player);
                }
            }
        }
        List<String> lore = null;
        switch (strengthResult.getType()){
            case 0:
                lore = setStrengthLoreUtils(strengthAttribute.getDefenceAttribute(), enableDefenceAttribute,
                        result, level, itemLore, strengthAttribute.getArmorDefence(), StrengthType.ARMOR_TYPE, strengthResult.isAdmin());
                break;
            case 1:
                lore = setStrengthLoreUtils(strengthAttribute.getMeleeAttribute(), enableMeleeAttribute,
                        result, level, itemLore, strengthAttribute.getMeleeDamage(), StrengthType.WEAPON_TYPE, strengthResult.isAdmin());
                break;
            case 2:
                lore = setStrengthLoreUtils(strengthAttribute.getRemoteAttribute(), enableRemoteAttribute,
                        result, level, itemLore, strengthAttribute.getRemotelyDamage(), StrengthType.BOW_TYPE, strengthResult.isAdmin());
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

    private final List<String> especialAttribute = new ArrayList<>();
    /**
     * 设置随机lore
     * @param attributeMap map数据
     * @param attributeList 列表数据
     * @param result 强化结果
     * @param level 等级
     * @param type 强化类型
     * @param baseAttribute 基础attribute
     * @param itemLore 物品lore
     * @param isAdmin 是否为管理员
     * @return
     */
    private List<String> setStrengthLoreUtils(Map<String, Attribute> attributeMap, List<Attribute> attributeList, boolean result, int level,
                                              List<String> itemLore, String baseAttribute, StrengthType type, boolean isAdmin){
        especialAttribute.clear();
        if(result){
            if(levelValues.get(level-1).isEspecialAttribute()){
                List<Attribute> copyAttributes;
                try {
                    copyAttributes = CollectionCopyUtils.deepCopy(attributeList);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
                Iterator<Attribute> iterator = copyAttributes.iterator();
                if(itemLore != null) {
                    while(iterator.hasNext()){
                        String attrName = iterator.next().getName();
                        for(String subLore : itemLore){
                            if(subLore.contains(attrName)){
                                iterator.remove();
                            }
                        }
                    }
                }
                if(isAdmin){
                    while(iterator.hasNext()){
                        especialAttribute.add(iterator.next().getName());
                    }
                }
                if(!isAdmin && copyAttributes.size()>0){
                    int i = random.nextInt(copyAttributes.size());
                    Attribute attribute = copyAttributes.get(i);
                    especialAttribute.add(attribute.getName());
                }
            }
        }
        return loreGenerateUtils.generateAttributesLore(level, itemLore, baseAttribute, especialAttribute, type);
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
        int randomChance = random.nextInt(101);
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
            //管理员强化有问题，需要修复
            if(!ItemStackCheckUtils.notNullMeta(strengthStone)){
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
                stoneList.remove(stone);
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
                if(strengthStones[0]!=null){
                    strengthStones[0].setAmount(strengthStones[0].getAmount()+1);
                }else{
                    strengthStones[1].setAmount(strengthStones[1].getAmount()+1);
                }
                strengthResult.setChanceExtra(0);
                //只有执行强化石退回才会退回保护券，不然是不会消耗保护券的
                /*if(strengthExtraStone!=null){
                    stoneExtra.setAmount(stoneExtra.getAmount()+1);
                }*/
            }
            throw new ItemStrengthException("&c 强化失败，您的强化石不匹配，请确保您有："+ stoneName);
        }
        if(strengthExtraStone!=null){
            stoneExtra.setAmount(stoneExtra.getAmount()-1);
        }
    }

    public static final String PLAYER_VARIABLE = "%player%";
    public static final String LEVEL_VARIABLE = "%level%";

    /**
     * 进行变量转换
     * @param translateText 转换文本
     * @param player 玩家变量
     * @param level 等级变量
     * @return 转换后字符串
     */
    private String variableTranslation(String translateText, Player player, int level){
        String msg = "";
        if(translateText.contains(PLAYER_VARIABLE)){
            msg = translateText.replace(PLAYER_VARIABLE, player.getName());
        }
        if("".equals(msg)){
            msg = translateText.replace(LEVEL_VARIABLE, ""+level);
        }else{
            msg = msg.replace(LEVEL_VARIABLE, ""+level);
        }
        return msg;
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

    @Override
    public void reloadConfig() {
        msgUtils.reloadMsgConfig();
        strengthMsg = strengthMsg.reloadConfigFile();
        levelValues = LevelValue.newInstance();
        strengthAttribute = strengthAttribute.reloadConfigFile();
    }
}
