package top.mccat.utils;

import com.sun.istack.internal.NotNull;
import top.mccat.enums.StrengthType;
import top.mccat.pojo.bean.Attribute;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.list.LoreList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 * @date 2022/09/05 17:47
 */
public class LoreGenerateUtils {
    private StrengthAttribute strengthAttribute;
    private final LoreList<String> loreList = new LoreList<>();
    private LoreGenerateUtils(){
        reloadBaseConfig();
    }

    /**
     * 生成强化lore方法
     * @param dataList 强化 lore list
     * @return 强化lore
     */
    private LoreList<String> generateStrengthLore(@NotNull List<String> dataList){
        loreList.add(strengthAttribute.getTitle());
        loreList.add(strengthAttribute.getDivider());
        loreList.addAll(dataList);
        loreList.add(strengthAttribute.getDivider());
        return loreList;
    }

    public static LoreGenerateUtils newInstance() {
        return new LoreGenerateUtils();
    }

    /**
     * 生成lore属性方法
     * @param level 等级
     * @param itemAttributeLore 特殊LoreList
     * @param strengthType 强化类型
     * @return loreList
     */
    public List<String> generateAttributesLore(int level, List<String> itemAttributeLore, String setAttribute, StrengthType strengthType){
        if(level == 0 || itemAttributeLore == null){
            List<String> dataList = new ArrayList<String>();
            switch (strengthType.getType()){
                case 0:
                    dataList.add(strengthAttribute.getArmorDefence()+ColorParseUtils.parseColorStr(" &c"+RomaMathGenerateUtil.intToRomanString(level)));
                    break;
                case 1:
                    dataList.add(strengthAttribute.getMeleeDamage()+ColorParseUtils.parseColorStr(" &c"+RomaMathGenerateUtil.intToRomanString(level)));
                    break;
                case 2:
                    dataList.add(strengthAttribute.getRemotelyDamage()+ColorParseUtils.parseColorStr(" &c"+RomaMathGenerateUtil.intToRomanString(level)));
                    break;
                default:
                    break;
            }
            return generateStrengthLore(dataList);
        }
        int indexOfAttribute = getIndexOfAttribute(itemAttributeLore , strengthType, level);
        if(indexOfAttribute == -1){
            return itemAttributeLore;
        }
        itemAttributeLore.set(indexOfAttribute, setAttribute+"&c " + level);
        return itemAttributeLore;
    }

    private int getIndexOfAttribute(List<String> strengthAttributes, StrengthType strengthType, int level) {
        switch (strengthType.getType()) {
            case 0:
                return strengthAttributes.indexOf(strengthAttribute.getDefenceAttribute() + "&c" + level);
            case 1:
                return strengthAttributes.indexOf(strengthAttribute.getMeleeDamage() + "&c" + level);
            case 2:
                return strengthAttributes.indexOf(strengthAttribute.getRemotelyDamage() + "&c" + level);
            default:
                return -1;
        }
    }

    public void reloadBaseConfig () {
        strengthAttribute = StrengthAttribute.newInstance();
    }
}
