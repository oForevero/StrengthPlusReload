package top.mccat.pojo.config;

import top.mccat.anno.Value;
import top.mccat.pojo.BaseData;
import top.mccat.pojo.dao.YamlConfigObject;
import top.mccat.utils.YamlLoadUtils;

import java.util.Optional;

/**
 * @author Raven
 * @date 2022/09/05 18:43
 */
public class StrengthMenu implements YamlConfigObject<StrengthMenu> {
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

    public static StrengthMenu newInstance() {
        Optional<Object> o = Optional.empty();
        try {
            o = YamlLoadUtils.loadConfigObject("strength-menu.yml", BaseData.BASE_DIR,
                    "strength-menu", StrengthMenu.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o.map(value -> (StrengthMenu) value).orElseGet(StrengthMenu::new);
    }

    @Override
    public StrengthMenu reloadConfigFile() {
        return newInstance();
    }
}
