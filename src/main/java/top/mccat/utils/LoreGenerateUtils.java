package top.mccat.utils;

import top.mccat.pojo.BaseData;
import top.mccat.pojo.config.BaseConfig;
import top.mccat.pojo.list.LoreList;

import java.io.IOException;

/**
 * @author Raven
 * @date 2022/09/05 17:47
 */
public class LoreGenerateUtils {
    private BaseConfig baseConfig;
    private LoreGenerateUtils(){
        try {
            baseConfig = (BaseConfig) YamlLoadUtils.loadYamlAsObject("config.yml",
                    BaseData.PLUGIN_PREFIX, "strength-extra", BaseConfig.class).get();
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private LoreList<String> generateTitleLore(){

    }

    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }
}
