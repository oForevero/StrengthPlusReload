package top.mccat.pojo.config;

import top.mccat.anno.Value;

/**
 * @author Distance
 * @date 2022/9/6
 */
public class BaseConfig {
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
}
