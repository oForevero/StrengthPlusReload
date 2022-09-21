package top.mccat.pojo.config;

import top.mccat.anno.Value;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.bean.StrengthStone;
import top.mccat.pojo.dao.YamlConfigObject;
import top.mccat.utils.YamlLoadUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Distance
 * @date 2022/9/6
 */
public class BaseConfig implements YamlConfigObject<BaseConfig> {
    @Value("pluginName")
    private String pluginName;
    @Value("debug")
    private boolean debug;
    @Value("enableMenu")
    private boolean enableMenu;


    public BaseConfig() {
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(Object pluginName) {
        this.pluginName = String.valueOf(pluginName);
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(Object debug) {
        this.debug = (boolean) debug;
    }

    public boolean isEnableMenu() {
        return enableMenu;
    }

    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }

    @Override
    public String toString() {
        return "BaseConfig{" +
                "pluginName='" + pluginName + '\'' +
                ", debug=" + debug +
                ", enableMenu=" + enableMenu +
                '}';
    }

    public static BaseConfig newInstance(){
        Optional<Object> o = Optional.empty();
        try {
            o = YamlLoadUtils.loadConfigObject("config.yml", BaseData.BASE_DIR,
                    "strengthPlus", BaseConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o.map(value -> (BaseConfig) value).orElseGet(BaseConfig::new);
    }

    @Override
    public BaseConfig reloadConfigFile(){
        return newInstance();
    }

}
