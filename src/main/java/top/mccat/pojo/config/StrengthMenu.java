package top.mccat.pojo.config;

import top.mccat.anno.Value;

/**
 * @author Raven
 * @date 2022/09/05 18:43
 */
public class StrengthMenu {
    @Value("enable")
    private boolean enable;
    @Value("menuTitle")
    private String menuTitle;
    @Value("chanceDisplay")
    private boolean chanceDisplay;

    public StrengthMenu() {
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public boolean isChanceDisplay() {
        return chanceDisplay;
    }

    public void setChanceDisplay(boolean chanceDisplay) {
        this.chanceDisplay = chanceDisplay;
    }

    @Override
    public String toString() {
        return "StrengthMenu{" +
                "enable=" + enable +
                ", menuTitle='" + menuTitle + '\'' +
                ", chanceDisplay=" + chanceDisplay +
                '}';
    }
}
