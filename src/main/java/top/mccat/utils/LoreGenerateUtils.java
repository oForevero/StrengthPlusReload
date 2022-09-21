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

    /**
     * 生成lore属性方法
     * @param level 等级
     * @param especialAttributes 特殊LoreList
     * @param baseAttribute 基础LoreList
     * @param strengthType 强化类型
     * @return loreList
     */
    private List<String> generateAttributesLore(int level, List<Attribute> especialAttributes, String baseAttribute, StrengthType strengthType){
        if(level == 0 && especialAttributes == null){
            List<String> dataList = new ArrayList<String>();
            switch (strengthType.getType()){
                case 0:
                    dataList.add(strengthAttribute.getArmorDefence()+" &3"+RomaMathGenerateUtil.intToRomanString(level));
                    break;
                case 1:
                    dataList.add(strengthAttribute.getMeleeDamage()+" &3"+RomaMathGenerateUtil.intToRomanString(level));
                    break;
                case 2:
                    dataList.add(strengthAttribute.getRemotelyDamage()+" &3"+RomaMathGenerateUtil.intToRomanString(level));
                    break;
                default:
                    break;
            }
            return generateStrengthLore(dataList);
        }
        switch(strengthType.getType()){
            case 0:

                break;
            default:
                break;
        }
        return null;
    }

    public void reloadBaseConfig() {
        strengthAttribute = StrengthAttribute.newInstance();
    }
}
