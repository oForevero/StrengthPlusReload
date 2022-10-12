package top.mccat.utils;

import com.sun.istack.internal.NotNull;
import top.mccat.enums.StrengthType;
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
    private final RomaMathGenerateUtil romaMathGenerateUtil;
    private LoreGenerateUtils(){
        romaMathGenerateUtil = new RomaMathGenerateUtil();
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
    public List<String> generateAttributesLore(int level, List<String> itemAttributeLore, String baseAttribute, String especialAttribute, StrengthType strengthType){
        loreList.clear();
        if(level == 1 || itemAttributeLore == null){
            LoreList<String> dataList = new LoreList<>();
            switch (strengthType.getType()){
                case 0:
                    dataList.add(strengthAttribute.getArmorDefence()+" &c"+romaMathGenerateUtil.intToRoman(level));
                    break;
                case 1:
                    dataList.add(strengthAttribute.getMeleeDamage()+" &c"+romaMathGenerateUtil.intToRoman(level));
                    break;
                case 2:
                    dataList.add(strengthAttribute.getRemotelyDamage()+" &c"+romaMathGenerateUtil.intToRoman(level));
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
        itemAttributeLore.set(indexOfAttribute, baseAttribute+" §c" + romaMathGenerateUtil.intToRoman(level));
        if(especialAttribute!=null && !"".equals(especialAttribute)){
            itemAttributeLore.add(indexOfAttribute+1 ,especialAttribute + " §e" + romaMathGenerateUtil.intToRoman(1));
        }
        //itemAttributeLore.add(setAttribute);
        return itemAttributeLore;
    }

    /**
     * 获取当前属性index地址
     * @param strengthAttributes 强化属性
     * @param strengthType 强化类型
     * @param level 等级参数
     * @return 当前属性地址
     */
    private int getIndexOfAttribute(List<String> strengthAttributes, StrengthType strengthType, int level) {
        switch (strengthType.getType()) {
            case 0:
                for(int i = 0; i < strengthAttributes.size(); i++) {
                    if(strengthAttributes.get(i).contains(strengthAttribute.getArmorDefence())){
                        return i;
                    }
                }
            case 1:
                for(int i = 0; i < strengthAttributes.size(); i++) {
                    if(strengthAttributes.get(i).contains(strengthAttribute.getMeleeDamage())){
                        return i;
                    }
                }

            case 2:
                for(int i = 0; i < strengthAttributes.size(); i++) {
                    if(strengthAttributes.get(i).contains(strengthAttribute.getRemotelyDamage())){
                        return i;
                    }
                }
            default:
                return -1;
        }
    }

    /**
     * 转换颜色字符list方法
     * @param loreList 强化loreList
     * @return 转换lore
     */
    public static List<String> parseColorLore(List<String> loreList){
        List<String> colorLore = new ArrayList<>();
        for (String s : loreList) {
            colorLore.add(ColorParseUtils.parseColorStr(s));
        }
        return colorLore;
    }

    public void reloadBaseConfig () {
        strengthAttribute = StrengthAttribute.newInstance();
    }
}
