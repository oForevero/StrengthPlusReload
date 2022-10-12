package top.mccat.service.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;
import top.mccat.enums.StrengthType;
import top.mccat.exception.ItemStrengthException;
import top.mccat.pojo.bean.Attribute;
import top.mccat.pojo.bean.LevelValue;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.config.StrengthItem;
import top.mccat.service.StrengthService;
import top.mccat.utils.LoreGenerateUtils;
import top.mccat.utils.MsgUtils;
import top.mccat.utils.RomaMathGenerateUtil;

import javax.annotation.Nullable;
import java.util.*;

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
    private final List<Attribute> enableMeleeAttribute = new ArrayList<>();
    private final List<Attribute> enableDefenceAttribute= new ArrayList<>();
    private final List<Attribute> enableRemoteAttribute= new ArrayList<>();
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
        //获取允许特殊强化的list数据
        initEnableAttributeList(strengthAttribute.getDefenceAttribute(),enableDefenceAttribute);
        initEnableAttributeList(strengthAttribute.getMeleeAttribute(),enableMeleeAttribute);
        initEnableAttributeList(strengthAttribute.getRemoteAttribute(),enableRemoteAttribute);
    }

    private void initEnableAttributeList(Map<String, Attribute> attributeMap, List<Attribute> attributeList){
        Set<String> keySet = attributeMap.keySet();
        for(String key : keySet) {
            Attribute attribute = attributeMap.get(key);
            if(attribute.isEnable()){
                attributeList.add(attribute);
            }
        }
    }

    @Override
    public boolean strengthItemInUi(ItemStack stack, Player player, StrengthResult strengthResult) {
        // 明天进行如下开发，首先取出额外lore参数，定义为基础属性下，如果当前等级拥有额外属性，在强化失败时删除最下面的额外属性lore，即最后获得的属性即新额外属性。
        // 强化成功后从额外属性中随机抽取出一个属性进行lore赋值。当属性已包含则进行深拷贝，删除已包含的属性，随机出其他属性
        // 当全部参数都已包含额外属性，则进行普通属性强化，并提示额外强化属性已满
        boolean result = false;
        int level = strengthResult.getLevel();
        //获取额外强化lore
        assert stack.getItemMeta() != null;
        //<String> especialLore = null;
        List<String> itemLore = stack.getItemMeta().getLore();
        /*if(stack.getItemMeta().hasLore()) {
            especialLore = parseEspecialStoneLore(itemLore);
        }*/
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
                            if(levelValues.get(level).isEspecialAttribute()){
                                //如果当前等级-1有额外属性，则删除额外属性lore
                                /**
                                 * 默认以第三位往后的地址为额外强化地址，范例如下：
                                 * [强化信息]  index 0
                                 * --------------------- index 1
                                 * 基础强化信息：等级 index 2
                                 * 额外强化信息1：等级 index 3
                                 * 额外强化等级2：等级 index 4
                                 * .....
                                 * ---------------------
                                 */
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
                lore = setStrengthLoreUtils(strengthAttribute.getDefenceAttribute(), enableDefenceAttribute,
                        result, level, itemLore, strengthAttribute.getArmorDefence(), StrengthType.ARMOR_TYPE);
                break;
            case 1:
                lore = setStrengthLoreUtils(strengthAttribute.getMeleeAttribute(), enableMeleeAttribute,
                        result, level, itemLore, strengthAttribute.getMeleeDamage(), StrengthType.WEAPON_TYPE);
                break;
            case 2:
                lore = setStrengthLoreUtils(strengthAttribute.getRemoteAttribute(), enableRemoteAttribute,
                        result, level, itemLore, strengthAttribute.getRemotelyDamage(), StrengthType.BOW_TYPE);
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

    /**
     * 设置随机lore
     * @param attributeMap
     * @param attributeList
     * @param result
     * @param level
     * @param type
     * @return
     */
    private List<String> setStrengthLoreUtils(Map<String, Attribute> attributeMap, List<Attribute> attributeList, boolean result, int level,
                                              List<String> itemlore, String baseAttribute,StrengthType type){
        //晚上加上levelvalue进行，等级判断是否有额外强化数据则增加lore
        //进行数据随机
        String especialAttribute = null;
        if(result){
            if(levelValues.get(level-1).isEspecialAttribute()){
                int i = random.nextInt(attributeMap.size());
                for(Attribute attribute : attributeList){
                    if(itemlore.contains(attribute.getName())) {
                        attributeList.remove(attribute);
                        if(i > 0){
                            i -= 1;
                        }
                    }
                }
                Attribute attribute = attributeList.get(i);
                especialAttribute = attribute.getName();
            }
        }
        return loreGenerateUtils.generateAttributesLore(level, itemlore, baseAttribute, especialAttribute, type);
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
