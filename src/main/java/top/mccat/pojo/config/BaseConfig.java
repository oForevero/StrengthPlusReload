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
    @Value("title")
    private String title;
    @Value("divider")
    private String divider;
    @Value("levelIcon")
    private String levelIcon;
    @Value("nextLineCount")
    private int nextLineCount;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = String.valueOf(title);
    }

    public String getDivider() {
        return divider;
    }

    public void setDivider(Object divider) {
        this.divider = String.valueOf(divider);
    }

    public String getLevelIcon() {
        return levelIcon;
    }

    public void setLevelIcon(Object levelIcon) {
        this.levelIcon = String.valueOf(levelIcon);
    }

    public int getNextLineCount() {
        return nextLineCount;
    }

    public void setNextLineCount(Object nextLineCount) {
        this.nextLineCount = (int) nextLineCount;
    }

    @Override
    public String toString() {
        return "BaseConfig{" +
                "pluginName='" + pluginName + '\'' +
                ", debug=" + debug +
                ", title='" + title + '\'' +
                ", divider='" + divider + '\'' +
                ", levelIcon='" + levelIcon + '\'' +
                ", nextLineCount=" + nextLineCount +
                '}';
    }
}
