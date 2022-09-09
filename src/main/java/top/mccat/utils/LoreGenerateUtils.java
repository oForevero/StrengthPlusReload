package top.mccat.utils;

import com.sun.istack.internal.NotNull;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.bean.Attribute;
import top.mccat.pojo.config.BaseConfig;
import top.mccat.pojo.config.StrengthAttribute;
import top.mccat.pojo.list.LoreList;

import java.io.IOException;
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

    private List<String> generateAttributesLore(int level, List<Attribute> especialAttributes, String baseAttribute){

    }

    public void reloadBaseConfig() {
        try {
            strengthAttribute = (StrengthAttribute) YamlLoadUtils.loadYamlAsObject("config.yml",
                    BaseData.PLUGIN_PREFIX, "strength-extra", StrengthAttribute.class).get();
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
